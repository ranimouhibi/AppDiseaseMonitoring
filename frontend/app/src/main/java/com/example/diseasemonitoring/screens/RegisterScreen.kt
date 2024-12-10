package com.example.diseasemonitoring.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diseasemonitoring.api.RegisterRequest
import com.example.diseasemonitoring.api.RetrofitInstance
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Nom") })
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Prénom") })
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Mot de passe") })
        TextField(value = age, onValueChange = { age = it }, label = { Text("Âge") })

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            coroutineScope.launch {
                val response = RetrofitInstance.api.registerUser(
                    RegisterRequest(name, lastName, email, password, age.toInt())
                )
                message = response.message
                if (response.success) onRegisterSuccess()
            }
        }) {
            Text("S'inscrire")
        }

        if (message.isNotEmpty()) Text(text = message)
    }
}
