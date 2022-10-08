package com.lilcode.examples.jetpack_compose_material3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lilcode.examples.jetpack_compose_material3.MainActivity.RouteName.lottery720RouteName
import com.lilcode.examples.jetpack_compose_material3.MainActivity.RouteName.mainHomeRouteName
import com.lilcode.examples.jetpack_compose_material3.lottery.LotteryHelper
import com.lilcode.examples.jetpack_compose_material3.lottery.goldenDp
import com.lilcode.examples.jetpack_compose_material3.lottery.lotteryColor
import com.lilcode.examples.jetpack_compose_material3.ui.modifier.simpleVerticalScrollbar
import com.lilcode.examples.jetpack_compose_material3.ui.theme.Jetpackcomposematerial3Theme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    companion object RouteName {
        const val mainHomeRouteName = "mainHome"
        const val lottery720RouteName = "lottery720"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LotteryApp()
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun LotteryApp() {
    Jetpackcomposematerial3Theme {
        Scaffold(
            bottomBar = {

            }
        ) { innerPadding ->
            MainHomeNavHost(
                Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun MainHomeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = mainHomeRouteName
) {
    NavHost(
        modifier = modifier,
        navController = navController, startDestination = startDestination
    ) {
        composable(mainHomeRouteName) {
            MainHome(onNavigateTo720 = {
                navController.navigate(lottery720RouteName)
            })
        }
        composable(lottery720RouteName) { Lottery720() }
        /*...*/
    }
}

@Composable
fun MainHome(onNavigateTo720: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onNavigateTo720
        ) {
            Text(text = "연금복권 추첨하기")
        }
    }
}

@Composable
fun Lottery720() {
    var lottery720items by remember {
        mutableStateOf(listOf<LotteryHelper.Data720>())
    }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (lc720List, btnPick720, btnRefresh720) = createRefs()
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .constrainAs(lc720List) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(btnPick720.top)
                }
                .padding(top = 64.dp, bottom = 32.dp)
                .simpleVerticalScrollbar(state = scrollState)
        ) {
            itemsIndexed(
                lottery720items
            ) { _, item ->
                Lottery720item(item)
            }
        }

        Button(
            modifier = Modifier.constrainAs(btnPick720) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 64.dp)
            },
            onClick = {
                lottery720items = lottery720items + LotteryHelper.get720Numbers()
                coroutineScope.launch {
                    scrollState.animateScrollToItem(lottery720items.lastIndex)
                }
            }) {
            Text(text = "연금 복권 추첨")
        }

        if (lottery720items.isNotEmpty()) {
            Button(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .constrainAs(btnRefresh720) {
                        start.linkTo(btnPick720.end)
                        top.linkTo(btnPick720.top)
                        bottom.linkTo(btnPick720.bottom)
                    },
                onClick = {
                    lottery720items = listOf()
                }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "refresh")
            }
        }

    }
}


@Preview(name = "lottery720item")
@Composable
fun Lottery720item(
    data720: LotteryHelper.Data720 = LotteryHelper.defaultData720
) {
    Row(
        modifier = Modifier
            .padding((goldenDp * 10).dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        data720.run {
            Avatar(
                color = groupNumber.lotteryColor,
                text = "${groupNumber}조",
                fontWeight = Bold
            )
            numbers.forEach {
                Avatar(
                    modifier = Modifier.padding(start = (goldenDp * 2).dp),
                    color = it.lotteryColor,
                    text = "$it",
                    fontWeight = Bold
                )
            }
        }
    }
}

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    color: Color,
    text: String,
    fontWeight: FontWeight? = null
) {
    Box(
        modifier = modifier
            .size((goldenDp * 20).dp)
            .clip(CircleShape)
            .background(color = color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontWeight = fontWeight)
    }
}

@Preview(name = "DefaultPreview", showBackground = true)
@Composable
fun DefaultPreview() {
    Jetpackcomposematerial3Theme {
//        Lottery720()
    }
}

