package com.pkm.PrototypePKM.ui.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pkm.PrototypePKM.BottomBarItem
import com.pkm.PrototypePKM.R
import com.pkm.PrototypePKM.ui.navGraph.HomeNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController :NavHostController= rememberNavController()) {
    Scaffold(
        topBar = {},
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)){
            HomeNavGraph(navController = navController)
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarItem.Prediksi,
        BottomBarItem.Beranda,
        BottomBarItem.Feedback
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }

}

@Composable
fun RowScope.AddItem(
    screen: BottomBarItem,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon!!,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        //colors = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            if(navController.currentDestination?.route != screen.route){
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        }
    )
}


@Composable
fun BackHandlerConfirmationDialog(activity: Activity){
    var isDialogShowed by remember { mutableStateOf(false) }
    BackHandler(true) {
        isDialogShowed = true
    }
    if (isDialogShowed){
        AlertDialog(
            confirmButton = {
                TextButton(onClick = {
                    activity.finish()
                }) {
                    Text(text = "Keluar",color= Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDialogShowed = false
                }) {
                    Text(text = "Batalkan")
                }
            },

            onDismissRequest = { isDialogShowed = false },
            title = {
                Text(stringResource(id = R.string.tutup_aplikasi))
            },
            text = {
                Text(stringResource(id = R.string.tutup_aplikasi_desc))
            }
        )
    }
}