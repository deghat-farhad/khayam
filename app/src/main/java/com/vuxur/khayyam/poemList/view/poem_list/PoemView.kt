package com.vuxur.khayyam.poemList.view.poem_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vuxur.khayyam.model.PoemItem
import java.text.NumberFormat

@Composable
fun PoemView(
    modifier: Modifier = Modifier,
    poemItem: PoemItem,
) {
    val density = LocalDensity.current
    var maxWidth by remember { mutableStateOf(0.dp) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = NumberFormat.getInstance().format(poemItem.id),
            )

            Divider(
                modifier = Modifier.width(maxWidth),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    maxWidth = with(density) {
                        coordinates.size.width.toDp() + 96.dp
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = poemItem.hemistich1,
                textAlign = TextAlign.Center,
            )

            Text(
                text = poemItem.hemistich2,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = poemItem.hemistich3,
                textAlign = TextAlign.Center,
            )

            Text(
                text = poemItem.hemistich4,
                textAlign = TextAlign.Center,
            )
        }
    }
}