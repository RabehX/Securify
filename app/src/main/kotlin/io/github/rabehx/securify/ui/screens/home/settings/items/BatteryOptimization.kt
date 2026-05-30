package io.github.rabehx.securify.ui.screens.home.settings.items

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@SuppressLint("BatteryLife", "QueryPermissionsNeeded")
@Composable
internal fun BatteryOptimizationPreference(
    showBatteryOptimizationHint: Boolean,
    onBatteryOptimizationStatusChange: (Boolean) -> Unit,
    context: Context = LocalContext.current
) {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    val packageName = context.packageName

    val isBatteryOptimizationIgnored = powerManager.isIgnoringBatteryOptimizations(packageName)

    LaunchedEffect(isBatteryOptimizationIgnored) { if (!isBatteryOptimizationIgnored) onBatteryOptimizationStatusChange(true) }

    val batteryOptimizationIntent =
        Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply { data = "package:$packageName".toUri() }

    val isIntentResolvable = remember {
        if (Build.VERSION.SDK_INT >= 33)
            context.packageManager.queryIntentActivities(
                batteryOptimizationIntent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            ).isNotEmpty()
        else
            context.packageManager.queryIntentActivities(
                batteryOptimizationIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            ).isNotEmpty()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val updatedStatus = powerManager.isIgnoringBatteryOptimizations(packageName)
        onBatteryOptimizationStatusChange(!updatedStatus)
    }
}