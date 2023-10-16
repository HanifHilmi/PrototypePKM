package com.pkm.PrototypePKM.ui.screen.feedback

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.pkm.PrototypePKM.R
import com.pkm.PrototypePKM.ui.theme.PrototypePKMTheme
import kotlinx.coroutines.delay
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

@Composable
fun FeedbackScreenNew() {
    val qrKeyList = listOf(
        "Miaw", "qrkey2", "qrkey3"
    )
    var qrResult by remember { mutableStateOf("") }
    var qrState by remember { mutableStateOf("") }
    var showFeedback by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = qrResult.isNotEmpty()) {
        if (qrKeyList.contains(qrResult)) {
            qrState = "SUCCESS"
            delay(500)
            showFeedback = true
        } else if (!qrKeyList.contains(qrResult) && qrResult.isNotEmpty()) {
            qrState = "FAILED"
            delay(3000)
            qrState = ""
            qrResult = ""
            showFeedback = false
        }
    }
    if (!showFeedback) {
        FeedbackContent(qrResult = qrResult)
    } else {
        FeedbackContent(qrResult = qrResult)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackContent(qrResult: String) {
    PrototypePKMTheme {
        val context = LocalContext.current
        var text by remember { mutableStateOf(TextFieldValue()) }
        var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
        val calendarState = rememberUseCaseState()
        val selectedFile = remember { mutableStateOf<String?>(null) }
        var showOptionsDialog by remember { mutableStateOf(false) }
        val contentResolver = context.contentResolver
        val file = context.createImageFile()
        val contentUri = FileProvider.getUriForFile(context, "com.pkm.PrototypePKM.provider", file)

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val selectedImageUri = data?.data
                selectedFile.value = selectedImageUri?.toString()

                if (selectedImageUri != null) {
                    val fileName = getFileNameFromUri(contentResolver, selectedImageUri)
                    selectedFile.value = fileName
                }
            }
        }

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"
                launcher.launch(galleryIntent)
            } else {
                // Handle permission denied
            }
        }

        var capturedImageUri by remember {
            mutableStateOf<Uri>(Uri.EMPTY)
        }

        val cameraLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
                capturedImageUri = contentUri
            }

        val permissionLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) {
                    Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                    cameraLauncher.launch(contentUri)
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }

        var selectedImageUri = when {
            !capturedImageUri.path.isNullOrEmpty() -> capturedImageUri
            !selectedFile.value.isNullOrEmpty() -> Uri.parse(selectedFile.value)
            else -> Uri.EMPTY
        }

        val storageRef = Firebase.storage.reference

        fun uploadImageToFirebase() {
            if (selectedImageUri != Uri.EMPTY) {
                val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")
                val contentResolver = context.contentResolver

                // Read the image data from the selected URI
                val inputStream = contentResolver.openInputStream(selectedImageUri)
                val buffer = ByteArrayOutputStream()
                val bufferData = ByteArray(1024)
                var bytesRead: Int
                while (inputStream?.read(bufferData, 0, bufferData.size).also { bytesRead = it!! } != -1) {
                    buffer.write(bufferData, 0, bytesRead)
                }
                val data = buffer.toByteArray()

                // Upload the image to Firebase Storage
                val uploadTask = imageRef.putBytes(data)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    // Upload completed successfully
                    val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl
                    if (downloadUrl != null) {
                        val imageUrl = downloadUrl.result.toString()
                        // Do something with the imageURL (e.g., store it in a database)
                    }
                }.addOnFailureListener { exception ->
                    // Handle the upload failure
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(1.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Anon",
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Divider(
                thickness = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                modifier = Modifier
                    .height(400.dp)
                    .fillMaxSize()
                    .border(width = 1.dp, color = Color.Transparent)
                    .clip(RoundedCornerShape(16.dp))
                    .verticalScroll(rememberScrollState()),
                value = text.text,
                onValueChange = {
                    text = text.copy(text = it)
                },
                textStyle = TextStyle(color = Color.Black),
                placeholder = {
                    Text("Input Feedback")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {

                    }
                )
            )


            Spacer(modifier = Modifier.height(8.dp))

            Card(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.weight(0.1f))

                    Text(
                        text = selectedDate.toString(),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Button(
                        onClick = { calendarState.show() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    CalendarDialog(
                        state = calendarState,
                        config = CalendarConfig(
                            yearSelection = true,
                            monthSelection = true,
                            style = CalendarStyle.MONTH,
                        ),
                        selection = CalendarSelection.Date(
                            selectedDate = selectedDate
                        ) { newDate ->
                            selectedDate = newDate
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Icon(
                        contentDescription = null,
                        painter = painterResource(id = R.drawable.ic_cam),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = if (selectedImageUri != Uri.EMPTY) {
                                selectedImageUri.toString().substringAfterLast("/")
                            } else {
                                "Foto"
                            },
                            color = if (selectedImageUri != Uri.EMPTY) {
                                Color.Black
                            } else {
                                Color.Gray
                            },
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState())
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.1f))

                    Button(
                        onClick = {
                            showOptionsDialog = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cam),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(56.dp))
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    uploadImageToFirebase()
                }
            ) {
                Text("Kirim")
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }

            if (showOptionsDialog) {
                AlertDialog(
                    onDismissRequest = { showOptionsDialog = false },
                    title = {
                        Text(text = "Pilih Sumber Gambar")
                    },
                    text = {
                        Text(text = "Pilih sumber gambar untuk mengunggah")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                selectedImageUri = Uri.EMPTY
                                capturedImageUri = Uri.EMPTY
                                if (PermissionChecker.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    ) == PermissionChecker.PERMISSION_GRANTED
                                ) {
                                    val galleryIntent = Intent(Intent.ACTION_PICK)
                                    galleryIntent.type = "image/*"
                                    launcher.launch(galleryIntent)
                                } else {
                                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                                showOptionsDialog = false
                            }
                        ) {
                            Text(text = "Dari Galeri")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                selectedImageUri = Uri.EMPTY
                                capturedImageUri = Uri.EMPTY
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.CAMERA
                                    )
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(contentUri)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                                showOptionsDialog = false
                            }
                        ) {
                            Text(text = "Ambil Foto")
                        }
                    }
                )
            }
        }
    }
}


private fun getFileNameFromUri(contentResolver: ContentResolver, uri: Uri): String? {
    val cursor = contentResolver.query(uri, null, null, null, null)

    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                val displayName = it.getString(displayNameIndex)
                return displayName
            }
        }
    }

    return null
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}



@Preview
@Composable
fun FeedbackPrev() {
    Surface {
        FeedbackContent(qrResult = "")
    }
}
