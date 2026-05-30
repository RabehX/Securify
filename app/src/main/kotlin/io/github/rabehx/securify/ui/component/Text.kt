package io.github.rabehx.securify.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SectionTitle(subtitle: Int) = Text(
    text = stringResource(subtitle),
    style = typography.labelLargeEmphasized,
    color = colorScheme.primary,
    modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, top = 16.dp, bottom = 12.dp)
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ToolbarTitle(
    title: Int,
) = Text(
    text = stringResource(title),
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    fontWeight = FontWeight.Bold,
    color = LocalContentColor.current
)

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SheetTitle(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = typography.headlineSmallEmphasized,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            fontWeight = FontWeight.Bold,
            color = colorScheme.onSurface
        )
    }
}