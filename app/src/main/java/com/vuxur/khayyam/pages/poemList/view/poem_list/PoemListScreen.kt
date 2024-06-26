package com.vuxur.khayyam.pages.poemList.view.poem_list

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.text.layoutDirection
import com.vuxur.khayyam.R
import com.vuxur.khayyam.model.LocaleItem
import com.vuxur.khayyam.model.PoemItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoemListScreen(
    poemList: List<PoemItem>,
    currentPoemIndex: Int,
    onRandomPoem: () -> Unit,
    onSearch: (String) -> Unit,
    onNextResult: () -> Unit,
    onPreviousResult: () -> Unit,
    setCurrentPoemIndex: (Int) -> Unit,
    isThereAnyResult: Boolean,
    isThereNextResult: Boolean,
    isTherePreviousResult: Boolean,
    onCopyPoemText: () -> Unit,
    onSharePoemText: () -> Unit,
    onSharePoemImage: (Bitmap) -> Unit,
    onNavigateToSetting: () -> Unit,
    localeItem: LocaleItem.CustomLocale,
) {
    var capture by remember { mutableStateOf({ _: Bitmap -> }) }

    val layoutDirection = when (localeItem.locale.layoutDirection) {
        0 -> LayoutDirection.Ltr
        1 -> LayoutDirection.Rtl
        else -> LayoutDirection.Ltr
    }
    Column(
        Modifier
            .systemBarsPadding()
            .imePadding()
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
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
                    captureCurrentPage = capture,
                    localeItem = localeItem,
                )
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        IconButton(
                            modifier = Modifier.padding(vertical = 12.dp),
                            onClick = onNavigateToSetting
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = stringResource(id = R.string.settings),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
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
                            contentDescription = stringResource(R.string.random_poem)
                        )
                    }
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
            localeItem = localeItem,
        )
    }
}