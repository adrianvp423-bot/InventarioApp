package com.example.inventarioapp.feature.product.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.inventarioapp.feature.product.presentation.section.ProductFormSection
import com.example.inventarioapp.feature.product.presentation.viewmodel.ProductFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    vm: ProductFormViewModel,
    productId: String?,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ui by vm.ui.collectAsStateWithLifecycle()

    LaunchedEffect(productId) {
        vm.load(productId)
    }

    LaunchedEffect(ui.saved) {
        if (ui.saved) {
            onDone()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (productId == null) "Nuevo producto" else "Editar producto",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onDone) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ProductFormSection(
                ui = ui,
                onName = vm::updateName,
                onDescription = vm::updateDescription,
                onPrice = vm::updatePrice,
                onQuantity = vm::updateQuantity,
                onSave = vm::save
            )
        }
    }
}
