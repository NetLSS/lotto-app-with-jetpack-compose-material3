package com.lilcode.examples.jetpack_compose_material3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.lilcode.examples.jetpack_compose_material3.lottery.LotteryHelper
import com.lilcode.examples.jetpack_compose_material3.lottery.goldenDp
import com.lilcode.examples.jetpack_compose_material3.lottery.lotteryColor
import com.lilcode.examples.jetpack_compose_material3.lottery.pickButtonBottomMargin
import com.lilcode.examples.jetpack_compose_material3.ui.modifier.simpleVerticalScrollbar
import com.lilcode.examples.jetpack_compose_material3.ui.theme.Jetpackcomposematerial3Theme
import com.lilcode.examples.jetpack_compose_material3.viewmodel.LotteryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object RouteName {
        const val mainHomeRouteName = "mainHome"
        const val lottery720RouteName = "lottery720"
        const val lottery645RouteName = "lottery645"
    }

    val lotteryViewModel by viewModels<LotteryViewModel>()

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
                    navController.navigate(lottery720RouteName)
                },
                onNavigateTo645 = {
                    navController.navigate(lottery645RouteName)
                }
            )
        }
        composable(lottery720RouteName) {
            Lottery720(
                onNavigateToHome = {
                    navController.navigate(
                        mainHomeRouteName
                    )
                },
                viewModel = viewModel
            )
        }
        composable(lottery645RouteName) {
            Lottery645(onNavigateToHome = {
                navController.navigate(
                    mainHomeRouteName
                )
            })
        }
        /*...*/
    }
}

@Composable
fun MainHome(onNavigateTo720: () -> Unit, onNavigateTo645: () -> Unit) {
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
            Text(text = "연금복권 추첨하기")
        }

        Button(
            onClick = onNavigateTo645
        ) {
            Text(text = "로또복권 추첨하기")
        }
    }
}

@Composable
fun Lottery720(onNavigateToHome: () -> Unit, viewModel: LotteryViewModel) {
    /**
     * remember를 사용하여 객체를 저장하는 컴포저블은 내부 상태를 생성하여 컴포저블을 스테이트풀(Stateful)로 만듭니다.
     * Lottery720 내부적으로 lottery720items 상태를 보존하고 수정하므로 스테이트풀(Stateful) 컴포저블의 한 예가 됩니다.
     * 이는 호출자가 상태를 제어할 필요가 없고 상태를 직접 관리하지 않아도 상태를 사용할 수 있는 경우에 유용합니다.
     * 그러나 내부 상태를 갖는 컴포저블은 재사용 가능성이 적고 테스트하기가 더 어려운 경향이 있습니다.
     *
     * 스테이트리스(Stateless) 컴포저블은 상태를 갖지 않는 컴포저블입니다. 스테이트리스(Stateless)를 달성하는 한 가지 쉬운 방법은 상태 호이스팅을 사용하는 것입니다.
     *
     * 재사용 가능한 컴포저블을 개발할 때는 동일한 컴포저블의 스테이트풀(Stateful) 버전과 스테이트리스(Stateless) 버전을 모두 노출해야 하는 경우가 있습니다.
     * 스테이트풀(Stateful) 버전은 상태를 염두에 두지 않는 호출자에 편리하며, 스테이트리스(Stateless) 버전은 상태를 제어하거나 끌어올려야 하는 호출자에 필요합니다.
     */
//    var lottery720items by remember {
//        mutableStateOf(listOf<LotteryHelper.Data720>())
//    }

    var lottery720items = viewModel.lottery720liveData.observeAsState().value ?: emptyList()

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
fun Lottery645(onNavigateToHome: () -> Unit) {
    var lottery645items by remember {
        mutableStateOf(listOf<LotteryHelper.Data645>())
    }

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
                lottery645items = lottery645items + LotteryHelper.get645Numbers()
                coroutineScope.launch {
                    scrollState.animateScrollToItem(lottery645items.lastIndex)
                }
            }) {
            Text(text = "로또 복권 추첨")
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
                    lottery645items = listOf()
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
    Row(
        modifier = Modifier
            .padding((goldenDp * 5).dp),
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
    Row(
        modifier = Modifier
            .padding((goldenDp * 5).dp),
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
    }
}

