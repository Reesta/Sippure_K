package com.example.sippure.view

import UserRepositoryImpl
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.example.sippure.R
import com.example.sippure.model.UserModel
import com.example.sippure.viewmodel.UserViewModel
import kotlinx.coroutines.delay

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                RegistrationScreen()
            }
        }
    }
}

@Composable
fun RegistrationScreen() {
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCountry by remember { mutableStateOf("Select Country") }
    var showContent by remember { mutableStateOf(false) }

    val options = listOf("Nepal", "India", "China")
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    // Animation state
    LaunchedEffect(Unit) {
        delay(300)
        showContent = true
    }

    // Herbal tea color palette - same as login
    val herbalGreen = Color(0xFF4A7C59)
    val lightGreen = Color(0xFF8FBC8F)
    val teaGold = Color(0xFFD4AF37)
    val creamWhite = Color(0xFFF5F5DC)
    val leafGreen = Color(0xFF228B22)

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF2D5016), // Deep forest green
            Color(0xFF4A7C59), // Herbal green
            Color(0xFF6B8E23), // Olive green
            Color(0xFF8FBC8F)  // Light sage green
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        // Floating tea leaves animation behind everything


        // Full screen logo image covering background
        Image(
            painter = painterResource(R.drawable.kotlin1),
            contentDescription = "Background Herbal Tea Image",
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            contentScale = ContentScale.Crop,
            alpha = 0.15f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Title section with animation
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(1000))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Join SiPPURE",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = creamWhite,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Start your herbal tea journey",
                        fontSize = 14.sp,
                        color = creamWhite.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Registration form card
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(animationSpec = tween(1200, delayMillis = 300)) + scaleIn()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(16.dp, RoundedCornerShape(24.dp)),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = creamWhite.copy(alpha = 0.95f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Create Your Account",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = herbalGreen,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // First name and Last name row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                placeholder = { Text("First Name", color = herbalGreen.copy(alpha = 0.6f)) },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = leafGreen,
                                    unfocusedIndicatorColor = lightGreen,
                                    focusedTextColor = herbalGreen,
                                    unfocusedTextColor = herbalGreen
                                )
                            )
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                placeholder = { Text("Last Name", color = herbalGreen.copy(alpha = 0.6f)) },
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = leafGreen,
                                    unfocusedIndicatorColor = lightGreen,
                                    focusedTextColor = herbalGreen,
                                    unfocusedTextColor = herbalGreen
                                )
                            )
                        }

                        // Email field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("abc@gmail.com", color = herbalGreen.copy(alpha = 0.6f)) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = leafGreen,
                                unfocusedIndicatorColor = lightGreen,
                                focusedTextColor = herbalGreen,
                                unfocusedTextColor = herbalGreen
                            )
                        )

                        // Country dropdown
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .onGloballyPositioned { coordinates ->
                                    textFieldSize = coordinates.size.toSize()
                                }
                                .clickable { expanded = true }
                        ) {
                            OutlinedTextField(
                                value = selectedCountry,
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(4.dp, RoundedCornerShape(16.dp)),
                                enabled = false,
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Select Country",
                                        tint = herbalGreen
                                    )
                                },
                                placeholder = { Text("Select Country", color = herbalGreen.copy(alpha = 0.6f)) },
                                shape = RoundedCornerShape(16.dp),
                                colors = TextFieldDefaults.colors(
                                    disabledContainerColor = Color.White,
                                    disabledIndicatorColor = lightGreen,
                                    disabledTextColor = herbalGreen,
                                    disabledTrailingIconColor = herbalGreen
                                )
                            )
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.width(
                                    with(LocalDensity.current) { textFieldSize.width.toDp() }
                                )
                            ) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option, color = herbalGreen) },
                                        onClick = {
                                            selectedCountry = option
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Password field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("Password", color = herbalGreen.copy(alpha = 0.6f)) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = leafGreen,
                                unfocusedIndicatorColor = lightGreen,
                                focusedTextColor = herbalGreen,
                                unfocusedTextColor = herbalGreen
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Register button
                        Button(
                            onClick = {
                                if (email.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Email and Password must not be empty", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                userViewModel.register(email, password) { success, message, userId ->
                                    if (success) {
                                        val userModel = UserModel(
                                            userId, email, firstName, lastName,
                                            "Male", "980805555", selectedCountry
                                        )
                                        userViewModel.addUserToDatabase(userId, userModel) { success2, message2 ->
                                            Toast.makeText(context, message2, Toast.LENGTH_LONG).show()
                                        }
                                    } else {
                                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .shadow(8.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = leafGreen,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                "Join the Tea Community",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Login link
                        Text(
                            text = "Already have an account? Sign in here",
                            color = teaGold,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    (context as? Activity)?.finish()
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun FloatingLeaves() {
    val leafPositions = remember {
        listOf(
            0.1f to 0.2f, 0.8f to 0.1f, 0.3f to 0.7f, 0.9f to 0.6f, 0.15f to 0.8f
        )
    }

    leafPositions.forEachIndexed { index, (x, y) ->
        val animatedY by animateFloatAsState(
            targetValue = y + 0.1f * kotlin.math.sin(System.currentTimeMillis() / 1000.0 + index).toFloat(),
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
                    .background(
                        Color(0xFF228B22).copy(alpha = 0.3f),
                        RoundedCornerShape(6.dp)
                    )
                    .blur(2.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewRegistration() {
    RegistrationScreen()
}