package com.lilcode.examples.jetpack_compose_material3.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lilcode.examples.jetpack_compose_material3.ui.theme.Jetpackcomposematerial3Theme


@Preview(name = "TestPreview", showBackground = true)
@Composable
fun TestPreviewStatelessScreen() {
    val (name, onNameChanged) = rememberSaveable { mutableStateOf("") }

    TestPreviewStateless(name, onNameChanged)
}
@Composable
fun TestPreviewStateless(name: String, onNameChange: (String) -> Unit) {
    Jetpackcomposematerial3Theme {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Hello!",
                modifier = Modifier.padding(bottom = 8.dp),
            )
            BasicTextField(
                value = name,
                onValueChange = onNameChange
            )
        }
    }
}