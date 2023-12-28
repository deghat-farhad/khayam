package com.raven.khayam.poemList.view.poem_list

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.raven.khayam.model.PoemItem

@Composable
fun PoemListScreen(
    poemList: List<PoemItem>,
    currentPoemIndex: Int,
    onRandomPoem: () -> Unit,
    onSearch: (String) -> Unit,
    onNextResult: (String) -> Unit,
    onPreviousResult: (String) -> Unit,
    setCurrentPoemIndex: (Int) -> Unit,
    isThereAnyResult: Boolean,
    isThereNextResult: Boolean,
    isTherePreviousResult: Boolean,
    onCopyPoemText: () -> Unit,
    onSharePoemText: () -> Unit,
    onSharePoemImage: (Bitmap) -> Unit,
) {
    var capture by remember { mutableStateOf({ _: Bitmap -> }) }
    Column(
        Modifier
            .systemBarsPadding()
            .imePadding()
    ) {
        Box(
            modifier = Modifier.weight(1.0f),
            contentAlignment = if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
                Alignment.BottomStart
            } else {
                Alignment.BottomEnd
            }
        ) {
            PoemHorizontalPager(
                modifier = Modifier.fillMaxSize(),
                poemList = poemList,
                currentPoemIndex = currentPoemIndex,
                setCurrentPoemIndex = setCurrentPoemIndex,
                captureCurrentPage = capture
            )
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                ShareButton(
                    onCopyText = onCopyPoemText,
                    onShareText = onSharePoemText,
                    onShareImage = {
                        capture = { bitmap ->
                            onSharePoemImage(bitmap)
                        }
                    },
                )
                FloatingActionButton(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = { onRandomPoem() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shuffle,
                        contentDescription = null
                    )
                }
            }
        }
        KSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 20.dp, end = 20.dp),
            onChange = onSearch,
            onNextResult = onNextResult,
            onPreviousResult = onPreviousResult,
            isThereAnyResult = isThereAnyResult,
            isThereNextResult = isThereNextResult,
            isTherePreviousResult = isTherePreviousResult,
        )
    }
}