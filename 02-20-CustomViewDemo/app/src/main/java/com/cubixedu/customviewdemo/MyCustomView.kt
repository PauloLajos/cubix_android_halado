package com.cubixedu.customviewdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton

class MyCustomView(context: Context, attrs: AttributeSet?) : AppCompatButton(context, attrs) {

    private var paintText: Paint = Paint()
    private var click = 0

    init {
        paintText.color = Color.BLUE
        paintText.style = Paint.Style.FILL_AND_STROKE
        paintText.textSize = 60f

        val attr = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CountColor,
            0, 0
        )
        var color: Int
        try {
            color = attr.getColor(R.styleable.CountColor_countcolor, Color.RED)
            paintText.color = color
        } finally {
            // release the TypedArray so that it can be reused.
            attr.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawText(
            "$click",
            width-120f,
            120f,
            paintText
        )
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_DOWN) {
            click++
            invalidate()
        }

        return super.onTouchEvent(event)
    }
}