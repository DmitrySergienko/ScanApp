package ru.ds.weatherfirst.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.newscanapp.presentation.screen.history.HistoryItemScreen
import ru.ds.weatherfirst.presentation.screens.history.HistoryScreen
import ru.ds.weatherfirst.presentation.screens.history.RecyclerHistoryItem
import ru.ds.weatherfirst.presentation.screens.navigation.Screen


@Composable
fun SetupNavGraph(
    navController: NavHostController,
    //weatherState:WeatherState
    //id: Int = 0
) {
    //remove single item from history screen
    // val mainScreenViewModel = hiltViewModel<ru.ds.weatherfirst.presentation.screens.HomeViewModel>()
    // val state by mainScreenViewModel.stateMain.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.History.route
    ) {

        composable(route = Screen.History.route) {
            HistoryScreen(navController = navController)
        }

        composable(route = Screen.HistoryItemScreen.route) {
            HistoryItemScreen(
                navController = navController,
                history = "",
                // historyViewModel = hiltViewModel()
            )
        }

/*        composable(
            route = Screen.UVscreen.route,
            arguments = listOf(navArgument(UV_ARG_KEY) {
                type = NavType.IntType
            })
        ) {
            //тут указываем экран куда переходим, и можно посмотреть есть ли аргументы
            UV_screen(navController = navController, it.arguments?.getInt(UV_ARG_KEY).toString())
        }*/

        /*composable(
            route = Screen.Search.route,
            arguments = listOf(navArgument(HISTORY_ARG_KEY) {
                type = NavType.StringType
            })
        ) {
            SearchScreen(navController = navController, it.arguments?.getString(HISTORY_ARG_KEY))
        }*/

        composable(route = Screen.RecyclerHistoryItem.route) {
            //сохраняю аргумента в backstackEntry
            LaunchedEffect(key1 = it) {
                val result =
                    navController.previousBackStackEntry?.savedStateHandle?.get<String>("city")
                Log.d("VVV", "result $result")
            }
            RecyclerHistoryItem(
                navController = navController,
                //historyViewModel = hiltViewModel()
            )
        }
    }
}