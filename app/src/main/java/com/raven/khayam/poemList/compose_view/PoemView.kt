package com.raven.khayam.poemList.compose_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import com.raven.khayam.model.PoemItem

@Composable
fun PoemView(
    modifier: Modifier = Modifier,
    poemItem: PoemItem,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.End
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    //modifier = Modifier.fillMaxWidth(),
                    text = poemItem.hemistich1,
                    textAlign = TextAlign.Start,
                )
                Text(
                    //modifier = Modifier.fillMaxWidth(),
                    text = poemItem.hemistich2,
                    textAlign = TextAlign.Start,
                )
            }
            Column {
                Text(
                    //modifier = Modifier.fillMaxWidth(),
                    text = poemItem.hemistich3,
                    textAlign = TextAlign.Start,
                )
                Text(
                    //modifier = Modifier.fillMaxWidth(),
                    text = poemItem.hemistich4,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}