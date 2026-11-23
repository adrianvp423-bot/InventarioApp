package com.example.inventarioapp.core.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.inventarioapp.R
import com.example.inventarioapp.core.di.FirebaseClients
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class AuthUser(
    val uid: String,
    val email: String?,
    val displayName: String?
)

interface AuthSessionProvider {
    fun observeUser(): Flow<AuthUser?>
    suspend fun signInWithGoogle(activityContext: Context): AuthUser
    fun signOut()
}

class FirebaseAuthSessionProvider @Inject constructor(
    private val firebaseClients: FirebaseClients
) : AuthSessionProvider {

    override fun observeUser(): Flow<AuthUser?> = callbackFlow {
        val auth = firebaseClients.auth
        if (auth == null) {
            trySend(null)
            awaitClose { }
            return@callbackFlow
        }

        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val current = firebaseAuth.currentUser
            trySend(
                current?.let {
                    AuthUser(
                        uid = it.uid,
                        email = it.email,
                        displayName = it.displayName
                    )
                }
            )
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signInWithGoogle(activityContext: Context): AuthUser {
        val auth = firebaseClients.auth
            ?: error("Falta app/google-services.json desde Firebase Console para iniciar sesión con Google")

        val credentialManager = CredentialManager.create(activityContext)
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(activityContext.getString(R.string.firebase_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            request = request,
            context = activityContext
        )
        val googleCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
        val firebaseCredential = GoogleAuthProvider.getCredential(googleCredential.idToken, null)
        val authResult = auth.signInWithCredential(firebaseCredential).await()
        val user = authResult.user ?: error("No fue posible autenticar con Google")
        return AuthUser(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName
        )
    }

    override fun signOut() {
        firebaseClients.auth?.signOut()
    }
}
