package com.lilcode.examples.jetpack_compose_material3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lilcode.examples.jetpack_compose_material3.MainActivity.RouteName.lottery645RouteName
import com.lilcode.examples.jetpack_compose_material3.MainActivity.RouteName.lottery720RouteName
import com.lilcode.examples.jetpack_compose_material3.MainActivity.RouteName.mainHomeRouteName
import com.lilcode.examples.jetpack_compose_material3.lottery.*
import com.lilcode.examples.jetpack_compose_material3.ui.modifier.simpleVerticalScrollbar
import com.lilcode.examples.jetpack_compose_material3.ui.popup.PopupWindowDialog
import com.lilcode.examples.jetpack_compose_material3.ui.row.CancelableRow
import com.lilcode.examples.jetpack_compose_material3.ui.theme.Jetpackcomposematerial3Theme
import com.lilcode.examples.jetpack_compose_material3.viewmodel.LotteryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object RouteName {
        const val mainHomeRouteName = "mainHome"
        const val lottery720RouteName = "lottery720"
        const val lottery645RouteName = "lottery645"
    }

    private val lotteryViewModel by viewModels<LotteryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LotteryApp(lotteryViewModel)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun LotteryApp(viewModel: LotteryViewModel) {
    Jetpackcomposematerial3Theme {
        Scaffold(
            bottomBar = {

            }
        ) { innerPadding ->
            MainHomeNavHost(
                Modifier.padding(innerPadding),
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun MainHomeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = mainHomeRouteName,
    viewModel: LotteryViewModel = LotteryViewModel()
) {
    NavHost(
        modifier = modifier,
        navController = navController, startDestination = startDestination
    ) {
        composable(mainHomeRouteName) {
            MainHome(
                onNavigateTo720 = {
                    navController.onNavigateTo720()
                },
                onNavigateTo645 = {
                    navController.onNavigateTo645()
                }
            )
        }
        composable(lottery720RouteName) {
            Lottery720(
                onNavigateToHome = {
                    navController.onNavigateToHome()
                },
                viewModel = viewModel
            )
        }
        composable(lottery645RouteName) {
            Lottery645(
                onNavigateToHome = {
                    navController.onNavigateToHome()
                },
                viewModel = viewModel
            )
        }
        /*...*/
    }
}

@Composable
fun MainHome(onNavigateTo720: () -> Unit, onNavigateTo645: () -> Unit) {

//    val activity = LocalContext.current as? Activity
//
//    BackPressHandler {
//        activity?.finish()
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = pickButtonBottomMargin.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onNavigateTo720
        ) {
            Text(text = "????????????")
        }

        Button(
            onClick = onNavigateTo645
        ) {
            Text(text = "????????????")
        }
    }
}

@Suppress("UNCHECKED_CAST")
@Composable
fun <T: LotteryHelper.LotteryData> LotteryPicker(
    onNavigateToHome: () -> Unit,
    itemTypeClazz: String,
    items: List<T>,
    onUpdateList: (List<T>) -> Unit,
    getNumbers: () -> T
) {

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (lc720List, btnPick720, btnRefresh720, btnGoHome) = createRefs()
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
            items(items) { item ->
                when (itemTypeClazz) {
                    LotteryHelper.Data720::class.toString() -> {
                        Lottery720item(item as LotteryHelper.Data720)
                    }
                    LotteryHelper.Data645::class.toString() -> {
                        Lottery645item(item as LotteryHelper.Data645)
                    }
                }
            }
        }

        Button(
            modifier = Modifier.constrainAs(btnPick720) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = pickButtonBottomMargin.dp)
            },
            onClick = {
                onUpdateList(items + (getNumbers as T))
                coroutineScope.launch {
                    scrollState.animateScrollToItem(items.lastIndex)
                }
            }) {
            Text(text = "?????? ??????")
        }

        if (items.isNotEmpty()) {
            Button(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .constrainAs(btnRefresh720) {
                        start.linkTo(btnPick720.end)
                        top.linkTo(btnPick720.top)
                        bottom.linkTo(btnPick720.bottom)
                    },
                onClick = {
                    onUpdateList(emptyList())
                }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "refresh")
            }
        }

        Button(
            modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(btnGoHome) {
                    end.linkTo(btnPick720.start)
                    top.linkTo(btnPick720.top)
                    bottom.linkTo(btnPick720.bottom)
                },
            onClick = onNavigateToHome
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back to home")
        }

    }
}


@Composable
fun Lottery720(onNavigateToHome: () -> Unit, viewModel: LotteryViewModel) {
    /**
     * remember??? ???????????? ????????? ???????????? ??????????????? ?????? ????????? ???????????? ??????????????? ???????????????(Stateful)??? ????????????.
     * Lottery720 ??????????????? lottery720items ????????? ???????????? ??????????????? ???????????????(Stateful) ??????????????? ??? ?????? ?????????.
     * ?????? ???????????? ????????? ????????? ????????? ?????? ????????? ?????? ???????????? ????????? ????????? ????????? ??? ?????? ????????? ???????????????.
     * ????????? ?????? ????????? ?????? ??????????????? ????????? ???????????? ?????? ?????????????????? ??? ????????? ????????? ????????????.
     *
     * ??????????????????(Stateless) ??????????????? ????????? ?????? ?????? ?????????????????????. ??????????????????(Stateless)??? ???????????? ??? ?????? ?????? ????????? ?????? ??????????????? ???????????? ????????????.
     *
     * ????????? ????????? ??????????????? ????????? ?????? ????????? ??????????????? ???????????????(Stateful) ????????? ??????????????????(Stateless) ????????? ?????? ???????????? ?????? ????????? ????????????.
     * ???????????????(Stateful) ????????? ????????? ????????? ?????? ?????? ???????????? ????????????, ??????????????????(Stateless) ????????? ????????? ??????????????? ??????????????? ?????? ???????????? ???????????????.
     */
//    var lottery720items by remember {
//        mutableStateOf(listOf<LotteryHelper.Data720>())
//    }

    val lottery720items = viewModel.lottery720liveData.observeAsState().value ?: emptyList()

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (lc720List, btnPick720, btnRefresh720, btnGoHome) = createRefs()
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
                bottom.linkTo(parent.bottom, margin = pickButtonBottomMargin.dp)
            },
            onClick = {
                viewModel.updateLottery720liveData(lottery720items + LotteryHelper.get720Numbers())
                coroutineScope.launch {
                    scrollState.animateScrollToItem(lottery720items.lastIndex)
                }
            }) {
            Text(text = "?????? ??????")
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
                    viewModel.updateLottery720liveData(emptyList())
                }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "refresh")
            }
        }

        Button(
            modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(btnGoHome) {
                    end.linkTo(btnPick720.start)
                    top.linkTo(btnPick720.top)
                    bottom.linkTo(btnPick720.bottom)
                },
            onClick = onNavigateToHome
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back to home")
        }

    }
}


@Composable
fun Lottery645(onNavigateToHome: () -> Unit, viewModel: LotteryViewModel) {
    val lottery645items = viewModel.lottery645liveData.observeAsState().value ?: emptyList()

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier.fillMaxSize(),
    ) {
        val (lc645List, btnPick645, btnRefresh645, btnGoHome) = createRefs()
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .constrainAs(lc645List) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(btnPick645.top)
                }
                .padding(top = 64.dp, bottom = 32.dp)
                .simpleVerticalScrollbar(state = scrollState)
        ) {
            itemsIndexed(
                lottery645items
            ) { _, item ->
                Lottery645item(item)
            }
        }

        Button(
            modifier = Modifier.constrainAs(btnPick645) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = pickButtonBottomMargin.dp)
            },
            onClick = {
                viewModel.updateLottery645liveData(lottery645items + LotteryHelper.get645Numbers())
                coroutineScope.launch {
                    scrollState.animateScrollToItem(lottery645items.lastIndex)
                }
            }) {
            Text(text = "?????? ??????")
        }

        if (lottery645items.isNotEmpty()) {
            Button(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .constrainAs(btnRefresh645) {
                        start.linkTo(btnPick645.end)
                        top.linkTo(btnPick645.top)
                        bottom.linkTo(btnPick645.bottom)
                    },
                onClick = {
                    viewModel.updateLottery645liveData(emptyList())
                }) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "refresh")
            }
        }

        Button(
            modifier = Modifier
                .padding(end = 8.dp)
                .constrainAs(btnGoHome) {
                    end.linkTo(btnPick645.start)
                    top.linkTo(btnPick645.top)
                    bottom.linkTo(btnPick645.bottom)
                },
            onClick = onNavigateToHome
        ) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back to home")
        }

    }
}


@Preview(name = "lottery645item")
@Composable
fun Lottery645item(
    data645: LotteryHelper.Data645 = LotteryHelper.defaultData645
) {
    CancelableRow(
        modifier = Modifier
            .padding((goldenDp * 5).dp),
        _isCanceled = data645.isCanceled.observeAsState().value ?: false,
        _onUpdateIsCanceled = {
            data645.isCanceled.value = it
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        data645.run {
            numbers.sorted().forEach {
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


@Preview(name = "lottery720item")
@Composable
fun Lottery720item(
    data720: LotteryHelper.Data720 = LotteryHelper.defaultData720
) {
    CancelableRow(
        modifier = Modifier
            .padding((goldenDp * 5).dp),
        _isCanceled = data720.isCanceled.observeAsState().value ?: false,
        _onUpdateIsCanceled = {
            data720.isCanceled.value = it
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        data720.run {
            Avatar(
                color = groupNumber.lotteryColor,
                text = "${groupNumber}???",
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
        Text(
            text = text,
            color = Color.White,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "DefaultPreview", showBackground = true)
@Composable
fun DefaultPreview() {
    Jetpackcomposematerial3Theme {
//        Lottery720()
        PopupWindowDialog()
    }
}

