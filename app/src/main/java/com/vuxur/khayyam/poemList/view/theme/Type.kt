package com.vuxur.khayyam.poemList.view.theme

import android.content.res.Resources
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vuxur.khayyam.R
import com.vuxur.khayyam.utils.getCurrentLocale

val isFarsi = getCurrentLocale(Resources.getSystem()).language == "fa"

// Set of Material typography styles to start with
val fontFamily = if (isFarsi)
    FontFamily(
        Font(
            R.font.iran_sans_x_regular
        )
    )
else
    FontFamily.Default

val fontSizeCoefficient = if (isFarsi)
    1.1f
else
    1f

val BodyLarge = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = (16 * fontSizeCoefficient).sp,
    lineHeight = 24.0.sp,
    letterSpacing = 0.5.sp
)

val BodyMedium = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (14 * fontSizeCoefficient).sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.2.sp
)

val BodySmall = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (12 * fontSizeCoefficient).sp,
    lineHeight = 16.0.sp,
    letterSpacing = 0.4.sp
)

val DisplayLarge = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (57 * fontSizeCoefficient).sp,
    lineHeight = 64.0.sp,
    letterSpacing = -0.2.sp
)

val DisplayMedium = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (45 * fontSizeCoefficient).sp,
    lineHeight = 52.0.sp,
    letterSpacing = 0.0.sp
)

val DisplaySmall = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (36 * fontSizeCoefficient).sp,
    lineHeight = 44.0.sp,
    letterSpacing = 0.0.sp
)

val HeadlineLarge = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (32 * fontSizeCoefficient).sp,
    lineHeight = 40.0.sp,
    letterSpacing = 0.0.sp
)

val HeadlineMedium = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (28 * fontSizeCoefficient).sp,
    lineHeight = 36.0.sp,
    letterSpacing = 0.0.sp
)

val HeadlineSmall = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (24 * fontSizeCoefficient).sp,
    lineHeight = 32.0.sp,
    letterSpacing = 0.0.sp
)

val LabelLarge = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Medium,
    fontSize = (14 * fontSizeCoefficient).sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp
)

val LabelMedium = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Medium,
    fontSize = (12 * fontSizeCoefficient).sp,
    lineHeight = 16.0.sp,
    letterSpacing = 0.5.sp
)

val LabelSmall = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Medium,
    fontSize = (11 * fontSizeCoefficient).sp,
    lineHeight = 16.0.sp,
    letterSpacing = 0.5.sp
)

val TitleLarge = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Normal,
    fontSize = (22 * fontSizeCoefficient).sp,
    lineHeight = 28.0.sp,
    letterSpacing = 0.0.sp
)

val TitleMedium = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Medium,
    fontSize = (16 * fontSizeCoefficient).sp,
    lineHeight = 24.0.sp,
    letterSpacing = 0.2.sp
)

val TitleSmall = TextStyle(
    fontFamily = fontFamily ,
    fontWeight = FontWeight.Medium,
    fontSize = (14 * fontSizeCoefficient).sp,
    lineHeight = 20.0.sp,
    letterSpacing = 0.1.sp
)

val Typography = Typography(
    bodyLarge = BodyLarge,
    bodyMedium = BodyMedium,
    bodySmall = BodySmall,
    displayLarge = DisplayLarge,
    displayMedium = DisplayMedium,
    displaySmall = DisplaySmall,
    headlineLarge = HeadlineLarge,
    headlineMedium = HeadlineMedium,
    headlineSmall = HeadlineSmall,
    labelLarge = LabelLarge,
    labelMedium = LabelMedium,
    labelSmall = LabelSmall,
    titleLarge = TitleLarge,
    titleMedium = TitleMedium,
    titleSmall = TitleSmall
)