package com.example.sippure.view

import UserRepositoryImpl
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.sippure.R
import com.example.sippure.Repository.AuthRepositoryImpl
import com.example.sippure.viewmodel.AuthViewModel
import com.example.sippure.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import kotlin.math.sin

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginBody()
        }
    }
}

@Composable
fun LoginBody() {

    val repo = remember { AuthRepositoryImpl(FirebaseAuth.getInstance()) }
    val authViewModel = remember { AuthViewModel(repo) }


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = context as? Activity
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()


    LaunchedEffect(Unit) {
        email = sharedPreferences.getString("email", "") ?: ""
        password = sharedPreferences.getString("password", "") ?: ""
    }

    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }


    val herbalGreen = Color(0xFF4A7C59)
    val creamWhite = Color(0xFFF5F5DC)
    val leafGreen = Color(0xFF228B22)
    val teaGold = Color(0xFFD4AF37)
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2D5016),
            Color(0xFF4A7C59),
            Color(0xFF6B8E23),
            Color(0xFF8FBC8F)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        FloatingTeaLeaves()
        Image(
            painter = painterResource(R.drawable.kotlin1),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.15f
        )

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            containerColor = Color.Transparent,
            modifier = Modifier.zIndex(2f)
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + slideInVertically { -100 }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Card(
                            modifier = Modifier.size(120.dp),
                            shape = CircleShape,
                            colors = CardDefaults.cardColors(containerColor = creamWhite)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.kotlin1),
                                contentDescription = "Logo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text("SiPPURE", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = creamWhite)
                        Text("Natural Herbal Tea Experience", fontSize = 14.sp, color = creamWhite.copy(alpha = 0.8f), fontStyle = FontStyle.Italic)
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                AnimatedVisibility(
                    visible = showContent,
                    enter = fadeIn() + scaleIn()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(16.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = creamWhite.copy(alpha = 0.95f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Welcome Back", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = herbalGreen)
                            Text("Sign in to your herbal journey", fontSize = 14.sp, color = herbalGreen.copy(alpha = 0.7f))

                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(4.dp, RoundedCornerShape(16.dp).testag("email")),
                                placeholder = { Text("Enter your email", color = herbalGreen.copy(alpha = 0.6f)) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = leafGreen,
                                    unfocusedIndicatorColor = herbalGreen.copy(alpha = 0.5f),
                                    focusedTextColor = herbalGreen,
                                    unfocusedTextColor = herbalGreen
                                ),
                                shape = RoundedCornerShape(16.dp),
                                leadingIcon = { Icon(Icons.Default.Email, null, tint = herbalGreen) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(4.dp, RoundedCornerShape(16.dp).testag("password")),
                                placeholder = { Text("Enter your password", color = herbalGreen.copy(alpha = 0.6f)) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = leafGreen,
                                    unfocusedIndicatorColor = herbalGreen.copy(alpha = 0.5f),
                                    focusedTextColor = herbalGreen,
                                    unfocusedTextColor = herbalGreen
                                ),
                                shape = RoundedCornerShape(16.dp),
                                leadingIcon = { Icon(Icons.Default.Lock, null, tint = herbalGreen) },
                                trailingIcon = {
                                    Icon(
                                        painterResource(if (passwordVisibility) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                                        null,
                                        tint = herbalGreen,
                                        modifier = Modifier.clickable {
                                            passwordVisibility = !passwordVisibility
                                        }
                                    )
                                },
                                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = rememberMe,
                                        onCheckedChange = { rememberMe = it },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = leafGreen,
                                            uncheckedColor = herbalGreen,
                                            checkmarkColor = Color.White
                                        )
                                    )
                                    Text("Remember me", color = herbalGreen, fontSize = 14.sp)
                                }
                                Text(
                                    "Forgot password?",
                                    color = teaGold,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    textDecoration = TextDecoration.Underline,
                                    modifier = Modifier.clickable {
                                        // Navigate to ForgetPasswordActivity
                                        context.startActivity(Intent(context, ForgetPasswordActivity::class.java))
                                        activity?.finish() // Optionally finish LoginActivity if you don't want it on the back stack
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    isLoading = true
                                    coroutineScope.launch {
                                        authViewModel.login(email, password) { success, message ->
                                            isLoading = false
                                            if (success) {
                                                if (rememberMe) {
                                                    editor.putString("email", email)
                                                    editor.putString("password", password)
                                                    editor.apply()
                                                } else {
                                                    // If rememberMe is false, clear stored credentials
                                                    editor.remove("email")
                                                    editor.remove("password")
                                                    editor.apply()
                                                }
                                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                                context.startActivity(Intent(context, DashboardActivity::class.java).apply {
                                                    putExtra("email", email) // Pass email to Dashboard
                                                })
                                                activity?.finish() // Finish LoginActivity
                                            } else {
                                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp).testTag("submit"),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = leafGreen,
                                    contentColor = Color.White
                                ),
                                enabled = !isLoading
                            ) {
                                Text(if (isLoading) "Brewing your session..." else "Sign In", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                "New to Sippure? Join our tea community",
                                color = teaGold,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textDecoration = TextDecoration.Underline,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.clickable {
                                    context.startActivity(Intent(context, RegistrationActivity::class.java))
                                    activity?.finish()
                                }
                            )

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }
}

private fun RoundedCornerShape.testag(string: String): Shape {
    TODO("Not yet implemented")
}

@Composable
fun FloatingTeaLeaves() {
    val leafPositions = remember {
        listOf(0.1f to 0.2f, 0.8f to 0.1f, 0.3f to 0.7f, 0.9f to 0.6f, 0.15f to 0.8f)
    }
    leafPositions.forEachIndexed { index, (x, y) ->
        val animatedY by animateFloatAsState(
            targetValue = y + 0.1f * sin(System.currentTimeMillis() / 1000.0 + index).toFloat(),
            animationSpec = tween(2000 + index * 200)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(
                    x = (x * 300).dp,
                    y = (animatedY * 600).dp
                )
                .zIndex(-1f)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color(0xFF228B22).copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                    .blur(2.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewLogin() {
    LoginBody()
}