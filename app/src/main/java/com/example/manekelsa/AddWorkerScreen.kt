package com.example.manekelsa

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

    var availableToday by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = stringResource(R.string.add_worker),
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text(stringResource(R.string.name))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = skill,
            onValueChange = {
                skill = it
            },
            label = {
                Text(stringResource(R.string.skill))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = phone,
            onValueChange = {
                phone = it
            },
            label = {
                Text(stringResource(R.string.phone_number))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rate,
            onValueChange = {
                rate = it
            },
            label = {
                Text(stringResource(R.string.daily_rate))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = city,
            onValueChange = {
                city = it
            },
            label = {
                Text(stringResource(R.string.city))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = experience,
            onValueChange = {
                experience = it
            },
            label = {
                Text(stringResource(R.string.experience))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = rating,
            onValueChange = {
                rating = it
            },
            label = {
                Text(stringResource(R.string.rating))
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = imageUrl,
            onValueChange = {
                imageUrl = it
            },
            label = {
                Text(stringResource(R.string.profile_image_url))
            },
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

            Text(
                text = stringResource(R.string.available_today)
            )
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

                    latitude = when(city.lowercase()) {
                        "bengaluru" -> 12.9716
                        "mysuru" -> 12.2958
                        "mangalore" -> 12.9141
                        else -> 12.9716
                    },

                    longitude = when(city.lowercase()) {
                        "bengaluru" -> 77.5946
                        "mysuru" -> 76.6394
                        "mangalore" -> 74.8560
                        else -> 77.5946
                    }
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

            Text(
                text = stringResource(R.string.add_worker)
            )
        }
    }
}