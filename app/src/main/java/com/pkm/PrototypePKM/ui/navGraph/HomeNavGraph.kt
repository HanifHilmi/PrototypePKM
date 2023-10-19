package com.pkm.PrototypePKM.ui.navGraph

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pkm.PrototypePKM.BottomBarItem
import com.pkm.PrototypePKM.ui.screen.BackHandlerConfirmationDialog
import com.pkm.PrototypePKM.ui.screen.beranda.AboutUsScreen
import com.pkm.PrototypePKM.ui.screen.beranda.BerandaScreen
import com.pkm.PrototypePKM.ui.screen.beranda.DetailCuacaScreen
import com.pkm.PrototypePKM.ui.screen.feedback.FeedbackScreenNew
import com.pkm.PrototypePKM.ui.screen.prediksi.PrediksiScreen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    val context = LocalContext.current as Activity
    NavHost(
        navController = navController,
        route = "graph_home",
        startDestination = BottomBarItem.Beranda.route,
    ){

        composable(route=BottomBarItem.Prediksi.route){
            BackHandlerConfirmationDialog(activity = context)
            PrediksiScreen()
        }
        composable(route=BottomBarItem.Beranda.route){
            BackHandlerConfirmationDialog(activity = context)
            BerandaScreen(
                onWeatherCardClicked = {
                    navController.navigate("beranda_detail_cuaca")
                },
                onAboutUsClicked = {
                    navController.navigate("beranda_about_us")
                }
            )
        }
        composable(route=BottomBarItem.Feedback.route){
            BackHandlerConfirmationDialog(activity = context)
            FeedbackScreenNew()
        }


        berandaCuacaDetailGraph(navController)
        berandaAboutUsGraph(navController)

    }
}

fun NavGraphBuilder.berandaCuacaDetailGraph(navController: NavHostController){
    navigation(
        route = "beranda_detail_cuaca",
        startDestination = BerandaDetails.MoreInfo.route
    ){
        composable(route = BerandaDetails.MoreInfo.route){
            DetailCuacaScreen()
        }
    }
}

fun NavGraphBuilder.berandaAboutUsGraph(navController: NavHostController){
    navigation(
        route = "beranda_about_us",
        startDestination = BerandaDetails.AboutUs.route
    ){
        composable(route = BerandaDetails.AboutUs.route){
            AboutUsScreen()
        }
    }
}


sealed class BerandaDetails(val route:String){
    object MoreInfo :BerandaDetails(route="MOREINFO")
    object AboutUs :BerandaDetails(route="AboutUs")
}