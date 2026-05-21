package com.example.ui.dashboard

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen() {
    var activeSocketsCount by remember { mutableStateOf(485002) }
    var messageThroughput by remember { mutableStateOf(12504) }
    var isAttackSimulated by remember { mutableStateOf(false) }
    var rateLimitTriggers by remember { mutableStateOf(34) }
    var serverCpuUsage by remember { mutableStateOf(24f) }
    
    // Live stream logs console
    val auditLogs = remember { mutableStateListOf<String>() }

    // Coroutine to simulate server metric fluctuations and live logs
    LaunchedEffect(isAttackSimulated) {
        // Initial setup logs
        if (auditLogs.isEmpty()) {
            auditLogs.add("[08:00:12] [SYSTEM] Kong API Gateway loaded certificates successfully.")
            auditLogs.add("[08:00:15] [DATABASE] Connected to core-shard-01 ( Zurich datashield ).")
            auditLogs.add("[08:00:20] [REDIS] Connected to pub-sub clustered broker array.")
        }

        var counter = 0
        while (true) {
            delay(if (isAttackSimulated) 500L else 2000L)
            counter++
            
            // Fluctuating values
            if (isAttackSimulated) {
                activeSocketsCount += (123..456).random()
                messageThroughput = (45000..52400).random()
                rateLimitTriggers += (2..8).random()
                serverCpuUsage = (840..920).random() / 10f
                
                // Attack log streams
                val attackIps = listOf("185.34.22.1", "90.180.2.14", "102.220.14.9")
                val paths = listOf("/api/v1/auth/login", "/api/v1/auth/otp")
                auditLogs.add(0, "[ATTACK SHIELD] Brute-force rate limits triggered on ip=${attackIps.random()} at router path=${paths.random()}. Code: 429.")
            } else {
                activeSocketsCount += (-500..500).random()
                messageThroughput = (12000..13100).random()
                serverCpuUsage = (220..270).random() / 10f
                
                // Normal audit logs
                val logTypes = listOf(
                    "[JWT TRUST] Rotating short-lived access-tokens for clients in Pixel Fold and MacBook Pro.",
                    "[ECDH DECRYPT] Deciphering message socket transaction on Node: 48.",
                    "[DB PARTITION] Horizontal shard re-balancer completed indexing tasks gracefully.",
                    "[SOCKET] Socket.IO bind success device=iPadPro auth=Verified_JWT.",
                    "[PNS RELAY] FCM push payload broadcast completed across 48 recipients."
                )
                auditLogs.add(0, "[LOGS ROUTER] ${logTypes.random()}")
            }

            // Cap logs size at 30 to prevent loading delays
            if (auditLogs.size > 30) {
                auditLogs.removeAt(auditLogs.lastIndex)
            }
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
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "NOIR SYSTEM METRICS",
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Consolidated cluster dashboards & defensive shield logs",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
            
            IconButton(onClick = {
                isAttackSimulated = !isAttackSimulated
                if (isAttackSimulated) {
                    auditLogs.add(0, "[CRITICAL WARNING] DDoS simulated traffic load testing initiated!")
                } else {
                    auditLogs.add(0, "[CORE STATE] Dynamic throttling attack tests terminated.")
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    tint = if (isAttackSimulated) Color.Red else Color.Gray,
                    contentDescription = "Simulate Fire"
                )
            }
        }

        // Sim Attack HUD Notice
        AnimatedVisibility(
            visible = isAttackSimulated,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Shield, contentDescription = "Active Protection", tint = MaterialTheme.colorScheme.onErrorContainer)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Simulating Brute-Force Traffic: API Limiters and DDoS Shields are actively throttling ingress requests (HTTP 429). Cpu rates elevated.",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Key stats grid (Rows of metrics Cards)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricStatsCard(
                icon = Icons.Default.CompareArrows,
                title = "Live WS Sockets",
                value = String.format("%,d", activeSocketsCount),
                desc = "Active global tunnels",
                col = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )

            MetricStatsCard(
                icon = Icons.Default.Speed,
                title = "Throughput Rate",
                value = "${messageThroughput} /s",
                desc = "Clustered transctions",
                col = Color(0xFF10B981),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricStatsCard(
                icon = Icons.Default.Security,
                title = "Rate Limit Hits",
                value = "$rateLimitTriggers triggers",
                desc = "Blocked abuse nodes",
                col = Color(0xFFF59E0B),
                modifier = Modifier.weight(1f)
            )

            MetricStatsCard(
                icon = Icons.Default.Hardware,
                title = "Pod CPU Load",
                value = "${String.format("%.1f", serverCpuUsage)}%",
                desc = "Horizontal scaled pods",
                col = if (isAttackSimulated) Color.Red else Color(0xFF8B5CF6),
                modifier = Modifier.weight(1f)
            )
        }

        // Cluster status layout indicator
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Primary Service Containers State", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface))
                Spacer(modifier = Modifier.height(12.dp))
                
                ServiceClusterRow(name = "noir-kong-ingress-gateway", replicas = "3/3 Active", status = "Healthy", col = MaterialTheme.colorScheme.primary)
                ServiceClusterRow(name = "noir-redis-hash-clustered-mesh", replicas = "6/6 Active", status = "Healthy", col = MaterialTheme.colorScheme.primary)
                ServiceClusterRow(name = "noir-chat-and-broadcaster-node", replicas = if (isAttackSimulated) "12/12 Scaled" else "4/4 Active", status = "Heavy Load", col = if (isAttackSimulated) Color(0xFFF59E0B) else MaterialTheme.colorScheme.primary)
                ServiceClusterRow(name = "noir-postgresql-primary-shards", replicas = "4/4 Active", status = "Healthy", col = MaterialTheme.colorScheme.primary)
            }
        }

        // Live streaming terminal console
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(Color(0xFF34D399), RoundedCornerShape(4.dp)))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SECURITY AUDIT LOGS INTERFACE",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF34D399),
                                fontFamily = FontFamily.Monospace
                            )
                        )
                    }

                    TextButton(onClick = { auditLogs.clear() }) {
                        Text(text = "CLR CONSOLE", color = Color.Gray, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                
                // Box terminal log items
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(8.dp))
                        .padding(10.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(auditLogs) { log ->
                            val col = when {
                                log.contains("[ATTACK SHIELD]") -> Color.Red
                                log.contains("[CRITICAL WARNING]") -> Color.Red
                                log.contains("[JWT TRUST]") -> Color(0xFF60A5FA)
                                log.contains("[ECDH DECRYPT]") -> Color(0xFF34D399)
                                log.contains("[SYSTEM]") -> Color(0xFFF59E0B)
                                else -> Color(0xFF94A3B8)
                            }
                            Text(
                                text = log,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = FontFamily.Monospace,
                                    color = col,
                                    lineHeight = 16.sp,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MetricStatsCard(icon: ImageVector, title: String, value: String, desc: String, col: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = title, tint = col, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = title, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = desc,
                color = Color.Gray,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun ServiceClusterRow(name: String, replicas: String, status: String, col: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.5f)) {
            Text(text = name, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace))
            Text(text = replicas, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
        }
        
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(col.copy(alpha = 0.15f))
                .border(BorderStroke(1.dp, col), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(text = status, color = col, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        }
    }
}
