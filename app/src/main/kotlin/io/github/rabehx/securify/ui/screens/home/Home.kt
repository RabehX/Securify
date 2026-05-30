package io.github.rabehx.securify.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.TonalToggleButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import io.github.lyxnx.compose.ui.tablericons.TablerIcons
import io.github.lyxnx.compose.ui.tablericons.filled.PlayerPlay
import io.github.lyxnx.compose.ui.tablericons.outline.Settings
import io.github.rabehx.rei.Rei
import io.github.rabehx.securify.utils.NetworkResult
import io.github.rabehx.securify.R
import io.github.rabehx.securify.Const.DEVICE_NAME
import io.github.rabehx.securify.Const.FINGERPRINT
import io.github.rabehx.securify.Const.KERNEL_VERSION
import io.github.rabehx.securify.Const.SECURITY_PATCH_LEVEL
import io.github.rabehx.securify.Const.SYSTEM_ABI
import io.github.rabehx.securify.Const.SYSTEM_VERSION
import io.github.rabehx.securify.Const.VERSION_CODE
import io.github.rabehx.securify.Const.VERSION_NAME
import io.github.rabehx.securify.ui.component.AppCard
import io.github.rabehx.securify.ui.component.AppScaffold
import io.github.rabehx.securify.ui.component.DetectionCard
import io.github.rabehx.securify.ui.component.InfoCard
import io.github.rabehx.securify.ui.component.ToolbarTitle
import io.github.rabehx.securify.ui.component.TooltipButton
import io.github.rabehx.securify.ui.component.TopAppBar
import io.github.rabehx.securify.viewmodel.HomeViewModel

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun Home(
    onNavigateTo: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )

    val rei = Rei(androidx.compose.ui.platform.LocalContext.current)
    val detections = rei.detections()
    val integrityState by viewModel.integrity.collectAsState()
    val isLoading = integrityState is NetworkResult.Loading
    val integrityPassed = stringResource(R.string.integrity_passed)
    val integrityFailed = stringResource(R.string.integrity_failed)

    val systemInfo = listOf(
        R.string.kernel_version to KERNEL_VERSION,
        R.string.device_name to DEVICE_NAME,
        R.string.system_abi to SYSTEM_ABI,
        R.string.system_version to SYSTEM_VERSION,
        R.string.security_patch_level to SECURITY_PATCH_LEVEL,
        R.string.fingerprint to FINGERPRINT
    )

    val integrityInfo = when (val state = integrityState) {
        is NetworkResult.Success -> state.data.run {
            buildList {
                add(R.string.integrity_basic to integrityStatus(meetsBasicIntegrity, integrityPassed, integrityFailed))
                add(R.string.integrity_device to integrityStatus(meetsDeviceIntegrity, integrityPassed, integrityFailed))
                add(R.string.integrity_strong to integrityStatus(meetsStrongIntegrity, integrityPassed, integrityFailed))
                appVerdict?.let {
                    add(R.string.app_recognition_verdict to it)
                }
                activityLevel?.let {
                    add(R.string.device_activity_level to it)
                }
                licensingVerdict?.let {
                    add(R.string.app_licensing_verdict to it)
                }
            }
        }

        is NetworkResult.Error -> listOf(R.string.integrity_error to state.message)

        else -> emptyList()
    }

    val detectionsList = rei.getDetectionResults()
        .takeIf { it.isNotBlank() }
        ?.split(",")
        ?.map { it.trim() }
        ?: emptyList()

    AppScaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    ToolbarTitle(R.string.overview)
                },
                actions = {
                    TooltipButton(
                        title = R.string.settings,
                        onClick = onNavigateTo,
                        icon = TablerIcons.Outline.Settings
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppCard(
                        name = R.string.app_name,
                        versionName = stringResource(R.string.version, VERSION_NAME, VERSION_CODE),
                        modifier = Modifier.weight(.6f),
                    )
                    DetectionCard(
                        modifier = Modifier.weight(.4f),
                        detectionCount = detections,
                        detections = detectionsList,
                    )
                }
            }

            item {
                IntegrityButton(
                    isLoading = isLoading,
                    onCheck = viewModel::checkPlayIntegrity,
                )
            }
            item {
                InfoCard(
                    systemInfoItems = systemInfo,
                    integrityItems = integrityInfo,
                )
            }
        }
    }
}

private fun integrityStatus(passed: Boolean, passedText: String, failedText: String): String =
    if (passed) passedText else failedText

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun IntegrityButton(
    isLoading: Boolean,
    onCheck: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TonalToggleButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(42.dp),
        checked = !isLoading,
        onCheckedChange = { onCheck() },
    ) {
        if (isLoading) {
            CircularWavyProgressIndicator(
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.integrity_requesting),
                style = typography.titleSmallEmphasized,
            )
        } else {
            Icon(
                imageVector = TablerIcons.Filled.PlayerPlay,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.integrity_run),
                style = typography.titleSmallEmphasized,
                fontWeight = FontWeight.Medium
            )
        }
    }
}