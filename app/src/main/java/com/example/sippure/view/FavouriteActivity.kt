package com.example.sippure.view

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sippure.model.FavouriteTea
import com.example.sippure.view.ui.theme.SippureTheme
import com.google.firebase.database.FirebaseDatabase

class FavouriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SippureTheme {
                FavouriteTeaBody()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteTeaBody() {
    val context = LocalContext.current
    val activity = context as? Activity

    var teaName by remember { mutableStateOf("") }
    var teaType by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val profileBackgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332),
            Color(0xFF2D5016),
            Color(0xFF40601E),
            Color(0xFF52734D)
        )
    )

    val cardContentTextColor = Color(0xFF4A7033)
    val cardContentAccentColor = Color(0xFF7AAA54)
    val buttonColor = Color(0xFF388E3C)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(profileBackgroundGradient)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Favourite Herbal Tea",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { activity?.finish() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Tea Details",
                        color = cardContentTextColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = teaName,
                        onValueChange = { teaName = it },
                        label = { Text("Tea Name", color = cardContentTextColor.copy(alpha = 0.7f)) },
                        placeholder = { Text("Enter your favourite tea", color = cardContentTextColor.copy(alpha = 0.5f)) },
                        leadingIcon = {
                            Icon(Icons.Default.Info, contentDescription = null, tint = cardContentAccentColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cardContentTextColor,
                            unfocusedTextColor = cardContentTextColor,
                            focusedBorderColor = cardContentAccentColor,
                            unfocusedBorderColor = cardContentTextColor.copy(alpha = 0.3f),
                            cursorColor = cardContentAccentColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = teaType,
                        onValueChange = { teaType = it },
                        label = { Text("Tea Type", color = cardContentTextColor.copy(alpha = 0.7f)) },
                        placeholder = { Text("e.g., Green, Chamomile", color = cardContentTextColor.copy(alpha = 0.5f)) },
                        leadingIcon = {
                            Icon(Icons.Default.List, contentDescription = null, tint = cardContentAccentColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cardContentTextColor,
                            unfocusedTextColor = cardContentTextColor,
                            focusedBorderColor = cardContentAccentColor,
                            unfocusedBorderColor = cardContentTextColor.copy(alpha = 0.3f),
                            cursorColor = cardContentAccentColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Why you like it", color = cardContentTextColor.copy(alpha = 0.7f)) },
                        placeholder = { Text("Describe why this tea is your favourite", color = cardContentTextColor.copy(alpha = 0.5f)) },
                        leadingIcon = {
                            Icon(Icons.Default.Info, contentDescription = null, tint = cardContentAccentColor)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = cardContentTextColor,
                            unfocusedTextColor = cardContentTextColor,
                            focusedBorderColor = cardContentAccentColor,
                            unfocusedBorderColor = cardContentTextColor.copy(alpha = 0.3f),
                            cursorColor = cardContentAccentColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (teaName.isBlank() || teaType.isBlank()) {
                        Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    isLoading = true

                    val database = FirebaseDatabase.getInstance()
                    val teaRef = database.getReference("favourites")

                    val tea = FavouriteTea(name = teaName, type = teaType, description = description)
                    val key = teaRef.push().key
                    if (key != null) {
                        teaRef.child(key).setValue(tea)
                            .addOnSuccessListener {
                                isLoading = false
                                Toast.makeText(context, "Favourite tea saved!", Toast.LENGTH_LONG).show()
                                activity?.finish()
                            }
                            .addOnFailureListener {
                                isLoading = false
                                Toast.makeText(context, "Failed to save tea: ${it.message}", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        isLoading = false
                        Toast.makeText(context, "Error generating key", Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saving...", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                } else {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Favourite Tea", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
