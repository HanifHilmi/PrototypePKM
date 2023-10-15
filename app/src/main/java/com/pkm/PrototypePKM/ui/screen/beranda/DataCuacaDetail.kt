package com.pkm.PrototypePKM.ui.screen.beranda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.pkm.PrototypePKM.utils.WeatherData
import com.pkm.PrototypePKM.viewModels.BerandaViewModel
import kotlin.math.roundToInt

@Composable
fun DetailCuacaScreen() {
    val viewModel :BerandaViewModel= viewModel()
    val dataCuaca100 by viewModel.weatherData.collectAsState()


    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        viewModel.fetchMultiData()
        if (dataCuaca100.isNotEmpty()){
            GrafikSuhu(dataCuaca100)
            GrafikKelembaban(dataCuaca100)
        }else{
            Text(text = "Loading...")
        }

    }
}




@Composable
fun GrafikSuhu(dataCuaca100: List<WeatherData>) {

    val pointsData: List<Point> = dataCuaca100
        .asReversed()
        .map { weatherData ->
            val id = weatherData.alat1_id.toFloat()
            val suhu = weatherData.alat1_suhu.toFloat()
            Point(id, suhu)
        }
    val maxPlotData = dataCuaca100.maxOf { it.alat1_suhu.toFloat() }

    val minPlotData = dataCuaca100.minOf { it.alat1_suhu.toFloat() }

    val xAxisData = AxisData.Builder()
        .axisStepSize(25.dp)
        .backgroundColor(Color.Transparent)
        .steps(pointsData.size - 1)
        .labelData { i->
            dataCuaca100.asReversed()[i].alat1_waktu
        }
        .labelAndAxisLinePadding(2.dp)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelAngle(70f)
        .axisLabelDescription { string->"string" }
        .build()

    val yAxisData = AxisData.Builder()
        .steps((maxPlotData-minPlotData).roundToInt())
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(25.dp)
        .labelData {i->
            (i+minPlotData.roundToInt()).toString()
        }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()


    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        lineType = LineType.Straight()
                    ),
                    IntersectionPoint(color = MaterialTheme.colorScheme.tertiary),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.primary),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.inversePrimary,
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp(popUpLabel = { id, suhu ->
                        "$suhu ℃"
                    })
                )
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        bottomPadding=40.dp,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant, alpha = .5f)
    )
    Divider()
    Text(text = "Grafik Suhu",style=MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical =6.dp))
    LineChart(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp), lineChartData = lineChartData )
    //Text(text = pointsData.toString())
}

@Composable
fun GrafikKelembaban(dataCuaca100: List<WeatherData>) {

    val pointsData: List<Point> = dataCuaca100
        .asReversed()
        .map { weatherData ->
            val id = weatherData.alat1_id.toFloat()
            val suhu = weatherData.alat1_kelembaban.toFloat()
            Point(id, suhu)
        }
    val maxPlotData = dataCuaca100.maxOf { it.alat1_kelembaban.toFloat() }

    val minPlotData = dataCuaca100.minOf { it.alat1_kelembaban.toFloat() }

    val xAxisData = AxisData.Builder()
        .axisStepSize(25.dp)
        .backgroundColor(Color.Transparent)
        .steps(pointsData.size - 1)
        .labelData { i->
            dataCuaca100.asReversed()[i].alat1_waktu
        }
        .labelAndAxisLinePadding(2.dp)
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelAngle(70f)
        .axisLabelDescription { string->"string" }
        .build()

    val yAxisData = AxisData.Builder()
        .steps((maxPlotData-minPlotData).roundToInt())
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(25.dp)
        .labelData {i->
            (i+minPlotData.roundToInt()).toString()
        }
        .axisLineColor(MaterialTheme.colorScheme.tertiary)
        .axisLabelColor(MaterialTheme.colorScheme.tertiary)
        .build()


    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        lineType = LineType.Straight()
                    ),
                    IntersectionPoint(color = MaterialTheme.colorScheme.tertiary),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.primary),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.inversePrimary,
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp(popUpLabel = { id, kelembaban ->
                        "$kelembaban %"
                    })
                )
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        bottomPadding=40.dp,
        gridLines = GridLines(color = MaterialTheme.colorScheme.outlineVariant, alpha = .5f)
    )

    Divider()
    Text(text = "Grafik Kelembaban",style=MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical =6.dp))
    LineChart(modifier = Modifier
        .fillMaxWidth()
        .height(300.dp), lineChartData = lineChartData )
}

