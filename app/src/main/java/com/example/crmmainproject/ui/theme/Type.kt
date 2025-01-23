package com.example.crmmainproject.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.crmmainproject.R

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)


var InternRegularWithHexa19da613sp: TextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.inter_regular)),
    fontWeight = FontWeight.Normal,
    fontSize = 13.sp,
    letterSpacing = TextUnit.Unspecified,
    color = Hexa19da6
)


var InternBoldWithHex31394f18sp: TextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.inter_bold)),
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    letterSpacing = TextUnit.Unspecified,
    color = Hex31394f
)

var InternMediumWithHex3b7ded14sp: TextStyle = TextStyle(
    fontFamily = FontFamily(Font(R.font.inter_medium)),
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = TextUnit.Unspecified,
    color = Hex3b7ded
)


