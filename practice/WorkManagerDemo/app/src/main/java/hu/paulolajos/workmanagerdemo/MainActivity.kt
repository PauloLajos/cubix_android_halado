package hu.paulolajos.workmanagerdemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import hu.paulolajos.workmanagerdemo.ForegroundWorker.Companion.ARG_PROGRESS
import hu.paulolajos.workmanagerdemo.databinding.ActivityMainBinding

/**
 * source: https://medium.com/@sdex/run-worker-as-foreground-service-b607819fd263
 * github: https://github.com/sdex/WorkManager-Samples/tree/master
 */

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            // we don't care about the result, start the job in any case
            startJob()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            // ask for permission to show notifications on android 13+
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
                || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startJob()
            } else {
                requestPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    private fun startJob() {
        val workManager = WorkManager.getInstance(this)
        val workRequest = OneTimeWorkRequestBuilder<ForegroundWorker>().build()
        // observe the job progress in the activity
        workManager.getWorkInfoByIdLiveData(workRequest.id)
            .observe(this) { workInfo: WorkInfo? ->
                if (workInfo != null) {
                    val progress = workInfo.progress
                    val value = progress.getInt(ARG_PROGRESS, 0)
                    binding.progressBar.progress = value

                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        binding.start.isEnabled = true
                    }
                }
            }
        // run the worker
        workManager.enqueue(workRequest)

        binding.start.isEnabled = false
    }
}