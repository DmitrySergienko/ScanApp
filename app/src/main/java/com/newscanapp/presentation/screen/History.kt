package com.newscanapp.presentation.screen


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.newscanapp.data.HistoryDatabase
import com.newscanapp.data.TestDB
import com.newscanapp.databinding.FragmentHistoryBinding
import com.newscanapp.presentation.utils.BaseFragment


class History : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeViewInitialLayout.setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    ItemList(context = requireContext())
                }
            }
        }
    }
}

@Composable
fun ItemList(context:Context){

    //room db

    val db = Room.databaseBuilder(context, HistoryDatabase::class.java, "new_db2").build()
    val dao = db.historyDao()

    val result by dao.readAll().collectAsState(initial = emptyList())

    val listHistory = mutableListOf(TestDB(0,"Name","ID"))

    // add to list from db
    for (i in result) listHistory.add(TestDB(0,i.name,i.emirates_id))

    MaterialTheme() {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(listHistory) { name ->
                //для каждого элемента мы Запускаем:
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = name.name)
                    Text(text = name.emirates_id)
                }
            }
        }
    }
}