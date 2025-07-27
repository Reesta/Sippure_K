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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Eco
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.sippure.ui.theme.SippureTheme
import com.example.sippure.R
import kotlinx.coroutines.delay

class ProductDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val teaName = intent.getStringExtra("teaName") ?: "Unknown Tea"
        val teaDescription = intent.getStringExtra("teaDescription") ?: "No description available."
        val teaImageRes = intent.getIntExtra("teaImageRes", 0)
        val teaPrice = intent.getDoubleExtra("teaPrice", 0.0)

        setContent {
            SippureTheme {
                ProductDetailScreen(
                    name = teaName,
                    description = teaDescription,
                    imageRes = teaImageRes,
                    price = teaPrice,
                    onBackClick = { finish() },
                    onBuyNow = { name, price ->
                        val intent = Intent(this, CheckoutActivity::class.java).apply {
                            putExtra("productName", name)
                            putExtra("productPrice", price)
                            putExtra("productImageRes", teaImageRes)
                        }
                        startActivity(intent)
                        Toast.makeText(this, "Proceeding to checkout for $name!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun AnimatedIconComponent(
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
fun PulsingBuyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
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
            .shadow(12.dp, RoundedCornerShape(20.dp)),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 16.dp,
            pressedElevation = 8.dp
        ),
        shape = RoundedCornerShape(20.dp),
        content = content
    )
}

@Composable
fun FeatureChip(
    text: String,
    icon: ImageVector,
    delay: Int = 0
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(delay.toLong())
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        ) + fadeIn()
    ) {
        Card(
            modifier = Modifier
                .shadow(4.dp, RoundedCornerShape(25.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1B4332).copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(25.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF388E3C)
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    name: String,
    description: String,
    imageRes: Int,
    price: Double,
    onBackClick: () -> Unit,
    onBuyNow: (String, Double) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332),
            Color(0xFF2D5016),
            Color(0xFF40601E),
            Color(0xFF52734D)
        )
    )

    // Animation states
    var headerVisible by remember { mutableStateOf(false) }
    var imageVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        headerVisible = true
        delay(200)
        imageVisible = true
        delay(300)
        contentVisible = true
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
                            text = name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
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
                // Product Image with enhanced styling
                AnimatedVisibility(
                    visible = imageVisible,
                    enter = scaleIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + fadeIn()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .shadow(16.dp, RoundedCornerShape(24.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0x40000000)
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (imageRes != 0) {
                                Image(
                                    painter = painterResource(id = imageRes),
                                    contentDescription = name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(24.dp))
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0xFF2D5016).copy(alpha = 0.8f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Eco,
                                            contentDescription = null,
                                            modifier = Modifier.size(48.dp),
                                            tint = Color.White.copy(alpha = 0.7f)
                                        )
                                        Text(
                                            "Premium Tea",
                                            color = Color.White.copy(alpha = 0.7f),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }

                            // Floating price badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                                    .background(
                                        Color(0xFF1B4332).copy(alpha = 0.95f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        2.dp,
                                        Color(0xFF388E3C).copy(alpha = 0.8f),
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = if (price % 1.0 == 0.0) {
                                        "Rs ${price.toInt()}"
                                    } else {
                                        "Rs ${String.format("%.2f", price)}"
                                    },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Feature chips
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                    ) {
                        FeatureChip("Premium Quality", Icons.Default.Star, 100)
                        FeatureChip("Best Price", Icons.Default.LocalOffer, 200)
                        FeatureChip("Natural", Icons.Default.Eco, 300)
                    }
                }

                // Product Details Card
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    ) + fadeIn()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(12.dp, RoundedCornerShape(20.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0x40000000)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Product Name with icon
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            Color(0xFF388E3C).copy(alpha = 0.7f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    AnimatedIconComponent(
                                        icon = Icons.Default.Eco,
                                        modifier = Modifier.size(24.dp),
                                        delay = 600
                                    )
                                }
                                Column {
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White
                                    )
                                    Text(
                                        text = "Premium Tea Collection",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            // Star rating
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                repeat(5) { index ->
                                    AnimatedIconComponent(
                                        icon = Icons.Default.Star,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color(0xFFFFD700),
                                        delay = 700 + (index * 100)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "4.8 (127 reviews)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }

                            // Description
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFF1B4332).copy(alpha = 0.6f)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(bottom = 12.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Eco,
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = Color(0xFF388E3C)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "About This Tea",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            color = Color.White
                                        )
                                    }
                                    Text(
                                        text = description,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White.copy(alpha = 0.9f),
                                        lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.2
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Enhanced Buy Now Button
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                ) {
                    PulsingBuyButton(
                        onClick = { onBuyNow(name, price) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "🛒 Buy Now",
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}