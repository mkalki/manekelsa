package com.example.manekelsa

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun WorkerListScreen() {

    val db = FirebaseFirestore.getInstance()

    var workers by remember {
        mutableStateOf(listOf<Worker>())
    }

    var searchText by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    DisposableEffect(Unit) {

        val listener = db.collection("workers")
            .addSnapshotListener { result, _ ->

                if (result != null) {

                    workers = result.map {

                        Worker(
                            name = it.getString("name") ?: "",
                            skill = it.getString("skill") ?: "",
                            phoneNumber = it.getString("phoneNumber") ?: "",
                            dailyRate = it.getLong("dailyRate")?.toInt() ?: 0,
                            availableToday = it.getBoolean("availableToday") ?: false,
                            experience = it.getLong("experience")?.toInt() ?: 0,
                            city = it.getString("city") ?: "",
                            rating = it.getDouble("rating") ?: 0.0,
                            profileImageUrl = it.getString("profileImageUrl") ?: "",
                            latitude = it.getDouble("latitude") ?: 0.0,
                            longitude = it.getDouble("longitude") ?: 0.0
                        )
                    }
                }
            }

        onDispose {
            listener.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Text(
            text = stringResource(R.string.find_workers),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = searchText,

            onValueChange = {
                searchText = it
            },

            label = {
                Text(stringResource(R.string.search_workers))
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(

                workers.filter {

                    it.name.contains(searchText, true) ||
                            it.skill.contains(searchText, true) ||
                            it.city.contains(searchText, true)
                }

            ) { worker ->

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        AsyncImage(
                            model = worker.profileImageUrl,
                            contentDescription = "Worker Image",

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),

                            placeholder = null,
                            error = null
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = worker.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            if (worker.skill == "cleaning")
                                stringResource(R.string.cleaning)
                            else
                                worker.skill
                        )

                        Text("₹${worker.dailyRate}/${stringResource(R.string.day)}")

                        Text(
                            if (worker.city.lowercase() == "bengaluru")
                                stringResource(R.string.bengaluru)
                            else
                                worker.city
                        )

                        Text("⭐ ${worker.rating}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(

                            onClick = {

                                val intent = Intent(
                                    Intent.ACTION_DIAL,
                                    "tel:${worker.phoneNumber}".toUri()
                                )

                                context.startActivity(intent)
                            }

                        ) {

                            Text(stringResource(R.string.call_worker))
                        }
                    }
                }
            }
        }
    }
}