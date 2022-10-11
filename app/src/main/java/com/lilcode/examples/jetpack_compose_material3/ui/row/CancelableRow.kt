package com.lilcode.examples.jetpack_compose_material3.ui.row

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun CancelableRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    _isCanceled: Boolean = false,
    _onUpdateIsCanceled: ((Boolean) -> Unit)? = null,
    content: @Composable RowScope.() -> Unit
) {

    val isCanceled: Boolean
    val onUpdateIsCanceled: (Boolean) -> Unit

    if (_onUpdateIsCanceled == null ){
        val (isClicked, onUpdateClicked) = remember {
            mutableStateOf(false)
        }
        isCanceled = isClicked
        onUpdateIsCanceled = onUpdateClicked
    } else {
        isCanceled = _isCanceled
        onUpdateIsCanceled = _onUpdateIsCanceled
    }

    Row(
        modifier = modifier
            .clickable {
                onUpdateIsCanceled(!isCanceled)
            }
            .drawWithContent {
                drawContent()
                if (isCanceled) {
                    drawLine(
                        color = Color.Black,
                        start = Offset(0f, this.size.height / 2),
                        end = Offset(this.size.width, this.size.height / 2),
                        strokeWidth = this.size.height / 10
                    )
                }
            },
        verticalAlignment = verticalAlignment,
        content = content
    )
}