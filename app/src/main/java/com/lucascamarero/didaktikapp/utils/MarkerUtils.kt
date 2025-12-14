package com.lucascamarero.didaktikapp.utils

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import com.lucascamarero.didaktikapp.R

fun createNumberedMarker(
    context: Context,
    number: Int
): Bitmap {

    val size = 96 // px
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    // Fondo circular
    val circlePaint = Paint().apply {
        color = 0xFFFFC93C.toInt()
        isAntiAlias = true
    }

    canvas.drawCircle(
        size / 2f,
        size / 2f,
        size / 2f,
        circlePaint
    )

    // Texto (número)
    val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 42f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }

    val textY = size / 2f - (textPaint.descent() + textPaint.ascent()) / 2
    canvas.drawText(
        number.toString(),
        size / 2f,
        textY,
        textPaint
    )

    return bitmap
}
