package com.vuxur.khayyam.ui.pages.poemList.view.poem_list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.vuxur.khayyam.model.PoemItem
import com.vuxur.khayyam.model.TranslationItem
import com.vuxur.khayyam.utils.getPoemsFontFamily
import com.vuxur.khayyam.utils.getPoemsFontSize
import com.vuxur.khayyam.utils.highlight
import kotlinx.coroutines.delay

@Composable
fun PoemView(
    modifier: Modifier = Modifier,
    poemItem: PoemItem,
    translationItem: TranslationItem,
    highlightPhrase: String?,
    showHighlights: Boolean,
) {

    val fontFamily = remember(translationItem) {
        getPoemsFontFamily(translationItem)
    }

    val fontSize = remember(translationItem) {
        getPoemsFontSize(translationItem)
    }

    var isHighlighted by remember { mutableStateOf(false) }

    LaunchedEffect(showHighlights, highlightPhrase) {
        isHighlighted = showHighlights
        if (showHighlights) {
            delay(500)
            isHighlighted = false
        }
    }

    val highlightGlowRadius by animateFloatAsState(
        targetValue = if (isHighlighted) 5f else 0f,
        animationSpec = tween(500)
    )
    val highlightColor by animateColorAsState(
        targetValue = if (isHighlighted) MaterialTheme.colorScheme.primary else LocalContentColor.current,
        animationSpec = tween(500)
    )

    val highlightSpanStyle = SpanStyle(
        shadow = Shadow(
            highlightColor,
            blurRadius = highlightGlowRadius
        ),
        color = highlightColor
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = poemItem.index,
                fontFamily = fontFamily,
                fontSize = fontSize,
            )

            HorizontalDivider(
                modifier = Modifier.width(304.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = poemItem.hemistich1.highlight(
                    highlightPhrase,
                    highlightSpanStyle,
                ),
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontSize = fontSize,
            )

            Text(
                text = poemItem.hemistich2.highlight(
                    highlightPhrase,
                    highlightSpanStyle,
                ),
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontSize = fontSize,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = poemItem.hemistich3.highlight(
                    highlightPhrase,
                    highlightSpanStyle,
                ),
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontSize = fontSize,
            )

            Text(
                text = poemItem.hemistich4.highlight(
                    highlightPhrase,
                    highlightSpanStyle,
                ),
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontSize = fontSize,
            )
        }
    }
}