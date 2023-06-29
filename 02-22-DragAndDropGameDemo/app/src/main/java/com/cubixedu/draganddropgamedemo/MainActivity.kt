package com.cubixedu.draganddropgamedemo

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.cubixedu.draganddropgamedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    private inner class MyTouchListener : View.OnTouchListener {
        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val shadowBuilder = View.DragShadowBuilder(
                    view
                )

                view.startDragAndDrop(
                    ClipData.newPlainText("", ""),
                    shadowBuilder, view, 0)
                draggedView = view

                return true
            }
            return false
        }
    }

    private var draggedView: View? = null
    private val myTouchListener = MyTouchListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.color1.setOnTouchListener(myTouchListener)
        mainBinding.color1.tag = "Blue"
        mainBinding.color2.setOnTouchListener(myTouchListener)
        mainBinding.color2.tag = "Green"
        mainBinding.color3.setOnTouchListener(myTouchListener)
        mainBinding.color3.tag = "Yellow"
        mainBinding.color4.setOnTouchListener(myTouchListener)
        mainBinding.color4.tag = "Red"

        mainBinding.basket.setOnDragListener(MyDragListener())
    }

    private inner class MyDragListener : View.OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                }
                DragEvent.ACTION_DRAG_ENTERED -> Toast.makeText(this@MainActivity, "Entered", Toast.LENGTH_SHORT)
                    .show()
                DragEvent.ACTION_DRAG_EXITED -> Toast.makeText(this@MainActivity, "Exited", Toast.LENGTH_SHORT)
                    .show()
                DragEvent.ACTION_DROP -> {
                    Toast.makeText(this@MainActivity, "Drop", Toast.LENGTH_SHORT)
                    .show()
                    if (draggedView != null) {
                        mainBinding.basket.background = draggedView?.background
                    }
                }
            }
            return true
        }
    }
}