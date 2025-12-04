package com.lucascamarero.didaktikapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.lucascamarero.didaktikapp.ScreenManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu() {
    val navController = rememberNavController()
    val drawState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf("Intro") }
    ModalNavigationDrawer(
        drawerState = drawState,
        drawerContent = {
            ModalDrawerSheet{
                Column(Modifier.padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())) {
                    Spacer(Modifier.height(16.dp))
                    Text("Menu", style = MaterialTheme.typography.bodyLarge)
                    NavigationDrawerItem(
                        label = {Text("Intro")},
                        selected = selectedItem == "Intro",
                        onClick = {selectedItem = "Intro"
                            navController.navigate("Intro")
                        }
                    )
                    NavigationDrawerItem(
                        label = {Text("Juego1")},
                        selected = selectedItem == "Juego1",
                        onClick = {selectedItem = "Juego1"
                        navController.navigate("Activity1")}
                    )
                    NavigationDrawerItem(
                        label = {Text("Juego2")},
                        selected = selectedItem == "Juego2",
                        onClick = {selectedItem="Juego2"
                        navController.navigate("Activity2")}
                    )
                    NavigationDrawerItem(
                        label = {Text("Juego3")},
                        selected = selectedItem == "Juego3",
                        onClick = {selectedItem="Juego3"
                        navController.navigate("Activity3")}
                    )
                    NavigationDrawerItem(
                        label = {Text("Juego4")},
                        selected = selectedItem == "Juego4",
                        onClick = {selectedItem="Juego4"
                        navController.navigate("Activity4")}
                    )
                    NavigationDrawerItem(
                        label = {Text("Juego5")},
                        selected = selectedItem == "Juego5",
                        onClick = {selectedItem="Juego5"
                            navController.navigate("Activity5")}
                    )
                    NavigationDrawerItem(
                        label = {Text("Juego6")},
                        selected = selectedItem == "Juego6",
                        onClick = {selectedItem="Juego6"
                            navController.navigate("Activity6")}
                    )
                    NavigationDrawerItem(
                        label = {Text("Juego7")},
                        selected = selectedItem == "Juego7",
                        onClick = {selectedItem="Juego7"
                            navController.navigate("Activity7")}
                    )


                }
            }
        }
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text("Menu")},
                    navigationIcon = {
                        IconButton({
                            scope.launch {
                                if (drawState.isClosed)
                                    drawState.open() else drawState.close()
                            }
                        }){
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) {
            ScreenManager()
        }
    }

}