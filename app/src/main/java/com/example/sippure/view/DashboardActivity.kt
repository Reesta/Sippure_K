package com.example.sippure.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // Import for clickable modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sippure.R
import com.example.sippure.ui.theme.SippureTheme
import kotlinx.coroutines.delay

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SippureTheme {
                UserDashboard()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDashboard() {
    val context = LocalContext.current
    val activity = context as Activity
    // Use Elvis operator for null safety and capitalize the first part of the email
    val email: String = activity.intent.getStringExtra("email")?.split('@')?.get(0)?.capitalize() ?: "Tea Lover"
    var isVisible by remember { mutableStateOf(false) }

    val profileBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332), // Dark Green
            Color(0xFF2D5016), // Slightly Lighter Green
            Color(0xFF40601E), // Even Lighter Green
            Color(0xFF52734D)  // Lightest Green/Grayish Green
        )
    )

    LaunchedEffect(Unit) {
        delay(400)
        isVisible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* No title needed here as we have a custom greeting */ },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                actions = {
                    IconButton(onClick = {
                        context.startActivity(Intent(context, SearchActivity::class.java))
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF3C8D3F),
                contentColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Stay on Dashboard */ },
                    icon = { Icon(painterResource(R.drawable.baseline_home_24), contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        context.startActivity(Intent(context, FavouriteActivity::class.java))
                    },
                    icon = { Icon(painterResource(R.drawable.baseline_favorite_24), contentDescription = "Favorites") },
                    label = { Text("Favorites") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        context.startActivity(Intent(context, ProfileActivity::class.java))
                    },
                    icon = { Icon(painterResource(R.drawable.baseline_person_24), contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(profileBackgroundGradient)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                AnimatedVisibility(visible = isVisible, enter = fadeIn()) {
                    Text(
                        text = "Good Morning, $email!",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dash),
                        contentDescription = "Herbal Tea Hero",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color(0xAA1B5E20)),
                                    startY = 100f
                                )
                            )
                    )
                    Text(
                        text = "Discover the Magic of Herbal Teas",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    )
                }

                Text(
                    text = "🌿 Featured Tea Blends",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(sampleTeaBlends) { tea ->
                        // Pass the whole TeaBlend object to the card and define its click behavior
                        FeaturedTeaCard(tea = tea) { clickedTea ->
                            // Handle click: Navigate to ProductDetailActivity
                            val intent = Intent(context, ProductDetailActivity::class.java).apply {
                                // Pass product details as extras
                                putExtra("teaName", clickedTea.name)
                                putExtra("teaDescription", clickedTea.description)
                                putExtra("teaImageRes", clickedTea.imageRes)
                                putExtra("teaPrice", clickedTea.price) // Pass the price
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            }


            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(24.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        // Navigate to AddProductActivity
                        context.startActivity(Intent(context, AddProductActivity::class.java))
                    },
                    containerColor = Color(0xFF388E3C),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Product")
                }
            }
        }
    }
}

// Enhanced TeaBlend data class with price
data class TeaBlend(val name: String, val description: String, val imageRes: Int, val price: Double)

val sampleTeaBlends = listOf(
    TeaBlend("Chamomile Calm", "Relax with soothing chamomile flowers. Perfect for unwinding after a long day. Helps promote restful sleep and reduce anxiety.", R.drawable.chamomiletea, 200.00),
    TeaBlend("Butterfly Fresh", "Invigorate your senses with fresh Butterfly Pea Flower tea. Known for its vibrant blue color and mild, earthy flavor. Great for an antioxidant boost and can change color with citrus!", R.drawable.menubutterfly, 250.00),
    TeaBlend("Herbal Tea", "Experience warmth and zest from a delightful blend of lemon and ginger. Ideal for soothing colds, aiding digestion, or simply enjoying a refreshing drink.", R.drawable.herbaltea, 200.00),
    TeaBlend("Hibiscus Tea", "Savor the tart and fruity notes of natural hibiscus extracts. A refreshing and healthy beverage, rich in Vitamin C, and known to support blood pressure.", R.drawable.kotlinhibis, 300.00)
)

@Composable
fun FeaturedTeaCard(tea: TeaBlend, onClick: (TeaBlend) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick(tea) }, // Make the card clickable
        colors = CardDefaults.cardColors(containerColor = Color(0x30FFFFFF)), // Semi-transparent white
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = tea.imageRes),
                contentDescription = tea.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = tea.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tea.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 2, // Limit lines to keep card height consistent
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis // Add ellipsis for overflow
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Rs. ${String.format("%.2f", tea.price)}", // <--- CHANGED HERE: "Rs. " prefix
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White
                )
            }
        }
    }
}