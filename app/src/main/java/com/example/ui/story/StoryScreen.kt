package com.example.ui.story

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StoryScreen(
    onBack: @Composable (() -> Unit)? = null
) {
    var activeStoryUser by remember { mutableStateOf<StoryUser?>(null) }
    
    val users = listOf(
        StoryUser("Elon Cosmic", "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150", 
            listOf(
                StorySlide("AETHER V2.0 Core Node is Live!", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=800", "Deploying Node 01 in Zurich..."),
                StorySlide("Fully Decentralized messaging scales to 1M requests/s", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=800", "Running on Kubernetes with horizontal sharding..."),
                StorySlide("Check out our new design schema!", "https://images.unsplash.com/photo-1634017839464-5c339ebe3cb4?w=800", "Futuristic glassmorphism in Action.")
            ),
            isUnread = true
        ),
        StoryUser("Sarah Cyber", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150",
            listOf(
                StorySlide("Enjoying code development in Helsinki", "https://images.unsplash.com/photo-1542831371-29b0f74f9713?w=800", "Nordic coding sessions"),
                StorySlide("Coffee is essential.", "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=800", "Black roast code juice")
            ),
            isUnread = true
        ),
        StoryUser("Alex Cipher", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            listOf(
                StorySlide("E2E Signal Protocol complete testing!", "https://images.unsplash.com/photo-1563986768609-322da13575f3?w=800", "0 leaks parsed safely")
            ),
            isUnread = false
        ),
        StoryUser("Luna Nebula", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            listOf(
                StorySlide("Workspace views", "https://images.unsplash.com/photo-1497366216548-37526070297c?w=800", "Cozy high-tech lounge")
            ),
            isUnread = false
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Welcome and Intro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "NOIR STORIES",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = "High-fidelity ephemeral broadcast vectors with animated reactions",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }
        }

        // Horizontal Story Grid
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // First item: Create Story User Avatar
            item {
                Column(
                    modifier = Modifier
                        .clickable {
                            // User can create simulated story!
                        }
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Story", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "My Story", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
            }

            items(users) { user ->
                val ringBrush = if (user.isUnread) {
                    Brush.linearGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary)
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant)
                    )
                }

                Column(
                    modifier = Modifier
                        .clickable { activeStoryUser = user }
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(74.dp)
                            .background(ringBrush, CircleShape)
                            .padding(2.5.dp)
                            .background(MaterialTheme.colorScheme.background, CircleShape)
                            .padding(3.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Display beautiful letter/placeholder avatar
                        Text(
                            text = user.name.take(2).uppercase(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = user.name.substringBefore(" "),
                        color = if (user.isUnread) Color.White else Color(0xFF64748B),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = if (user.isUnread) FontWeight.Bold else FontWeight.Normal)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Onboarding / Instruction inside main feed
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111827)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Spark", tint = Color(0xFFF59E0B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Ephemeral Broadcasts Network", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "NOIR Stories disappear automatically after 24 hours. They are globally end-to-end encrypted; only users containing verification keys associated with your profile handshake can decipher media segments.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8), lineHeight = 18.sp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "👉 TAP ANY AVATAR ABOVE TO EXPERIENCE THE PRECISE STORY VIEWER SYSTEM IN HIGH DEFINITION.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                )
            }
        }

        // Active Story Viewer Modal / Full Screen Layer
        AnimatedVisibility(
            visible = activeStoryUser != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            if (activeStoryUser != null) {
                ActiveStoryViewer(
                    user = activeStoryUser!!,
                    onDismiss = { activeStoryUser = null }
                )
            }
        }
    }
}

@Composable
fun ActiveStoryViewer(
    user: StoryUser,
    onDismiss: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var currentSlideIndex by remember { mutableStateOf(0) }
    var progress by remember { mutableStateOf(0f) }
    val currentSlide = user.slides[currentSlideIndex]
    
    // Animated story reactions list
    val floatingEmojis = remember { mutableStateListOf<Pair<String, Long>>() } // Pair(Emoji, Timestamp)

    // Automatically transition slides when progress hits 100%
    LaunchedEffect(currentSlideIndex) {
        progress = 0f
        val slideDurationMs = 5000f // 5 seconds per slide
        val tickMs = 30L
        val increment = tickMs / slideDurationMs

        while (progress < 1.0f) {
            delay(tickMs)
            progress += increment
        }

        // Slide Done -> transition next
        if (currentSlideIndex < user.slides.lastIndex) {
            currentSlideIndex++
        } else {
            onDismiss()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Futuristic Cosmic graphic representing story scene background as fallback content loading
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF1E1E38), Color.Black)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Default.Cyclone, contentDescription = "Cosmic icon", tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), modifier = Modifier.size(100.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "NOIR Node Media Decrypting...", color = Color.White.copy(alpha = 0.4f), style = MaterialTheme.typography.bodySmall)
            }
        }

        // High resolution design presentation layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Slide Indicators & User info
            Column {
                // Progress top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    user.slides.forEachIndexed { idx, _ ->
                        val fillProgress = when {
                            idx < currentSlideIndex -> 1.0f
                            idx > currentSlideIndex -> 0.0f
                            else -> progress
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.5.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.Gray.copy(alpha = 0.5f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(fillProgress)
                                    .background(Color.White)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // User details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3B82F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = user.name.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(text = user.name, color = Color.White, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                            Text(text = "End-to-End Encrypted", color = Color(0xFF10B981), style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Row {
                        IconButton(onClick = {
                            // Viewers log panel trigger overlay
                        }) {
                            Icon(imageVector = Icons.Default.Visibility, contentDescription = "Viewers", tint = Color.White)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                        }
                    }
                }
            }

            // Central layout display: Slide Title & Body
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        // Tapping right screen advances, left returns
                        // Let's implement dynamic tapping sectors!
                    },
                contentAlignment = Alignment.Center
            ) {
                // Tapping navigation sectors
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (currentSlideIndex > 0) {
                                    currentSlideIndex--
                                }
                            }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (currentSlideIndex < user.slides.lastIndex) {
                                    currentSlideIndex++
                                } else {
                                    onDismiss()
                                }
                            }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentSlide.headline,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentSlide.body,
                        color = Color(0xFF94A3B8),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                }

                // Render floating reaction emojis
                floatingEmojis.forEach { (emoji, ts) ->
                    // simple animations using state offsets
                    val elapsedTime = System.currentTimeMillis() - ts
                    if (elapsedTime < 2000) {
                        val factor = elapsedTime / 2000f
                        Text(
                            text = emoji,
                            fontSize = 32.sp,
                            modifier = Modifier
                                .graphicsLayer(
                                    translationY = -300f * factor,
                                    alpha = 1.0f - factor,
                                    scaleX = 0.8f + (1.2f * factor),
                                    scaleY = 0.8f + (1.2f * factor)
                                )
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }

            // Bottom overlay: Interactive Emoji Reaction bar + Viewers Counter
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = "Viewers Log", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "48 Viewers", color = Color.White, style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.weight(1f))
                    
                    Text(text = "Fast Reactions:", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Chat feedback
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0xFF1E293B))
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(text = "Send secure reply...", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    val reactions = listOf("🔥", "❤️", "😂", "🙌", "💥", "😮")
                    reactions.forEach { emoji ->
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1F2937))
                                .clickable {
                                    floatingEmojis.add(Pair(emoji, System.currentTimeMillis()))
                                }
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = emoji, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }
    }
}

// Data models
data class StoryUser(val name: String, val avatarUrl: String, val slides: List<StorySlide>, val isUnread: Boolean)
data class StorySlide(val headline: String, val mediaUrl: String, val body: String)
