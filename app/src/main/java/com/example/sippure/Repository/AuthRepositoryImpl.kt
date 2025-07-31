package com.example.sippure.Repository


import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class AuthRepositoryImpl(val auth: FirebaseAuth) : AuthRepository {

    override fun login(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Login successful for email: $email")
                    callback(true, "Login successfully")
                } else {
                    val errorMessage = task.exception?.message ?: "An unknown error occurred during login."
                    Log.e(TAG, "Login failed for email: $email - Error: $errorMessage", task.exception)
                    callback(false, errorMessage)
                }
            }
    }

    override fun register(
        email: String,
        password: String,
        callback: (Boolean, String, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    Log.d(TAG, "Registration successful for email: $email, User ID: $userId")
                    callback(true, "Registered successfully", userId ?: "")
                } else {
                    val errorMessage = task.exception?.message ?: "An unknown error occurred during registration."
                    Log.e(TAG, "Registration failed for email: $email - Error: $errorMessage", task.exception)
                    // Optional: More specific error messages for FirebaseAuthException
                    if (task.exception is FirebaseAuthException) {
                        when ((task.exception as FirebaseAuthException).errorCode) {
                            "ERROR_WEAK_PASSWORD" -> callback(false, "Password is too weak. Please choose a stronger one.", "")
                            "ERROR_INVALID_EMAIL" -> callback(false, "The email address is not valid.", "")
                            "ERROR_EMAIL_ALREADY_IN_USE" -> callback(false, "This email address is already registered.", "")
                            else -> callback(false, errorMessage, "")
                        }
                    } else {
                        callback(false, errorMessage, "")
                    }
                }
            }
    }

}