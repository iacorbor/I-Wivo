package com.icb.iwivo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

fun uriToBase64(
    context: Context,
    uri: Uri,
    maxSize: Int = 320,
    quality: Int = 60
): String {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: return ""

    val originalBitmap = BitmapFactory.decodeStream(inputStream)
        ?: return ""

    val resizedBitmap = resizeBitmap(
        bitmap = originalBitmap,
        maxSize = maxSize
    )

    val outputStream = ByteArrayOutputStream()

    resizedBitmap.compress(
        Bitmap.CompressFormat.JPEG,
        quality,
        outputStream
    )

    return Base64.encodeToString(
        outputStream.toByteArray(),
        Base64.NO_WRAP
    )
}

fun base64ToBitmap(base64: String): Bitmap? {
    if (base64.isBlank()) return null

    return try {
        val bytes = Base64.decode(base64, Base64.NO_WRAP)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: Exception) {
        null
    }
}

private fun resizeBitmap(
    bitmap: Bitmap,
    maxSize: Int
): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    if (width <= maxSize && height <= maxSize) {
        return bitmap
    }

    val ratio = width.toFloat() / height.toFloat()

    val newWidth: Int
    val newHeight: Int

    if (ratio > 1) {
        newWidth = maxSize
        newHeight = (maxSize / ratio).toInt()
    } else {
        newHeight = maxSize
        newWidth = (maxSize * ratio).toInt()
    }

    return Bitmap.createScaledBitmap(
        bitmap,
        newWidth,
        newHeight,
        true
    )
}