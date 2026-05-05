package com.example.fe.feature.solver.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fe.feature.solver.SolverViewModel
import com.example.fe.feature.solver.component.SmartKeyboardPanel
import com.example.fe.ui.theme.BgDivider
import com.example.fe.ui.theme.BgPrimary
import com.example.fe.ui.theme.BgSurface
import com.example.fe.ui.theme.CodeBgDark
import com.example.fe.ui.theme.Primary
import com.example.fe.ui.theme.TextPrimary
import com.example.fe.ui.theme.TextSecondary

@Composable
fun EditorFullScreen(
    problemId: Long,
    viewModel: SolverViewModel,
    onBack: () -> Unit = {}
) {

    val uiState by viewModel.uiState.collectAsState()
    val codeFromVm = uiState.code

    var tfv by remember {
        mutableStateOf(TextFieldValue(codeFromVm, selection = TextRange(codeFromVm.length)))
    }

    LaunchedEffect(codeFromVm) {
        if (codeFromVm != tfv.text) {
            tfv = tfv.copy(text = codeFromVm, selection = TextRange(codeFromVm.length))
        }
    }

    var editorFocused by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgSurface)
                    .padding(top = 10.dp, end = 16.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.CloseFullscreen,
                        contentDescription = "닫기",
                        tint = TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(18.dp),
                    color = CodeBgDark
                ) {
                    BasicTextField(
                        value = tfv,
                        onValueChange = { newValue ->
                            tfv = newValue
                            viewModel.updateCode(newValue.text)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp)
                            .onFocusChanged { editorFocused = it.isFocused },
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            color = TextPrimary,
                            lineHeight = 20.sp
                        ),
                        cursorBrush = SolidColor(Primary),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Ascii,
                            imeAction = ImeAction.None
                        )
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = editorFocused,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .imePadding()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = BgSurface,
                shadowElevation = 8.dp
            ) {
                Column {
                    HorizontalDivider(color = BgDivider)
                    SmartKeyboardPanel(
                        onInsert = { insert ->
                            val updated = insertIntoTextFieldValue(tfv, insert)
                            tfv = updated
                            viewModel.updateCode(updated.text)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}

private fun insertIntoTextFieldValue(value: TextFieldValue, insert: String): TextFieldValue {
    val text = value.text
    val start = value.selection.start.coerceIn(0, text.length)
    val end = value.selection.end.coerceIn(0, text.length)

    val newText = buildString(text.length + insert.length) {
        append(text.substring(0, start))
        append(insert)
        append(text.substring(end))
    }

    val newCursor = start + insert.length
    return value.copy(text = newText, selection = TextRange(newCursor))
}
