package com.noemi.newschecker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.noemi.newschecker.R

val philosopherFamily = FontFamily(
    Font(R.font.philosopher_regular, FontWeight.Normal),
    Font(R.font.philosopher_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.philosopher_bold, FontWeight.Bold)
)

val Typography = Typography(

    bodyLarge = TextStyle(
        fontFamily = philosopherFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        textDirection = TextDirection.Content
    ),

    bodyMedium = TextStyle(
        fontFamily = philosopherFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color.Black,
        textDirection = TextDirection.Content
    ),

    bodySmall = TextStyle(
        fontFamily = philosopherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = Color.Black,
        textDirection = TextDirection.Content
    ),

    titleMedium = TextStyle(
        fontFamily = philosopherFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = Color.Black,
        textAlign = TextAlign.Start
    )
)