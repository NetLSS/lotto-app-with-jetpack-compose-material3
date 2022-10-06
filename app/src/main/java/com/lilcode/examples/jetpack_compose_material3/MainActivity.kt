package com.lilcode.examples.jetpack_compose_material3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lilcode.examples.jetpack_compose_material3.lottery.LotteryHelper
import com.lilcode.examples.jetpack_compose_material3.ui.theme.Jetpackcomposematerial3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Jetpackcomposematerial3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting() {
    var lottery720items by remember {
        mutableStateOf(listOf<LotteryHelper.Data720>())
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
        ) {
            itemsIndexed(
                lottery720items
            ) { _, item ->
                Lottery720item(item)
            }
        }

        Button(
            onClick = {
                lottery720items = lottery720items + LotteryHelper.get720Numbers()
            }) {
            Text(text = "연금 복권 추첨")
        }
    }
}

@Preview(name = "lottery720item")
@Composable
fun Lottery720item(
    data720: LotteryHelper.Data720 = LotteryHelper.defaultData720
) {
    Row() {
        data720.run {
            Text(text = "$groupNumber 조")
            numbers.forEach {
                Text(text = " $it")
            }
        }

    }
}

@Preview(name = "DefaultPreview", showBackground = true)
@Composable
fun DefaultPreview() {
    Jetpackcomposematerial3Theme {
        Greeting()
    }
}