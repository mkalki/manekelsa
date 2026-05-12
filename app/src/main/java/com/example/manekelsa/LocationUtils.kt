package com.example.manekelsa.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
fun getCurrentLocation(

    context: Context,
    onLocationReceived: (Double, Double) -> Unit

) {

    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(context)

    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->

            if (location != null) {

                onLocationReceived(
                    location.latitude,
                    location.longitude
                )
            }
        }
}