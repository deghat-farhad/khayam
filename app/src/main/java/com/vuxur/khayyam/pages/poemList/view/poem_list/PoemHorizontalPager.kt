package com.vuxur.khayyam.pages.poemList.view.poem_list

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Picture
import android.graphics.PorterDuff
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.model.TranslationItem
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
    )
}

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
    CaptureableCard(
        modifier = modifier
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
        capture = capture,
    ) {
        content.invoke()
    }
}

@Composable
private fun CaptureableCard(
    modifier: Modifier = Modifier,
    capture: ((Bitmap) -> Unit)?,
    content: @Composable () -> Unit,
) {
    val picture = remember { Picture() }
    LaunchedEffect(capture) {
        capture?.invoke(createBitmapFromPicture(picture))
    }
    Card(
        modifier = modifier
            .drawWithCache {
                val width = this.size.width.toInt()
                val height = this.size.height.toInt()
                onDrawWithContent {
                    val pictureCanvas =
                        androidx.compose.ui.graphics.Canvas(
                            picture.beginRecording(
                                width,
                                height
                            )
                        )
                    draw(this, this.layoutDirection, pictureCanvas, this.size) {
                        this@onDrawWithContent.drawContent()
                    }
                    picture.endRecording()

                    drawIntoCanvas { canvas -> canvas.nativeCanvas.drawPicture(picture) }
                }
            },
    ) {
        content()
    }
}

private fun createBitmapFromPicture(picture: Picture): Bitmap {
    val bitmap = Bitmap.createBitmap(
        picture.width,
        picture.height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    canvas.drawPicture(picture)
    return bitmap
}