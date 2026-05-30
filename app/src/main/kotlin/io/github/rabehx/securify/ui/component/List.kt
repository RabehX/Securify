package io.github.rabehx.securify.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.lyxnx.compose.ui.tablericons.TablerIcons
import io.github.lyxnx.compose.ui.tablericons.outline.Check
import io.github.lyxnx.compose.ui.tablericons.outline.X

@Composable
fun PreferencesGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceItem(
    title: Int,
    subtitle: Int? = null,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.985f else 1f,
        animationSpec = tween(120),
        label = "pressScale"
    )

    Surface(
        color = colorScheme.surfaceContainerHighest,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .alpha(if (enabled) 1f else 0.55f)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                enabled = enabled,
                role = Role.Button,
                indication = ripple(),
                interactionSource = interactionSource,
                onClick = onClick
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.Center
                ) { leadingIcon() }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = if (trailingIcon != null) 8.dp else 0.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(title),
                    style = typography.titleMediumEmphasized,
                    color = colorScheme.onSurface
                )

                if (subtitle != null) {
                    Text(
                        text = stringResource(subtitle),
                        style = typography.bodyMediumEmphasized,
                        color = colorScheme.onSurfaceVariant
                    )
                }
            }

            if (trailingIcon != null) {
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) { trailingIcon() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SwitchPreferenceItem(
    title: Int,
    subtitle: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val thumbContent: (@Composable () -> Unit)? = remember(checked) {
        {
            Icon(
                imageVector = if (checked) TablerIcons.Outline.Check else TablerIcons.Outline.X,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    val pressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.985f else 1f,
        animationSpec = tween(120),
        label = "pressScale"
    )

    Surface(
        color = colorScheme.surfaceContainerHighest,
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .alpha(if (enabled) 1f else 0.55f)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                enabled = enabled,
                role = Role.Button,
                indication = ripple(),
                interactionSource = interactionSource,
                onClick = {
                    onCheckedChange(!checked)
                }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (leadingIcon != null) {
                Box(
                    modifier = Modifier.padding(end = 4.dp).size(24.dp),
                    contentAlignment = Alignment.Center
                ) { leadingIcon() }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(title),
                    style = typography.titleMediumEmphasized,
                    color = colorScheme.onSurface
                )
                Text(
                    text = stringResource(subtitle),
                    style = typography.bodyMediumEmphasized,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                interactionSource = interactionSource,
                thumbContent = thumbContent,
                colors =
                    SwitchDefaults.colors(
                        checkedThumbColor = colorScheme.onPrimary,
                        checkedTrackColor = colorScheme.primary,
                        uncheckedThumbColor = colorScheme.onSurface,
                        uncheckedTrackColor = colorScheme.surfaceVariant
                    )
            )
        }
    }
}
