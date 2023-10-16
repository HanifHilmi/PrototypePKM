package com.pkm.PrototypePKM.ui.screen.prediksi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.pkm.PrototypePKM.R
import java.time.LocalDate

@Composable
fun PrediksiScreen() {
    val listTanaman = listOf("Buncis", "Tomat", "Cabe","Sawi Putih")

    var selectedDate by remember {mutableStateOf<LocalDate?>(LocalDate.now())}
    var selectedTanaman by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.padding(16.dp)) {
            Card(modifier = Modifier.padding(bottom=16.dp)) {
                Column(modifier =Modifier.padding(16.dp)) {
                    Text(text = "Prediksi Panen Tanaman", style = MaterialTheme.typography.headlineSmall,modifier = Modifier.fillMaxWidth())
                }
            }
            Card{
                PilihJenisTanaman(
                    listTanaman = listTanaman,
                    tanamanSelected = selectedTanaman,
                    changeTanaman = { selectedTanaman = it }
                )
                PickTanggalTanam(
                    selectedDate = selectedDate,
                    changeDate = { selectedDate = it }
                )
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {},
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(text = "Proses")
                    }
                }
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PilihJenisTanaman(
    listTanaman:List<String>,
    tanamanSelected:String,
    changeTanaman:(String)-> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        expanded = isExpanded, onExpandedChange = { isExpanded = it }
    ) {
        TextField(
            label = {
                Text(text = "Pilih Jenis Tanaman")
            },
            value = tanamanSelected,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            for (tanaman in listTanaman){
                DropdownMenuItem(
                    text = { Text(text = tanaman) },
                    onClick = {
//                        tanamanSelected = tanaman
                        changeTanaman(tanaman)
                        isExpanded = false
                    })
            }


        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickTanggalTanam(
    selectedDate: LocalDate?,
    changeDate: (LocalDate)->Unit
){
    //var selectedDate = remember {mutableStateOf<LocalDate?>(LocalDate.now())}
    val calendarState = rememberUseCaseState()

    TextField(
        label = {
            Text(text = "Pilih Tanggal Tanam")
        },
        value = selectedDate.toString(),
        onValueChange = {},
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { calendarState.show() }) {
                Icon(painter = painterResource(id = R.drawable.baseline_calendar_month_24), contentDescription ="" )
            }
        },
        colors = ExposedDropdownMenuDefaults.textFieldColors(),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
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
//            selectedDate = newDate
            changeDate(newDate)
        }
    )

}





@Preview
@Composable
fun PrediksiPrev() {
    MaterialTheme{
        Surface(modifier = Modifier.fillMaxSize()) {
            PrediksiScreen()
        }
    }
}