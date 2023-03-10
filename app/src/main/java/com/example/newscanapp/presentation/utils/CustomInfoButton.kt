package com.example.newscanapp.presentation.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun CustomInfoButton(
    text: String,
    navController: NavController,
    icon: Painter,
    index: Int,
){
    TextButton(
        onClick = {

        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent,

        )
    ) {
        Icon(
            painter = icon,
            contentDescription = "Custom_button",
            modifier = Modifier.size(34.dp)
        )
        Spacer(modifier = Modifier.width(14.dp))
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal
        )
    }
}