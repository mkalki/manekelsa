package com.example.manekelsa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.manekelsa.location.getCurrentLocation
import com.example.manekelsa.ui.theme.ManekelsaTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            var showSplash by remember {
                mutableStateOf(true)
            }

            ManekelsaTheme {

                val navController = rememberNavController()

                Scaffold(

                    floatingActionButton = {

                        FloatingActionButton(

                            onClick = {
                                navController.navigate("addWorker")
                            }

                        ) {

                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Worker"
                            )
                        }
                    }

                ) { _ ->

                    if (showSplash) {

                        SplashScreen(

                            onTimeout = {
                                showSplash = false
                            }
                        )

                    } else {

                        NavHost(

                            navController = navController,
                            startDestination = "workerList"

                        ) {

                            composable("workerList") {

                                WorkerListScreen()
                            }

                            composable("addWorker") {

                                AddWorkerScreen(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkerListScreen() {

    val db = FirebaseFirestore.getInstance()

    var workers by remember {
        mutableStateOf(listOf<Worker>())
    }

    var searchText by remember {
        mutableStateOf("")
    }

    var aiPrompt by remember {
        mutableStateOf("")
    }

    var aiResponse by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    var userLatitude by remember {
        mutableStateOf(0.0)
    }

    var userLongitude by remember {
        mutableStateOf(0.0)
    }

    LaunchedEffect(Unit) {

        if (

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION

            ) == PackageManager.PERMISSION_GRANTED

        ) {

            getCurrentLocation(context) { lat, lon ->

                userLatitude = lat
                userLongitude = lon
            }

        } else {

            ActivityCompat.requestPermissions(
                context as MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
    }

    DisposableEffect(Unit) {

        val listener = db.collection("workers")
            .addSnapshotListener { result, error ->

                if (error != null) return@addSnapshotListener

                if (result != null) {

                    val list = result.map { document ->

                        Worker(
                            name = document.getString("name") ?: "",
                            skill = document.getString("skill") ?: "",
                            phoneNumber = document.getString("phoneNumber") ?: "",
                            dailyRate = document.getLong("dailyRate")?.toInt() ?: 0,
                            availableToday = document.getBoolean("availableToday") ?: false,
                            experience = document.getLong("experience")?.toInt() ?: 0,
                            city = document.getString("city") ?: "",
                            rating = document.getDouble("rating") ?: 0.0,
                            profileImageUrl = document.getString("profileImageUrl") ?: "",
                            latitude = document.getDouble("latitude") ?: 0.0,
                            longitude = document.getDouble("longitude") ?: 0.0
                        )
                    }

                    workers = list.sortedBy { worker ->

                        val results = FloatArray(1)

                        Location.distanceBetween(
                            userLatitude,
                            userLongitude,
                            worker.latitude,
                            worker.longitude,
                            results
                        )

                        results[0]
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
            .padding(top = 40.dp)

    ) {

        OutlinedTextField(

            value = aiPrompt,

            onValueChange = {
                aiPrompt = it
            },

            label = {
                Text("Ask AI")
            },

            placeholder = {
                Text("Need cleaner under ₹700 in Bangalore")
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )

        Button(

            onClick = {

                val suggestion = when {

                    aiPrompt.contains("clean", true) ||
                            aiPrompt.contains("maid", true) ->

                        workers.filter {
                            it.skill.contains("clean", true)
                        }

                    aiPrompt.contains("garden", true) ->

                        workers.filter {
                            it.skill.contains("garden", true)
                        }

                    aiPrompt.contains("cook", true) ->

                        workers.filter {
                            it.skill.contains("cook", true)
                        }

                    aiPrompt.contains("electric", true) ->

                        workers.filter {
                            it.skill.contains("electric", true)
                        }

                    aiPrompt.contains("plumb", true) ->

                        workers.filter {
                            it.skill.contains("plumb", true)
                        }

                    else -> workers
                }

                aiResponse =

                    if (suggestion.isNotEmpty()) {

                        "Best Matches:\n\n" +

                                suggestion.joinToString("\n\n") {

                                    "${it.name}\n" +
                                            "Skill: ${it.skill}\n" +
                                            "Rate: ₹${it.dailyRate}/day\n" +
                                            "City: ${it.city}\n" +
                                            "Rating: ${it.rating}"
                                }

                    } else {

                        "No matching workers found"
                    }
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)

        ) {

            Text("Get AI Suggestion")
        }

        if (aiResponse.isNotEmpty()) {

            Card(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)

            ) {

                Text(

                    text = aiResponse,

                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        OutlinedTextField(

            value = searchText,

            onValueChange = {
                searchText = it
            },

            label = {
                Text("Search by skill, name, city")
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        LazyColumn(

            modifier = Modifier.fillMaxSize(),

            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {

            items(

                workers.filter {

                    it.skill.contains(
                        searchText,
                        ignoreCase = true
                    ) ||

                            it.name.contains(
                                searchText,
                                ignoreCase = true
                            ) ||

                            it.city.contains(
                                searchText,
                                ignoreCase = true
                            )
                }

            ) { worker ->

                Card(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .fillMaxWidth(),

                    shape = RoundedCornerShape(20.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {

                    Column(

                        modifier = Modifier.padding(16.dp),

                        verticalArrangement = Arrangement.spacedBy(10.dp),

                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        AsyncImage(
                            model = worker.profileImageUrl,
                            contentDescription = "Worker Image",

                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),

                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = worker.name,
                            fontSize = 24.sp
                        )

                        Text(
                            text = worker.skill,
                            color = Color.Gray,
                            fontSize = 18.sp
                        )

                        Text(
                            text = "₹${worker.dailyRate}/day",
                            fontSize = 18.sp
                        )

                        Text(
                            text = "Experience: ${worker.experience} years"
                        )

                        Text(
                            text = "City: ${worker.city}"
                        )

                        Text(
                            text = "⭐ ${worker.rating}"
                        )

                        if (worker.availableToday) {

                            Text(
                                text = "Available Today",
                                color = Color(0xFF2E7D32),
                                fontSize = 18.sp
                            )

                        } else {

                            Text(
                                text = "Not Available",
                                color = Color.Red,
                                fontSize = 18.sp
                            )
                        }

                        Button(

                            modifier = Modifier.fillMaxWidth(),

                            onClick = {

                                val intent = Intent(
                                    Intent.ACTION_DIAL,
                                    "tel:${worker.phoneNumber}".toUri()
                                )

                                context.startActivity(intent)
                            }

                        ) {

                            Text("Call Worker")
                        }
                    }
                }
            }
        }
    }
}