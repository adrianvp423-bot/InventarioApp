package com.example.inventarioapp.feature.product.data.repository

import com.example.inventarioapp.core.di.FirebaseClients
import com.example.inventarioapp.feature.product.data.local.ProductDao
import com.example.inventarioapp.feature.product.data.mapper.toDomain
import com.example.inventarioapp.feature.product.data.mapper.toDto
import com.example.inventarioapp.feature.product.data.mapper.toEntity
import com.example.inventarioapp.feature.product.data.remote.FirestoreProductDataSource
import com.example.inventarioapp.feature.product.domain.model.Product
import com.example.inventarioapp.feature.product.domain.repository.ProductRepository
import com.example.inventarioapp.feature.product.domain.repository.SyncReport
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dao: ProductDao,
    private val remote: FirestoreProductDataSource,
    private val firebaseClients: FirebaseClients
) : ProductRepository {
    private val syncMutex = Mutex()

    override fun observeProducts(): Flow<List<Product>> =
        dao.observeVisible().map { rows -> rows.map { it.toDomain() } }

    override suspend fun findById(id: String): Product? =
        dao.find(id)?.takeUnless { it.isDeleted }?.toDomain()

    override suspend fun save(product: Product) {
        dao.upsert(product.copy(updatedAt = System.currentTimeMillis()).toEntity())
    }

    override suspend fun delete(id: String) {
        val current = dao.find(id) ?: return
        dao.upsert(
            current.copy(
                isDeleted = true,
                pending = true,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun syncNow(): SyncReport = syncMutex.withLock {
        val auth = firebaseClients.auth
            ?: error("Agrega app/google-services.json desde Firebase Console para sincronizar con Firestore")
        val uid = auth.currentUser?.uid
            ?: error("Inicia sesión con Google para sincronizar con Firestore")

        var uploaded = 0
        for (local in dao.getPending()) {
            remote.push(uid, local.toDto())
            dao.markSynced(local.id, local.updatedAt)
            uploaded++
        }

        val cloudItems = remote.fetchFromServer(uid)
        for (dto in cloudItems) {
            val cloud = dto.toEntity()
            val local = dao.find(cloud.id)
            if (local?.pending == true) continue
            if (local == null || cloud.updatedAt > local.updatedAt) {
                dao.upsert(cloud)
            }
        }

        SyncReport(uploaded = uploaded, downloaded = cloudItems.size)
    }
}
