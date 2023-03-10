package com.example.newscanapp


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.newscanapp.data.HistoryDatabase
import com.example.newscanapp.data.TestDB
import com.example.newscanapp.databinding.FragmentHistoryBinding
import com.example.newscanapp.presentation.utils.BaseFragment

const val MY_TAG ="VVV"

class History : BaseFragment<FragmentHistoryBinding>(FragmentHistoryBinding::inflate) {



    //navigation between screens
    lateinit var navController: NavHostController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.composeViewInitialLayout.setContent {
            MaterialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    navController = rememberNavController()

                    val db =
                        Room.databaseBuilder(requireContext(), HistoryDatabase::class.java, "new_db2").build()
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
                                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = name.name)
                                    Text(text = name.emirates_id)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}