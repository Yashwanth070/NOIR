package com.example.ui.settings

import androidx.compose.animation.*
import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    darkThemeState: MutableState<Boolean>
) {
    var showQrSharing by remember { mutableStateOf(false) }
    var isSignalKeysReset by remember { mutableStateOf(false) }
    var twoFactorToggle by remember { mutableStateOf(true) }
    var storiesPrivacyToggle by remember { mutableStateOf("Contacts Only") }
    var activePresenceShare by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header profile card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Avatar with dynamic primary background
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "EC", color = Color.White, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Elon Cosmic",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Text(
                    text = "@elon_cosmic • Master Node Zurich",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bio Description
                Text(
                    text = "Lead software architect & mobile builder inventing secure channels and decentralized message routing parameters.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showQrSharing = !showQrSharing },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(imageVector = Icons.Default.QrCode, contentDescription = "QR Code", tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Share Contact", color = Color.White, fontSize = 12.sp)
                    }

                    Button(
                        onClick = { isSignalKeysReset = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(imageVector = Icons.Default.VpnKey, contentDescription = "Keys", tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Reset Key", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }

        // Animated QR Card Section
        AnimatedVisibility(
            visible = showQrSharing,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "NOIR Peer-to-Peer Handshake", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    Text(text = "Let peers scan this QR code to generate a localized shared secret using X3DH, bypassing centralized structures.", color = Color.Gray, style = MaterialTheme.typography.labelSmall, textAlign = TextAlign.Center)
                    
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    // Simple custom rendered QR mockup using canvas
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val cellSize = size.width / 8f
                            // Draw nice QR block artifacts
                            val positions = listOf(
                                Pair(0,0), Pair(0,1), Pair(0,2), Pair(1,0), Pair(2,0), Pair(1,2), Pair(2,2), // Top-Left Anchor
                                Pair(5,0), Pair(6,0), Pair(7,0), Pair(5,1), Pair(5,2), Pair(7,1), Pair(7,2), // Top-Right Anchor
                                Pair(0,5), Pair(1,5), Pair(2,5), Pair(0,6), Pair(0,7), Pair(1,7), Pair(2,7), // Bottom-Left Anchor
                                Pair(3,3), Pair(4,3), Pair(3,4), Pair(5,5), Pair(6,6), Pair(4,5) // Data Bits
                            )
                            positions.forEach { (r, c) ->
                                drawRect(
                                    color = Color.Black,
                                    size = androidx.compose.ui.geometry.Size(cellSize, cellSize),
                                    topLeft = androidx.compose.ui.geometry.Offset(c * cellSize, r * cellSize)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "FINGERPRINT SHA-256 MATCH:", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    Text(text = "8E:1F:B1:A7:20:26:93:CF:D2:BC:60:A5:FA:34:D3:99", color = MaterialTheme.colorScheme.primary, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
                }
            }
        }

        // Reset Keys Notification
        AnimatedVisibility(
            visible = isSignalKeysReset,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Key Rollover Successful!", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    Text(text = "New Identity Keys generated. Pre-keys uploaded to Zurich cluster, replacing historical pre-key caches safely.", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(10.dp))
                    TextButton(onClick = { isSignalKeysReset = false }) {
                        Text(text = "OK", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // Category: Theme Configuration
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Design & Theme Customization", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.DarkMode, contentDescription = "Dark Mode", tint = Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Cosmic Dark Mode Preference", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                    }
                    Switch(
                        checked = darkThemeState.value,
                        onCheckedChange = { darkThemeState.value = it }
                    )
                }
            }
        }

        // Category: Cybersecurity Toggles
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "Trust & Cybersecurity settings", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
                
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                // Toggle 1: MFA
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.5f)) {
                        Text(text = "Biometric Master Lock Screen", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Requirement for unlocking app session triggers", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                    Switch(checked = twoFactorToggle, onCheckedChange = { twoFactorToggle = it })
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                // Toggle 2: Online presence
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1.5f)) {
                        Text(text = "Share Online presence indicators", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Broadcast live typing and last-seen statuses", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                    }
                    Switch(checked = activePresenceShare, onCheckedChange = { activePresenceShare = it })
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                // Selector: Stories Privacy
                Column {
                    Text(text = "Stories Ephemeral Privacy Broadcast Scope", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Everyone", "Contacts Only", "Private Secret List").forEach { option ->
                            val isActive = storiesPrivacyToggle == option
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)
                                    .clickable { storiesPrivacyToggle = option }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Share Profile",
                                    color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
