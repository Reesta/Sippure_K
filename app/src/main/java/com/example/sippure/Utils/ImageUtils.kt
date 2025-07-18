package com.example.sippure.Utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast // Import Toast for user feedback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class ImageUtils(private val activity: Activity, private val registryOwner: ActivityResultRegistryOwner) {
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var onImageSelectedCallback: ((Uri?) -> Unit)? = null

    /**
     * Registers the ActivityResultLaunchers for gallery selection and permission requests.
     * This method should be called once in the `onCreate` or `onAttach` of your Activity/Fragment
     * using the `ActivityResultRegistryOwner` (which your Activity implements).
     *
     * @param onImageSelected A callback function that will be invoked with the Uri of the
     * selected image (or null if selection is cancelled or fails).
     */
    fun registerLaunchers(onImageSelected: (Uri?) -> Unit) {
        onImageSelectedCallback = onImageSelected

        // Register for selecting image from gallery
        galleryLauncher = registryOwner.activityResultRegistry.register(
            "galleryLauncher", ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val uri = result.data?.data
            if (result.resultCode == Activity.RESULT_OK && uri != null) {
                // Image successfully selected
                onImageSelectedCallback?.invoke(uri)
            } else {
                // Image selection cancelled or failed
                Log.e("ImageUtils", "Image selection cancelled or failed: ResultCode=${result.resultCode}, Uri is null=${uri == null}")
                onImageSelectedCallback?.invoke(null) // Notify callback with null for cancellation/failure
                Toast.makeText(activity, "Image selection cancelled.", Toast.LENGTH_SHORT).show() // User feedback
            }
        }

        // Register permission request
        permissionLauncher = registryOwner.activityResultRegistry.register(
            "permissionLauncher", ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission granted, proceed to open gallery
                openGallery()
            } else {
                // Permission denied, inform the user
                Log.e("ImageUtils", "Permission to access photos denied.")
                Toast.makeText(
                    activity,
                    "Permission to access photos denied. Please enable it in settings to select an image.",
                    Toast.LENGTH_LONG
                ).show()
                onImageSelectedCallback?.invoke(null) // Notify callback with null as no image can be selected
            }
        }
    }

    /**
     * Launches the image picker. It first checks for necessary permissions
     * and requests them if not already granted.
     */
    fun launchImagePicker() {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            permissionLauncher.launch(permission)
        } else {
            // Permission already granted, open gallery directly
            openGallery()
        }
    }

    /**
     * Internal helper function to create and launch the gallery intent.
     */
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*" // Ensure only image files are shown
        }
        galleryLauncher.launch(intent)
    }
}