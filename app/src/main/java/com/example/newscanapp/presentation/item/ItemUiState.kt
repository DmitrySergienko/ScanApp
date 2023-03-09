package com.example.newscanapp.presentation.item

import com.example.newscanapp.data.Item

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val id: Int = 0,
    val name: String = "",
    val emirates_id: Int = 0,

)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemUiState.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun ItemUiState.toItem(): Item = Item(
    id = id,
    name = name,
    emirates_id = emirates_id ?: 0
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Item.toItemUiState(actionEnabled: Boolean = false): ItemUiState = ItemUiState(
    id = id,
    name = name,
    emirates_id =emirates_id

)

fun ItemUiState.isValid() : Boolean {
    return name.isNotBlank()
}
