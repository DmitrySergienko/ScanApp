package ru.ds.weatherfirst.presentation.screens.history

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(

): ViewModel(){

    var remove_item = mutableStateOf<String>("")
    private set // можем поменять только из viewModel
    fun removeItem(newItem:MutableState<String>){
        remove_item =newItem
    }
}