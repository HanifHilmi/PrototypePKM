package com.pkm.PrototypePKM.ui.screen.feedback

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.unit.dp
import androidx.core.content.PermissionChecker
import androidx.navigation.compose.rememberNavController
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.pkm.PrototypePKM.R
import com.pkm.PrototypePKM.ui.theme.PrototypePKMTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Composable
fun FeedbackFinish(){
    val qrKeyList = listOf(
        "qrkey1","qrkey2","qrkey3"
    )
    var qrResult by remember { mutableStateOf("")}
    var qrState by remember { mutableStateOf("")}
    var showFeedback by remember {mutableStateOf(false)}

    LaunchedEffect(key1 = qrResult.isNotEmpty()){

        if (qrKeyList.contains(qrResult)){       // QR key valid
            qrState = "SUCCESS"
            delay(500)
            showFeedback = true

        }else if(!qrKeyList.contains(qrResult) && qrResult.isNotEmpty()){//QR key invalid
            qrState = "FAILED"
            delay(3000)
            qrState = ""
            qrResult = ""
            showFeedback = false
        }
    }
    if (!showFeedback){
        QrCam(
            getQrResult = {
                qrResult = it
            },
            qrState = qrState,
            onBackPressed = {
                showFeedbackFinish = false
                sharedFlag = false
            }
        )
    }else {
        FeedbackContent()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackContent() {
    PrototypePKMTheme {
        val context = LocalContext.current
        var text by remember { mutableStateOf(TextFieldValue()) }
        //val selectedDateText = remember { mutableStateOf("Pilih tanggal")}
        var selectedDate by remember { mutableStateOf<LocalDate?>(LocalDate.now()) }
        val calendarState = rememberUseCaseState()
        var selectedImageUri: Uri? by remember { mutableStateOf(null) }
        val selectedFile = remember { mutableStateOf<String?>(null) }
        var showOptionsDialog by remember { mutableStateOf(false) }
        val contentResolver = context.contentResolver
        var imgURL = remember { mutableStateOf<String?>(null) }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                selectedImageUri = data?.data
                selectedFile.value = selectedImageUri?.toString()

                if (selectedImageUri != null) {
                    // Jika gambar dipilih dari galeri, dapatkan nama file dari URI
                    val fileName = getFileNameFromUri(contentResolver, selectedImageUri!!)
                    selectedFile.value = fileName
                }
            }
        }

        val requestPermissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Izin diberikan, Anda sekarang dapat mengakses gambar
                val galleryIntent = Intent(Intent.ACTION_PICK)
                galleryIntent.type = "image/*"
                launcher.launch(galleryIntent)
            } else {
                // Izin ditolak, tangani dengan baik
                // Anda dapat menampilkan pesan kepada pengguna atau mengambil tindakan yang sesuai
            }
        }

        val launcherCam = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                if (selectedImageUri != null) {
                    val fileName = getFileNameFromUri(contentResolver, selectedImageUri!!)
                    selectedFile.value = fileName
                }
            } else {
                // Pengguna membatalkan pengambilan gambar, lakukan sesuatu jika diperlukan
            }
        }

        val isFeedbackFinished: MutableState<Boolean> = remember { mutableStateOf(false) }

        fun setFeedbackFinished(isFinished: Boolean) {
            isFeedbackFinished.value = isFinished
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
                    modifier = Modifier.size(24.dp) // Sesuaikan ukuran ikon
                )
                Text(
                    text = "Anon", // Teks yang ingin Anda tambahkan
                    modifier = Modifier.padding(start = 8.dp), // Sesuaikan jarak antara ikon dan teks
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

            Card(modifier = Modifier.fillMaxSize()){
                Row(modifier = Modifier.padding(8.dp)){

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
                        onClick = { calendarState.show() } // Update the value of showDialog
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
                            yearSelection =true,
                            monthSelection =true,
                            style = CalendarStyle.MONTH,
                        ),
                        selection = CalendarSelection.Date(
                            selectedDate= selectedDate
                        ){newDate ->
                            selectedDate = newDate
                        }
                    )


                }

            }

            Spacer(modifier = Modifier.height(8.dp))
            Card(modifier = Modifier.fillMaxSize()){
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
                    ){
                        Text(
                            text = selectedFile.value ?: "Foto",
                            color = if (selectedFile.value.isNullOrEmpty()) {
                                Color.Gray
                            } else {
                                Color.Black
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


            fun uploadImageToFirebaseStorage(selectedImageUri: Uri) {
                val storageRef: StorageReference = Firebase.storage.reference
                val fileName = getFileNameFromUri(contentResolver, selectedImageUri)

                if (fileName != null) {
                    // Tentukan path ke mana Anda ingin mengunggah gambar di Firebase Storage
                    val imageRef = storageRef.child("PKM/$fileName")

                    // Menampilkan dialog loading
                    val progressDialog = ProgressDialog(context)
                    progressDialog.setTitle("Uploading")
                    progressDialog.setMessage("Please wait...")
                    progressDialog.show()

                    // Mulai proses pengunggahan
                    val uploadTask = imageRef.putFile(selectedImageUri)

                    uploadTask.addOnSuccessListener { taskSnapshot ->
                        progressDialog.dismiss() // Menutup dialog loading
                        //Toast.makeText(context, "Succed", Toast.LENGTH_SHORT).show()
                        imageRef.downloadUrl
                            .addOnSuccessListener { uri ->
                                val imageUrl = uri.toString()
                                imgURL.value = imageUrl

                            }
                            .addOnFailureListener { exception ->
                                // Handle kesalahan jika ada
                            }
                        sharedFlag = true
                        showFeedbackFinish = false
                    }.addOnFailureListener { exception ->
                        progressDialog.dismiss() // Menutup dialog loading
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(56.dp))
            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    if (selectedImageUri != null && text != null && selectedDate != null) {
                        uploadImageToFirebaseStorage(selectedImageUri!!) //FUNGSI KIRIM DATABASE
                    } else {
                        Toast.makeText(context, "Pilih Foto Dahulu", Toast.LENGTH_SHORT).show()
                    }

                }
            ) {
                Text("Kirim")
                Icon(Icons.Filled.ArrowForward, contentDescription = null)
            }


            fun takePicture() {
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageFileName = "JPEG_$timeStamp.jpg"

                val imageCollection = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else {
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                }

                val contentValues = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                }

                val imageUri = contentResolver.insert(imageCollection, contentValues)

                if (imageUri != null) {
                    selectedImageUri = imageUri // Menyimpan URI gambar yang ditangkap ke dalam variable selectedImageUri
                    launcherCam.launch(imageUri)
                } else {
                    // Penanganan jika gagal membuat URI gambar
                }
            }

            if (showOptionsDialog) {
                AlertDialog(
                    onDismissRequest = { showOptionsDialog = false },
                    title = {
                        Text(text = "Pilih Sumber Gambar")
                    },
                    text = {
                        Text(text =  "Pilih sumber gambar untuk mengunggah")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
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
                                takePicture()
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

private const val CAMERA_REQUEST_CODE = 123
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