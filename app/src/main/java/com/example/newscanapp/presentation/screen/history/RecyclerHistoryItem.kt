package ru.ds.weatherfirst.presentation.screens.history


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.room.Room
import com.example.newscanapp.data.HistoryDatabase
import com.example.newscanapp.presentation.screen.history.HistoryItemScreen


@Composable
fun RecyclerHistoryItem(
    navController: NavController,
   // historyViewModel: ru.ds.weatherfirst.presentation.screens.HomeViewModel
) {


    //====Database===========
    val db =
        Room.databaseBuilder(LocalContext.current, HistoryDatabase::class.java, "new_db").build()
    val dao = db.historyDao()

    val result by dao.readAll().collectAsState(initial = emptyList())

    // add "name" to list from db
    val listHistory = mutableListOf<String>()
    for (i in result) listHistory.add(i.name)
    //==============

    //remove single item from listHistory
   // val item_remove = historyViewModel.remove_item
    //if(item_remove.isNotEmpty()&& item_remove=="") listHistory.remove(item_remove)

    MaterialTheme() {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(listHistory) { name ->
                //для каждого элемента мы Запускаем:
                HistoryItemScreen(
                    history = name,
                    navController,
                    //historyViewModel = hiltViewModel()
                )
            }
        }
    }
}