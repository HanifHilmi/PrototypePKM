package com.pkm.PrototypePKM

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarItem(
    val route: String,
    val title: String,
    val icon: ImageVector?
) {
    object Prediksi : BottomBarItem(
        route = "Prediksi",
        title = "Prediksi",
        icon = Icons.Default.List
    )

    object Beranda : BottomBarItem(
        route = "Beranda",
        title = "Beranda",
        icon = Icons.Default.Home
    )

    object Feedback : BottomBarItem(
        route = "Feedback",
        title = "Feedback",
        icon = Icons.Default.Email
    )

}
