package com.unltm.distance.datasource

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.unltm.distance.repository.Result
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AuthDataSource {
    private val auth = FirebaseAuth.getInstance()
    suspend fun sign(email: String, password: String) =
        suspendCancellableCoroutine<Result<AuthResult>> { coroutine ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { coroutine.resume(Result.Success(it)) }
                .addOnFailureListener { coroutine.resume(Result.Error(it)) }
        }

    suspend fun login(email: String, password: String) =
        suspendCancellableCoroutine<Result<AuthResult>> { coroutine ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { coroutine.resume(Result.Success(it)) }
                .addOnFailureListener { coroutine.resume(Result.Error(it)) }
        }
}