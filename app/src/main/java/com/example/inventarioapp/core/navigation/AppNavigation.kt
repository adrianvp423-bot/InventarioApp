package com.example.inventarioapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.inventarioapp.feature.product.presentation.screen.ProductFormScreen
import com.example.inventarioapp.feature.product.presentation.screen.ProductListScreen
import com.example.inventarioapp.feature.product.presentation.viewmodel.ProductFormViewModel
import com.example.inventarioapp.feature.product.presentation.viewmodel.ProductListViewModel
import kotlinx.serialization.Serializable

@Serializable
data object ProductListRoute : NavKey

@Serializable
data class ProductFormRoute(val id: String? = null) : NavKey

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(ProductListRoute)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<ProductListRoute> {
                val vm: ProductListViewModel = hiltViewModel()
                ProductListScreen(
                    vm = vm,
                    onCreate = { backStack.add(ProductFormRoute()) },
                    onEdit = { id -> backStack.add(ProductFormRoute(id)) }
                )
            }
            entry<ProductFormRoute> { route ->
                val vm: ProductFormViewModel = hiltViewModel()
                ProductFormScreen(
                    vm = vm,
                    productId = route.id,
                    onDone = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
