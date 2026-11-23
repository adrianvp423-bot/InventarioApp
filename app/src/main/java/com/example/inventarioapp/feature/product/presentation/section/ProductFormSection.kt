package com.example.inventarioapp.feature.product.presentation.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.inventarioapp.feature.product.presentation.composables.ProductFormField
import com.example.inventarioapp.feature.product.presentation.state.ProductFormUiState

@Composable
fun ProductFormSection(
    ui: ProductFormUiState,
    onName: (String) -> Unit,
    onDescription: (String) -> Unit,
    onPrice: (String) -> Unit,
    onQuantity: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        if (ui.error != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = ui.error,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProductFormField(
                    label = "Nombre",
                    value = ui.name,
                    onValueChange = onName
                )
                ProductFormField(
                    label = "Descripción",
                    value = ui.description,
                    onValueChange = onDescription,
                    singleLine = false,
                    minLines = 3,
                    maxLines = 5
                )
                ProductFormField(
                    label = "Precio en COP",
                    value = ui.price,
                    onValueChange = onPrice,
                    keyboardType = KeyboardType.Number
                )
                ProductFormField(
                    label = "Cantidad",
                    value = ui.quantity,
                    onValueChange = onQuantity,
                    keyboardType = KeyboardType.Number
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = onSave,
            enabled = !ui.saving,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (ui.saving) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onSecondary,
                    strokeWidth = 2.5.dp,
                    modifier = Modifier.size(22.dp)
                )
            } else {
                Text(
                    text = "Guardar Producto",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
