package com.practice.tennisplayers.ui

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import com.squareup.picasso.Transformation
import kotlin.math.min

class CircleTransformation : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val size = min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2
        val result = Bitmap.createBitmap(source, x, y, size, size)
        if (result != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, result.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(result, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        result.recycle()

        return bitmap
    }

    override fun key(): String {
        return "circle()"
    }
}