package com.pkm.PrototypePKM.ui.screen.beranda


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pkm.PrototypePKM.R
import com.pkm.PrototypePKM.utils.API_TEST
import com.pkm.PrototypePKM.utils.Alat1
import com.pkm.PrototypePKM.utils.Alat2
import com.pkm.PrototypePKM.utils.ForecastWeatherData
import com.pkm.PrototypePKM.viewModels.BerandaViewModel

@Composable
fun BerandaScreen(
    onWeatherCardClicked: () -> Unit,
    berandaViewModel: BerandaViewModel= viewModel()
) {

    val alat1Data by berandaViewModel.latestAlat1Data.collectAsState()
    val alat2Data by berandaViewModel.latestAlat2Data.collectAsState()

    LaunchedEffect(key1 = alat1Data,key2 = alat2Data){
        Log.d(API_TEST,"getLatestData Alat1${alat1Data.toString()}")
        Log.d(API_TEST,"getLatestData Alat2:${alat2Data.toString()}")
    }


    BerandaContent(alat1Data,alat2Data, onWeatherCardClicked = onWeatherCardClicked)


}

@Composable
fun BerandaContent(
    alat1:Alat1?,
    alat2:Alat2?,
    onWeatherCardClicked: () -> Unit = {}
) {

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())) {

            Divider(thickness = 2.dp,modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp))
            Text(text = "Cuaca saat ini", style = MaterialTheme.typography.labelLarge)
            if (alat1 != null && alat2 != null){
                WeatherCard(dataAlat1 = alat1, dataAlat2 = alat2){
                    onWeatherCardClicked()
                }
            }
            Divider(thickness = 2.dp,modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp))
            Text(text = "Prediksi Cuaca", style = MaterialTheme.typography.labelLarge)
            ForecastCard(
                listOf(
                    ForecastWeatherData("Kamis, 5 Oktober 2023","Cerah"),
                    ForecastWeatherData("Jumat, 6 Oktober 2023","Cerah Berawan"),
                    ForecastWeatherData("Sabtu, 7 Oktober 2023","Hujan Ringan"),
                    ForecastWeatherData("Minggu, 8 Oktober 2023","Hujan"),

                    )
            )
            Divider(thickness = 2.dp,modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp))
            Text(text = "Early Warning", style = MaterialTheme.typography.labelLarge)
            EarlyWarningCard()
        }
    }
}


@Composable
fun WeatherCard(
    dataAlat1: Alat1,
    dataAlat2: Alat2,
    onWeatherCardClicked: ()-> Unit = {}
) {
    Card(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)) {

        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {

                Text(text = "Rabu, 4 Oktober 2023", style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = "-Cuaca-")

            }
            Icon(
                painter = painterResource(R.drawable.icon_cuaca_cerah),
                contentDescription = "icon cuaca",
                modifier= Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f))
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
            .horizontalScroll(rememberScrollState())
            .clickable { onWeatherCardClicked() },
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        WeatherItemCard(iconID = R.drawable.baseline_device_thermostat_24, title = "Suhu" , content =  "${dataAlat1.suhu} \n\u2103" )
        WeatherItemCard(iconID = R.drawable.noun_humidity_3780917, title = "Kelembaban" , content =  "${dataAlat1.kelembaban}\n%" )
        WeatherItemCard(iconID = R.drawable.wi_strong_wind, title = "Kecepatan angin" , content =  "${dataAlat1.kec_angin}\nm/s" )
        WeatherItemCard(iconID = R.drawable.wi_raindrops, title = "curah hujan" , content =  "${dataAlat2.curah_hujan}\nmm" )
        WeatherItemCard(iconID = R.drawable.wi_hot, title = "Radiasi Matahari" , content =  "${dataAlat1.radiasi} \nW/m^2 " )
    }
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Text(text = "Pembaharuan terakhir: ", style = MaterialTheme.typography.labelSmall)
            Text(text = "${dataAlat1.waktu},${dataAlat1.tanggal} ", style = MaterialTheme.typography.labelSmall)
            Text(text = "${dataAlat2.waktu},${dataAlat2.tanggal} ", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun WeatherItemCard(
    iconID: Int,
    title: String, content: String,
    modifier: Modifier = Modifier
) = Card(
        modifier = modifier
            .padding(bottom = 16.dp)
            .padding(horizontal = 4.dp)
            .width(100.dp)

    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
//            Row(
//                modifier = Modifier.padding(4.dp),
//            ) {
//            }
            Text(text = title,style=MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier
                .padding(2.dp)
                .weight(1f))
            Icon(
                painterResource(id = iconID),
                contentDescription = "icon $title",
                modifier = Modifier.width(30.dp).aspectRatio(1f)
            )
            Text(text = content, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }





@Composable
fun ForecastCard(list:List<ForecastWeatherData>) {

    LazyRow(
        modifier = Modifier
            .padding(bottom = 16.dp, top = 8.dp)

    ){
        items(list){data ->
            PrediksiHarianCard(
                date = data.tanggal,
                cuaca = data.cuaca,
                iconID = data.getIconId()
            )
        }
    }


}


@Composable
fun PrediksiHarianCard(
    date:String,cuaca:String, iconID:Int
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .width(IntrinsicSize.Min)

    ){
        Column(modifier =Modifier.padding(12.dp)){
            Text(text = date)
            Icon(
                painterResource(id = iconID),
                contentDescription = null,
                modifier = Modifier
                    .width(90.dp)
                    .aspectRatio(1f))
            Text(text = cuaca)
        }
    }
}


@Composable
fun EarlyWarningCard() {
    Card(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)){
        Column(modifier =Modifier.padding(12.dp)) {
            Text(text = "Tidak Ada peringatan untuk saat ini")
        }
    }
}

@Preview
@Composable
fun BerandaPrev() {
    MaterialTheme{
        Surface {
            BerandaContent(
                Alat1(
                    id = "19567",
                    tanggal = "16-10-2023",
                    waktu= "12:07:11",
                    suhu= "29.25",
                    kelembaban="59.95",
                    kec_angin= "0.00",
                    radiasi = "868.05"
                ),
                alat2 = Alat2(
                    id = "19567",
                    tanggal = "16-10-2023",
                    waktu= "12:07:11",
                    curah_hujan =  "0.00",
                )
            )
        }
    }
}