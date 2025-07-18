package com.example.sippure.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow // Added for button shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sippure.R
import kotlinx.coroutines.delay


import com.example.sippure.ui.theme.SippureTheme
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SippureTheme { // Apply your theme here for consistency
                SplashBody()
            }
        }
    }
}

@Composable
fun SplashBody() {
    val context = LocalContext.current
    val activity = context as Activity
    val sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE)
    val localEmail: String = sharedPreferences.getString("email", "").toString()

    // State variables for animation visibility
    var logoVisible by remember { mutableStateOf(false) }
    var textVisible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }
    var showGetStarted by remember { mutableStateOf(false) }
    var particlesVisible by remember { mutableStateOf(false) }

    // --- Animation Values ---
    val logoScale by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0.5f, // Starts smaller
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy, // More bounce
            stiffness = Spring.StiffnessLow // Less stiff for a softer bounce
        ),
        label = "logo_scale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (logoVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1200, easing = LinearOutSlowInEasing), // Slightly longer, smoother fade
        label = "logo_alpha"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (textVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), // Smooth fade for text
        label = "text_alpha"
    )

    val buttonAlpha by animateFloatAsState(
        targetValue = if (buttonVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing), // Button fade
        label = "button_alpha"
    )

    val buttonScale by animateFloatAsState(
        targetValue = if (buttonVisible) 1f else 0.9f, // Starts slightly smaller
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "button_scale"
    )

    // Infinite transition for continuous animations (particles, logo pulse)
    val infiniteTransition = rememberInfiniteTransition(label = "infinite_splash_animation")

    val particle1Y by infiniteTransition.animateFloat(
        initialValue = -20f, targetValue = 20f, // More range
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "particle1_y"
    )
    val particle1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(2500, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "particle1_alpha"
    )

    val particle2Y by infiniteTransition.animateFloat(
        initialValue = 25f, targetValue = -15f, // More range
        animationSpec = infiniteRepeatable(tween(3500, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "particle2_y"
    )
    val particle2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.9f,
        animationSpec = infiniteRepeatable(tween(2800, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "particle2_alpha"
    )

    val particle3Rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "particle3_rotation"
    )
    val particle3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f, targetValue = 0.7f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "particle3_alpha"
    )

    val particle4Y by infiniteTransition.animateFloat(
        initialValue = 10f, targetValue = -30f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "particle4_y"
    )
    val particle4Alpha by infiniteTransition.animateFloat(
        initialValue = 0.25f, targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(3200, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse),
        label = "particle4_alpha"
    )

    val logoPulse by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), repeatMode = RepeatMode.Reverse),
        label = "logo_pulse"
    )

    val loadingIndicatorAlpha by animateFloatAsState(
        targetValue = if (showGetStarted) 0f else 1f, // Fades out when button appears
        animationSpec = tween(durationMillis = 300),
        label = "loading_indicator_alpha"
    )

    // --- Animation Sequence (Timing Adjusted) ---
    LaunchedEffect(Unit) {
        delay(500) // Initial delay
        logoVisible = true
        delay(1200) // Wait for logo animation
        textVisible = true
        delay(1000) // Wait for text animation
        particlesVisible = true
        delay(700) // Small delay before button
        buttonVisible = true
        delay(700) // Wait for button animation to complete its initial bounce
        showGetStarted = true // Signal that button is ready to interact
    }

    // Function to handle navigation
    fun handleGetStarted() {
        if (localEmail.isEmpty()) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            activity.finish()
        } else {
            val intent = Intent(context, DashboardActivity::class.java)
            context.startActivity(intent)
            activity.finish()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(R.drawable.galleryjs),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark Gradient Overlay (Enhanced)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.4f), // Slightly more opaque at top
                            Color(0xFF1B4332).copy(alpha = 0.90f) // Deep dark green at bottom
                        )
                    )
                )
        )

        // --- Particles ---
        if (particlesVisible) {
            // Particle 1 (White)
            Box(
                modifier = Modifier
                    .offset(x = 50.dp, y = 150.dp + particle1Y.dp)
                    .size(10.dp) // Slightly larger
                    .alpha(particle1Alpha)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.8f))
            )
            // Particle 2 (Cyan)
            Box(
                modifier = Modifier
                    .offset(x = 320.dp, y = 200.dp + particle2Y.dp)
                    .size(7.dp) // Slightly larger
                    .alpha(particle2Alpha)
                    .clip(CircleShape)
                    .background(Color.Cyan.copy(alpha = 0.8f))
            )
            // Particle 3 (White - Rotating)
            Box(
                modifier = Modifier
                    .offset(x = 280.dp, y = 400.dp)
                    .size(12.dp) // Larger
                    .alpha(particle3Alpha)
                    .rotate(particle3Rotation)
                    .clip(CircleShape) // Ensure it's a circle
                    .background(Color.White.copy(alpha = 0.6f))
            )
            // Particle 4 (Green)
            Box(
                modifier = Modifier
                    .offset(x = 80.dp, y = 450.dp + particle4Y.dp)
                    .size(5.dp) // Slightly larger
                    .alpha(particle4Alpha)
                    .clip(CircleShape)
                    .background(Color.Green.copy(alpha = 0.7f))
            )
        }

        // --- Logo and Text Content ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Image(
                painter = painterResource(R.drawable.sippure),
                contentDescription = "App Logo",
                modifier = Modifier
                    .scale(logoScale * logoPulse) // Combine scale and pulse
                    .alpha(logoAlpha)
                    .size(140.dp) // Slightly larger logo
                    .clip(RoundedCornerShape(20.dp)) // Slightly more rounded corners
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Main Tagline
            Text(
                text = "Pure Water, Pure Life",
                fontSize = 28.sp, // Larger font
                fontWeight = FontWeight.ExtraBold, // Extra bold for impact
                color = Color.White.copy(alpha = 0.95f), // Slightly brighter white
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(textAlpha)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Sub-tagline/Description
            Text(
                text = "Experience the refreshing taste of purity in every drop",
                fontSize = 16.sp, // Slightly larger font
                color = Color.White.copy(alpha = 0.85f), // Slightly brighter white
                textAlign = TextAlign.Center,
                lineHeight = 22.sp, // Slightly more line height
                modifier = Modifier.alpha(textAlpha)
            )
        }

        // --- Get Started Button or Loading Indicator ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            if (showGetStarted) {
                Button(
                    onClick = { handleGetStarted() },
                    modifier = Modifier
                        .alpha(buttonAlpha) // Button fade in
                        .scale(buttonScale) // Button scale in
                        .shadow(8.dp, RoundedCornerShape(28.dp)) // Add shadow
                        .height(60.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)), // Green button from Dashboard
                    shape = RoundedCornerShape(28.dp) // Fully rounded shape
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 20.sp, // Larger font for button
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            } else {
                // Loading indicator fades out as button appears
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .alpha(loadingIndicatorAlpha) // Fades out
                        .size(36.dp) // Slightly larger
                )
            }
        }

        // --- Bottom Particle Row ---
        // These are static, could also be animated or made part of the main particle system
        if (particlesVisible) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp) // Positioned above the button area
                    .alpha(textAlpha), // Fades in with the text
                horizontalArrangement = Arrangement.spacedBy(10.dp) // More spacing
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(10.dp) // Larger dots
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.8f))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true) // Show background for preview
@Composable
fun PreviewSplash() {
    SippureTheme {
        SplashBody()
    }
}