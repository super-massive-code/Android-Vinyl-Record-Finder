package com.supermassivecode.vinylfinder.ui

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.supermassivecode.vinylfinder.CurrencyUtils
import com.supermassivecode.vinylfinder.R

@Composable
fun GenericAlertDialog(
    context: Context,
    messageId: Int,
) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "Attention") },
            text = { Text(context.getString(messageId)) },
            confirmButton = {
                Button(onClick = {
                    openDialog.value = false
                }) {
                    Text("OK")
                }
            },
        )
    }
}

@Composable
fun MaxPriceDialog(
    title: String,
    message: String,
    initialPrice: Float? = null,
    dismissLabel: String,
    onSave: (Float) -> Unit,
    onDismiss: () -> Unit,
) {
    var priceText by remember {
        mutableStateOf(initialPrice?.let { CurrencyUtils.toLocalString(initialPrice.toBigDecimal()) } ?: "")
    }

    var isError by remember { mutableStateOf(false) }

    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val price = priceText.toFloatOrNull()
                    if (price != null) {
                        onSave(price)
                    } else {
                        isError = true
                    }
                },
            ) {
                Text(stringResource(R.string.dialog_cta_positive))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                },
            ) {
                Text(stringResource(R.string.dialog_cta_cancel))
            }
        },
    )
}
