package com.cubix.contentproviderdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.cubix.contentproviderdemo.databinding.ActivityMainBinding
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.provider.ContactsContract
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.TimeZone

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 1001
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CALENDAR)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            setViews()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                setViews()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun setViews() {
        binding.btnGet.isEnabled = true
        binding.btnInsert.isEnabled = true

        binding.btnGet.setOnClickListener {
            getContacts()
        }
        binding.btnInsert.setOnClickListener {
            insertCalendarEvent()
        }
    }

    private fun insertCalendarEvent() {
        try {
            val values = ContentValues()
            values.put(CalendarContract.Events.DTSTART, System.currentTimeMillis())
            values.put(CalendarContract.Events.DTEND, System.currentTimeMillis() + 60000)
            values.put(CalendarContract.Events.TITLE, "Vége")
            values.put(CalendarContract.Events.DESCRIPTION, "Legyen már vége a mostani órának :)")
            values.put(CalendarContract.Events.CALENDAR_ID, 1)
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID())

            val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

            //contentResolver.delete(CalendarContract.Events.CONTENT_URI, CalendarContract.Events._ID+"=599", null)

        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val cursorContacts =
            contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ),
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE '%Tamas%'",
                //null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " DESC"
            )

        Toast.makeText(this, "" + cursorContacts!!.count, Toast.LENGTH_LONG).show();

        while (cursorContacts.moveToNext()) {
            val name = cursorContacts.getString(
                cursorContacts.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                )
            )
            Toast.makeText(this@MainActivity, name, Toast.LENGTH_LONG).show()
        }
    }
}