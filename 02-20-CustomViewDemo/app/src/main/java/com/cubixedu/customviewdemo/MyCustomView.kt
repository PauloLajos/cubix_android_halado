package com.cubixedu.customviewdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paintBg: Paint = Paint()

    init {
        paintBg.color = Color.BLUE
        paintBg.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(),paintBg)
    }

}