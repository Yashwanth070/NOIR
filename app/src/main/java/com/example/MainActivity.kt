package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.blueprint.BlueprintScreen
import com.example.ui.chat.ChatScreen
import com.example.ui.story.StoryScreen
import com.example.ui.auth.AuthScreen
import com.example.ui.auth.LauncherGateScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.settings.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkThemeState = remember { mutableStateOf(true) }
            val introCompletedState = remember { mutableStateOf(false) }
            
            MyApplicationTheme(darkTheme = darkThemeState.value) {
                if (!introCompletedState.value) {
                    LauncherGateScreen(
                        onComplete = { introCompletedState.value = true }
                    )
                } else {
                    MainLayout(darkThemeState)
                }
            }
        }
    }
}

@Composable
fun MainLayout(darkThemeState: MutableState<Boolean>) {
    var selectedTab by remember { mutableStateOf(0) }
    var showProfileOverlay by remember { mutableStateOf(false) }

    // Navigation Items
    val navigationItems = listOf(
        NavigationTab("Inbox", Icons.Default.ChatBubble),
        NavigationTab("Stories", Icons.Default.AutoAwesome),
        NavigationTab("Security", Icons.Default.Fingerprint),
        NavigationTab("Metrics", Icons.Default.Analytics),
        NavigationTab("Specs", Icons.Default.IntegrationInstructions)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            // Sleek Telegram-Style Header bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "N", 
                                color = Color.White, 
                                fontSize = 16.sp, 
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "NOIR",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }

                    // Top-right Profile Toggle Badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                            .clickable { showProfileOverlay = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Elegant Telegram-Style bottom navigation
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                navigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // Primary Screen content with visual fade animations
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }, label = "TabContentTransition"
            ) { tab ->
                when (tab) {
                    0 -> ChatScreen()
                    1 -> StoryScreen()
                    2 -> AuthScreen()
                    3 -> DashboardScreen()
                    4 -> BlueprintScreen()
                }
            }

            // Slide-up Profile Settings Sheet Overlay
            AnimatedVisibility(
                visible = showProfileOverlay,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                ) {
                    // Back drop clicks to dismiss
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { showProfileOverlay = false }
                    )

                    // Profile settings pane
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.92f)
                            .align(Alignment.BottomCenter)
                            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .clickable(enabled = false) {} // block clickthrough
                    ) {
                        // Slider Handle
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(4.dp)
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 10.dp)
                                .clip(CircleShape)
                                .background(Color.Gray)
                        )

                        // Sheet title bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Settings & Preferences",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            IconButton(onClick = { showProfileOverlay = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss profile",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Embed Profile & Settings Screen inside the slide up sheet
                        Box(modifier = Modifier.weight(1f)) {
                            SettingsScreen(darkThemeState = darkThemeState)
                        }
                    }
                }
            }
        }
    }
}

data class NavigationTab(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
