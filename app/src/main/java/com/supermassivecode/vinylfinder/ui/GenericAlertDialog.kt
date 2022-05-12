package com.supermassivecode.vinylfinder.ui

import android.content.Context
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun GenericAlertDialog(context: Context, messageId: Int) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "Attention") },
            text = { Text(context.getString(messageId)) },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text("OK")
                }
            },
        )
    }
}
