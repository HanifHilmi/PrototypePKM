package com.pkm.PrototypePKM.ui.screen.feedback

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.pkm.PrototypePKM.ui.theme.PrototypePKMTheme
import com.pkm.PrototypePKM.ui.theme.QRSuccessGreen
import com.pkm.PrototypePKM.ui.theme.QRfailedRed
import com.pkm.PrototypePKM.ui.theme.QRidleGray
import com.plcoding.qrcodescannercompose.QrCodeAnalyzer


@Composable
fun QrCam(
    qrState: String,
    getQrResult:(String)->Unit
) {
    var code by remember {mutableStateOf("")}
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission(),
//        onResult = {granted->
//            hasCamPermission = granted
//        }
//    )
//
//    LaunchedEffect(key1 = true){
//        launcher.launch(Manifest.permission.CAMERA)
//    }


    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                hasCamPermission = isGranted
            } else {
                hasCamPermission = isGranted
                Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    val showCamera = remember { mutableStateOf(false) }

    // Menggunakan LaunchedEffect untuk memulai pemindaian QR code secara otomatis saat showCamera berubah menjadi true
    LaunchedEffect(showCamera.value) {
        if (showCamera.value) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            // Memeriksa izin kamera saat showCamera berubah menjadi true
            checkCamPermission(
                activity = (context as Activity),
                requestPermissionLauncher = requestPermissionLauncher,
                permission = Manifest.permission.CAMERA,
                showCamera = showCamera
            )
        }
    }





    Column(modifier = Modifier.fillMaxSize()) {
        if(hasCamPermission){
            Box(modifier = Modifier
                .weight(1f)
                .fillMaxWidth()){

                AndroidView(
                    factory = {context->
                        val previewView = PreviewView(context)
                        val preview = androidx.camera.core.Preview.Builder().build()
                        val selector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                            .build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(
                                android.util.Size(
                                    previewView.width,
                                    previewView.height
                                )
                            )
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                        imageAnalysis.setAnalyzer(
                            ContextCompat.getMainExecutor(context),
                            QrCodeAnalyzer{result->
                                code = result
                                getQrResult(code)
                            }
                        )
                        try {
                            cameraProviderFuture.get().bindToLifecycle(
                                lifeCycleOwner,
                                selector,
                                preview,
                                imageAnalysis
                            )

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
                TransparentClipLayout(

                    width = 300.dp,
                    height = 300.dp,
                    offsetY = 150.dp,
                    qrState = qrState
                )
            }
            Text(
                text = code,
            )
        }
    }
}

//@Composable
//fun QrPlaceHolder() {
//    Box(modifier = ) {
//
//    }
//}

@Preview
@Composable
fun PlaceholderPrev() {
    PrototypePKMTheme {
        Surface {
            //QrPlaceHolder()
        }
    }
}

@Composable
fun TransparentClipLayout(
    modifier: Modifier = Modifier,
    width: Dp,
    height: Dp,
    offsetY: Dp,
    qrState:String
) {

    val offsetInPx: Float
    val widthInPx: Float
    val heightInPx: Float
    var pesanScan = ""

    with(LocalDensity.current) {
        offsetInPx = offsetY.toPx()
        widthInPx = width.toPx()
        heightInPx = height.toPx()
    }
    Box(modifier = modifier){
        Text(
            "Scan QR yang telah diberikan",
            style = MaterialTheme.typography.displaySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .widthIn(max = width)
                .padding(bottom = 24.dp)
        )
        if (qrState.isNotEmpty()){

            pesanScan = when (qrState) {
                "SUCCESS" -> "QR Valid"
                "FAILED" -> "QR yang anda scan tidak sesuai"
                else -> ""
            }

            Text(
                text = pesanScan,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .widthIn(max = width)
            )
        }
        Canvas(modifier = modifier.fillMaxSize()) {

            val canvasWidth = size.width

            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)

                // Destination
                if (qrState == "SUCCESS")drawRect(QRSuccessGreen)
                else if((qrState == "FAILED"))drawRect(QRfailedRed)
                else if(qrState.isEmpty()) drawRect(QRidleGray)



                // Source
                drawRoundRect(
                    topLeft = Offset(
                        x = (canvasWidth - widthInPx) / 2,
                        y = offsetInPx
                    ),
                    size = Size(widthInPx, heightInPx),
                    cornerRadius = CornerRadius(30f,30f),
                    color = Color.Transparent,
                    blendMode = BlendMode.Clear
                )

                restoreToCount(checkPoint)
            }

        }
    }

}

private fun checkCamPermission(
    activity: Activity,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    permission: String,
    showCamera: MutableState<Boolean>
) {
    if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
        showCamera.value = true
    } else if (androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {

    } else {
        requestPermissionLauncher.launch(permission)
    }
}

@Preview
@Composable
fun PrevQrOverlay() {
    PrototypePKMTheme {
        Surface(modifier= Modifier
            .fillMaxSize()
            .background(Color.Red)) {
            TransparentClipLayout(modifier = Modifier ,width = 300.dp, height = 300.dp, offsetY = 150.dp,qrState="")
        }
    }
}