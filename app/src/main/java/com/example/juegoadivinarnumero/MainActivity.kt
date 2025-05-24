package com.example.juegoadivinarnumero

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.juegoadivinarnumero.ui.theme.JuegoAdivinarNumeroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JuegoAdivinarNumeroTheme {
                AdivinarNumeroApp()
            }
        }
    }
}

@Composable
fun AdivinarNumeroApp() {
    val rosaBebe = Color(0xFFFFEC4DD)
    val textoBoton = Color(0xFFFF7F5F5)
    val rojoMonchi = Color(0xFFFBF4545)
    val fuenteBukhari = FontFamily(Font(R.font.bukhariscriptalternates))

    var numeroSecreto by remember { mutableStateOf((0..100).random()) }
    var intentos by remember { mutableStateOf(3) }
    var input by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var habilitado by remember { mutableStateOf(true) }
    var tiempoRestante by remember { mutableStateOf(60) }

    // 🔁 Control del temporizador
    var timer by remember { mutableStateOf<CountDownTimer?>(null) }
    var restartKey by remember { mutableStateOf(0) }

    // 🧩 Función para reiniciar el juego
    fun reiniciarJuego() {
        timer?.cancel()  // Cancelamos el temporizador anterior
        numeroSecreto = (0..100).random()
        intentos = 3
        input = ""
        mensaje = ""
        habilitado = true
        tiempoRestante = 60
        restartKey++
    }

    // Temporizador controlado por restartKey
    LaunchedEffect(key1 = restartKey) {
        if (habilitado) {
            timer = object : CountDownTimer(60000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tiempoRestante = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    mensaje = "¡Tiempo agotado! El número era $numeroSecreto"
                    habilitado = false
                }
            }.start()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(rosaBebe)
    ) {
        // 🔄 Botón reiniciar
        Image(
            painter = painterResource(id = R.drawable.reiniciar),
            contentDescription = "Reiniciar juego",
            modifier = Modifier
                .padding(16.dp)
                .size(36.dp)
                .align(Alignment.TopEnd)
                .clickable { reiniciarJuego() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título de mi juegoo
            Text(
                text = "Juego adivina el número",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = fuenteBukhari
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.monchi_preguntas),
                contentDescription = "Monchi ilustrando el juego",
                modifier = Modifier
                    .size(200.dp)
                    .padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Tienes $intentos intentos")

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Tiempo restante: ")
                Text(
                    text = "$tiempoRestante",
                    fontSize = 40.sp,
                    fontFamily = fuenteBukhari,
                    fontWeight = FontWeight.Normal,
                    color = rojoMonchi
                )
                Text(text = " segundos")
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Número del 0 al 100") },
                enabled = habilitado
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    val numeroUsuario = input.toIntOrNull()
                    if (numeroUsuario == null) {
                        mensaje = "Por favor, ingresa un número válido."
                        return@Button
                    }

                    intentos--

                    when {
                        numeroUsuario == numeroSecreto -> {
                            mensaje = "¡Correcto! Adivinaste el número."
                            habilitado = false
                        }
                        intentos > 0 -> {
                            val pista = if (numeroUsuario < numeroSecreto) "mayor" else "menor"
                            mensaje = "Intenta un número $pista. Te quedan $intentos intentos."
                        }
                        else -> {
                            mensaje = "¡Perdiste! El número era $numeroSecreto"
                            habilitado = false
                        }
                    }

                    input = ""
                },
                enabled = habilitado,
                colors = ButtonDefaults.buttonColors(
                    containerColor = rojoMonchi,
                    contentColor = textoBoton
                )
            ) {
                Text("Adivinar")
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = mensaje)
        }
    }
}
