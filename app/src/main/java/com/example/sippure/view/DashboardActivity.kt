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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    val email: String? = activity.intent.getStringExtra("email") ?: "Tea Lover"
    var isVisible by remember { mutableStateOf(false) }

    // Define the gradient from ProfileActivity
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    context.startActivity(Intent(context, SearchActivity::class.java))
                },
                containerColor = Color(0xFF388E3C), // Keeping this a slightly brighter green
                contentColor = Color.White,
                shape = RoundedCornerShape(50)
            ) {
                Icon(Icons.Filled.Search, contentDescription = "Search Tea Blends")
            }
        },
        bottomBar = {
            NavigationBar(
                // >>> CHANGE MADE HERE <<<
                // Using a semi-transparent version of the darkest green for a subtle, cohesive look
                containerColor = Color(0x901B4332), // Alpha 0x90 (144) for ~56% opacity of the dark green
                contentColor = Color.White, // Keep content (icons/text) white
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(sampleTeaBlends) { tea ->
                        FeaturedTeaCard(tea.name, tea.description, tea.imageRes)
                    }
                }
            }
        }
    }
}

data class TeaBlend(val name: String, val description: String, val imageRes: Int)

val sampleTeaBlends = listOf(
    TeaBlend("Chamomile Calm", "Relax with soothing chamomile flowers.", R.drawable.chamomiletea),
    TeaBlend("Butterfly Fresh", "Invigorate your senses with fresh Flower.", R.drawable.menubutterfly),
    TeaBlend("Herbal Tea", "Warmth and zest from lemon and ginger.", R.drawable.herbaltea)
)

@Composable
fun FeaturedTeaCard(name: String, description: String, imageRes: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4E342E)
                )
            }
        }
    }
}