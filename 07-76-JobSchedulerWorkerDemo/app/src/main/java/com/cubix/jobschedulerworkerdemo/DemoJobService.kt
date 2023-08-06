package com.cubix.jobschedulerworkerdemo

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import android.widget.Toast

class DemoJobService : JobService() {
    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d("TAG_DEMO","DEMO JOB SERVICE START")
        Toast.makeText(this, "DEMO JOB SERVICE START", Toast.LENGTH_LONG).show()

        // periodic min 5 minute
        MainActivity.scheduleJob(this)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d("TAG_DEMO","DEMO JOB SERVICE STOP")
        Toast.makeText(this, "DEMO JOB SERVICE STOP", Toast.LENGTH_LONG).show()

        return true
    }
}