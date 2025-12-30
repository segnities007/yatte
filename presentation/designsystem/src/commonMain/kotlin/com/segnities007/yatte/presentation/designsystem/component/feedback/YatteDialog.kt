package com.segnities007.yatte.presentation.designsystem.component.feedback

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.segnities007.yatte.presentation.designsystem.component.button.YatteTextButton
import com.segnities007.yatte.presentation.designsystem.theme.YatteSpacing

/**
 * Yatte確認ダイアログ
 *
 * シンプルなタイトル＋メッセージの確認ダイアログ。
 */
@Composable
fun YatteConfirmDialog(
    title: String,
    message: String,
    confirmText: String = "確認",
    dismissText: String = "キャンセル",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDestructive: Boolean = false,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            YatteTextButton(
                onClick = onConfirm,
                text = confirmText,
                textColor = if (isDestructive) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.primary
                },
            )
        },
        dismissButton = {
            YatteTextButton(
                onClick = onDismiss,
                text = dismissText,
            )
        },
        shape = RoundedCornerShape(YatteSpacing.lg),
    )
}

/**
 * Yatte汎用ダイアログ
 *
 * カスタムコンテンツを持つダイアログ。
 * フォーム入力やカラー選択などに使用。
 *
 * @param title タイトル
 * @param onDismiss 閉じる時のコールバック
 * @param confirmButton 確認ボタン
 * @param dismissButton キャンセルボタン（省略可）
 * @param content ダイアログ本体のコンテンツ
 */
@Composable
fun YatteDialog(
    title: String,
    onDismiss: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            androidx.compose.foundation.layout.Column {
                content()
            }
        },
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        shape = RoundedCornerShape(YatteSpacing.lg),
    )
}

@Preview(showBackground = true)
@Composable
private fun YatteConfirmDialogPreview() {
    YatteConfirmDialog(
        title = "Delete Task?",
        message = "Are you sure you want to delete this task? This action cannot be undone.",
        onConfirm = {},
        onDismiss = {},
        isDestructive = true
    )
}

@Preview(showBackground = true)
@Composable
private fun YatteDialogPreview() {
    YatteDialog(
        title = "Custom Dialog",
        onDismiss = {},
        confirmButton = { YatteTextButton(text = "OK", onClick = {}) },
        dismissButton = { YatteTextButton(text = "Cancel", onClick = {}) },
    ) {
        Text("Custom content can be placed here.")
    }
}
