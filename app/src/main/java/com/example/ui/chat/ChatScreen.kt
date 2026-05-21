package com.example.ui.chat

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GeminiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatScreen() {
    var selectedThread by remember { mutableStateOf<ChatThread?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    
    // Default mock data that updates live when user interacts!
    val threads = remember {
        mutableStateListOf(
            ChatThread(
                id = "ai_bot",
                name = "NOIR AI Assistant",
                lastMessage = "Ask me anything about messaging, analytics, or node configurations!",
                time = "Online",
                isGroup = false,
                isAi = true,
                unreadCount = 0,
                statusText = "Premium Intelligence Node",
                messages = mutableStateListOf(
                    ChatMessage("ai_bot", "Hello! I am NOIR's smart assistant. Send me any queries and I will call the Gemini REST API in real-time!", false)
                )
            ),
            ChatThread(
                id = "zurich_group",
                name = "Zurich Engineering Core",
                lastMessage = "Sarah: Merged the new gateway configurations.",
                time = "10:42 AM",
                isGroup = true,
                isAi = false,
                unreadCount = 2,
                statusText = "14 developers online",
                messages = mutableStateListOf(
                    ChatMessage("Sarah", "Welcome to the Zurich internal core node! We design modern messaging protocols here.", false),
                    ChatMessage("Alex", "Is the secure double handshake verified?", false),
                    ChatMessage("Sarah", "Yes, verified! Zero security vulnerabilities detected.", false)
                )
            ),
            ChatThread(
                id = "broadcast_communities",
                name = "NOIR Broadcaster (Channel)",
                lastMessage = "Root: Broadcast v2.5 channel documentation.",
                time = "Yesterday",
                isGroup = false,
                isChannel = true,
                isAi = false,
                unreadCount = 0,
                statusText = "9.4k subscribers",
                messages = mutableStateListOf(
                    ChatMessage("Root", "Welcome to the Global NOIR Broadcast community! Updates, startup matrices, and DevOps charts will be dispatched here dynamically.", false)
                )
            )
        )
    }

    // Filter threads based on search queries
    val filteredThreads = remember(searchQuery, threads) {
        if (searchQuery.isEmpty()) {
            threads
        } else {
            threads.filter { it.name.contains(searchQuery, ignoreCase = true) || it.lastMessage.contains(searchQuery, ignoreCase = true) }
        }
    }

    AnimatedContent(
        targetState = selectedThread,
        transitionSpec = {
            if (targetState != null) {
                // Sliding in Chat Detail
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                // Sliding out back to Chat List
                slideInHorizontally { width -> -width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> width } + fadeOut()
            }
        }, label = "ChatTransition"
    ) { activeThread ->
        if (activeThread == null) {
            // Screen 1: Chat Inbox Lists
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // Top Search & Title Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.ChatBubble, contentDescription = "Chats", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "NOIR CHATS",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search chats, channels, assistants...", color = Color.Gray, fontSize = 14.sp) },
                            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon", tint = Color.Gray) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Chat Threads List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredThreads) { thread ->
                        ChatThreadRow(
                            thread = thread,
                            onClick = {
                                selectedThread = thread
                                thread.unreadCount = 0 // Clear unread on entry
                            }
                        )
                    }
                }
            }
        } else {
            // Screen 2: Active Conversation Window (with Live Gemini Chat)
            ConversationWindow(
                thread = activeThread,
                onBack = { selectedThread = null }
            )
        }
    }
}

@Composable
fun ChatThreadRow(thread: ChatThread, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Avatar Badge
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    when {
                        thread.isAi -> MaterialTheme.colorScheme.primary
                        thread.isChannel -> Color(0xFFE57C3E) // Classic Telegram orange channel
                        else -> Color(0xFF2A9E58) // Classic Telegram green
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when {
                    thread.isAi -> Icons.Default.AutoAwesome
                    thread.isChannel -> Icons.Default.VolumeUp
                    thread.isGroup -> Icons.Default.Groups
                    else -> Icons.Default.Person
                },
                contentDescription = thread.name,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Name & Messages Short Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = thread.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
                )
                Text(
                    text = thread.time,
                    color = if (thread.isAi) MaterialTheme.colorScheme.primary else Color.Gray,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = thread.lastMessage,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Unread Badge Counter
        if (thread.unreadCount > 0) {
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = thread.unreadCount.toString(),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationWindow(thread: ChatThread, onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var inputMessage by remember { mutableStateOf("") }
    var isRecordingVo by remember { mutableStateOf(false) }
    var isPrivateSessionLock by remember { mutableStateOf(false) }
    var isAiTyping by remember { mutableStateOf(false) }
    var voiceNoteDuration by remember { mutableStateOf(0) }
    
    // Bottom sheet dialog for message actions
    var activeActionMessage by remember { mutableStateOf<ChatMessage?>(null) }

    // Coroutine counter to simulate voice note elapsed recording durations
    LaunchedEffect(isRecordingVo) {
        if (isRecordingVo) {
            voiceNoteDuration = 0
            while (isRecordingVo) {
                delay(1000)
                voiceNoteDuration++
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // App bar
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (thread.isAi) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = if (thread.isAi) Icons.Default.AutoAwesome else Icons.Default.Groups, contentDescription = thread.name, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = thread.name, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (isAiTyping) "typing..." else thread.statusText,
                            color = if (isAiTyping) MaterialTheme.colorScheme.primary else Color.Gray,
                            fontSize = 11.sp
                        )
                    }
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back", tint = Color.White)
                }
            },
            actions = {
                // E2E lock toggler
                IconButton(onClick = { isPrivateSessionLock = !isPrivateSessionLock }) {
                    Icon(
                        imageVector = if (isPrivateSessionLock) Icons.Default.Lock else Icons.Default.LockOpen,
                        tint = if (isPrivateSessionLock) MaterialTheme.colorScheme.primary else Color.Gray,
                        contentDescription = "Session Security lock"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        // Security key warning banner matching Telegram secret privacy style
        AnimatedVisibility(
            visible = isPrivateSessionLock,
            enter = slideInVertically() + fadeIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)))
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.VerifiedUser, contentDescription = "Active encryption keys", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ECDH handshakes locked • End-to-end encryption verified peer-to-peer.",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Messages history body
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(thread.messages) { msg ->
                MessageBubble(
                    message = msg,
                    onLongClick = { activeActionMessage = msg }
                )
            }
        }

        // Bottom composition controls bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Attachment drawer button
            IconButton(onClick = { /* Media selector simulation */ }) {
                Icon(imageVector = Icons.Default.AttachFile, contentDescription = "Select Attachment", tint = Color.Gray)
            }

            if (!isRecordingVo) {
                // Text input wrapper layout
                OutlinedTextField(
                    value = inputMessage,
                    onValueChange = { inputMessage = it },
                    placeholder = { Text("Message...", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedContainerColor = MaterialTheme.colorScheme.background
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    maxLines = 4
                )

                if (inputMessage.isNotEmpty()) {
                    // Send button
                    IconButton(
                        onClick = {
                            val userMsgText = inputMessage
                            val userMsg = ChatMessage("user", userMsgText, true)
                            thread.messages.add(userMsg)
                            thread.lastMessage = "You: $userMsgText"
                            inputMessage = ""

                            // Process AI Chatbot respond behavior if inside AI Chatbot node
                            if (thread.isAi) {
                                isAiTyping = true
                                coroutineScope.launch {
                                    // Compile historic turns
                                    val hist = thread.messages.dropLast(1).map {
                                        val role = if (it.isMe) "user" else "model"
                                        Pair(role, it.body)
                                    }
                                    val response = GeminiClient.chatWithBot(userMsgText, hist)
                                    isAiTyping = false
                                    thread.messages.add(ChatMessage("ai_bot", response, false))
                                    thread.lastMessage = "NOIR AI: $response"
                                }
                            } else {
                                // Group chat auto-relay simulated replies
                                coroutineScope.launch {
                                    delay(2000)
                                    val simulatedAnswers = listOf(
                                        "Message packets parsed.",
                                        "I am in Zurich and verify this signature.",
                                        "Yes, Redis clusters successfully processed sharding."
                                    )
                                    val ans = simulatedAnswers.random()
                                    thread.messages.add(ChatMessage("Alex", ans, false))
                                    thread.lastMessage = "Alex: $ans"
                                }
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send secure packets", tint = Color(0xFF3B82F6))
                    }
                } else {
                    // Microphone record audio button triggers
                    IconButton(
                        onClick = { isRecordingVo = true }
                    ) {
                        Icon(imageVector = Icons.Default.Mic, contentDescription = "Record voice note", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            } else {
                // Voice recorder recording wave presentation HUD overlay
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).background(MaterialTheme.colorScheme.primary, CircleShape))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Recording Voice Note... ${voiceNoteDuration}s", color = Color.White, fontSize = 12.sp)
                    }
                    
                    // Simple canvas wave animations
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        listOf(8, 14, 28, 16, 24, 10, 20).forEach { ht ->
                            Box(
                                modifier = Modifier
                                    .width(2.5.dp)
                                    .height(ht.dp)
                                    .background(Color.White)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Delete Voice recording
                IconButton(onClick = { isRecordingVo = false }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Discard Voice note", tint = Color.Red)
                }

                // Send Voice recording
                IconButton(
                    onClick = {
                        isRecordingVo = false
                        thread.messages.add(ChatMessage("user", "🎙️ Simulated Voice Note (0:0${voiceNoteDuration})", true, isVoiceNote = true))
                        thread.lastMessage = "You: 🎙️ Voice Note"
                    }
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Dispatched voice note", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

    // Modal Drawer dialog for clicked message item options
    if (activeActionMessage != null) {
        AlertDialog(
            onDismissRequest = { activeActionMessage = null },
            title = { Text(text = "Message Actions", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)) },
            containerColor = MaterialTheme.colorScheme.surface,
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                thread.messages.remove(activeActionMessage)
                                thread.lastMessage = "Message deleted locally."
                                activeActionMessage = null
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Local", tint = Color.Red)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Delete Message locally (Wipe sector)", color = Color.White)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeActionMessage!!.body = "✏️ (Edited) " + activeActionMessage!!.body
                                activeActionMessage = null
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit local", tint = Color(0xFFF59E0B))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Edit encrypted body payload", color = Color.White)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                activeActionMessage!!.isPinned = true
                                activeActionMessage = null
                            }
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.PushPin, contentDescription = "Pin Message", tint = Color(0xFF3B82F6))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "Pin message in chat index", color = Color.White)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { activeActionMessage = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(message: ChatMessage, onLongClick: () -> Unit) {
    val bubbleColor = if (message.isMe) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant
    val alignment = if (message.isMe) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = onLongClick
            ),
        contentAlignment = alignment
    ) {
        Column(
            horizontalAlignment = if (message.isMe) Alignment.End else Alignment.Start
        ) {
            // Sender name (only if from others in group)
            if (!message.isMe) {
                Text(
                    text = message.senderId,
                    color = Color.Gray,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }

            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = if (message.isMe) 12.dp else 2.dp,
                            bottomEnd = if (message.isMe) 2.dp else 12.dp
                        )
                    )
                    .background(bubbleColor)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
                    .widthIn(max = 280.dp)
            ) {
                Column {
                    if (message.isPinned) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
                            Icon(imageVector = Icons.Default.PushPin, contentDescription = "Pinned", tint = Color.LightGray, modifier = Modifier.size(10.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Pinned", color = Color.LightGray, fontSize = 9.sp)
                        }
                    }

                    Text(
                        text = message.body,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

// Data Classes
data class ChatThread(
    val id: String,
    val name: String,
    var lastMessage: String,
    val time: String,
    val isGroup: Boolean,
    val isAi: Boolean = false,
    val isChannel: Boolean = false,
    var unreadCount: Int,
    val statusText: String,
    val messages: MutableList<ChatMessage>
)

data class ChatMessage(
    val senderId: String,
    var body: String,
    val isMe: Boolean,
    val isVoiceNote: Boolean = false,
    var isPinned: Boolean = false
)
