package com.example.newscanapp.presentation.screen.history

//import androidx.compose.ui.text.style.TextForegroundStyle.Unspecified.brush
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.example.newscanapp.R
import com.example.newscanapp.data.HistoryDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import ru.ds.weatherfirst.presentation.screens.navigation.Screen

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun HistoryItemScreen(
    history: String,
    navController: NavController,
   // historyViewModel: com.example.newscanapp.presentation.screen.HomeViewModel
) {
    //====Database
    val db = Room.databaseBuilder(LocalContext.current, HistoryDatabase::class.java, "new_db").build()
    val dao = db.historyDao()

    //===========swipe========
    val archive = SwipeAction(
        onSwipe = {
            //remove item from database
            GlobalScope.launch {
                val item = dao.getByName(history)
                dao.deleteItem(item)
            }
            //send arg to search screen to remove single (this) item from the list
           // historyViewModel.passItem(history)
            navController.navigate(route = Screen.History.route) //update history screen
        },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.ic_baseline_clear_24),
                contentDescription = null,
               // tint = BlueLight
            )
        },
        background = Color.Transparent
    )
    val clean = SwipeAction(
        onSwipe = {
            //remove item from database
            GlobalScope.launch {
                val item = dao.getByName(history)
                dao.deleteItem(item)
            }
            //send arg to search screen to remove single (this) item from the list
           // historyViewModel.passItem(history)
            navController.navigate(route = Screen.History.route) //update history screen
        },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.ic_baseline_clear_24),
                contentDescription = null,
                //tint = BlueLight
            )
        }, background = Color.Transparent
    )

    //==========shimmer effect option =========
/*    val shimmentColors = listOf(
        BlueLight.copy(alpha = 0.7f),
        BlueLight.copy(alpha = 0.1f),
        BlueLight.copy(alpha = 0.4f),
    )*/

    /*val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing,
            ),
            repeatMode = RepeatMode.Reverse
        ),*/

/*        )
    val brush = Brush.linearGradient(
        colors = shimmentColors,
        start = Offset.Zero,
        end = Offset(
            x = translateAnim.value,
            y = translateAnim.value,
        )
    )*/

    //================swipe single item=============
    SwipeableActionsBox(
        modifier = Modifier.background(Color.Transparent),
        swipeThreshold = 50.dp,
        startActions = listOf(archive),
        endActions = listOf(clean),
        backgroundUntilSwipeThreshold = Color.Transparent
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.9f)
                .padding(bottom = 5.dp),
            //backgroundColor = BlueLight,
            elevation = 0.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        //.background(brush)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 5.dp, start = 15.dp)
                            .clickable {
                                //navController.navigate(route = Screen.Search.passHistoryArg(history))
                            },
                        text = history,
                        style = TextStyle(fontSize = 15.sp),
                        color = Color.Black,
                        fontSize = MaterialTheme.typography.h5.fontSize,
                        fontWeight = FontWeight.Normal,
                       // fontFamily = fontFamily,
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_clear_24),
                    contentDescription = "Clean_image",
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .size(20.dp)
                        .alpha(0.7f)
                        .clickable {
                            //remove item from database
                            GlobalScope.launch {
                                val item = dao.getByName(history)
                                dao.deleteItem(item)
                            }
                            //send arg to search screen to remove single (this) item from the list
                            //historyViewModel.passItem(history)
                            navController.navigate(route = Screen.History.route) //update history screen
                        },
                )
            }
        }
    }
}