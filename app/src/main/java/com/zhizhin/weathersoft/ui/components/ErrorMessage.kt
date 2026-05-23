package com.zhizhin.weathersoft.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Диалог с ошибкой.
 * Показывается поверх экрана с текстом ошибки и кнопкой "Понятно".
 */
@Composable
fun ErrorMessage(message: String, onDismiss: () -> Unit) {
    // Локальный стейт – показывать диалог или нет
    var show by remember { mutableStateOf(true) }

    if (show) {
        AlertDialog(
            onDismissRequest = {
                show = false
                onDismiss()
            },
            title = { Text("Ошибка") },
            text = { Text(message) },
            confirmButton = {
                Button(
                    onClick = {
                        show = false
                        onDismiss()
                    }
                ) {
                    Text("Понятно")
                }
            }
        )
    }
}
