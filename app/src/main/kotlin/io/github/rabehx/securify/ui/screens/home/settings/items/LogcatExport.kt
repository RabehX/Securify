package io.github.rabehx.securify.ui.screens.home.settings.items

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import io.github.rabehx.securify.R
import io.github.rabehx.securify.core.designsystem.component.PreferenceItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
internal fun LogcatExportPreference() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                coroutineScope.launch {
                    exportLogcat(context, uri)
                }
            }
        }
    }

    PreferenceItem(
        title = R.string.export_logcat,
        subtitle = R.string.export_logcat_desc,
        onClick = {
            val timestamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
            val fileName = "SecurifyLogcat_$timestamp.txt"

            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, fileName)
            }
            exportLauncher.launch(intent)
        },
    )
}

private suspend fun exportLogcat(context: Context, uri: Uri) {
    withContext(Dispatchers.IO) {
        runCatching {
            val process = Runtime.getRuntime().exec("/system/bin/logcat -d -v threadtime")
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    process.inputStream.use { inputStream ->
                        inputStream.copyTo(outputStream, bufferSize = 8 * 1024)
                    }
                }
            } finally {
                process.destroy()
            }
        }
    }
}
