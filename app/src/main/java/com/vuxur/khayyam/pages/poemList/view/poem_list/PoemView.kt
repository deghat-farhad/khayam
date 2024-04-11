package com.vuxur.khayyam.pages.poemList.view.poem_list

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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.model.LocaleItem
import com.vuxur.khayyam.model.PoemItem

@Composable
fun PoemView(
    modifier: Modifier = Modifier,
    poemItem: PoemItem,
    localeItem: LocaleItem.CustomLocale,
) {
    val density = LocalDensity.current
    var maxWidth by remember { mutableStateOf(0.dp) }
    val fontFamily = FontFamily(
        Font(
            if (localeItem.locale.language == "fa") R.font.iran_sans_x_regular
            else R.font.e_b_garamond_regular
        )
    )

    val fontSize = if (localeItem.locale.language == "fa") 19.sp else 18.sp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = poemItem.id,
                fontFamily = fontFamily,
                fontSize = fontSize,
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
                fontFamily = fontFamily,
                fontSize = fontSize,
            )

            Text(
                text = poemItem.hemistich2,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontSize = fontSize,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = poemItem.hemistich3,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontSize = fontSize,
            )

            Text(
                text = poemItem.hemistich4,
                textAlign = TextAlign.Center,
                fontFamily = fontFamily,
                fontSize = fontSize,
            )
        }
    }
}