package com.pkm.PrototypePKM.ui.screen.feedback

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Top
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pkm.PrototypePKM.R
import com.pkm.PrototypePKM.ui.screen.feedback.sharedFlag

@Composable
fun FeedbackScreenNew() {


    if (!showFeedbackFinish && !sharedFlag){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Text(
                text = "Feedback",
                fontSize = 32.sp, // Menggunakan 'sp' untuk ukuran huruf
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize(align = Alignment.Center)
            )

            Spacer(modifier = Modifier.height(100.dp))
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                shape = CircleShape
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_mail), // Ganti dengan gambar Anda
                    contentDescription = null, // Isi dengan deskripsi gambar
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    showFeedbackFinish = true
                }
            ) {
                Text(text = "Upload FeedBack")
            }

            Spacer(modifier = Modifier.height(22.dp))

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    text = "Cara Melaporkan :\n\n" +
                            "1. Scan QR Code yang telah diberikan untuk menerima akses\n" +
                            "2. Masukkan keluhan pada kolom feedback seperti penyakit, cuaca saat hari kejadian\n" +
                            "3. Masukkan tanggal kejadian\n" +
                            "4. Masukkan foto tanaman saat kejadian\n\n" +
                            "*Tambahan\n" +
                            "Pelaporan anda akan digunakan untuk memperbaiki model yang telah dibuat",
                    textAlign = TextAlign.Justify
                )
            }
        }
    }else if(showFeedbackFinish) {
        FeedbackFinish()
    }else if (sharedFlag){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Terimakasih",
                fontSize = 32.sp, // Menggunakan 'sp' untuk ukuran huruf
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .wrapContentSize(align = Alignment.Center)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Feedback Terkirim",
                fontSize = 18.sp, // Menggunakan 'sp' untuk ukuran huruf
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .wrapContentSize(align = Alignment.Center)
            )

            Spacer(modifier = Modifier.height(100.dp))
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                shape = CircleShape
            ) {
                Spacer(modifier = Modifier.height(40.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_check), // Ganti dengan gambar Anda
                    contentDescription = null, // Isi dengan deskripsi gambar
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    showFeedbackFinish = false
                    sharedFlag = false
                }
            ) {
                Text(text = "Lanjut")
            }

            Spacer(modifier = Modifier.height(22.dp))

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    text =  "Terimakasih atas feedback yang anda berikan \n" +
                            "Feedback anda akan digunakan untuk membuat pemrosesan data yang lebih baik lagi",
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}


@Preview
@Composable
fun FeedbackPrev() {

    Surface {
        FeedbackScreenNew()
    }

}