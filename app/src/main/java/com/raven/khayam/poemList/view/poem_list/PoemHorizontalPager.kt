package com.raven.khayam.poemList.view.poem_list

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Picture
import android.graphics.PorterDuff
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.raven.khayam.model.PoemItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PoemHorizontalPager(
    modifier: Modifier = Modifier,
    poemList: List<PoemItem>,
    currentPoemIndex: Int,
    setCurrentPoemIndex: (Int) -> Unit,
    captureCurrentPage: (Bitmap) -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = {
        poemList.size
    })

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

        CaptureableCard(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 8.dp)
                .fillMaxSize(),
            capture = cardCapture,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                PoemView(
                    modifier = Modifier.padding(horizontal = 56.dp),
                    poemItem = poemList[page]
                )
            }
        }
    }
}

@Composable
fun CaptureableCard(
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