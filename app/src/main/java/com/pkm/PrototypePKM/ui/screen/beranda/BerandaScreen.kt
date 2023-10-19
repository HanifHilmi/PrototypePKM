package com.pkm.PrototypePKM.ui.screen.beranda


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pkm.PrototypePKM.R
import com.pkm.PrototypePKM.utils.API_TEST
import com.pkm.PrototypePKM.utils.Alat1
import com.pkm.PrototypePKM.utils.Alat2
import com.pkm.PrototypePKM.utils.currentDateTime
import com.pkm.PrototypePKM.utils.formatDateAndTime
import com.pkm.PrototypePKM.utils.hasPassedDateTime
import com.pkm.PrototypePKM.viewModels.BerandaViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BerandaScreen(
    onWeatherCardClicked: () -> Unit,
    onAboutUsClicked: () -> Unit,
    berandaViewModel: BerandaViewModel= viewModel()
) {

    val alat1Data by berandaViewModel.latestAlat1Data.collectAsState()
    val alat2Data by berandaViewModel.latestAlat2Data.collectAsState()
    val dataForecast by berandaViewModel.dataCuacaList.collectAsState()


    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    var currentTime by remember {mutableStateOf(timeFormat.format(Date()))}


    LaunchedEffect(key1 = alat1Data,key2 = alat2Data){
        Log.d(API_TEST,"getLatestData Alat1${alat1Data.toString()}")
        Log.d(API_TEST,"getLatestData Alat2:${alat2Data.toString()}")
    }

    LaunchedEffect(key1 = Unit){
        while (true) {
            currentTime = timeFormat.format(Date())
            delay(1000) // Delay selama 1 detik
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                onAboutUsClicked = onAboutUsClicked
            )
        }
    ){
        BerandaContent(
            modifier = Modifier.padding(it),
            alat1 = alat1Data,
            alat2 = alat2Data,
            currentTime = currentTime,
            onWeatherCardClicked = onWeatherCardClicked,
            dataForecast = dataForecast,
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TopBar(
    onAboutUsClicked:()-> Unit ={}
) {
    TopAppBar(
        title = {
            Row(modifier = Modifier
                .padding(6.dp)
                .height(IntrinsicSize.Min)) {
                Text(text = "Selamat datang di CIPTA: Cuaca & Iklim Pertanian",style = MaterialTheme.typography.titleLarge)

            }
                },
        modifier = Modifier,
        navigationIcon = {
                 IconButton(onClick = { }) {
                     Image(
                         painterResource(id = R.drawable.logo_pkm),
                         contentDescription = "",
                         contentScale = ContentScale.Fit,
                         modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                     )
                 }


        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(),
        actions = {
            IconButton(
                onClick = {
                    onAboutUsClicked()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Info"
                )
            }
        },
        scrollBehavior = null
    )

}

@Composable
fun BerandaContent(
    modifier:Modifier = Modifier,
    alat1: Alat1?,
    alat2: Alat2?,
    currentTime:String,
    onWeatherCardClicked: () -> Unit = {},
    dataForecast: List<Triple<String, String, String>>
) = Box(modifier = modifier.fillMaxSize()){
        Column(modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())) {

            Divider(thickness = 2.dp,modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp))
            Text(text = "Cuaca saat ini", style = MaterialTheme.typography.labelLarge)
            if (alat1 != null && alat2 != null){
                WeatherCard(
                    dataAlat1 = alat1,
                    dataAlat2 = alat2,
                    currentTime = currentTime,
                    onWeatherCardClicked = onWeatherCardClicked
                )
            }else
                Loading()
            Divider(thickness = 2.dp,modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp))
            Text(text = "Prediksi Cuaca", style = MaterialTheme.typography.labelLarge)

            if(dataForecast.isNotEmpty())
                ForecastCard(dataForecast)
            else
                Loading()

            if (alat1 != null && alat2 != null) {
                EarlyWarningCard(
                    alat1.kec_angin.toFloat(),
                    alat2.curah_hujan.toFloat(),
                )
            }
        }
    }


@Composable
fun Loading() {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        CircularProgressIndicator()
        Text(text = "Loading..")
    }
}

@Composable
fun WeatherCard(
    dataAlat1: Alat1,
    dataAlat2: Alat2,
    currentTime:String,
    onWeatherCardClicked: ()-> Unit = {}
) {
    Card(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)) {

        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Max), horizontalArrangement = Arrangement.SpaceBetween) {
            val kodeCuaca = penenentuanCodeCuaca(dataAlat2.curah_hujan.toFloat())
            Column(modifier = Modifier.weight(1f)) {

                Text(text = currentDateTime().first, style = MaterialTheme.typography.labelLarge)
                Text(text = currentTime, style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.padding(8.dp))
                Text(text = getKondisiCuaca(kodeCuaca))

            }
            Icon(
                painter = painterResource(getIDiconCuaca(kodeCuaca)),
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
        WeatherItemCard(iconID = R.drawable.wi_strong_wind, title = "Kecepatan Angin" , content =  "${dataAlat1.kec_angin}\nm/s" )
        WeatherItemCard(iconID = R.drawable.wi_raindrops, title = "Curah Hujan" , content =  "${dataAlat2.curah_hujan}\nmm" )
        WeatherItemCard(iconID = R.drawable.wi_hot, title = "Radiasi Matahari" , content =  "${dataAlat1.radiasi} \nW/m\u00B2 " )
    }
    Card (modifier = Modifier.fillMaxSize()){
        Column (modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()){
            Text(text = "Update terakhir: ", style = MaterialTheme.typography.labelSmall)
            Text(text = "${"Alat 1. "}${dataAlat1.waktu},${dataAlat1.tanggal} ", style = MaterialTheme.typography.labelSmall)
            Text(text = "${"Alat 2. "}${dataAlat2.waktu},${dataAlat2.tanggal} ", style = MaterialTheme.typography.labelSmall)

        }
    }
}

fun penenentuanCodeCuaca(curahHujan: Float):Int{
    return if (curahHujan in 0.1..5.0)  60
    else if (curahHujan > 5.0 && curahHujan <= 20.0)  61
    else if (curahHujan > 20.0) 63
    else 0
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
            .height(150.dp)

    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
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
                modifier = Modifier
                    .width(30.dp)
                    .aspectRatio(1f)
            )
            Text(text = content, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }



@Composable
fun ForecastCard(list:List<Triple<String,String,String>>) {

    LazyRow(
        modifier = Modifier
            .padding(bottom = 16.dp, top = 8.dp)

    ){
        items(list){data ->
            PrediksiHarianCard(
                date = data.second,
                code = data.first.toInt(),
                //iconID = data.getIconId()
            )
        }
    }
    Row(modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "",modifier = Modifier.fillMaxHeight())
        Column {
            Text(text = "Lokasi, Kab. Bandung",style = MaterialTheme.typography.labelSmall)
            Text(text = "Sumber data: data.bmkg.go.id",style = MaterialTheme.typography.labelSmall)
        }
    }


}


fun getKondisiCuaca(code:Int):String{
    return when (code) {
        0 -> "Cerah"
        1, 2 -> "Cerah Berawan"
        3 -> "Berawan"
        4 -> "Berawan Tebal"
        5 -> "Udara Kabur"
        10 -> "Asap"
        45 -> "Kabut"
        60 -> "Hujan Ringan"
        61 -> "Hujan Sedang"
        63 -> "Hujan Lebat"
        80 -> "Hujan Lokal"
        95, 97 -> "Hujan Petir"
        else -> "Kondisi tidak diketahui"
    }
}

fun getIDiconCuaca(code:Int):Int{
    return when (code) {
        0 -> R.drawable.icon_cuaca_cerah
        1, 2 -> R.drawable.icon_cuaca_cerah_berawan
        3 -> R.drawable.icon_cuaca_berawan
        4 -> R.drawable.icon_cuaca_berawan_tebal
        5 -> R.drawable.icon_cuaca_udara_kabur
        10 -> R.drawable.icon_cuaca_asap
        45 -> R.drawable.icon_cuaca_kabut
        60 -> R.drawable.icon_cuaca_hujan_ringan
        61 -> R.drawable.icon_cuaca_hujan_sedang
        63 -> R.drawable.icon_cuaca_hujan_lebat
        80 -> R.drawable.icon_cuaca_hujan_ringan
        95, 97 -> R.drawable.icon_cuaca_hujan_petir
        else -> 0
    }
}


@Composable
fun PrediksiHarianCard(
    date:String,
    code:Int,

) {
    val hasTimePassed = hasPassedDateTime(date)
    val localeDateFormat = formatDateAndTime(date)

    if (!hasTimePassed){
        Card(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .width(IntrinsicSize.Min)

        ){


            Column(modifier =Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = localeDateFormat.first, textAlign = TextAlign.Center)
                Text(text = localeDateFormat.second)
                Icon(
                    painterResource(id = getIDiconCuaca(code)),
                    contentDescription = null,
                    modifier = Modifier
                        .width(90.dp)
                        .aspectRatio(1f))
                Text(text = getKondisiCuaca(code))
            }
        }
    }
}



@Composable
fun EarlyWarningCard(
    kecAngin: Float,
    curahHujan: Float,
) {
    Divider(thickness = 2.dp,modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp))
    Text(text = "Early Warning", style = MaterialTheme.typography.labelLarge)
    Card(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)){
        Row(modifier =Modifier.padding(12.dp)) {
            if (kecAngin >= 10.2889f && curahHujan >= 20.0f){
                Icon(imageVector = Icons.Default.Warning,contentDescription = null)
                Spacer(modifier = Modifier.padding(6.dp))
                Text(text = "Waspada! Hujan lebat disertai Angin kencang ")
            }else if(kecAngin >= 10.2889f){
                Icon(imageVector = Icons.Default.Warning,contentDescription = null)
                Text(text = "Waspada! Angin Kencang")
            }else if(curahHujan >= 20.0f){
                Icon(imageVector = Icons.Default.Warning,contentDescription = null)
                Text(text = "Waspada! Hujan Lebat")
            }else{
                Text(text = "Tidak Ada peringatan untuk saat ini")
            }

        }
    }
}

@Preview
@Composable
fun BerandaPrev() {
    MaterialTheme{
        Surface {
            BerandaContent(
                alat1= Alat1(
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
                    curah_hujan =  "20.00",
                ),
                currentTime= "12:07:11",
                dataForecast = emptyList(),
            )
        }
    }
}