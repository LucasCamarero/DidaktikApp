package com.lucascamarero.didaktikapp.screens.activities.finalactivity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Diploma(navController: NavController){

    // SE PUEDE BORRAR (Codigo para probar que funciona el menu)
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Spacer(modifier = Modifier.padding(vertical = 20.dp))
            Text("Diploma")
        }
    }
}