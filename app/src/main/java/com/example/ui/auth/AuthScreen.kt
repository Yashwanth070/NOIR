package com.example.ui.auth

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AuthScreen() {
    var emailInput by remember { mutableStateOf("yashwanthdodda28@gmail.com") }
    var passwordInput by remember { mutableStateOf("secretpasswordtest") }
    var isMfaMode by remember { mutableStateOf(false) }
    var otpInput by remember { mutableStateOf("") }
    var authSuccess by remember { mutableStateOf(false) }
    var otpSentMessage by remember { mutableStateOf<String?>(null) }
    
    // Rotating JWT keys
    var accessToken by remember { mutableStateOf("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI0Z...") }
    var refreshToken by remember { mutableStateOf("rot_76asdfg8a901hjasdf7981263kljas...") }
    
    // Argon2 hash calculation preview
    val argon2Preview = remember(passwordInput) {
        if (passwordInput.isEmpty()) {
            "argon2id\$v=19\$m=65536,t=3,p=4\$..."
        } else {
            // Simulated real Argon2 hash
            val bytes = passwordInput.hashCode().toString(16)
            "argon2id\$v=19\$m=65536,t=3,p=4\$sAltKeySalt2026\$${bytes}a82b9e110cf91f1c2930263f18ea1b0d5"
        }
    }

    val coroutineScope = rememberCoroutineScope()

    // Key rotator simulation
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            // Generate simulated rotated refresh keys
            val rand = (100000..999999).random().toString(16)
            accessToken = "eyJhbGciOiJIUzI1NiI...jwt_rot_$rand"
            refreshToken = "rot_token_gen_v3_$rand"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Icon
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.Security, contentDescription = "Security Status", tint = Color(0xFF10B981), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = "NOIR SECURITY", style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold))
                Text(text = "Dual-layer authentication & end-to-end signal matrix", style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray))
            }
        }

        if (!authSuccess) {
            if (!isMfaMode) {
                // Phase 1: Main Login Panel
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(text = "Secure Portal Identity", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
                        
                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Email Identity", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Password", color = Color.Gray) },
                            visualTransformation = PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Password Hashing Metadata
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .padding(10.dp)
                        ) {
                            Text(text = "Argon2id Memory Hashing Preview (Client-Side Hashed prior to dispatch):", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = argon2Preview,
                                color = Color(0xFF10B981),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                maxLines = 2
                            )
                        }

                        Button(
                            onClick = {
                                if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                                    isMfaMode = true
                                    otpSentMessage = "Security token dispatched to $emailInput!"
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text(text = "Verify Password Key", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                // Phase 2: MFA OTP Screen
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(text = "Multi-Factor Access Code", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
                        
                        if (otpSentMessage != null) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.OfflinePin, contentDescription = "Sent Code", tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = otpSentMessage!!, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }

                        Text(
                            text = "Please enter the 6-digit verification code sent to your device identity to establish authentication handshakes.",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )

                        OutlinedTextField(
                            value = otpInput,
                            onValueChange = { if (it.length <= 6) otpInput = it },
                            placeholder = { Text("E.g. 574283", color = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    isMfaMode = false
                                    otpSentMessage = null
                                    otpInput = ""
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                            ) {
                                Text(text = "Back", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Button(
                                onClick = {
                                    // Simulated real verification
                                    authSuccess = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .weight(1.5f)
                                    .height(48.dp)
                            ) {
                                Text(text = "Establish Handshake", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // Success State: Session information view
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = "Verified User", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(54.dp))
                    Text(text = "Identity Handshake Established!", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold))
                    Text(
                        text = "Your device has completed the secure cryptographic handshake using Signal-inspired triple Diffie-Hellman matrices.",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = {
                            authSuccess = false
                            isMfaMode = false
                            otpInput = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Simulate New Encryption Login", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        }

        // Token Rotation Monitor Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Cached, contentDescription = "Rotator Logo", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Active JWT Access & Refresh Token Rotation", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
                }
                Text(
                    text = "NOIR servers rotate short-lived JWT access tokens every 15 minutes. Refresh tokens are hash-locked to specific client devices and rotated on every single Refresh demand to thwart reuse session attacks.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )

                HorizontalDivider(color = MaterialTheme.colorScheme.surface)

                Text(text = "ACCESS TOKEN (SHORT LIVED):", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = accessToken,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                )

                Text(text = "REFRESH TOKEN (ROTATED ON USE):", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                Text(
                    text = refreshToken,
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.background)
                        .padding(8.dp)
                )
            }
        }

        // Active Devices Panel List
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Authenticated Devices (Active Handshakes)", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
                Spacer(modifier = Modifier.height(14.dp))
                
                DeviceRow(
                    icon = Icons.Default.PhoneAndroid,
                    name = "Pixel Fold (Current Device)",
                    status = "Authorized (Rotates keys 3s ago)",
                    col = MaterialTheme.colorScheme.primary
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.padding(vertical = 10.dp))
                DeviceRow(
                    icon = Icons.Default.Web,
                    name = "MacBook Pro M3 Max (Chrome Sandbox)",
                    status = "Authorized (Active last seen 24 mins ago)",
                    col = MaterialTheme.colorScheme.secondary
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.padding(vertical = 10.dp))
                DeviceRow(
                    icon = Icons.Default.Computer,
                    name = "Arch Linux Kernel Node Client",
                    status = "Authorized (Active last seen 2 days ago)",
                    col = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun DeviceRow(icon: androidx.compose.ui.graphics.vector.ImageVector, name: String, status: String, col: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = name, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = name, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).background(col, RoundedCornerShape(2.dp)))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = status, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
