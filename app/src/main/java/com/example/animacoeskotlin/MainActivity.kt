package com.example.animacoeskotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// ============================================================
// ENTRY POINT
// ============================================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavegacao()
                }
            }
        }
    }
}

// ============================================================
// NAVEGAÇÃO — define as rotas "primeira" e "segunda"
// ============================================================
@Composable
fun AppNavegacao() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "primeira"
    ) {
        // Rota da Tela 1 — entra deslizando da esquerda
        composable(
            route = "primeira",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) + fadeIn(tween(400))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) + fadeOut(tween(400))
            }
        ) {
            TelaPrincipal(navController)
        }

        // Rota da Tela 2 — entra deslizando da direita
        composable(
            route = "segunda",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) + fadeIn(tween(400))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) + fadeOut(tween(400))
            }
        ) {
            TelaSecundaria(navController)
        }
    }
}

// ============================================================
// TELA 1 — animações + botão que navega para a Tela 2
// ============================================================
@Composable
fun TelaPrincipal(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🎬 Tela 1 — Animações", fontSize = 22.sp, fontWeight = FontWeight.Bold)

        ExemploVisibilidade()
        ExemploCor()
        ExemploTamanho()
        ExemploSlide()

        // Botão de navegação para a Tela 2
        Button(
            onClick = { navController.navigate("segunda") },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Ir para a Tela 2 →", fontSize = 16.sp, color = Color.White)
        }
    }
}

// ============================================================
// TELA 2 — tela de destino
// ============================================================
@Composable
fun TelaSecundaria(navController: NavHostController) {
    // Animação de escala ao entrar na tela
    var apareceu by remember { mutableStateOf(false) }
    val escala by animateFloatAsState(
        targetValue = if (apareceu) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "escalaEntrada"
    )

    LaunchedEffect(Unit) { apareceu = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A0050))
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .scale(escala)
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFBB86FC)),
            contentAlignment = Alignment.Center
        ) {
            Text("🚀", fontSize = 48.sp)
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text = "Você chegou na Tela 2!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.scale(escala)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "A navegação usou slideInHorizontally\n+ fadeIn para esta transição.",
            fontSize = 14.sp,
            color = Color(0xFFBB86FC),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(40.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Text("← Voltar para a Tela 1")
        }
    }
}

// ============================================================
// EXEMPLOS DE ANIMAÇÃO
// ============================================================
@Composable
fun ExemploVisibilidade() {
    var visivel by remember { mutableStateOf(true) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("1. AnimatedVisibility", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Button(onClick = { visivel = !visivel }) {
            Text(if (visivel) "Ocultar" else "Mostrar")
        }
        Spacer(Modifier.height(8.dp))
        AnimatedVisibility(
            visible = visivel,
            enter = fadeIn() + expandVertically(),
            exit  = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF6200EE)),
                contentAlignment = Alignment.Center
            ) { Text("Olá!", color = Color.White) }
        }
    }
}

@Composable
fun ExemploCor() {
    var ativo by remember { mutableStateOf(false) }
    val cor by animateColorAsState(
        targetValue = if (ativo) Color(0xFF03DAC5) else Color(0xFFB00020),
        animationSpec = tween(600), label = "cor"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("2. animateColorAsState", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(cor))
        Spacer(Modifier.height(8.dp))
        Button(onClick = { ativo = !ativo }) {
            Text(if (ativo) "Verde-água" else "Vermelho")
        }
    }
}

@Composable
fun ExemploTamanho() {
    var grande by remember { mutableStateOf(false) }
    val escala by animateFloatAsState(
        targetValue = if (grande) 1.5f else 1.0f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "escala"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("3. animateFloatAsState (spring)", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.scale(escala).size(60.dp).clip(CircleShape)
                .background(Color(0xFFFFC107)),
            contentAlignment = Alignment.Center
        ) { Text("★", fontSize = 28.sp) }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { grande = !grande }) {
            Text(if (grande) "Diminuir" else "Crescer")
        }
    }
}

@Composable
fun ExemploSlide() {
    var pagina by remember { mutableStateOf(0) }
    val paginas = listOf("🏠 Início", "📚 Aula", "🎉 Fim")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("4. AnimatedContent (slide)", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        AnimatedContent(
            targetState = pagina,
            transitionSpec = {
                if (targetState > initialState)
                    slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                else
                    slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
            },
            label = "slide"
        ) { paginaAtual ->
            Box(
                modifier = Modifier.fillMaxWidth().height(60.dp)
                    .clip(RoundedCornerShape(12.dp)).background(Color(0xFF3700B3)),
                contentAlignment = Alignment.Center
            ) { Text(paginas[paginaAtual], color = Color.White, fontSize = 18.sp) }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = { if (pagina > 0) pagina-- }, enabled = pagina > 0) {
                Text("← Anterior")
            }
            Button(onClick = { if (pagina < paginas.lastIndex) pagina++ }, enabled = pagina < paginas.lastIndex) {
                Text("Próxima →")
            }
        }
    }
}
