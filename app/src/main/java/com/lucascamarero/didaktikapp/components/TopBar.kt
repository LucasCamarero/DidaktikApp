package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.viewmodels.CounterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    counterViewModel: CounterViewModel,
    onMenuClick: () -> Unit
) {
    val counter by counterViewModel.count.observeAsState(0)

    TopAppBar(
        title = {},
        navigationIcon = {

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { navController.navigate("map") }) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = "Volver atrás",
                        tint = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.size(40.dp)
                    )
                }

                BadgedBox(
                    badge = {
                        Badge(modifier = Modifier.size(30.dp),
                            containerColor = MaterialTheme.colorScheme.scrim,
                            contentColor = MaterialTheme.colorScheme.secondaryContainer
                        ) { Text(counter.toString()) }
                    }
                ) { Image(
                    painter = painterResource(id = R.drawable.rostrojolin),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) }

                IconButton(onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Abrir menú",
                        tint = MaterialTheme.colorScheme.scrim,
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.secondaryContainer,
            titleContentColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}