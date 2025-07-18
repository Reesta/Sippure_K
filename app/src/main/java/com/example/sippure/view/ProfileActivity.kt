package com.example.sippure.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sippure.view.ui.theme.SippureTheme
import kotlinx.coroutines.delay

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SippureTheme {
                ProfileApp()
            }
        }
    }
}

sealed class Screen {
    object Profile : Screen()
    object EditProfile : Screen()
    object ChangePassword : Screen()
    object TeaPreferences : Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Profile) }
    val context = androidx.compose.ui.platform.LocalContext.current

    when (currentScreen) {
        Screen.Profile -> ProfileScreen(
            onNavigate = { currentScreen = it },
            onBack = {
                (context as? ComponentActivity)?.finish()
            }
        )
        Screen.EditProfile -> EditProfileScreen(
            onBack = { currentScreen = Screen.Profile },
            onSaveSuccess = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                currentScreen = Screen.Profile
            },
            onSaveError = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        )
        Screen.ChangePassword -> ChangePasswordScreen(
            onBack = { currentScreen = Screen.Profile },
            onSaveSuccess = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                currentScreen = Screen.Profile
            },
            onSaveError = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        )
        Screen.TeaPreferences -> TeaPreferencesScreen(
            onBack = { currentScreen = Screen.Profile },
            onSaveSuccess = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                currentScreen = Screen.Profile
            },
            onSaveError = { message ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onNavigate: (Screen) -> Unit, onBack: () -> Unit) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332), Color(0xFF2D5016),
            Color(0xFF40601E), Color(0xFF52734D)
        )
    )
    val mockUser = UserModel(fullName = "Tea Enthusiast", email = "user@sippure.com")
    var currentUser by remember { mutableStateOf(mockUser) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Profile",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .background(gradient)
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF8FBC8F))
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF8FBC8F), Color(0xFF90EE90), Color(0xFF98FB98)
                                    )
                                )
                            )
                            .border(4.dp, Color(0xFF228B22), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser.fullName.firstOrNull()?.toString()?.uppercase() ?: "U",
                            color = Color(0xFF1B4332), fontWeight = FontWeight.Bold, fontSize = 56.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = currentUser.fullName,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentUser.email,
                        color = Color(0xFFE8F5E8),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0x60228B22)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "🍃", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Herbal Tea Lover",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    ProfileOptionCard(
                        icon = Icons.Default.Edit,
                        title = "Edit Profile",
                        subtitle = "Update your tea preferences & info",
                        onClick = { onNavigate(Screen.EditProfile) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileOptionCard(
                        icon = Icons.Default.Lock,
                        title = "Change Password",
                        subtitle = "Secure your Sippure account",
                        onClick = { onNavigate(Screen.ChangePassword) },
                        accentColor = Color(0xFF9ACD32)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    ProfileOptionCard(
                        icon = Icons.Default.CheckCircle,
                        title = "Tea Preferences",
                        subtitle = "Customize your herbal tea experience",
                        onClick = { onNavigate(Screen.TeaPreferences) },
                        accentColor = Color(0xFFADFF2F)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            // Implement logout logic
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC143C).copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Leave Sippure",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sippure - Your Herbal Tea Companion",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    accentColor: Color = Color(0xFF8FBC8F)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x50000000)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = accentColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Text(text = subtitle, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
            }
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

data class UserModel(val fullName: String, val email: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSaveSuccess: (String) -> Unit,
    onSaveError: (String) -> Unit
) {
    var fullName by remember { mutableStateOf("Tea Enthusiast") }
    var email by remember { mutableStateOf("user@sippure.com") }
    var isLoading by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332), Color(0xFF2D5016),
            Color(0xFF40601E), Color(0xFF52734D)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Profile",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .background(gradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF8FBC8F), Color(0xFF90EE90), Color(0xFF98FB98)
                                )
                            )
                        )
                        .border(3.dp, Color(0xFF228B22), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = fullName.firstOrNull()?.toString()?.uppercase() ?: "U",
                        color = Color(0xFF1B4332), fontWeight = FontWeight.Bold, fontSize = 48.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { /* TODO: Change profile picture */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8FBC8F).copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Change Photo", color = Color.White, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x40000000)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text("Full Name", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF8FBC8F),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                cursorColor = Color(0xFF8FBC8F)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF8FBC8F),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                cursorColor = Color(0xFF8FBC8F)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        isLoading = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8FBC8F)),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Save Changes",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (isLoading) {
                    LaunchedEffect(Unit) {
                        delay(2000)
                        val success = (0..1).random() == 1
                        if (success) {
                            onSaveSuccess("Profile updated successfully!")
                        } else {
                            onSaveError("Failed to update profile.")
                        }
                        isLoading = false
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onSaveSuccess: (String) -> Unit,
    onSaveError: (String) -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332), Color(0xFF2D5016),
            Color(0xFF40601E), Color(0xFF52734D)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Change Password",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .background(gradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9ACD32).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF9ACD32),
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Secure Your Account",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Update your password to keep your tea journey safe",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x40000000)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        OutlinedTextField(
                            value = currentPassword,
                            onValueChange = { currentPassword = it },
                            label = { Text("Current Password", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF9ACD32),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                cursorColor = Color(0xFF9ACD32)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("New Password", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF9ACD32),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                cursorColor = Color(0xFF9ACD32)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirm New Password", color = Color.White.copy(alpha = 0.7f)) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF9ACD32),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                                cursorColor = Color(0xFF9ACD32)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        isLoading = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9ACD32)),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Update Password",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (isLoading) {
                    LaunchedEffect(Unit) {
                        delay(2000)
                        val success = (0..1).random() == 1
                        if (success) {
                            onSaveSuccess("Password updated successfully!")
                        } else {
                            onSaveError("Failed to update password.")
                        }
                        isLoading = false
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaPreferencesScreen(
    onBack: () -> Unit,
    onSaveSuccess: (String) -> Unit,
    onSaveError: (String) -> Unit
) {
    var preferences by remember { mutableStateOf(setOf<String>()) }
    var isLoading by remember { mutableStateOf(false) }

    val teaOptions = listOf(
        "🌼 Chamomile" to "Calming & Relaxing",
        "🍃 Peppermint" to "Refreshing & Energizing",
        "🫚 Ginger" to "Warming & Digestive",
        "🌿 Lemongrass" to "Citrusy & Uplifting",
        "🌹 Hibiscus" to "Fruity & Antioxidant-rich",
        "🍯 Honey Lavender" to "Sweet & Soothing"
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1B4332), Color(0xFF2D5016),
            Color(0xFF40601E), Color(0xFF52734D)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tea Preferences",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .background(gradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFADFF2F).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🍵", fontSize = 40.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Choose Your Favorites",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Select the herbal teas that match your taste and wellness goals",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0x40000000)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        teaOptions.forEach { (tea, description) ->
                            val teaName = tea.split(" ").drop(1).joinToString(" ")
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        preferences = if (preferences.contains(teaName)) {
                                            preferences - teaName
                                        } else {
                                            preferences + teaName
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (preferences.contains(teaName))
                                        Color(0xFFADFF2F).copy(alpha = 0.2f)
                                    else
                                        Color(0x20FFFFFF)
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = preferences.contains(teaName),
                                        onCheckedChange = {
                                            preferences = if (it) {
                                                preferences + teaName
                                            } else {
                                                preferences - teaName
                                            }
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = Color(0xFFADFF2F),
                                            uncheckedColor = Color.White.copy(alpha = 0.6f),
                                            checkmarkColor = Color(0xFF1B4332)
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = tea,
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = description,
                                            color = Color.White.copy(alpha = 0.7f),
                                            fontSize = 12.sp
                                        )
                                    }
                                    if (preferences.contains(teaName)) {
                                        Icon(
                                            Icons.Default.Favorite,
                                            contentDescription = null,
                                            tint = Color(0xFFADFF2F),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = {
                        isLoading = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADFF2F)),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading && preferences.isNotEmpty()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            "Save Preferences",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                if (preferences.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Selected: ${preferences.joinToString(", ")}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                if (isLoading) {
                    LaunchedEffect(Unit) {
                        delay(2000)
                        val success = (0..1).random() == 1
                        if (success) {
                            onSaveSuccess("Tea preferences saved successfully!")
                        } else {
                            onSaveError("Failed to save preferences.")
                        }
                        isLoading = false
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    SippureTheme {
        ProfileScreen(onNavigate = {}, onBack = {})
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    SippureTheme {
        EditProfileScreen(onBack = {}, onSaveSuccess = {}, onSaveError = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ChangePasswordScreenPreview() {
    SippureTheme {
        ChangePasswordScreen(onBack = {}, onSaveSuccess = {}, onSaveError = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TeaPreferencesScreenPreview() {
    SippureTheme {
        TeaPreferencesScreen(onBack = {}, onSaveSuccess = {}, onSaveError = {})
    }
}