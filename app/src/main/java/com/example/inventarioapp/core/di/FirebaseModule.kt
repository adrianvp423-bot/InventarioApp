package com.example.inventarioapp.core.di

import android.content.Context
import com.example.inventarioapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

data class FirebaseClients(
    val auth: FirebaseAuth?,
    val firestore: FirebaseFirestore?
)

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun firebaseClients(@ApplicationContext context: Context): FirebaseClients {
        val app = FirebaseApp.getApps(context).firstOrNull()
            ?: FirebaseApp.initializeApp(context)
            ?: return FirebaseClients(auth = null, firestore = null)

        val databaseId = context.getString(R.string.firestore_database_id)
        val auth = FirebaseAuth.getInstance(app)
        val firestore = FirebaseFirestore.getInstance(app, databaseId)
        return FirebaseClients(auth = auth, firestore = firestore)
    }
}
