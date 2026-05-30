package io.github.rabehx.securify.core.designsystem.component

import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.lyxnx.compose.ui.tablericons.TablerIcons
import io.github.lyxnx.compose.ui.tablericons.automirrored.outline.ArrowLeft
import io.github.lyxnx.compose.ui.tablericons.outline.Check
import io.github.rabehx.securify.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TooltipButton(
    modifier: Modifier = Modifier,
    title: Int,
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true,
    tooltipState: TooltipState = rememberTooltipState(),
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(colorScheme.surfaceContainerLow),
) {
    fun View.performSlightHapticFeedback() =
        this.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)

    val view = LocalView.current

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above,
            4.dp
        ),
        tooltip = { PlainTooltip { Text(stringResource(id = title)) } },
        state = tooltipState
    ) {
        FilledIconButton(
            onClick = {
                onClick()
                view.performSlightHapticFeedback()
            },
            colors = colors,
            enabled = enabled
        ) {
            Icon(
                imageVector = icon,
                modifier = modifier,
                contentDescription = stringResource(id = title),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BackButton(

    onBack: () -> Unit,
) {
    val view = LocalView.current

    fun View.performSlightHapticFeedback() =
        this.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
        tooltip = { PlainTooltip { Text(stringResource(id = R.string.back)) } },
        state = rememberTooltipState()
    ) {
        FilledIconButton(
            onClick = {
                onBack()
                view.performSlightHapticFeedback()
            },
            modifier =
                Modifier
                    .minimumInteractiveComponentSize()
                    .size(
                        IconButtonDefaults.smallContainerSize(
                            IconButtonDefaults.IconButtonWidthOption.Narrow
                        )
                    ),
            colors = IconButtonDefaults.filledIconButtonColors(containerColor = colorScheme.surfaceContainerLow),
        ) {
            Icon(
                imageVector = TablerIcons.AutoMirrored.Outline.ArrowLeft,
                contentDescription = null,
                tint = colorScheme.onSurface,
                modifier = Modifier.size(IconButtonDefaults.smallIconSize),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SelectorItem(
    key: String,
    selectedKey: String,
    label: String,
    onSelectionChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isSelected = key == selectedKey

    val containerColor by animateColorAsState(
        targetValue = if (isSelected) colorScheme.primaryContainer else colorScheme.surfaceContainer,
        label = "containerColor"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) colorScheme.onPrimaryContainer else colorScheme.onSurface,
        label = "contentColor"
    )

    Surface(
        onClick = { onSelectionChanged(key) },
        shape = shapes.largeIncreased,
        color = containerColor,
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMediumEmphasized,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = contentColor,
                modifier = Modifier.weight(1f, fill = false)
            )

            AnimatedVisibility(
                visible = isSelected,
                enter =
                    scaleIn(
                        initialScale = 0.8f,
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ) +
                            fadeIn(
                                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                            ),
                exit =
                    scaleOut(
                        targetScale = 0.8f,
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ) +
                            fadeOut(
                                animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                            )
            ) {
                Icon(
                    imageVector = TablerIcons.Outline.Check,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}
