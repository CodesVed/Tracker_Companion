package com.example.trackercompanion.util

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import androidx.core.graphics.scale

private const val TARGET_MAX_DIMENSION = 400
private const val WEBP_QUALITY = 85

fun processAndSaveWrestlerImage(context: Context, sourceUri: Uri): String? {
    return try {
        val boundsOption = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(sourceUri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, boundsOption)
        }

        val sourceWidth = boundsOption.outWidth
        val sourceHeight = boundsOption.outHeight
        if (sourceWidth <= 0 || sourceHeight <= 0) return null

        var sampleSize = 1
        var halvedWidth =  sourceWidth
        var halvedHeight = sourceHeight
        while ((halvedWidth/2) >= TARGET_MAX_DIMENSION && (halvedHeight/2) >= TARGET_MAX_DIMENSION) {
            halvedWidth /= 2
            halvedHeight /= 2
            sampleSize *= 2
        }

        val decodeOptions = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val sampledBitmap = context.contentResolver.openInputStream(sourceUri)?.use { stream ->
            BitmapFactory.decodeStream(stream, null, decodeOptions)
        } ?: return null

        val scale = TARGET_MAX_DIMENSION.toFloat() / maxOf(sampledBitmap.width, sampledBitmap.height)
        val finalBitmap = if (scale < 1f) {
            val newWidth = (sampledBitmap.width * scale).toInt().coerceAtLeast(1)
            val newHeight = (sampledBitmap.height * scale).toInt().coerceAtLeast(1)
            val scaled = sampledBitmap.scale(newWidth, newHeight)

            if (scaled != sampledBitmap) sampledBitmap.recycle()
            scaled
        } else {
            sampledBitmap
        }

        val imagesDir = File(context.filesDir, "images").apply { mkdirs() }
        val fileName = "wrestler_${UUID.randomUUID()}.webp"
        val outputFile = File(imagesDir, fileName)

        FileOutputStream(outputFile).use { out ->
            val format = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Bitmap.CompressFormat.WEBP_LOSSY
            } else {
                Bitmap.CompressFormat.WEBP
            }
            finalBitmap.compress(format, WEBP_QUALITY, out)
        }

        finalBitmap.recycle()

        "file://${outputFile.absolutePath}"
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}