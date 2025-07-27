package com.example.sippure.view

import android.app.Activity
import android.content.Context
import android.content.Intent // Import Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack // Import ArrowBack icon
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.sippure.R
import com.example.sippure.Repository.ProductRepositoryImpl
import com.example.sippure.Utils.ImageUtils
import com.example.sippure.model.ProductModel
import com.example.sippure.viewmodel.ProductViewModel
import com.example.sippure.ui.theme.SippureTheme

class AddProductActivity : ComponentActivity() {
    lateinit var imageUtils: ImageUtils
    var selectedImageUri by mutableStateOf<Uri?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        imageUtils = ImageUtils(this, this)
        imageUtils.registerLaunchers { uri ->
            selectedImageUri = uri
        }
        setContent {
            SippureTheme {
                AddProductScreen(
                    selectedImageUri = selectedImageUri,
                    onPickImage = { imageUtils.launchImagePicker() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    selectedImageUri: Uri?,
    onPickImage: () -> Unit
) {
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }

    val repo = remember { ProductRepositoryImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    val context = LocalContext.current
    val activity = context as? Activity

    val appBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332), // Dark Green
            Color(0xFF2D5016), // Slightly Lighter Green
            Color(0xFF40601E), // Even Lighter Green
            Color(0xFF52734D)  // Lightest Green/Grayish Green
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { }, // No title in the center, as the custom text "Add New Product" is used below
                navigationIcon = {
                    IconButton(onClick = {
                        // Navigate back to DashboardActivity
                        val intent = Intent(context, DashboardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                        activity?.finish() // Finish current activity
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Dashboard",
                            tint = Color.White // Set icon color
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Make top bar transparent
                    titleContentColor = Color.White // Title color if you were to have one
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackgroundGradient)
                .padding(paddingValues) // Apply padding from Scaffold
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    // Header (moved inside LazyColumn, after TopAppBar padding)
                    Text(
                        text = "✨ Add New Product",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 30.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    EnhancedImagePicker(
                        selectedImageUri = selectedImageUri,
                        onPickImage = onPickImage
                    )
                }

                item {
                    EnhancedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        placeholder = "Product Name",
                        icon = Icons.Default.Info,
                        keyboardType = KeyboardType.Text
                    )
                }

                item {
                    EnhancedTextField(
                        value = productDescription,
                        onValueChange = { productDescription = it },
                        placeholder = "Product Description",
                        icon = Icons.Default.AddCircle,
                        keyboardType = KeyboardType.Text,
                        maxLines = 3
                    )
                }

                item {
                    EnhancedTextField(
                        value = productPrice,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() || it == '.' }) {
                                productPrice = newValue
                            }
                        },
                        placeholder = "Product Price",
                        icon = Icons.Default.Check,
                        keyboardType = KeyboardType.Number
                    )
                }

                item {
                    EnhancedSubmitButton(
                        onClick = {
                            val priceDouble = productPrice.toDoubleOrNull()
                            if (selectedImageUri == null) {
                                Toast.makeText(context, "Please select an image first", Toast.LENGTH_SHORT).show()
                            } else if (productName.isBlank() || productDescription.isBlank() || productPrice.isBlank()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            } else if (priceDouble == null || priceDouble <= 0) {
                                Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.uploadImage(context, selectedImageUri) { imageUrl ->
                                    if (imageUrl != null) {
                                        val model = ProductModel(
                                            "",
                                            productName,
                                            priceDouble,
                                            productDescription,
                                            imageUrl
                                        )
                                        viewModel.addProduct(model) { success, message ->
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                            if (success) {
                                                // Navigate back to DashboardActivity on success
                                                val intent = Intent(context, DashboardActivity::class.java)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                context.startActivity(intent)
                                                activity?.finish() // Finish current activity
                                            }
                                        }
                                    } else {
                                        Log.e("Upload Error", "Failed to upload image to Cloudinary")
                                        Toast.makeText(context, "Image upload failed. Try again.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        enabled = productName.isNotBlank() && productDescription.isNotBlank() && productPrice.isNotBlank() && productPrice.toDoubleOrNull() != null
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun EnhancedImagePicker(
    selectedImageUri: Uri?,
    onPickImage: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "imagePickerScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .scale(scale)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.3f)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                isPressed = true
                onPickImage()
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected Product Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            CircleShape
                        )
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Change Image",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val pulseAnimation = rememberInfiniteTransition(label = "pulse")
                    val alpha by pulseAnimation.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, easing = LinearOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "alpha"
                    )

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .background(
                                Color.White.copy(alpha = alpha * 0.3f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Image",
                            tint = Color.White,
                            modifier = Modifier.size(45.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Tap to add product image",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isFocused) 12.dp else 6.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = Color.Black.copy(alpha = 0.2f),
                spotColor = Color.White.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = if (isFocused) 0.15f else 0.1f)
        )
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .onFocusChanged { focusState -> isFocused = focusState.isFocused },
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            maxLines = maxLines,
            textStyle = LocalTextStyle.current.copy(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White.copy(alpha = 0.8f),
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White.copy(alpha = 0.9f),
                disabledTextColor = Color.White.copy(alpha = 0.5f),
                errorTextColor = Color.Red.copy(alpha = 0.8f),
                errorBorderColor = Color.Red.copy(alpha = 0.8f)
            )
        )
    }
}

@Composable
fun EnhancedSubmitButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "buttonScale"
    )

    val gradientColors = if (enabled) {
        listOf(
            Color(0xFF4CAF50),
            Color(0xFF388E3C)
        )
    } else {
        listOf(
            Color.Gray.copy(alpha = 0.6f),
            Color.Gray.copy(alpha = 0.4f)
        )
    }

    Button(
        onClick = {
            if (enabled) {
                isPressed = true
                onClick()
            }
        },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale)
            .shadow(
                elevation = if (enabled) 12.dp else 4.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color.Black.copy(alpha = 0.3f),
                spotColor = Color(0xFF4CAF50).copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(gradientColors),
                    RoundedCornerShape(28.dp)
                )
                .graphicsLayer {
                    alpha = 0.99f
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Product",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Add Product",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductBodyPreview() {
    SippureTheme {
        AddProductScreen(
            selectedImageUri = null,
            onPickImage = {}
        )
    }
}