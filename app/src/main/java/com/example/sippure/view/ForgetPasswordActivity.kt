package com.example.sippure.view

import UserRepositoryImpl
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sippure.viewmodel.UserViewModel

// Theme Colors
object SippureTheme {
    val PrimaryBackgroundGreen = Color(0xFF1F6E23)
    val DeepGradientGreen = Color(0xFF155018)
    val AccentGreen = Color(0xFF66BB6A)
    val ButtonGreen = Color(0xFF388E3C)
    val White = Color(0xFFFFFFFF)
    val TextDark = Color(0xFF212121)
    val TextLight = Color(0xFF424242)
}

class ForgetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SippureForgotPasswordTheme {
                ForgetBody()
            }
        }
    }
}

@Composable
fun SippureForgotPasswordTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = SippureTheme.ButtonGreen,
            secondary = SippureTheme.AccentGreen,
            background = SippureTheme.PrimaryBackgroundGreen,
            surface = SippureTheme.White,
            onPrimary = SippureTheme.White,
            onSecondary = SippureTheme.White,
            onBackground = SippureTheme.White,
            onSurface = SippureTheme.TextDark
        ),
        content = content
    )
}

@Composable
fun ForgetBody() {
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }
    val context = LocalContext.current
    val activity = context as? Activity

    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SippureTopAppBar(
                onBackClick = {
                    activity?.let {
                        val intent = Intent(it, LoginActivity::class.java)
                        it.startActivity(intent)
                        it.finish()
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            SippureTheme.DeepGradientGreen,
                            SippureTheme.PrimaryBackgroundGreen
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 24.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = SippureTheme.AccentGreen,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Forgot Password?",
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = SippureTheme.White
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Don't worry! Enter your email address and we'll send you a link to reset your password.",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = SippureTheme.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SippureTheme.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Email Address",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = SippureTheme.TextDark
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        SippureTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = "Enter your email address",
                            leadingIcon = Icons.Default.Email,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (email.isNotBlank()) {
                            isLoading = true
                            userViewModel.forgetPassword(email) { success, message ->
                                isLoading = false
                                if (success) {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    activity?.let {
                                        val intent = Intent(it, LoginActivity::class.java)
                                        it.startActivity(intent)
                                        it.finish()
                                    }
                                } else {
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(context, "Please enter your email address", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SippureTheme.ButtonGreen),
                    shape = RoundedCornerShape(16.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = SippureTheme.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Send Reset Link",
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = SippureTheme.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        activity?.let {
                            val intent = Intent(it, LoginActivity::class.java)
                            it.startActivity(intent)
                            it.finish()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = SippureTheme.AccentGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Back to Login",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = SippureTheme.AccentGreen
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SippureTopAppBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "🌿 Sippure",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SippureTheme.White
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = SippureTheme.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun SippureTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                placeholder,
                color = SippureTheme.TextLight
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = SippureTheme.ButtonGreen
            )
        },
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SippureTheme.ButtonGreen,
            unfocusedBorderColor = SippureTheme.AccentGreen,
            focusedTextColor = SippureTheme.TextDark,
            unfocusedTextColor = SippureTheme.TextDark,
            cursorColor = SippureTheme.ButtonGreen
        ),
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}

@Preview(showBackground = true)
@Composable
fun PrevForgetPassword() {
    SippureForgotPasswordTheme {
        ForgetBody()
    }
}
