package com.example.sippure.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PriceChange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sippure.Repository.ProductRepositoryImpl
import com.example.sippure.model.ProductModel
import com.example.sippure.ui.theme.SippureTheme // Assuming your theme is here
import com.example.sippure.viewmodel.ProductViewModel

class UpdateProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SippureTheme { // Apply your theme here
                UpdateProductBody()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProductBody() {
    val context = LocalContext.current
    val activity = context as? Activity

    // Retrieve productId from intent
    val productId: String? = activity?.intent?.getStringExtra("productId")

    val repo = remember { ProductRepositoryImpl() }
    val viewModel = remember { ProductViewModel(repo) }

    // Observe product data from ViewModel
    val productData by viewModel.products.observeAsState(initial = null)

    // State variables for form fields
    var pName by remember { mutableStateOf("") }
    var pPrice by remember { mutableStateOf("") }
    var pDesc by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Fetch product data when productId is available
    LaunchedEffect(productId) {
        if (productId != null) {
            viewModel.getProductById(productId)
        }
    }

    // Update form fields when productData changes (i.e., after fetching)
    // This ensures fields are populated only once the data is loaded
    LaunchedEffect(productData) {
        if (productData != null) {
            pName = productData?.productName ?: ""
            pDesc = productData?.productDesc ?: ""
            pPrice = productData?.productPrice?.toString() ?: ""
        }
    }

    // Consistent Background Gradient
    val appBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332), // Dark Green
            Color(0xFF2D5016), // Slightly Lighter Green
            Color(0xFF40601E), // Even Lighter Green
            Color(0xFF52734D)  // Lightest Green/Grayish Green
        )
    )

    // Colors for content on the dark card background
    val cardBackgroundColor = Color(0x40000000) // Semi-transparent black, like Profile options cards
    val cardContentPrimaryColor = Color.White
    val cardContentSecondaryColor = Color.White.copy(alpha = 0.7f) // For labels/placeholders
    val cardContentBorderColor = Color.White.copy(alpha = 0.8f) // For focused borders/icons

    // Color for the update button
    val updateButtonColor = Color(0xFF388E3C) // Consistent vibrant green

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Update Product",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent // Scaffold container transparent to show Box background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appBackgroundGradient) // Apply consistent background
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp)) // Top padding
                    Text(
                        text = "✏️ Edit Product Details",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 28.sp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = Color.Black.copy(alpha = 0.2f)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Product Name TextField
                            OutlinedTextField(
                                value = pName,
                                onValueChange = { pName = it },
                                label = { Text("Product Name", color = cardContentSecondaryColor) },
                                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = cardContentBorderColor) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                textStyle = LocalTextStyle.current.copy(color = cardContentPrimaryColor, fontSize = 16.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = cardContentBorderColor,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = cardContentPrimaryColor,
                                    focusedTextColor = cardContentPrimaryColor,
                                    unfocusedTextColor = cardContentPrimaryColor.copy(alpha = 0.9f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Product Price TextField
                            OutlinedTextField(
                                value = pPrice,
                                onValueChange = { newValue ->
                                    // Allow only digits and a single decimal point
                                    if (newValue.all { it.isDigit() || it == '.' } && newValue.count { it == '.' } <= 1) {
                                        pPrice = newValue
                                    }
                                },
                                label = { Text("Product Price", color = cardContentSecondaryColor) },
                                leadingIcon = { Icon(Icons.Default.PriceChange, contentDescription = null, tint = cardContentBorderColor) },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                textStyle = LocalTextStyle.current.copy(color = cardContentPrimaryColor, fontSize = 16.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = cardContentBorderColor,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = cardContentPrimaryColor,
                                    focusedTextColor = cardContentPrimaryColor,
                                    unfocusedTextColor = cardContentPrimaryColor.copy(alpha = 0.9f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Product Description TextField
                            OutlinedTextField(
                                value = pDesc,
                                onValueChange = { pDesc = it },
                                label = { Text("Product Description", color = cardContentSecondaryColor) },
                                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, tint = cardContentBorderColor) },
                                modifier = Modifier.fillMaxWidth(),
                                minLines = 3,
                                maxLines = 5,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                textStyle = LocalTextStyle.current.copy(color = cardContentPrimaryColor, fontSize = 16.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = cardContentBorderColor,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = cardContentPrimaryColor,
                                    focusedTextColor = cardContentPrimaryColor,
                                    unfocusedTextColor = cardContentPrimaryColor.copy(alpha = 0.9f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                item {
                    // Update Product Button
                    Button(
                        onClick = {
                            val priceDouble = pPrice.toDoubleOrNull()
                            if (pName.isBlank() || pDesc.isBlank() || pPrice.isBlank()) {
                                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                            } else if (priceDouble == null || priceDouble <= 0) {
                                Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                            } else {
                                isLoading = true
                                val data = mutableMapOf<String, Any?>(
                                    "productDesc" to pDesc,
                                    "productPrice" to priceDouble,
                                    "productName" to pName,
                                    "productId" to productId // Ensure productId is included if needed by backend
                                )
                                viewModel.updateProduct(
                                    productId.toString(), data
                                ) { success, message ->
                                    isLoading = false
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                    if (success) {
                                        activity?.finish()
                                    }
                                }
                            }
                        },
                        enabled = !isLoading && pName.isNotBlank() && pDesc.isNotBlank() && pPrice.isNotBlank() && pPrice.toDoubleOrNull() != null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .shadow(8.dp, RoundedCornerShape(28.dp), ambientColor = Color.Black.copy(alpha = 0.3f)),
                        colors = ButtonDefaults.buttonColors(containerColor = updateButtonColor),
                        shape = RoundedCornerShape(28.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Updating...", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Update Product", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp)) // Bottom padding
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewUpdateProductBody() {
    SippureTheme { // Wrap preview in theme
        UpdateProductBody()
    }
}