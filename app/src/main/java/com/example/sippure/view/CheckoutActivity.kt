package com.example.sippure.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sippure.R
import com.example.sippure.ui.theme.SippureTheme
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class CheckoutActivity : ComponentActivity() {
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase
        database = Firebase.database

        val productName = intent.getStringExtra("productName") ?: "Selected Product"
        val productPrice = intent.getDoubleExtra("productPrice", 0.0)
        val productImageRes = intent.getIntExtra("productImageRes", 0)

        setContent {
            SippureTheme {
                CheckoutScreen(
                    productName = productName,
                    productPrice = productPrice,
                    productImageRes = productImageRes,
                    onBackClick = { finish() },
                    onConfirmPurchase = { selectedPaymentMethod, deliveryAddress ->
                        if (deliveryAddress.isBlank()) {
                            Toast.makeText(this, "Please enter your delivery address.", Toast.LENGTH_SHORT).show()
                        } else {
                            // ✅ Save order to Firebase Realtime Database
                            saveOrderToFirebase(
                                productName = productName,
                                productPrice = productPrice,
                                productImageRes = productImageRes,
                                deliveryAddress = deliveryAddress,
                                paymentMethod = selectedPaymentMethod
                            ) { success, message ->
                                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                                if (success) {
                                    // Navigate to Dashboard
                                    val intent = Intent(this, DashboardActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    private fun saveOrderToFirebase(
        productName: String,
        productPrice: Double,
        productImageRes: Int,
        deliveryAddress: String,
        paymentMethod: String,
        onComplete: (success: Boolean, message: String) -> Unit
    ) {
        // In real app, use FirebaseAuth.getInstance().currentUser?.uid
        val userId = "user_123"
        val orderId = database.getReference("orders").push().key ?: return

        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val orderData = mapOf(
            "orderId" to orderId,
            "productName" to productName,
            "productPrice" to productPrice,
            "productImageRes" to productImageRes,
            "deliveryAddress" to deliveryAddress,
            "paymentMethod" to paymentMethod,
            "timestamp" to timestamp
        )

        database.getReference("orders/$userId/$orderId")
            .setValue(orderData)
            .addOnSuccessListener {
                onComplete(true, "Order confirmed and saved successfully!")
            }
            .addOnFailureListener { exception ->
                onComplete(false, "Failed to save order: ${exception.message}")
            }
    }
}

@Composable
fun AnimatedIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = Color.White,
    delay: Int = 0
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delay.toLong())
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
    }
}

@Composable
fun PulsingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF388E3C)
        ),
        modifier = modifier
            .scale(scale)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 12.dp,
            pressedElevation = 8.dp
        ),
        content = content
    )
}

@Composable
fun SectionCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    delay: Int = 0,
    content: @Composable ColumnScope.() -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delay.toLong())
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy
            )
        ) + fadeIn()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x40000000) // Semi-transparent dark
            ),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                Color(0xFF388E3C).copy(alpha = 0.7f), // Green accent
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedIcon(
                            icon = icon,
                            modifier = Modifier.size(24.dp),
                            delay = delay + 200
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    productName: String,
    productPrice: Double,
    productImageRes: Int,
    onBackClick: () -> Unit,
    onConfirmPurchase: (String, String) -> Unit
) {
    val context = LocalContext.current
    var deliveryAddress by remember { mutableStateOf("") }
    var selectedPaymentOption by remember { mutableStateOf("Cash on Delivery") }
    val scrollState = rememberScrollState()
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332),
            Color(0xFF2D5016),
            Color(0xFF40601E),
            Color(0xFF52734D)
        )
    )

    // Header animation
    var headerVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        headerVisible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedVisibility(
                        visible = headerVisible,
                        enter = slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )
                    ) {
                        Text(
                            text = "Checkout",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = headerVisible,
                        enter = scaleIn(
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                        )
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier
                                .background(
                                    Color(0xFF1B4332).copy(alpha = 0.8f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Animated Header
                AnimatedVisibility(
                    visible = headerVisible,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    ) + fadeIn()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✨ Confirm Your Order",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Review your selection and complete your purchase",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Product Details Card
                SectionCard(
                    title = "Order Summary",
                    icon = Icons.Default.ShoppingCart,
                    delay = 300
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (productImageRes != 0) {
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .shadow(8.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2D5016).copy(alpha = 0.8f))
                                    .padding(8.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = productImageRes),
                                    contentDescription = productName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        Text(
                            text = productName,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFF1B4332).copy(alpha = 0.9f),
                                    RoundedCornerShape(25.dp)
                                )
                                .border(
                                    2.dp,
                                    Color(0xFF388E3C).copy(alpha = 0.6f),
                                    RoundedCornerShape(25.dp)
                                )
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = if (productPrice % 1.0 == 0.0) {
                                    "Total: Rs ${productPrice.toInt()}"
                                } else {
                                    "Total: Rs ${String.format("%.2f", productPrice)}"
                                },
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                        }
                    }
                }

                // Delivery Address Section
                SectionCard(
                    title = "Delivery Address",
                    icon = Icons.Default.LocationOn,
                    delay = 500
                ) {
                    OutlinedTextField(
                        value = deliveryAddress,
                        onValueChange = { deliveryAddress = it },
                        label = {
                            Text(
                                "🏠 Enter your complete delivery address",
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        },
                        singleLine = false,
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF388E3C),
                            unfocusedBorderColor = Color(0xFF52734D),
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.8f),
                            cursorColor = Color(0xFF388E3C),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White.copy(alpha = 0.9f)
                        ),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1B4332).copy(alpha = 0.7f))
                    )
                }

                // Payment Method Section
                SectionCard(
                    title = "Payment Method",
                    icon = Icons.Default.Payment,
                    delay = 700
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF1B4332).copy(alpha = 0.8f))
                            .border(
                                2.dp,
                                if (selectedPaymentOption == "Cash on Delivery")
                                    Color(0xFF388E3C).copy(alpha = 0.8f)
                                else Color.Transparent,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedPaymentOption = "Cash on Delivery" }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedPaymentOption == "Cash on Delivery",
                            onClick = { selectedPaymentOption = "Cash on Delivery" },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF388E3C),
                                unselectedColor = Color.White.copy(alpha = 0.7f)
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "💰 Cash on Delivery",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Pay when your order arrives",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (selectedPaymentOption == "Cash on Delivery") {
                            AnimatedIcon(
                                icon = Icons.Default.CheckCircle,
                                modifier = Modifier.size(24.dp),
                                tint = Color(0xFF388E3C)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Confirm Button
                PulsingButton(
                    onClick = { onConfirmPurchase(selectedPaymentOption, deliveryAddress) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "🚀 Confirm Purchase",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}