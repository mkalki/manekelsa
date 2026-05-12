package com.example.manekelsa

import androidx.compose.foundation.layout.*
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddWorkerScreen(
    navController: NavController
) {

    val db = FirebaseFirestore.getInstance()

    var name by remember { mutableStateOf("") }
    var skill by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var rate by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var availableToday by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Add Worker",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = skill,
            onValueChange = { skill = it },
            label = { Text("Skill") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rate,
            onValueChange = { rate = it },
            label = { Text("Daily Rate") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = experience,
            onValueChange = { experience = it },
            label = { Text("Experience") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rating,
            onValueChange = { rating = it },
            label = { Text("Rating") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Profile Image URL") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Checkbox(
                checked = availableToday,
                onCheckedChange = {
                    availableToday = it
                }
            )

            Text(text = "Available Today")
        }

        Button(
            onClick = {

                val worker = Worker(
                    name = name,
                    skill = skill,
                    phoneNumber = phone,
                    dailyRate = rate.toIntOrNull() ?: 0,
                    city = city,
                    experience = experience.toIntOrNull() ?: 0,
                    rating = rating.toDoubleOrNull() ?: 0.0,
                    profileImageUrl = imageUrl,
                    availableToday = availableToday,
                    latitude = 12.9716,
                    longitude = 77.5946
                )

                db.collection("workers")
                    .add(worker)
                navController.popBackStack()

                name = ""
                skill = ""
                phone = ""
                rate = ""
                city = ""
                experience = ""
                rating = ""
                imageUrl = ""
                availableToday = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(text = "Save Worker")
        }
    }
}