package com.sample.snoozeloo.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.sample.snoozeloo.R
import com.sample.snoozeloo.ui.theme.DIMEN_50_dp


@Composable
fun CustomAlertDialog(shouldShowDialog: MutableState<Boolean>, selectedValue: String, onSave: (String) -> Unit) {

    if (shouldShowDialog.value) {

        var text by remember { mutableStateOf(selectedValue) } // Move the text state here to share with Save

        AlertDialog(
            onDismissRequest = {
                shouldShowDialog.value = false
            },

            title = { Text(text = stringResource(R.string.sz_alarm_name)) },

            text = {

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = DIMEN_50_dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    placeholder = {

                        Text(
                            text = stringResource(R.string.sz_enter_your_alarm_name),
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.outlineVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },

                    keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences)
                )
            },

            confirmButton = {
                Button(
                    onClick = {
                        onSave(text) // Pass the entered text back
                        shouldShowDialog.value = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.sz_save),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }
}

