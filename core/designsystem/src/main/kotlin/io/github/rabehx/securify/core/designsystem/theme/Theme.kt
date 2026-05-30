package io.github.rabehx.securify.core.designsystem.theme

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import com.materialkolor.dynamiccolor.ColorSpec
import com.materialkolor.rememberDynamicColorScheme
import androidx.compose.ui.res.colorResource

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalTextApi::class)
@Composable
fun AppTheme(
    useDynamicColor: Boolean,
    useDarkTheme: Boolean,
    seedColor: Long,
    useAmoledTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = rememberDynamicColorScheme(
        seedColor = if (useDynamicColor) colorResource(android.R.color.system_accent1_500) else Color(seedColor),
        isDark = useDarkTheme,
        isAmoled = useAmoledTheme,
        specVersion = ColorSpec.SpecVersion.SPEC_2025,
    ).let {
        val adjustedScheme = if (useDarkTheme) it else it.copy(surfaceContainerHighest = colorScheme.surfaceContainerLow)
        if (useAmoledTheme) adjustedScheme.copy(
            surfaceContainer = Color.Black,
        ) else adjustedScheme
    }

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        motionScheme = MotionScheme.expressive(),
        typography = Typography,
        content = content
    )
}
