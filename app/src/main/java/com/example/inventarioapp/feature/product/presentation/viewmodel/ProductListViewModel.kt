package com.example.inventarioapp.feature.product.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventarioapp.core.auth.AuthSessionProvider
import com.example.inventarioapp.feature.product.domain.repository.ProductRepository
import com.example.inventarioapp.feature.product.presentation.state.ProductListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ProductRepository,
    private val authSession: AuthSessionProvider
) : ViewModel() {

    private val _ui = MutableStateFlow(ProductListUiState())
    val ui: StateFlow<ProductListUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeProducts()
                .catch { e ->
                    _ui.update { it.copy(message = e.message ?: "Error al observar el inventario local") }
                }
                .collect { products ->
                    _ui.update { it.copy(products = products) }
                }
        }

        viewModelScope.launch {
            authSession.observeUser()
                .catch { /* Ignore auth stream errors */ }
                .collect { user ->
                    _ui.update {
                        it.copy(
                            userUid = user?.uid,
                            userEmail = user?.email,
                            userDisplayName = user?.displayName
                        )
                    }
                    if (user != null) {
                        sync()
                    }
                }
        }
    }

    fun signInWithGoogle(activityContext: Context) = viewModelScope.launch {
        _ui.update { it.copy(isSyncing = true, message = null) }
        try {
            authSession.signInWithGoogle(activityContext)
            _ui.update { it.copy(isSyncing = false) }
        } catch (e: Exception) {
            _ui.update {
                it.copy(
                    isSyncing = false,
                    message = e.message ?: "No se pudo iniciar sesión con Google"
                )
            }
        }
    }

    fun signOut() {
        authSession.signOut()
        _ui.update {
            it.copy(message = "Sesión cerrada. El inventario local sigue disponible offline.")
        }
    }

    fun sync() = viewModelScope.launch {
        _ui.update { it.copy(isSyncing = true, message = null) }
        try {
            val report = repository.syncNow()
            _ui.update {
                it.copy(
                    message = "Sincronizado: ${report.uploaded} enviados, ${report.downloaded} descargados"
                )
            }
        } catch (e: Exception) {
            _ui.update {
                it.copy(
                    message = e.message ?: "Sin conexión: datos locales disponibles"
                )
            }
        } finally {
            _ui.update { it.copy(isSyncing = false) }
        }
    }

    fun delete(id: String) = viewModelScope.launch {
        repository.delete(id)
        _ui.update { it.copy(message = "Producto eliminado del inventario local") }
    }

    fun clearMessage() {
        _ui.update { it.copy(message = null) }
    }
}
