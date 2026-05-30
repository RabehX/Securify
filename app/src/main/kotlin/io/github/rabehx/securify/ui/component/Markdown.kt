package io.github.rabehx.securify.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

/**
 * A composable that renders Markdown-formatted text using native Compose text primitives.
 * Supports: headers (#–######), bold (**), italic (*), links [text](url),
 * unordered lists (* / - / +), ordered lists (1. / a) / (i)),
 * horizontal rules (---), and regular paragraphs with continuation lines.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    color: Color = colorScheme.onSurface,
    skipFirstHeader: Boolean = true,
) {
    val uriHandler = LocalUriHandler.current
    val linkColor = colorScheme.primary
    val blocks = remember(markdown, skipFirstHeader) {
        val parsed = parseMarkdownBlocks(markdown)
        if (skipFirstHeader) {
            val firstH1Index = parsed.indexOfFirst { it is MarkdownBlock.Header && it.level == 1 }
            if (firstH1Index != -1) parsed.filterIndexed { index, _ -> index != firstH1Index }
            else parsed
        } else parsed
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        blocks.forEach { block ->
            when (block) {
                is MarkdownBlock.Header -> {
                    if (block.level <= 2) {
                        Spacer(Modifier.height(8.dp))
                    }
                    val annotated = parseInlineMarkdown(block.text, linkColor)
                    Text(
                        text = annotated,
                        style = when (block.level) {
                            1 -> typography.headlineMedium
                            2 -> typography.titleLarge
                            3 -> typography.titleMedium
                            4 -> typography.titleSmall
                            else -> typography.titleSmall
                        },
                        fontWeight = FontWeight.Bold,
                        color = color,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (block.level <= 2) {
                        Spacer(Modifier.height(2.dp))
                    }
                }

                is MarkdownBlock.Paragraph -> {
                    val annotated = parseInlineMarkdown(block.text, linkColor)
                    ClickableText(
                        text = annotated,
                        style = typography.bodyMedium.merge(TextStyle(color = color.copy(alpha = 0.9f))),
                        onClick = { offset ->
                            annotated.getStringAnnotations("URL", offset, offset)
                                .firstOrNull()
                                ?.item
                                ?.takeIf(::isSafeWebUrl)
                                ?.let { runCatching { uriHandler.openUri(it) } }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                is MarkdownBlock.ListItem -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = (8 + block.indent * 16).dp)
                    ) {
                        Text(
                            text = block.bullet,
                            style = typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary,
                        )
                        Spacer(Modifier.width(8.dp))
                        val annotated = parseInlineMarkdown(block.text, linkColor)
                        ClickableText(
                            text = annotated,
                            style = typography.bodyMedium.merge(TextStyle(color = color.copy(alpha = 0.9f))),
                            onClick = { offset ->
                                annotated.getStringAnnotations("URL", offset, offset)
                                    .firstOrNull()
                                    ?.item
                                    ?.takeIf(::isSafeWebUrl)
                                    ?.let { runCatching { uriHandler.openUri(it) } }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                is MarkdownBlock.Divider -> {
                    Spacer(Modifier.height(4.dp))
                    HorizontalDivider(color = colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(Modifier.height(4.dp))
                }

                is MarkdownBlock.Blank -> {
                    Spacer(Modifier.height(6.dp))
                }
            }
        }
    }
}

// --- Markdown block parser ---

private sealed class MarkdownBlock {
    data class Header(val level: Int, val text: String) : MarkdownBlock()
    data class Paragraph(val text: String) : MarkdownBlock()
    data class ListItem(val text: String, val bullet: String = "•", val indent: Int = 0) : MarkdownBlock()
    data object Divider : MarkdownBlock()
    data object Blank : MarkdownBlock()
}

// Regex patterns
private val horizontalRuleRegex = Regex("^\\s*(-{3,}|\\*{3,}|_{3,})\\s*$")
private val headerRegex = Regex("^\\s*(#{1,6})\\s+(.*)")
private val unorderedListRegex = Regex("^(\\s*)[*\\-+]\\s+(.*)")
private val orderedListRegex = Regex("^(\\s*)(\\d+[.)]) (.*)")

private fun isSafeWebUrl(url: String): Boolean =
    url.startsWith("https://", ignoreCase = true) || url.startsWith("http://", ignoreCase = true)

private fun parseMarkdownBlocks(markdown: String): List<MarkdownBlock> {
    val lines = markdown.lines()
    val blocks = mutableListOf<MarkdownBlock>()
    val paragraphBuffer = StringBuilder()

    fun flushParagraph() {
        if (paragraphBuffer.isNotEmpty()) {
            blocks.add(MarkdownBlock.Paragraph(paragraphBuffer.toString().trim()))
            paragraphBuffer.clear()
        }
    }

    for (line in lines) {
        val trimmedEnd = line.trimEnd()

        when {
            // Horizontal rule (must check before unordered list to avoid conflict with ---)
            horizontalRuleRegex.matches(trimmedEnd) -> {
                flushParagraph()
                blocks.add(MarkdownBlock.Divider)
            }

            // Header
            headerRegex.matches(trimmedEnd) -> {
                flushParagraph()
                val match = headerRegex.find(trimmedEnd)!!
                val level = match.groupValues[1].length.coerceAtMost(6)
                val text = match.groupValues[2].trim()
                blocks.add(MarkdownBlock.Header(level, text))
            }

            // Unordered list item (* / - / +)
            unorderedListRegex.matches(trimmedEnd) -> {
                flushParagraph()
                val match = unorderedListRegex.find(trimmedEnd)!!
                val indent = match.groupValues[1].length / 2
                val text = match.groupValues[2].trim()
                blocks.add(MarkdownBlock.ListItem(text = text, bullet = "•", indent = indent))
            }

            // Ordered list item (1. / 2) etc.)
            orderedListRegex.matches(trimmedEnd) -> {
                flushParagraph()
                val match = orderedListRegex.find(trimmedEnd)!!
                val indent = match.groupValues[1].length / 2
                val bullet = match.groupValues[2]
                val text = match.groupValues[3].trim()
                blocks.add(MarkdownBlock.ListItem(text = text, bullet = bullet, indent = indent))
            }

            // Blank line
            trimmedEnd.isBlank() -> {
                flushParagraph()
                blocks.add(MarkdownBlock.Blank)
            }

            // Regular text / continuation line
            else -> {
                if (paragraphBuffer.isNotEmpty()) paragraphBuffer.append(" ")
                paragraphBuffer.append(trimmedEnd.trimStart())
            }
        }
    }
    flushParagraph()
    return blocks
}

// --- Inline markdown parser (bold, italic, links, inline code) ---

private fun parseInlineMarkdown(text: String, linkColor: Color): AnnotatedString {
    return buildAnnotatedString {
        var i = 0
        while (i < text.length) {
            when {
                // Inline code: `code`
                text.startsWith("`", i) -> {
                    val end = text.indexOf("`", i + 1)
                    if (end != -1) {
                        withStyle(SpanStyle(
                            fontWeight = FontWeight.Medium,
                            background = Color.Gray.copy(alpha = 0.15f)
                        )) {
                            append(text.substring(i + 1, end))
                        }
                        i = end + 1
                    } else {
                        append("`")
                        i++
                    }
                }

                // Bold + Italic: ***text***
                text.startsWith("***", i) -> {
                    val end = text.indexOf("***", i + 3)
                    if (end != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)) {
                            appendInlineRecursive(text.substring(i + 3, end), linkColor)
                        }
                        i = end + 3
                    } else {
                        append("*")
                        i++
                    }
                }

                // Bold: **text**
                text.startsWith("**", i) -> {
                    val end = findClosingDelimiter(text, "**", i + 2)
                    if (end != -1) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            appendInlineRecursive(text.substring(i + 2, end), linkColor)
                        }
                        i = end + 2
                    } else {
                        append("*")
                        i++
                    }
                }

                // Italic: *text* (but not **)
                text.startsWith("*", i) && !text.startsWith("**", i) -> {
                    val end = text.indexOf("*", i + 1)
                    if (end != -1) {
                        withStyle(SpanStyle(fontStyle = FontStyle.Italic)) {
                            appendInlineRecursive(text.substring(i + 1, end), linkColor)
                        }
                        i = end + 1
                    } else {
                        append("*")
                        i++
                    }
                }

                // Link: [text](url)
                text.startsWith("[", i) -> {
                    val closeBracket = text.indexOf("]", i + 1)
                    val openParen = if (closeBracket != -1) closeBracket + 1 else -1
                    val closeParen = if (openParen != -1 && openParen < text.length && text[openParen] == '(')
                        text.indexOf(")", openParen + 1) else -1

                    if (closeBracket != -1 && closeParen != -1) {
                        val linkText = text.substring(i + 1, closeBracket)
                        val url = text.substring(openParen + 1, closeParen)
                        pushStringAnnotation("URL", url)
                        withStyle(
                            SpanStyle(
                                color = linkColor,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(linkText)
                        }
                        pop()
                        i = closeParen + 1
                    } else {
                        append("[")
                        i++
                    }
                }

                else -> {
                    append(text[i])
                    i++
                }
            }
        }
    }
}

/**
 * Finds the closing delimiter, handling nested content properly.
 * This allows bold text to contain links and other inline elements.
 */
private fun findClosingDelimiter(text: String, delimiter: String, startIndex: Int): Int {
    var i = startIndex
    while (i <= text.length - delimiter.length) {
        if (text.startsWith(delimiter, i)) {
            return i
        }
        // Skip over links to avoid matching ] as part of delimiter
        if (text[i] == '[') {
            val closeBracket = text.indexOf(']', i + 1)
            if (closeBracket != -1) {
                val nextChar = if (closeBracket + 1 < text.length) text[closeBracket + 1] else ' '
                if (nextChar == '(') {
                    val closeParen = text.indexOf(')', closeBracket + 2)
                    if (closeParen != -1) {
                        i = closeParen + 1
                        continue
                    }
                }
            }
        }
        i++
    }
    return -1
}

/**
 * Recursively parses inline markdown within an already-styled span.
 * Handles links inside bold text, etc.
 */
private fun AnnotatedString.Builder.appendInlineRecursive(text: String, linkColor: Color) {
    var i = 0
    while (i < text.length) {
        when {
            // Link inside styled text
            text.startsWith("[", i) -> {
                val closeBracket = text.indexOf("]", i + 1)
                val openParen = if (closeBracket != -1) closeBracket + 1 else -1
                val closeParen = if (openParen != -1 && openParen < text.length && text[openParen] == '(')
                    text.indexOf(")", openParen + 1) else -1

                if (closeBracket != -1 && closeParen != -1) {
                    val linkText = text.substring(i + 1, closeBracket)
                    val url = text.substring(openParen + 1, closeParen)
                    pushStringAnnotation("URL", url)
                    withStyle(
                        SpanStyle(
                            color = linkColor,
                            textDecoration = TextDecoration.Underline
                        )
                    ) {
                        append(linkText)
                    }
                    pop()
                    i = closeParen + 1
                } else {
                    append("[")
                    i++
                }
            }

            else -> {
                append(text[i])
                i++
            }
        }
    }
}
