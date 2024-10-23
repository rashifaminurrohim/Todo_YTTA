package com.dicoding.todoapp.setting

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.notification.NotificationWorker
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskViewModel
import com.dicoding.todoapp.utils.NOTIFICATION_CHANNEL_ID
import java.util.concurrent.TimeUnit

class SettingsActivity : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Notifications permission granted")
            } else {
                showToast("Notifications will not show without permission")
            }
        }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT > 32) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        private val settingsViewModel: SettingsViewModel by viewModels {
            ViewModelFactory.getInstance(requireContext())
        }

        private lateinit var workManager: WorkManager

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
            prefNotification?.setOnPreferenceChangeListener { preference, newValue ->
                val channelName = getString(R.string.notify_channel_name)
                //TODO 13 : Schedule and cancel daily reminder using WorkManager with data channelName
                if (newValue as Boolean){
                    settingsViewModel.nearTask.observe(viewLifecycleOwner) { task ->
                        task?.let {
                            val data = Data.Builder()
                                .putString(NOTIFICATION_CHANNEL_ID, task.title)

                            val constraints = Constraints.Builder()
                                .setRequiresBatteryNotLow(true)
                                .build()

                            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                                .setConstraints(constraints)
                                .setInputData(data.build())
                                .setInitialDelay(calculateInitialDelay(task.dueDateMillis), TimeUnit.MILLISECONDS)
                                .build()

                            workManager.enqueueUniqueWork(
                                channelName,
                                ExistingWorkPolicy.REPLACE,
                                workRequest
                            )
                            workManager.getWorkInfoByIdLiveData(workRequest.id)
                        }
                    }
                } else {
                    workManager.cancelUniqueWork(channelName)
                }

                true
            }

        }

        private fun calculateInitialDelay(dueDate: Long): Long {
            val currentTime = System.currentTimeMillis()
            return if (dueDate > currentTime) {
                dueDate - currentTime
            } else {
                0
            }
        }

    }
}