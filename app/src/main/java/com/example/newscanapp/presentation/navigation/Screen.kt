package ru.ds.weatherfirst.presentation.screens.navigation

const val UV_ARG_KEY = "uv_argument"
const val HISTORY_ARG_KEY = "history_argument"

sealed class Screen(val route:String){

    object Home: Screen(route = "home_screen")
    object HistoryItemScreen: Screen(route = "history_item_screen")
    object History: Screen(route = "history_screen")
    object RecyclerHistoryItem: Screen(route = "recycler_history_item")

/*    object UVscreen: Screen(route = "UV_screen/{$UV_ARG_KEY}"){
        fun passUVARG(uv_arg: Int):String{
            return "UV_screen/$uv_arg"
        }
    }

    object Search: Screen(route = "search_screen/{$HISTORY_ARG_KEY}"){
        fun passHistoryArg(arg:String):String{
            return "search_screen/$arg"
        }
    }*/
}
