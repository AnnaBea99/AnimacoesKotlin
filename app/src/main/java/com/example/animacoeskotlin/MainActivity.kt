package com.example.animacoeskotlin

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ============================================================
// TELA PRINCIPAL — reúne todos os exemplos de animação
// ============================================================
@Composable
fun AnimacoesDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Animações no Compose",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        ExemploVisibilidade()   // 1. Mostrar / ocultar com animação
        ExemploCor()            // 2. Transição de cor
        ExemploTamanho()        // 3. Escala (efeito de pulsação)
        ExemploSlide()          // 4. Entrada deslizante
    }
}

// ============================================================
// 1. ANIMATEDVISIBILITY — aparece e desaparece suavemente
// ============================================================
@Composable
fun ExemploVisibilidade() {
    // 'visivel' guarda o estado: verdadeiro = caixa aparece
    var visivel by remember { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("1. AnimatedVisibility", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Button(onClick = { visivel = !visivel }) {
            Text(if (visivel) "Ocultar" else "Mostrar")
        }

        Spacer(Modifier.height(8.dp))

        // O bloco dentro de AnimatedVisibility entra e sai com animação
        AnimatedVisibility(
            visible = visivel,
            enter = fadeIn() + expandVertically(),   // animação de entrada
            exit  = fadeOut() + shrinkVertically()   // animação de saída
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF6200EE)),
                contentAlignment = Alignment.Center
            ) {
                Text("Olá!", color = Color.White)
            }
        }
    }
}

// ============================================================
// 2. ANIMATECOLORASSTATE — muda de cor suavemente
// ============================================================
@Composable
fun ExemploCor() {
    var ativo by remember { mutableStateOf(false) }

    // animateColorAsState interpola automaticamente entre as duas cores
    val cor by animateColorAsState(
        targetValue = if (ativo) Color(0xFF03DAC5) else Color(0xFFB00020),
        animationSpec = tween(durationMillis = 600),   // duração: 600 ms
        label = "cor"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("2. animateColorAsState", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(cor)  // usa a cor animada
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = { ativo = !ativo }) {
            Text(if (ativo) "Verde-água" else "Vermelho")
        }
    }
}

// ============================================================
// 3. ANIMATESCALEASSTATE — efeito de pulsação (scale)
// ============================================================
@Composable
fun ExemploTamanho() {
    var grande by remember { mutableStateOf(false) }

    // A escala varia entre 1.0 (normal) e 1.5 (maior)
    val escala by animateFloatAsState(
        targetValue = if (grande) 1.5f else 1.0f,
        animationSpec = spring(               // mola: dá o efeito "elástico"
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessLow
        ),
        label = "escala"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("3. animateFloatAsState (spring)", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .scale(escala)          // aplica a escala calculada
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFC107)),
            contentAlignment = Alignment.Center
        ) {
            Text("★", fontSize = 28.sp)
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { grande = !grande }) {
            Text(if (grande) "Diminuir" else "Crescer")
        }
    }
}

// ============================================================
// 4. ANIMATEDCONTENT — troca de conteúdo com slide
// ============================================================
@Composable
fun ExemploSlide() {
    var pagina by remember { mutableStateOf(0) }
    val paginas = listOf("🏠 Início", "📚 Aula", "🎉 Fim")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("4. AnimatedContent (slide)", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        // AnimatedContent anima a transição sempre que 'pagina' muda
        AnimatedContent(
            targetState = pagina,
            transitionSpec = {
                // desliza para a esquerda ao avançar, para a direita ao voltar
                if (targetState > initialState) {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                } else {
                    slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { it } + fadeOut()
                }
            },
            label = "slide"
        ) { paginaAtual ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF3700B3)),
                contentAlignment = Alignment.Center
            ) {
                Text(paginas[paginaAtual], color = Color.White, fontSize = 18.sp)
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { if (pagina > 0) pagina-- },
                enabled = pagina > 0
            ) { Text("← Anterior") }

            Button(
                onClick = { if (pagina < paginas.lastIndex) pagina++ },
                enabled = pagina < paginas.lastIndex
            ) { Text("Próxima →") }
        }
    }
}