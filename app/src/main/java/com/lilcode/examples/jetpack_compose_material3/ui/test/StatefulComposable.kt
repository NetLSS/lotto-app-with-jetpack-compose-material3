package com.lilcode.examples.jetpack_compose_material3.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilcode.examples.jetpack_compose_material3.ui.theme.Jetpackcomposematerial3Theme

@Preview(name = "TestPreview", showBackground = true)
@Composable
fun TestPreview() {
    Jetpackcomposematerial3Theme {
        Column(modifier = Modifier.padding(16.dp)) {
            val (name, setValue) = remember { mutableStateOf("") }
            Text(
                text = "Hello!",
                modifier = Modifier.padding(bottom = 8.dp),
            )
            BasicTextField(
                value = name,
                onValueChange = setValue
            )
        }
    }
}