package com.pkm.PrototypePKM.utils

import kotlinx.serialization.Serializable

@Serializable
data class InAgro(
    val user:String,
    val awal_tanam:String,
    val jenis_tanaman:String
)


@Serializable
data class InFeedback(
    val hpt:String,
    val foto:String,
    val tanggal:String,
)


class HasilPrediksi(
    val totalHari:Int,
    val inAgro: InAgro,
    val fase1:Int,
    val fase2:Int,
    val fase3:Int,
    val fase4:Int,
    val fase5:Int,
    val fase6:Int,
){
    fun getFaseDesc(fase:Int):String = when(fase){
        1-> "Perkecambahan"
        2 ->  "Pembentukan daun"
        3 -> when(inAgro.jenis_tanaman.lowercase()){
            "buncis" -> "Pembentukan bunga"
            "tomat" -> "Pembentukan cabang"
            "cabai" -> "Pembentukan cabang"
            else -> ""
        }
        4-> when(inAgro.jenis_tanaman.lowercase()) {
            "buncis" -> "Pembentukan polong"
            "tomat" ->  "Pembungaan"
            "cabai" -> "Pembungaan"
            else-> ""
        }
        5-> when(inAgro.jenis_tanaman.lowercase()) {
            "buncis" -> "Panen"
            "tomat" ->  "Pembentukan Buah"
            "cabai" -> "Pembentukan Buah"
            else-> ""
        }
        6-> when(inAgro.jenis_tanaman.lowercase()) {
            "buncis" -> ""
            "tomat" ->  "Panen"
            "cabai" -> "Panen"
            else-> ""
        }
        else-> ""

    }

    fun getTotalHariPerFase(fase:Int):Int = when(fase){
            1-> fase1
            2-> fase1 + fase2
            3-> fase1 + fase2+ fase3
            4-> fase1 + fase2+ fase3 + fase4
            5-> fase1 + fase2+ fase3 + fase4 + fase5
            6-> fase1 + fase2+ fase3 + fase4 + fase5 + fase6
            else-> 0
        }

}

