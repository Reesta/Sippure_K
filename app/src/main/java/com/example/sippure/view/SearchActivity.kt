package com.example.sippure.view

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
// import androidx.compose.material.icons.filled.Person // Removed profile icon import
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sippure.R
import com.example.sippure.view.ui.theme.SippureTheme
import androidx.compose.ui.text.font.FontWeight // Added for potential bolding of title

// Updated Tea model using drawable resources
data class TeaModel(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val imageResId: Int
)

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SippureTheme {
                TeaSearchScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // Added for TextFieldDefaults.colors
@Composable
fun TeaSearchScreen() {
    // Define the dark green gradient consistent with your other activities
    val profileBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332), // Dark Green
            Color(0xFF2D5016), // Slightly Lighter Green
            Color(0xFF40601E), // Even Lighter Green
            Color(0xFF52734D)  // Lightest Green/Grayish Green
        )
    )

    val context = LocalContext.current

    val teaList = remember {
        listOf(
            TeaModel("1", "Chamomile Tea", "Relaxing and soothing", R.drawable.chamomile),
            TeaModel("2", "Butterfly Tea", "Fresh flavor", R.drawable.butterfly),
            TeaModel("3", "Hibiscus Tea", "Rich red color and tart flavor", R.drawable.hibiscus),
            TeaModel("4", "Herbal Tea", "Light citrus notes", R.drawable.herbal)
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var favoriteIds by remember { mutableStateOf(setOf<String>()) }

    val filteredTeas = teaList.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.description.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(profileBackgroundGradient) // <<< Changed Background Gradient >>>
            .padding(16.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = {
                    (context as? Activity)?.finish()
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f)) // This spacer helps center the title
                Text(
                    "Sippure Teas",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold // Made title bold for better visibility
                )
                Spacer(modifier = Modifier.weight(1f)) // This spacer helps center the title
                // Removed: Profile Icon IconButton
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search herbal teas...", color = Color.LightGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(filteredTeas) { tea ->
                    val isFavorite = favoriteIds.contains(tea.id)
                    TeaItem(
                        tea = tea,
                        isFavorite = isFavorite,
                        onFavoriteClick = {
                            favoriteIds = if (isFavorite) {
                                favoriteIds - tea.id
                            } else {
                                favoriteIds + tea.id
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TeaItem(
    tea: TeaModel,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    // Current TeaItem styling is a Row directly on the background.
    // For better visual appeal on the dark background, consider wrapping this in a Card
    // with a slightly darker semi-transparent background, similar to the previous suggestion,
    // but the prompt explicitly asked for *only* background change to the screen.
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = tea.imageResId),
            contentDescription = tea.name,
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(tea.name, color = Color.White, fontSize = 14.sp) // Stays white
            Text(tea.description, color = Color.LightGray, fontSize = 12.sp, maxLines = 1) // Stays light gray
        }

        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = if (isFavorite) Color(0xFFE91E63) else Color.LightGray // Stays original colors
            )
        }
    }
}