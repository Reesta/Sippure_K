import com.example.sippure.UserRepository
import com.example.sippure.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException // Import for specific error handling
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log // For logging

class UserRepositoryImpl : UserRepository {

    // Constants for database nodes
    companion object {
        private const val TAG = "UserRepositoryImpl" // For logging
        private const val USERS_NODE = "Users"
    }

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref: DatabaseReference = database.reference.child(USERS_NODE)

    override fun addUserToDatabase(
        userId: String,
        model: UserModel,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).setValue(model).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User data added for user ID: $userId")
                callback(true, "User added")
            } else {
                val errorMessage = task.exception?.message ?: "An unknown error occurred while adding user data."
                Log.e(TAG, "Failed to add user data for user ID: $userId - Error: $errorMessage", task.exception)
                callback(false, errorMessage)
            }
        }
    }

    override fun updateProfile(
        userId: String,
        data: MutableMap<String, Any?>,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId).updateChildren(data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "User profile updated for user ID: $userId")
                callback(true, "User updated")
            } else {
                val errorMessage = task.exception?.message ?: "An unknown error occurred while updating profile."
                Log.e(TAG, "Failed to update profile for user ID: $userId - Error: $errorMessage", task.exception)
                callback(false, errorMessage)
            }
        }
    }

    override fun forgetPassword(
        email: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Password reset email sent to: $email")
                    callback(true, "Reset email sent to $email")
                } else {
                    val errorMessage = task.exception?.message ?: "An unknown error occurred while sending reset email."
                    Log.e(TAG, "Failed to send password reset email to: $email - Error: $errorMessage", task.exception)
                    callback(false, errorMessage)
                }
            }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    override fun getUserById(
        userId: String,
        callback: (UserModel?, Boolean, String) -> Unit
    ) {
        // Using addListenerForSingleValueEvent for a single fetch.
        // If you need real-time updates, use addValueEventListener and manage its lifecycle.
        ref.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    if (userModel != null) {
                        Log.d(TAG, "User data fetched successfully for ID: $userId")
                        callback(userModel, true, "Data fetched successfully")
                    } else {
                        Log.w(TAG, "User data exists but is malformed or null for ID: $userId")
                        callback(null, false, "User data is null or malformed")
                    }
                } else {
                    Log.d(TAG, "User does not exist for ID: $userId")
                    callback(null, false, "User does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database operation cancelled for ID: $userId - Error: ${error.message}", error.toException())
                callback(null, false, error.message)
            }
        })
    }

    override fun logout(callback: (Boolean, String) -> Unit) {
        try {
            auth.signOut()
            Log.d(TAG, "User logged out successfully.")
            callback(true, "logout successfully")
        } catch (e: Exception) {
            val errorMessage = e.message ?: "An unknown error occurred during logout."
            Log.e(TAG, "Logout failed - Error: $errorMessage", e)
            callback(false, errorMessage)
        }
    }
}