package io.github.rabehx.securify.ui.screens.home.settings.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.rabehx.securify.R
import io.github.rabehx.securify.core.designsystem.component.BottomSheet
import io.github.rabehx.securify.ui.component.MarkdownText
import io.github.rabehx.securify.core.designsystem.component.PreferenceItem
import io.github.rabehx.securify.core.designsystem.component.SheetTitle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun PrivacyPolicyPreference(
    content: String,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openBottomSheet by remember { mutableStateOf(false) }
    if (openBottomSheet) {
        BottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = rememberBottomSheetState(
                initialValue = SheetValue.Expanded,
            ),
        ) {
            Box(modifier = modifier.fillMaxWidth()) {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        SheetTitle(stringResource(R.string.privacy_policy))
                    }
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        MarkdownText(markdown = content)
                        Spacer(modifier = Modifier.height(38.dp))
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surfaceContainerLow
                                )
                            )
                        )
                )
            }
        }
    }
    PreferenceItem(
        title = R.string.privacy_policy,
        onClick = {
            onOpen()
            openBottomSheet = true
        }
    )
}
