package com.vuxur.khayyam.ui.pages.poemList.view.poem_list

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.model.TranslationItem
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PoemHorizontalPager(
    modifier: Modifier = Modifier,
    poemList: List<PoemItem>,
    currentPoemIndex: Int,
    setCurrentPoemIndex: (Int) -> Unit,
    captureCurrentPage: (Bitmap) -> Unit,
    translationItem: TranslationItem,
    highlightPhrase: String?,
) {
    val pagerState = rememberPagerState(
        pageCount = {
            poemList.size
        },
        initialPage = currentPoemIndex
    )

    LaunchedEffect(pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress && pagerState.currentPage != currentPoemIndex)
            setCurrentPoemIndex(pagerState.currentPage)
    }

    LaunchedEffect(currentPoemIndex) {
        if (currentPoemIndex != pagerState.currentPage)
            pagerState.animateScrollToPage(currentPoemIndex)
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { page ->
        var cardCapture: ((Bitmap) -> Unit)? by remember { mutableStateOf(null) }
        LaunchedEffect(captureCurrentPage) {
            cardCapture =
                if (!pagerState.isScrollInProgress && page == currentPoemIndex)
                    captureCurrentPage
                else
                    null
        }
        AnimatedCaptureableCard(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .fillMaxSize(),
            currentPageIndex = pagerState.currentPage,
            thisPageIndex = page,
            currentPageOffsetFraction = pagerState.currentPageOffsetFraction,
            scalingFactor = .9f,
            capture = cardCapture,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .paint(
                        painter = painterResource(
                            id =
                                if (isSystemInDarkTheme())
                                    R.drawable.paper_dark
                                else
                                    R.drawable.paper_light
                        ),
                        contentScale = ContentScale.Crop
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AnimatedPoemView(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    currentPageIndex = pagerState.currentPage,
                    thisPageIndex = page,
                    currentPageOffsetFraction = pagerState.currentPageOffsetFraction,
                    poemItem = poemList[page],
                    translationItem = translationItem,
                    highlightPhrase = highlightPhrase,
                    showHighlights = (!pagerState.isScrollInProgress && page == pagerState.currentPage)
                )
            }
        }
    }
}

@Composable
private fun AnimatedPoemView(
    modifier: Modifier = Modifier,
    poemItem: PoemItem,
    currentPageIndex: Int,
    thisPageIndex: Int,
    currentPageOffsetFraction: Float,
    translationItem: TranslationItem,
    highlightPhrase: String?,
    showHighlights: Boolean,
) {
    val direction = LocalLayoutDirection.current
    PoemView(
        modifier = modifier
            .graphicsLayer {
                val pageOffset = currentPageIndex - thisPageIndex + currentPageOffsetFraction
                val translationFactor = size.width / 2
                translationX = translationFactor * lerp(
                    start = 0f,
                    stop =
                    if (direction == LayoutDirection.Rtl)
                        1f
                    else
                        -1f,
                    fraction = pageOffset.coerceIn(-1f, 1f)
                )
            },
        poemItem = poemItem,
        translationItem = translationItem,
        highlightPhrase = highlightPhrase,
        showHighlights = showHighlights,
    )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeApi::class)
@Composable
private fun AnimatedCaptureableCard(
    modifier: Modifier = Modifier,
    currentPageIndex: Int,
    thisPageIndex: Int,
    currentPageOffsetFraction: Float,
    scalingFactor: Float,
    capture: ((Bitmap) -> Unit)?,
    content: @Composable () -> Unit,
) {
    val direction = LocalLayoutDirection.current
    val captureController = rememberCaptureController()
    LaunchedEffect(capture) {
        val bitmapAsync = captureController.captureAsync()
        val bitmap = bitmapAsync.await().asAndroidBitmap()
        capture?.invoke(bitmap)
    }
    Card(
        modifier = modifier
            .capturable(captureController)
            .graphicsLayer {
                val pageOffset = currentPageIndex - thisPageIndex + currentPageOffsetFraction
                val scale = lerp(1f, scalingFactor, pageOffset.absoluteValue.coerceIn(0f, 1f))
                val translationFactor = size.width * (1 - scalingFactor) / 2
                val translationX = translationFactor * lerp(
                    start = 0f,
                    stop = if (direction == LayoutDirection.Rtl)
                        -1f
                    else
                        1f,
                    fraction = pageOffset.coerceIn(-1f, 1f)
                )
                scaleX = scale
                scaleY = scale
                this.translationX = translationX
                transformOrigin = TransformOrigin(.5f, 1f)
            },
    ) {
        content.invoke()
    }
}