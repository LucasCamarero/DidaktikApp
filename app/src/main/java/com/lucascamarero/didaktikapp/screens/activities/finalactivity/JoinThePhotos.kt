package com.lucascamarero.didaktikapp.screens.activities.finalactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lucascamarero.didaktikapp.R
import com.lucascamarero.didaktikapp.components.CreateTitle2
import com.lucascamarero.didaktikapp.ui.theme.Typography3
import com.lucascamarero.didaktikapp.viewmodels.FinalGameViewModel

/**
 * Pantalla de la actividad final "Join the Photos".
 *
 * El usuario debe asociar correctamente las imágenes presentadas en una cuadrícula.
 * La lógica de selección, emparejamiento y validación se delega al [FinalGameViewModel].
 *
 * Cuando todas las imágenes han sido emparejadas correctamente,
 * se navega automáticamente a la pantalla de escritura de frase.
 *
 * @param navController Controlador de navegación utilizado para avanzar a la siguiente pantalla.
 * @param viewModel ViewModel que gestiona el estado del juego final (selecciones y emparejamientos).
 */
@Composable
fun JoinThePhotos(
    navController: NavController,
    viewModel: FinalGameViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Título principal de la actividad
            CreateTitle2(stringResource(R.string.asocia))

            Spacer(modifier = Modifier.height(25.dp))

            // Mensaje informativo o de feedback mostrado al usuario (si existe)
            if (viewModel.messageResId != null) {
                Text(
                    text = stringResource(viewModel.messageResId!!),
                    style = Typography3.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Cuadrícula de imágenes que el usuario debe asociar
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {

                items(viewModel.photos.size) { index ->
                    val photo = viewModel.photos[index]

                    // Color del borde según el estado de la imagen
                    val borderColor = when {
                        viewModel.isMatched(photo) -> Color.Green
                        viewModel.isSelected(photo) -> Color.Yellow
                        else -> Color.Transparent
                    }

                    Image(
                        painter = painterResource(photo.drawable),
                        contentDescription = null,
                        modifier = Modifier
                            .size(140.dp)
                            .border(4.dp, borderColor)
                            .clickable(
                                // Se deshabilita el click si la imagen ya está emparejada
                                enabled = !viewModel.isMatched(photo)
                            ) {
                                // Gestiona la lógica de selección/emparejamiento
                                val finished = viewModel.onPhotoClicked(photo)

                                // Si el juego ha finalizado, navega a la siguiente pantalla
                                if (finished) {
                                    navController.navigate("writesentence")
                                }
                            }
                    )
                }
            }
        }
    }
}