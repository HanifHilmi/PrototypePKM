package com.pkm.PrototypePKM.utils

import kotlinx.serialization.Serializable

@Serializable
data class InAgro(
    val user:String,
    val awal_tanam:String,
    val jenis_tanaman:String
)