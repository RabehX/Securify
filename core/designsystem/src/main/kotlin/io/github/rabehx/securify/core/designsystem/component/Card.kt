package io.github.rabehx.securify.core.designsystem.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.rabehx.securify.core.designsystem.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppCard(
    name: Int,
    versionName: String,
    modifier: Modifier = Modifier,
) = Card(
    modifier = modifier
        .fillMaxWidth()
        .height(200.dp),
    shape = shapes.medium,
    colors = CardDefaults.cardColors(
        containerColor = colorScheme.primary
    )
) {
    SelectionContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = stringResource(name),
                    style = typography.titleMediumEmphasized,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimary
                )
                Text(
                    text = versionName,
                    style = typography.bodyMediumEmphasized,
                    color = colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetectionCard(
    modifier: Modifier = Modifier,
    detectionCount: Int,

    detections: List<String> = emptyList(),
) {
    var openBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    val hasDetections = detectionCount > 0

    val displayedDetections by animateIntAsState(
        targetValue = detectionCount.coerceAtLeast(0),
        animationSpec = tween(450),
        label = "detectionCount"
    )

    val accentColor by animateColorAsState(
        targetValue = if (hasDetections) colorScheme.error else colorScheme.tertiary,
        animationSpec = tween(500),
        label = "accentColor"
    )

    val onContainerColor = if (hasDetections) colorScheme.onErrorContainer else colorScheme.onTertiaryContainer

    val containerColor by animateColorAsState(
        targetValue = if (hasDetections) colorScheme.errorContainer else colorScheme.tertiaryContainer,
        animationSpec = tween(500),
        label = "containerColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = shapes.medium,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        onClick = { if (hasDetections) openBottomSheet = true }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = shapes.medium,
                    color = accentColor.copy(alpha = 0.14f),
                    contentColor = accentColor
                ) {
                    Text(
                        text = if (hasDetections) stringResource(R.string.threats_found) else stringResource(R.string.all_clear),
                        style = typography.labelSmallEmphasized,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Text(
                text = "$displayedDetections",
                style = typography.displayMediumEmphasized,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )

            if (hasDetections) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.tap_to_view),
                        style = typography.labelSmallEmphasized,
                        color = onContainerColor.copy(alpha = 0.8f)
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = onContainerColor.copy(alpha = 0.8f)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (openBottomSheet) {
        DetectionBottomSheet(
            sheetState = sheetState,
            detections = detections,
            onDismiss = { openBottomSheet = false },
        )
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun InfoCard(
    systemInfoItems: List<Pair<Int, String>>,
    integrityItems: List<Pair<Int, String>>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = shapes.largeIncreased,
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceContainerHighest)
    ) {
        @Composable
        fun Section(items: List<Pair<Int, String>>) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items.forEach { (titleRes, value) ->
                    key(titleRes, value) {
                        SelectionContainer {
                            Column(Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(titleRes),
                                    style = typography.titleSmallEmphasized,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = value,
                                    style = typography.bodyMediumEmphasized,
                                    modifier = Modifier.padding(top = 2.dp),
                                    color = LocalContentColor.current.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }

        Section(systemInfoItems)

        if (integrityItems.isNotEmpty()) {
            HorizontalDivider(
                thickness = 3.dp,
                color = colorScheme.surfaceContainer
            )
            Section(integrityItems)
        }
    }
}
