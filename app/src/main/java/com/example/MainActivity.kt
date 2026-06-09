package com.example

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Structure representing custom color themes
data class GreenAccent(
    val name: String,
    val primary: Color,
    val darkAccent: Color,
    val container: Color
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    SaaSWorkspaceLandingPage(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SaaSWorkspaceLandingPage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    // UI Theme and responsive state selectors
    var isDesktopView by remember { mutableStateOf(false) }
    var isDarkMode by remember { mutableStateOf(false) }
    var selectedAccentIndex by remember { mutableIntStateOf(0) }
    
    // Lists of beautiful modern green accent palettes (meeting custom branding goals)
    val greenAccents = listOf(
        GreenAccent("Emerald SaaS", Color(0xFF10B981), Color(0xFF047857), Color(0xFFD1FAE5)),
        GreenAccent("Forest Mint", Color(0xFF0D9488), Color(0xFF0F766E), Color(0xFFCCFBF1)),
        GreenAccent("Brite Lime", Color(0xFF84CC16), Color(0xFF4D7C0F), Color(0xFFECFCCB)),
        GreenAccent("Teal Dream", Color(0xFF14B8A6), Color(0xFF0D9488), Color(0xFFE0F2FE))
    )
    val currentAccent = greenAccents[selectedAccentIndex]
    
    // Viewport Theme Colors
    val bgColor = if (isDarkMode) Color(0xFF0B1215) else Color(0xFFFFFFFF)
    val cardColor = if (isDarkMode) Color(0xFF131C1F) else Color(0xFFF8FAFC)
    val borderColor = if (isDarkMode) Color(0xFF22292E) else Color(0xFFE2E8F0)
    val textPrimary = if (isDarkMode) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    val textSecondary = if (isDarkMode) Color(0xFF94A3B8) else Color(0xFF475569)
    val badgeBg = if (isDarkMode) Color(0xFF1F2B30) else Color(0xFFF1F5F9)

    // Simulation Interaction States
    var mobileMenuOpen by remember { mutableStateOf(false) }
    var demoProgressActive by remember { mutableStateOf(false) }
    var fileVaultStatus by remember { mutableStateOf(mapOf(
        "enterprise_spec_Q3.key" to "Encrypted",
        "product_sheet_v2.vault" to "Encrypted",
        "api_secret_config.env" to "Encrypted"
    )) }
    var selectedFileForDecrypt by remember { mutableStateOf<String?>(null) }
    var decryptProgress by remember { mutableStateOf(0f) }
    val coroutineScope = rememberCoroutineScope()
    
    // Floating Chat stream simulation state
    val messageList = remember { mutableStateListOf(
        "Alex: Initializing Workspace connection..." to "system",
        "Sarah (Lead Engineer): Main code deployment is sealed with AES-256." to "user",
        "Security Guard (Bot): Threat level at 0%." to "bot"
    ) }
    var customChatMessage by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Trigger encryption decryption animation
    fun startDecryptSimulation(fileName: String) {
        if (fileVaultStatus[fileName] == "Decrypted") {
            // Re-encrypt
            val updated = fileVaultStatus.toMutableMap()
            updated[fileName] = "Encrypted"
            fileVaultStatus = updated
            Toast.makeText(context, "$fileName re-encrypted securely.", Toast.LENGTH_SHORT).show()
            return
        }
        
        selectedFileForDecrypt = fileName
        decryptProgress = 0f
        coroutineScope.launch {
            for (step in 1..10) {
                delay(120)
                decryptProgress = step / 10f
            }
            val updated = fileVaultStatus.toMutableMap()
            updated[fileName] = "Decrypted"
            fileVaultStatus = updated
            selectedFileForDecrypt = null
            decryptProgress = 0f
            Toast.makeText(context, "Decrypted: $fileName successfully verified!", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .background(Color(0xFF0F172A)) // Aesthetic dark base workspace control board
            .fillMaxSize()
    ) {
        // --- CONTROL PANEL STATION (Aesthetic dashboard to interact with landing page preview!) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Control Settings",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "RESPONSIVE SAAS PREVIEW CONSOLE",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = Color(0xFF94A3B8)
                    )
                }
                
                // Active Device configuration pill indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(currentAccent.container.copy(alpha = 0.2f))
                        .border(1.dp, currentAccent.primary.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isDesktopView) "DESKTOP VIEW" else "MOBILE VIEW",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = currentAccent.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(10.dp))
            
            // Interaction Row selectors
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Device toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF334155))
                        .clickable { isDesktopView = !isDesktopView }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isDesktopView) Icons.Default.Settings else Icons.Default.Share,
                        contentDescription = "Toggle Device",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isDesktopView) "Switch to Mobile" else "Switch to Desktop",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Theme color tint changer
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF334155))
                        .clickable { isDarkMode = !isDarkMode }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Toggle Contrast",
                        tint = if (isDarkMode) Color(0xFFF1F5F9) else Color(0xFFFBBF24),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isDarkMode) "Light Canvas" else "Dark Canvas",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Green hue toggler
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF334155))
                        .clickable {
                            selectedAccentIndex = (selectedAccentIndex + 1) % greenAccents.size
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(currentAccent.primary)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Accent: ${currentAccent.name}",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // --- THE RENDERED SaaS SECTION CONTAINER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF0F172A))
                .padding(if (isDesktopView) 16.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isDesktopView) {
                // --- DESKTOP VIEWPORT BROWSER MOCKUP ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .shadow(16.dp, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor)
                        .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                ) {
                    // Mock Chrome browser top bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isDarkMode) Color(0xFF1E262B) else Color(0xFFF1F5F9))
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                drawLine(color = borderColor, start = Offset(0f, size.height), end = Offset(size.width, size.height), strokeWidth = strokeWidth)
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Window controls
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFEF4444)))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFF10B981)))
                        }
                        
                        Spacer(modifier = Modifier.width(20.dp))
                        
                        // Mock Address bar
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isDarkMode) Color(0xFF0F1215) else Color(0xFFFFFFFF))
                                .border(1.dp, borderColor, RoundedCornerShape(6.dp))
                                .padding(vertical = 4.dp, horizontal = 12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Secure Lock",
                                    tint = currentAccent.primary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "https://workspace.secure.io/pricing-plans",
                                    fontSize = 11.sp,
                                    color = textSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Render Desktop Hero Page content in a scrollable view
                    Box(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        DesktopHeroSaaSLayout(
                            bgColor = bgColor,
                            textColor = textPrimary,
                            subColor = textSecondary,
                            borderColor = borderColor,
                            cardBg = cardColor,
                            accent = currentAccent,
                            fileVaultStatus = fileVaultStatus,
                            selectedFileForDecrypt = selectedFileForDecrypt,
                            decryptProgress = decryptProgress,
                            onFileClick = { startDecryptSimulation(it) },
                            messageList = messageList,
                            customChatMessage = customChatMessage,
                            onChatValChange = { customChatMessage = it },
                            onSendChat = {
                                if (customChatMessage.isNotBlank()) {
                                    messageList.add("User: $customChatMessage" to "user")
                                    customChatMessage = ""
                                    keyboardController?.hide()
                                }
                            },
                            onTriggerDemo = {
                                coroutineScope.launch {
                                    demoProgressActive = true
                                    delay(2000)
                                    demoProgressActive = false
                                    Toast.makeText(context, "Secure pipeline verified (AES-GCM benchmark: 24.3 Gbps)", Toast.LENGTH_LONG).show()
                                }
                            },
                            demoProgressActive = demoProgressActive
                        )
                    }
                }
            } else {
                // --- MOBILE VIEWPORT NATIVE ---
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bgColor)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Header
                        MobileSaaSNavBar(
                            accent = currentAccent,
                            textColor = textPrimary,
                            subColor = textSecondary,
                            borderColor = borderColor,
                            menuOpen = mobileMenuOpen,
                            onMenuToggle = { mobileMenuOpen = !mobileMenuOpen }
                        )
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Column {
                                MobileSaaSContentHero(
                                    accent = currentAccent,
                                    textColor = textPrimary,
                                    subColor = textSecondary,
                                    cardBg = cardColor,
                                    borderColor = borderColor,
                                    fileVaultStatus = fileVaultStatus,
                                    selectedFileForDecrypt = selectedFileForDecrypt,
                                    decryptProgress = decryptProgress,
                                    onFileClick = { startDecryptSimulation(it) },
                                    messageList = messageList,
                                    customChatMessage = customChatMessage,
                                    onChatValChange = { customChatMessage = it },
                                    onSendChat = {
                                        if (customChatMessage.isNotBlank()) {
                                            messageList.add("User: $customChatMessage" to "user")
                                            customChatMessage = ""
                                            keyboardController?.hide()
                                        }
                                    },
                                    onTriggerDemo = {
                                        coroutineScope.launch {
                                            demoProgressActive = true
                                            delay(2000)
                                            demoProgressActive = false
                                            Toast.makeText(context, "Secure pipeline verified (AES-GCM benchmark: 24.3 Gbps)", Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    demoProgressActive = demoProgressActive
                                )
                            }
                        }
                    }

                    // Mobile Menu Full-screen Drawer overlay
                    androidx.compose.animation.AnimatedVisibility(
                        visible = mobileMenuOpen,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 56.dp) // align directly under navigation bar
                                .background(bgColor)
                                .drawBehind {
                                    val strokeWidth = 1.dp.toPx()
                                    drawLine(color = borderColor, start = Offset(0f, size.height), end = Offset(size.width, size.height), strokeWidth = strokeWidth)
                                }
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                "Product Features",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary,
                                modifier = Modifier.clickable {
                                    Toast.makeText(context, "Selected features list", Toast.LENGTH_SHORT).show()
                                    mobileMenuOpen = false
                                }
                            )
                            Text(
                                "Enterprise Pricing",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary,
                                modifier = Modifier.clickable {
                                    Toast.makeText(context, "Navigating to premium plans", Toast.LENGTH_SHORT).show()
                                    mobileMenuOpen = false
                                }
                            )
                            Text(
                                "Customer Support",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary,
                                modifier = Modifier.clickable {
                                    Toast.makeText(context, "Connecting to support desk", Toast.LENGTH_SHORT).show()
                                    mobileMenuOpen = false
                                }
                            )
                            
                            Divider(color = borderColor)
                            
                            Button(
                                onClick = {
                                    Toast.makeText(context, "Signed up for free tier!", Toast.LENGTH_LONG).show()
                                    mobileMenuOpen = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = currentAccent.primary),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().testTag("mobile_menu_signup")
                            ) {
                                Text("Get Started", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// DESKTOP LAYOUT COMPONENTS
// ==========================================

@Composable
fun DesktopHeroSaaSLayout(
    bgColor: Color,
    textColor: Color,
    subColor: Color,
    borderColor: Color,
    cardBg: Color,
    accent: GreenAccent,
    fileVaultStatus: Map<String, String>,
    selectedFileForDecrypt: String?,
    decryptProgress: Float,
    onFileClick: (String) -> Unit,
    messageList: List<Pair<String, String>>,
    customChatMessage: String,
    onChatValChange: (String) -> Unit,
    onSendChat: () -> Unit,
    onTriggerDemo: () -> Unit,
    demoProgressActive: Boolean
) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .padding(vertical = 12.dp)
    ) {
        // --- TOP NAVIGATION BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    Toast.makeText(context, "Welcome to secureplace workspace studio", Toast.LENGTH_SHORT).show()
                }
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(accent.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "secureplace SaaS logo icon",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "secureplace",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    ),
                    color = textColor
                )
            }

            // Product pricing support links
            Row(
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Product",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = subColor,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Viewing features", Toast.LENGTH_SHORT).show()
                    }
                )
                Text(
                    "Pricing",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = subColor,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Viewing cost index", Toast.LENGTH_SHORT).show()
                    }
                )
                Text(
                    "Support",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = subColor,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Opening instant support desk", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            // Compact layout CTA Button
            Button(
                onClick = {
                    Toast.makeText(context, "Account verification initialized", Toast.LENGTH_LONG).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = textColor),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("desktop_nav_cta")
            ) {
                Text(
                    "Get Started",
                    color = bgColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(borderColor)
        )

        // --- CENTER HERO SECTION & INTEGRATION PREVIEWS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            // Left Text column
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .align(Alignment.CenterVertically)
            ) {
                // Feature tag
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(32.dp))
                        .background(accent.container.copy(alpha = 0.3f))
                        .border(1.dp, accent.primary.copy(alpha = 0.4f), RoundedCornerShape(32.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(accent.primary)
                        )
                        Text(
                            text = "WORK CLOUD SEEDING NOW ACTIVE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = accent.primary,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero Header text: The secure workspace for modern teams
                Text(
                    text = "The secure workspace\nfor modern teams",
                    style = MaterialTheme.typography.displayMedium.copy(
                        lineHeight = 44.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1.5).sp
                    ),
                    color = textColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Under headline subdescription text
                Text(
                    text = "End-to-end encrypted collaboration, secure document sharing, and unified communication tools designed for high-performance organizations.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 22.sp
                    ),
                    color = subColor
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Action Buttons container with soft shadow glow accents
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Solid button with shadow
                    Button(
                        onClick = {
                            Toast.makeText(context, "Trial workspace created!", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = accent.primary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(12.dp),
                                ambientColor = accent.primary.copy(alpha = 0.4f),
                                spotColor = accent.primary.copy(alpha = 0.6f)
                            )
                            .testTag("desktop_cta_get_started")
                    ) {
                        Text(
                            "Get Started Free",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    // Transparent view demo action
                    OutlinedButton(
                        onClick = onTriggerDemo,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, if (demoProgressActive) accent.primary else borderColor),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("desktop_cta_view_demo")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (demoProgressActive) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = accent.primary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Analyzing...", color = accent.primary, fontWeight = FontWeight.Bold)
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Trigger secure demo simulation",
                                    tint = textColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("View Demo", color = textColor, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Key assurances
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Done, "Verified", tint = accent.primary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ISO-27001 Certified", fontSize = 11.sp, color = subColor)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Done, "Verified", tint = accent.primary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("No Credit Card Needed", fontSize = 11.sp, color = subColor)
                    }
                }
            }

            // Right column dashboard previewer
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(cardBg)
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                InteractiveDashboardPreviewArea(
                    accent = accent,
                    textColor = textColor,
                    subColor = subColor,
                    borderColor = borderColor,
                    cardBg = cardBg,
                    fileVaultStatus = fileVaultStatus,
                    selectedFileForDecrypt = selectedFileForDecrypt,
                    decryptProgress = decryptProgress,
                    onFileClick = onFileClick,
                    messageList = messageList,
                    customChatMessage = customChatMessage,
                    onChatValChange = onChatValChange,
                    onSendChat = onSendChat
                )
            }
        }
    }
}

// ==========================================
// MOBILE LAYOUT COMPONENTS
// ==========================================

@Composable
fun MobileSaaSNavBar(
    accent: GreenAccent,
    textColor: Color,
    subColor: Color,
    borderColor: Color,
    menuOpen: Boolean,
    onMenuToggle: () -> Unit
) {
    val context = LocalContext.current
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.0f))
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(color = borderColor, start = Offset(0f, size.height), end = Offset(size.width, size.height), strokeWidth = strokeWidth)
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                Toast.makeText(context, "SaaS mobile hub dashboard", Toast.LENGTH_SHORT).show()
            }
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(accent.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "secureplace mobile workspace shield icon",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "secureplace",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                color = textColor
            )
        }

        // Burger / close button
        IconButton(
            onClick = onMenuToggle,
            modifier = Modifier.testTag("mobile_menu_burger")
        ) {
            Icon(
                imageVector = if (menuOpen) Icons.Default.Close else Icons.Default.Menu,
                contentDescription = "Toggle adaptive navigation menu sheet",
                tint = textColor
            )
        }
    }
}

@Composable
fun MobileSaaSContentHero(
    accent: GreenAccent,
    textColor: Color,
    subColor: Color,
    borderColor: Color,
    cardBg: Color,
    fileVaultStatus: Map<String, String>,
    selectedFileForDecrypt: String?,
    decryptProgress: Float,
    onFileClick: (String) -> Unit,
    messageList: List<Pair<String, String>>,
    customChatMessage: String,
    onChatValChange: (String) -> Unit,
    onSendChat: () -> Unit,
    onTriggerDemo: () -> Unit,
    demoProgressActive: Boolean
) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Feature tag
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(accent.container.copy(alpha = 0.3f))
                .border(1.dp, accent.primary.copy(alpha = 0.4f), RoundedCornerShape(32.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(accent.primary)
                )
                Text(
                    text = "MOBILE SECURE PREVIEW ENGINE",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent.primary,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hero title structured for mobile constraints
        Text(
            text = "The secure workspace\nfor modern teams",
            fontSize = 26.sp,
            lineHeight = 32.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = textColor
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtext
        Text(
            text = "End-to-end encrypted collaboration, secure document sharing, and unified communication tools designed for high-performance organizations.",
            fontSize = 13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center,
            color = subColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Mobile actions
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    Toast.makeText(context, "SaaS pipeline initialized!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = accent.primary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(10.dp),
                        ambientColor = accent.primary.copy(alpha = 0.3f),
                        spotColor = accent.primary.copy(alpha = 0.5f)
                    )
                    .testTag("mobile_cta_get_started")
            ) {
                Text(
                    "Get Started Free",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            OutlinedButton(
                onClick = onTriggerDemo,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (demoProgressActive) accent.primary else borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("mobile_cta_view_demo")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (demoProgressActive) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = accent.primary,
                            strokeWidth = 1.5.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Verifying Encryption...", color = accent.primary, fontSize = 13.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Trigger live decrypt demo metrics",
                            tint = textColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("View Demo Pipeline", color = textColor, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Workspace dashboard element mimicking beautiful interactive sandbox features!
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(cardBg)
                .border(1.dp, borderColor, RoundedCornerShape(14.dp))
                .padding(12.dp)
        ) {
            InteractiveDashboardPreviewArea(
                accent = accent,
                textColor = textColor,
                subColor = subColor,
                borderColor = borderColor,
                cardBg = cardBg,
                fileVaultStatus = fileVaultStatus,
                selectedFileForDecrypt = selectedFileForDecrypt,
                decryptProgress = decryptProgress,
                onFileClick = onFileClick,
                messageList = messageList,
                customChatMessage = customChatMessage,
                onChatValChange = onChatValChange,
                onSendChat = onSendChat
            )
        }
    }
}

// ==========================================
// INTERACTIVE DASHBOARD SIMULATOR PANEL
// ==========================================

@Composable
fun InteractiveDashboardPreviewArea(
    accent: GreenAccent,
    textColor: Color,
    subColor: Color,
    borderColor: Color,
    cardBg: Color,
    fileVaultStatus: Map<String, String>,
    selectedFileForDecrypt: String?,
    decryptProgress: Float,
    onFileClick: (String) -> Unit,
    messageList: List<Pair<String, String>>,
    customChatMessage: String,
    onChatValChange: (String) -> Unit,
    onSendChat: () -> Unit
) {
    val context = LocalContext.current
    
    Column {
        // Dashboard Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(accent.primary)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "SECURE DATA VAULT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = accent.primary,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(
                "AES-256 PIPELINE ACTIVE",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = accent.darkAccent,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Interactivity Guideline
        Text(
            text = "👇 Tap on any encrypted data payload below to simulate instantaneous cryptographic decryption!",
            fontSize = 11.sp,
            lineHeight = 15.sp,
            color = subColor
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Vault list
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            fileVaultStatus.forEach { (fileName, decStatus) ->
                val isSelectedForThisDecrypt = fileName == selectedFileForDecrypt
                
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (decStatus == "Decrypted") accent.container.copy(alpha = 0.2f) else borderColor.copy(alpha = 0.2f))
                        .border(
                            1.dp,
                            if (decStatus == "Decrypted") accent.primary.copy(alpha = 0.4f) else borderColor,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { onFileClick(fileName) }
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (decStatus == "Decrypted") Icons.Default.Done else Icons.Default.Lock,
                                contentDescription = "Security Status icon",
                                tint = if (decStatus == "Decrypted") accent.primary else subColor,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = fileName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace,
                                color = textColor
                            )
                        }

                        // Status pill badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (decStatus == "Decrypted") accent.primary else accent.darkAccent.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = decStatus,
                                fontSize = 9.sp,
                                color = if (decStatus == "Decrypted") Color.White else accent.darkAccent,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Progress animation for decrypt preview is visible
                    if (isSelectedForThisDecrypt) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { decryptProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = accent.primary,
                            trackColor = borderColor
                        )
                    }
                    
                    if (decStatus == "Decrypted") {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "📦 [Decrypted Data Stream] Token payload verified. Team permissions authenticated.",
                            fontSize = 10.sp,
                            color = accent.darkAccent,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Simulated chat box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(borderColor)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Safe chat stream icon",
                tint = accent.primary,
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "TEAM WORKSPACE CHAT PIPELINE",
                fontSize = 10.sp,
                color = subColor,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Logs block showing safe workspace communications
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(115.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(borderColor.copy(alpha = 0.15f))
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            messageList.forEach { (message, type) ->
                val bubbleColor = when(type) {
                    "user" -> accent.container.copy(alpha = 0.15f)
                    "bot" -> accent.primary.copy(alpha = 0.1f)
                    else -> Color.Transparent
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(bubbleColor)
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = message,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = textColor,
                        lineHeight = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Send a secure ping interaction
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = customChatMessage,
                onValueChange = onChatValChange,
                placeholder = { Text("Send secure chat ping...", fontSize = 11.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSendChat() }),
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accent.primary,
                    unfocusedBorderColor = borderColor
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("chat_input_ping")
            )
            
            Spacer(modifier = Modifier.width(6.dp))
            
            IconButton(
                onClick = onSendChat,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(accent.primary)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    modifier = Modifier.size(14.dp),
                    contentDescription = "Transmit safe message payload",
                    tint = Color.White
                )
            }
        }
    }
}
