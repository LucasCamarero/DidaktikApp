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
import com.lucascamarero.didaktikapp.components.CreateTitle
import com.lucascamarero.didaktikapp.ui.theme.Typography2
import com.lucascamarero.didaktikapp.ui.theme.Typography3
import com.lucascamarero.didaktikapp.viewmodels.FinalGameViewModel

@Composable
fun JoinThePhotos(
    navController: NavController,
    viewModel: FinalGameViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CreateTitle(stringResource(R.string.asocia))
            /*
            Text(
                text = stringResource(R.string.asocia),
                style = Typography3.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )*/

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {

                items(viewModel.photos.size) { index ->
                    val photo = viewModel.photos[index]

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
                                enabled = !viewModel.isMatched(photo)
                            ) {
                                val finished = viewModel.onPhotoClicked(photo)
                                if (finished) {
                                    navController.navigate("writesentence")
                                }
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (viewModel.messageResId != null) {
                Text(
                    text = stringResource(viewModel.messageResId!!),
                    style = Typography3.bodyLarge,
                    color = if (viewModel.messageResId == R.string.asocia1)
                        Color.Green
                    else
                        Color.Red
                )
            }
        }
    }
}