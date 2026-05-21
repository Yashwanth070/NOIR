package com.example.ui.blueprint

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BlueprintScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        TabItem("System Architecture", Icons.Default.Schema, "Full System Topology"),
        TabItem("Database Schema", Icons.Default.Storage, "PostgreSQL & ER Diagram"),
        TabItem("API & WebSockets", Icons.Default.Code, "Socket.IO + REST Routes"),
        TabItem("DevOps & Scaling", Icons.Default.CloudQueue, "Scaling to Millions"),
        TabItem("Roadmap & Business", Icons.Default.TrendingUp, "MVP to Startup Scale")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 12.dp)
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Engineering,
                        contentDescription = "Engineering Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "NOIR BLUEPRINT",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                Text(
                    text = "Interactive Full-Stack Architecture Handbook",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    )
                )
            }
        }

        // Tab Selector Row
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            edgePadding = 12.dp,
            divider = { HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant) },
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.title,
                                modifier = Modifier.size(18.dp),
                                tint = if (selectedTab == index) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) MaterialTheme.colorScheme.onSurface else Color.Gray
                                )
                            )
                        }
                    }
                )
            }
        }

        // Active Screen Pane
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            when (selectedTab) {
                0 -> ArchitectureTab()
                1 -> DatabaseTab()
                2 -> ApiWebSocketsTab()
                3 -> DevOpsScaleTab()
                4 -> RoadmapBusinessTab()
            }
        }
    }
}

data class TabItem(val title: String, val icon: ImageVector, val description: String)

@Composable
fun ArchitectureTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                title = "Billion-Dollar Startup Infrastructure Topology",
                description = "Horizontal scalability, zero single-point-of-failure routing, redundant cluster networks and multi-datacenter capabilities."
            )
        }

        item {
            Text(
                text = "SYSTEM TOPOLOGY MAP",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B82F6),
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            TopologyCanvas()
        }

        item {
            Text(
                text = "ARCHITECTURE LAYERS HIGHLIGHT",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3B82F6),
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            PointCard(
                icon = Icons.Default.Cloud,
                title = "Edge Layer (Cloudflare)",
                description = "Anycast routing, DDoS shielding, SSL termination, and static asset caching at near-zero latency worldwide."
            )
        }

        item {
            PointCard(
                icon = Icons.Default.AltRoute,
                title = "Gateway Tier (Kubernetes Ingress / Kong)",
                description = "Dynamic route parsing, rate-limiting, JWT authentication validation, and custom TLS verification middleware."
            )
        }

        item {
            PointCard(
                icon = Icons.Default.Groups,
                title = "Microservice Tier (Docker / Node.js)",
                description = "Decoupled specialized services for Auth (JWT token rotation), Chats (Express endpoints), Voice/Media transcoder engines, and Multi-device push relays."
            )
        }

        item {
            PointCard(
                icon = Icons.Default.SyncAlt,
                title = "Pub/Sub Messaging & Cache Network (Redis)",
                description = "Low-latency atomic token caches, session records, user online presences, and system-wide Socket.IO pub/sub inter-process brokers."
            )
        }

        item {
            PointCard(
                icon = Icons.Default.Dataset,
                title = "Primary Storage Tier (PostgreSQL / AWS S3)",
                description = "Highly optimized schema for user, contact arrays, group configurations, historical audit logs, and Amazon S3 for durable media attachments."
            )
        }
    }
}

@Composable
fun DatabaseTab() {
    val clipboardManager = LocalClipboardManager.current
    var copiedText by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                title = "Relational Storage & Entity Configuration",
                description = "PostgreSQL sharding strategies matching Signal's security paradigms, complete with Foreign Key integrity and complex indices."
            )
        }

        item {
            Text(
                text = "INTERACTIVE ENTITY-RELATIONSHIP MAP",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF10B981),
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            InteractiveErGraph()
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "POSTGRESQL PRODUCTION DDL SCHEMA",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981),
                        letterSpacing = 1.sp
                    )
                )
                TextButton(
                    onClick = {
                        val ddl = """
-- Users table with Argon2 hashing
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(32) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(2048),
    two_factor_secret VARCHAR(128),
    is_two_factor_enabled BOOLEAN DEFAULT FALSE,
    public_status VARCHAR(128),
    last_seen TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Active device sessions
CREATE TABLE user_devices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    device_name VARCHAR(128) NOT NULL,
    device_token VARCHAR(255) UNIQUE NOT NULL,
    refresh_token_hash VARCHAR(255) NOT NULL,
    last_active TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Chat rooms (DMs or Groups)
CREATE TABLE rooms (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(128), -- NULL for Direct Messages
    is_group BOOLEAN DEFAULT FALSE,
    avatar_url VARCHAR(2048),
    admin_id UUID REFERENCES users(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Chat room participants
CREATE TABLE room_members (
    room_id UUID REFERENCES rooms(id) ON DELETE CASCADE,
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (room_id, user_id)
);

-- Encrypted Messages
CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    room_id UUID REFERENCES rooms(id) ON DELETE CASCADE,
    sender_id UUID REFERENCES users(id) ON DELETE CASCADE,
    encrypted_body TEXT NOT NULL,
    media_url VARCHAR(2048),
    media_type VARCHAR(64),
    is_edited BOOLEAN DEFAULT FALSE,
    parent_message_id UUID REFERENCES messages(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
                        """.trimIndent()
                        clipboardManager.setText(AnnotatedString(ddl))
                        copiedText = "Copied SQL Schema!"
                    }
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy Icon", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Copy SQL DDL", color = Color(0xFF10B981))
                }
            }
            if (copiedText != null) {
                Text(
                    text = copiedText!!,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF10B981))
                )
            }
        }

        item {
            CodeConsole(
                code = """
-- Primary indices for sub-millisecond query parsing
CREATE INDEX idx_messages_room_created ON messages (room_id, created_at DESC);
CREATE INDEX idx_users_username_trgm ON users USING gin (username gym_ops);
CREATE INDEX idx_room_members_user ON room_members (user_id);
                """.trimIndent()
            )
        }
    }
}

@Composable
fun ApiWebSocketsTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                title = "Modern WebSocket Communication & Gateway Routers",
                description = "Integrating highly secure handshakes, JWT session binding, and Express routers running standard error filters."
            )
        }

        item {
            Text(
                text = "SOCKET.IO BI-DIRECTIONAL EVENT SYSTEM",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B),
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            CodeConsole(
                code = """
// 1. Connection & Handshake verification
io.use((socket, next) => {
  const token = socket.handshake.auth.token;
  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    socket.userId = decoded.userId;
    next();
  } catch (err) {
    next(new Error("Authentication session failed"));
  }
});

// 2. Chat messaging flow 
socket.on("send_message", async (data) => {
  const { roomId, encryptedBody, replyId } = data;
  
  const savedMsg = await db.saveMessage({
    roomId,
    senderId: socket.userId,
    encryptedBody,
    replyId
  });

  // Relay back to room via Redis PubSub broker
  io.to(roomId).emit("message_received", savedMsg);
  
  // Update offline users via Firebase Cloud Messaging push service
  pushNotificationEngine.notifyMembers(roomId, savedMsg);
});
                """.trimIndent()
            )
        }

        item {
            Text(
                text = "SECURE REST ROUTING TREE (NODEJS / JWT)",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF59E0B),
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            CodeConsole(
                code = """
// POST /api/v1/auth/login -> Argon2 checking + JWT dual token sign
// GET  /api/v1/auth/refresh -> Validate Refresh Rotate access payload
// GET  /api/v1/chats/discovery?username=john -> Find friend metadata
// POST /api/v1/stories/upload -> Secure AWS S3 Presigned URL router
// PUT  /api/v1/settings/privacy -> Encrypted key profile toggle
                """.trimIndent()
            )
        }
    }
}

@Composable
fun DevOpsScaleTab() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HeaderCard(
                title = "Deployments, Containers, and Scaling Strategies",
                description = "A visual and technical configuration optimized to comfortably manage 10 million concurrent WebSocket threads."
            )
        }

        item {
            Text(
                text = "PRODUCTION DOCKERFILE CONFIG",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B5CF6),
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            CodeConsole(
                code = """
# Secure, multi-stage Node.js Alpine image 
FROM node:20-alpine AS builder
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm ci --only=production
COPY . .

FROM node:20-alpine AS runner
WORKDIR /usr/src/app
COPY --from=builder /usr/src/app ./
USER node
ENV NODE_ENV=production
EXPOSE 3000
CMD ["node", "dist/server.js"]
                """.trimIndent()
            )
        }

        item {
            Text(
                text = "SCALING INITIATIVES",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B5CF6),
                    letterSpacing = 1.sp
                )
            )
        }

        item {
            PointCard(
                icon = Icons.Default.FlashOn,
                title = "Redis Cluster Replication",
                description = "Sharding volatile states, active rooms, and WebSocket instances across an ultra-fast Redis cluster."
            )
        }

        item {
            PointCard(
                icon = Icons.Default.Dns,
                title = "PostgreSQL Horizontal Sharding",
                description = "Routing conversation histories based on the 'room_id' partition key to reduce individual shard limits."
            )
        }

        item {
            PointCard(
                icon = Icons.Default.Stream,
                title = "Rate Limiter (Token Bucket / Redis)",
                description = "Triggering real-time HTTP 429 back-offs globally securely protecting critical authentication endpoints."
            )
        }
    }
}

@Composable
fun RoadmapBusinessTab() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Simple 1-2 paragraph presentation matching communication design for high value
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeaderCard(
                title = "Startup Pitch, Monetization & Roadmap",
                description = "AETHER is designed to marry the high-performance utility of Telegram with pure, trustless security protocols of Signal and Discord Communities."
            )

            Text(
                text = "ROADMAP HIGHLIGHTS",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEC4899),
                    letterSpacing = 1.sp
                )
            )

            PointCard(
                icon = Icons.Default.Flag,
                title = "Phase 1: Secure Core (MVP)",
                description = "End-to-End messaging, basic stories view, profile setup, and SQLite persistent caching."
            )

            PointCard(
                icon = Icons.Default.Language,
                title = "Phase 2: Global Scalability",
                description = "Multi-device synchronization arrays, Web and Desktop fully responsive ports, channels and broad community nodes."
            )

            PointCard(
                icon = Icons.Default.AutoAwesome,
                title = "Phase 3: Deep AI Assistant Integrations",
                description = "Semantic context Summarizer, customized automated channels chatbots, and decentralized key sharing panels."
            )

            Text(
                text = "MONETIZATION MATRIX",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFEC4899),
                    letterSpacing = 1.sp
                )
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Premium NOIR subscription ($4.99/mo): Live transcoder matching voice to text, high res animated emoji collections, custom profile backgrounds, bigger upload caps up to 4GB.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "2. Creator Channel tools: Seamless integration with paid channels, charging transaction structures on community paid subscriptions.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp)
                    )
                }
            }
        }
    }
}

// Sub components
@Composable
fun HeaderCard(title: String, description: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray,
                    lineHeight = 22.sp
                )
            )
        }
    }
}

@Composable
fun PointCard(icon: ImageVector, title: String, description: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}

@Composable
fun TopologyCanvas() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF111827))
            .border(BorderStroke(1.dp, Color(0xFF1E293B)))
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw simple relational background lines safely
            val width = size.width
            val height = size.height

            // Cloudflare to Kong
            drawLine(
                color = Color(0xFF3B82F6),
                start = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.15f),
                end = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.4f),
                strokeWidth = 3f
            )

            // Kong to Auth & Chat Service (split)
            drawLine(
                color = Color(0xFF10B981),
                start = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.4f),
                end = androidx.compose.ui.geometry.Offset(width * 0.25f, height * 0.65f),
                strokeWidth = 3f
            )
            drawLine(
                color = Color(0xFF10B981),
                start = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.4f),
                end = androidx.compose.ui.geometry.Offset(width * 0.75f, height * 0.65f),
                strokeWidth = 3f
            )

            // Services to Redis / DB
            drawLine(
                color = Color(0xFF8B5CF6),
                start = androidx.compose.ui.geometry.Offset(width * 0.25f, height * 0.65f),
                end = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.85f),
                strokeWidth = 3f
            )
            drawLine(
                color = Color(0xFF8B5CF6),
                start = androidx.compose.ui.geometry.Offset(width * 0.75f, height * 0.65f),
                end = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.85f),
                strokeWidth = 3f
            )
        }

        // Overlaid Composable badges
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tier 1: CDN
            ArchitectureBadge(name = "Cloudflare Edge CDN", bg = Color(0xFFEB5723), textCol = Color.White)

            // Tier 2: Gateway
            ArchitectureBadge(name = "Kong API Gateway", bg = Color(0xFF1E293B), textCol = Color(0xFFF59E0B))

            // Tier 3: Decentralized Backend Services
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ArchitectureBadge(name = "Auth Service", bg = Color(0xFF1E293B), textCol = Color(0xFF60A5FA))
                ArchitectureBadge(name = "Socket & Chat", bg = Color(0xFF1E293B), textCol = Color(0xFF34D399))
            }

            // Tier 4: DB & Redis
            ArchitectureBadge(name = "PostgreSQL DB + Redis Cache Ingress", bg = Color(0xFF1F2937), textCol = Color(0xFFC084FC))
        }
    }
}

@Composable
fun InteractiveErGraph() {
    var highlightedTable by remember { mutableStateOf<String?>(null) }
    val tables = listOf(
        ErTable(
            "Users", Color(0xFF3B82F6), listOf(
                "id : UUID [PK]",
                "username : VARCHAR(32) [UNIQUE]",
                "email : VARCHAR(255) [UNIQUE]",
                "password_hash : VARCHAR",
                "is_2fa_enabled : BOOLEAN",
                "last_seen : TIMESTAMP"
            )
        ),
        ErTable(
            "Rooms", Color(0xFF10B981), listOf(
                "id : UUID [PK]",
                "name : VARCHAR",
                "is_group : BOOLEAN",
                "admin_id : UUID [FK]"
            )
        ),
        ErTable(
            "Messages", Color(0xFFF59E0B), listOf(
                "id : UUID [PK]",
                "room_id : UUID [FK]",
                "sender_id : UUID [FK]",
                "encrypted_body : TEXT",
                "created_at : TIMESTAMP"
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF111827))
            .border(BorderStroke(1.dp, Color(0xFF1E293B)))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        tabsRowWithLabels(tables, highlightedTable) { selected ->
            highlightedTable = if (highlightedTable == selected) null else selected
        }

        // Render custom highlighted details
        AnimatedVisibility(
            visible = highlightedTable != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            val tbl = tables.find { it.name == highlightedTable }
            if (tbl != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2937)),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, tbl.color)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(10.dp).background(tbl.color, RoundedCornerShape(2.dp)))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Table Detail: ${tbl.name}", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        tbl.fields.forEach { field ->
                            Text(text = " • $field", style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace, color = Color(0xFF94A3B8)))
                        }
                    }
                }
            }
        }
    }
}

data class ErTable(val name: String, val color: Color, val fields: List<String>)

@Composable
fun tabsRowWithLabels(tables: List<ErTable>, active: String?, onSelect: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tables.forEach { table ->
            val isActive = active == table.name
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isActive) table.color else Color(0xFF1F2937))
                    .border(BorderStroke(1.dp, if (isActive) Color.White else Color(0xFF334155)), RoundedCornerShape(8.dp))
                    .clickable { onSelect(table.name) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = table.name,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isActive) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
fun ArchitectureBadge(name: String, bg: Color, textCol: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = textCol
            )
        )
    }
}

@Composable
fun CodeConsole(code: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF030712))
            .border(BorderStroke(1.dp, Color(0xFF1E293B)))
            .padding(14.dp)
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF34D399),
                lineHeight = 18.sp
            )
        )
    }
}
