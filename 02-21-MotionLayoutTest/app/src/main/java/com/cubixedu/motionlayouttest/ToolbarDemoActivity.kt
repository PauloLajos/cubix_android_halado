package com.cubixedu.motionlayouttest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cubixedu.motionlayouttest.ui.User
import com.cubixedu.motionlayouttest.ui.UserAdapter

class ToolbarDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_demo)
    }

    private val nameList = listOf(
        "Alexa Maddox",
        "Emyr Jimenez",
        "Marie Rivers",
        "Cooper Mcneil",
        "Sophie-Louise Chang",
        "Hafsa Arnold",
        "Kerys Bender",
        "Brennan Battle",
        "Leyton Wheatley",
        "Ottilie Ireland",
        "Kory Gilmore",
        "Abdurrahman Shepherd",
    )

    override fun onResume() {
        super.onResume()
        setupList()
    }

    private fun setupList() {
        val adapter = UserAdapter()
        val list = findViewById<RecyclerView>(R.id.list)
        list.adapter = adapter
        adapter.submitList(getDummyUserList())
    }

    private fun getDummyUserList(): List<User>? {
        return nameList.map { User(name = it, photo = getUrlForImage(it)) }
    }

    private fun getUrlForImage(name: String): String {
        return "https://eu.ui-avatars.com/api/?size=128&name=" + name.replace(' ', '+')
    }
}