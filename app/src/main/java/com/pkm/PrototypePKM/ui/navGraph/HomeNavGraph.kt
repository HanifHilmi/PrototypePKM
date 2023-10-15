package com.pkm.PrototypePKM.ui.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.pkm.PrototypePKM.BottomBarItem
import com.pkm.PrototypePKM.ui.screen.beranda.BerandaScreen
import com.pkm.PrototypePKM.ui.screen.beranda.DetailCuacaScreen
import com.pkm.PrototypePKM.ui.screen.feedback.FeedbackScreenNew
import com.pkm.PrototypePKM.ui.screen.prediksi.PrediksiScreen

@Composable
fun HomeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = "graph_home",
        startDestination = BottomBarItem.Beranda.route,
    ){
        composable(route=BottomBarItem.Prediksi.route){
            PrediksiScreen()
        }
        composable(route=BottomBarItem.Beranda.route){
            BerandaScreen(
                onWeatherCardClicked = {
                    navController.navigate("beranda_detail_cuaca")
                }
            )
        }
        composable(route=BottomBarItem.Feedback.route){
            FeedbackScreenNew()
        }

        berandaCuacaDetailGraph(navController)


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


sealed class BerandaDetails(val route:String){
    object MoreInfo :BerandaDetails(route="MOREINFO")
}