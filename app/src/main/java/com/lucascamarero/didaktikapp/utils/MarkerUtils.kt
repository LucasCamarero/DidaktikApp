package com.lucascamarero.didaktikapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface

/**
 * Crea un marcador circular numerado como [Bitmap].
 *
 * Este marcador está pensado para su uso en mapas basados en APIs Android
 * clásicas (por ejemplo, OSMDroid), donde se requiere un `Bitmap` o `Drawable`
 * como icono del marcador.
 *
 * El marcador consiste en:
 * - Un fondo circular de color configurable.
 * - Un número centrado que representa el identificador de la actividad.
 *
 * Los colores se reciben como enteros ARGB (`Int`), lo que permite integrarlo
 * fácilmente con colores provenientes de Compose mediante `toArgb()`.
 *
 * @param context Contexto de Android necesario para la creación de recursos gráficos.
 * @param number Número que se mostrará en el centro del marcador.
 * @param backgroundColor Color de fondo del marcador en formato ARGB.
 *        Por defecto, amarillo.
 * @param textColor Color del texto del número en formato ARGB.
 *        Por defecto, negro.
 *
 * @return Un [Bitmap] que representa el marcador numerado.
 */
fun createNumberedMarker(
    context: Context,
    number: Int,
    backgroundColor: Int = 0xFFF5DC21.toInt(), // amarillo
    textColor: Int = 0xFF000000.toInt() // negro
): Bitmap {

    /**
     * Tamaño del marcador en píxeles.
     */
    val size = 96 // px

    /**
     * Bitmap base sobre el que se dibuja el marcador.
     */
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    /**
     * Pintura utilizada para el fondo circular.
     */
    val circlePaint = Paint().apply {
        color = backgroundColor
        isAntiAlias = true
    }

    // Dibujamos el fondo circular
    canvas.drawCircle(
        size / 2f,
        size / 2f,
        size / 2f,
        circlePaint
    )

    /**
     * Pintura utilizada para el texto (número).
     */
    val textPaint = Paint().apply {
        color = textColor
        textSize = 42f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        typeface = Typeface.DEFAULT_BOLD
    }

    /**
     * Cálculo de la posición vertical del texto para centrarlo correctamente.
     */
    val textY = size / 2f - (textPaint.descent() + textPaint.ascent()) / 2

    // Dibujamos el número centrado
    canvas.drawText(
        number.toString(),
        size / 2f,
        textY,
        textPaint
    )

    return bitmap
}