package com.vuxur.khayyam.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

fun String.highlight(stringToBeBold: String?, highlightStyle: SpanStyle): AnnotatedString {
    return stringToBeBold?.let {
        buildAnnotatedString {
            val splitted = split(stringToBeBold)
            splitted.forEachIndexed { index, part ->
                if (index < splitted.size - 1) {
                    if (part.isEmpty())
                        withStyle(style = highlightStyle) {
                            append(stringToBeBold)
                        }
                    else {
                        append(part)
                        withStyle(style = highlightStyle) {
                            append(stringToBeBold)
                        }
                    }
                } else
                    append(part)
            }
        }
    } ?: AnnotatedString(this)
}