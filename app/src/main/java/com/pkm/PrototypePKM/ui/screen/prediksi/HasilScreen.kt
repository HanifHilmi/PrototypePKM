package com.pkm.PrototypePKM.ui.screen.prediksi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pkm.PrototypePKM.utils.HasilPrediksi
import com.pkm.PrototypePKM.utils.InAgro
import com.pkm.PrototypePKM.utils.formatDateTimeForPredisi


@Composable
fun ShowHasilPrediksi(hasilPrediksi: HasilPrediksi,retryInput:()->Unit) {


    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Card(modifier= Modifier.padding(bottom = 12.dp)) {
            Column(Modifier.padding(12.dp)) {

                Text(
                    text = "Jenis Tanaman: ${hasilPrediksi.inAgro.jenis_tanaman}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Tanggal Tanam: ${formatDateTimeForPredisi(hasilPrediksi.inAgro.awal_tanam)}",
                    style = MaterialTheme.typography.titleMedium
                )


            }
        }

        Card(modifier = Modifier.padding(bottom = 12.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                hasilPrediksi.let {
                    Text(text = "Prediksi total Waktu Panen:")
                    Text(text = "${it.totalHari} Hari ", fontWeight = FontWeight.Bold)
                    Text(text = "Estimasi tanggal Panen")
                    Text(text = formatDateTimeForPredisi(it.inAgro.awal_tanam,it.totalHari), fontWeight = FontWeight.Bold)
                }
            }
        }
        Card(modifier = Modifier.padding(bottom = 12.dp)){
            Column(modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                ) {
                Text(text = "Rincian: ")
                RincianPrediksi(hasilPrediksi = hasilPrediksi)

                Spacer(modifier = Modifier.padding(8.dp))

            }
        }
        Row{
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { retryInput()}) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Ulangi")
                Text(text = "Ulangi Input")
            }
        }
    }
}

@Composable
fun RincianPrediksi(hasilPrediksi: HasilPrediksi) {

    hasilPrediksi.let {
        for (i in 1..6){
            Spacer(modifier = Modifier.padding(4.dp))
            if (it.getFaseDesc(i).isNotEmpty()){
                Text(text = "Fase ke-$i: ${it.getFaseDesc(i)}", fontWeight = FontWeight.Bold)
                Text(text = "Estimasi Waktu: ${it.getTotalHariPerFase(i)} hari setelah tanam")
                Text(text = "(${formatDateTimeForPredisi(hasilPrediksi.inAgro.awal_tanam,it.getTotalHariPerFase(i))})")
            }
        }

    }

}

@Preview
@Composable
fun HasilCardPrev() {
    MaterialTheme{
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(16.dp)) {
                ShowHasilPrediksi(HasilPrediksi(
                    totalHari = 27,
                    InAgro("test","2023-10-17","Buncis"),
                    fase1 = 2,
                    fase2 = 2,
                    fase3 = 13,
                    fase4 = 3,
                    fase5 = 7,
                    fase6 = 0
                ),{

                }
                )
            }
        }
    }
}