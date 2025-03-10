package app.saaslaunchpad.saaslaunchpadapp.bottomnavigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.saaslaunchpad.saaslaunchpadapp.presentation.viewmodel.DetailViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

class TabFourScreenDetail(
    val number: Int,
    onEditClick: () -> Unit,
    onBackClick: () -> Unit
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel = koinViewModel<DetailViewModel>(parameters = { parametersOf(number) })
        val selectedMeme by viewModel.selectedMeme
        val isFavorite by viewModel.isFavorite
        val painterResource = asyncPainterResource(data = selectedMeme?.image ?: "")
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Details")},
                    navigationIcon = {
                        IconButton(onClick = {
                            navigator?.pop()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navigator?.push(TabFourScreenManage(
                                number = number
                            ))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit icon"
                            )
                        }
                        IconButton(
                            onClick = {
                                navigator?.push(TabFourScreenManage(
                                    number = -1
                                ))
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .alpha(if (isFavorite) 1f else 0.38f),
                                imageVector = Icons.Default.Add,
                                contentDescription = "Star icon"
                            )
                        }
                        IconButton(
                            onClick = {
                                viewModel.setFavoriteMeme()
                                navigator?.pop()
                            }
                        ) {
                            Icon(
                                modifier = Modifier
                                    .alpha(if (isFavorite) 1f else 0.38f),
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Star icon"
                            )
                        }
                        IconButton(
                            onClick = {
                                // TODO-FIXME-BRINGBACK viewModel.deleteMeme()
                                viewModel.setFavoriteMeme()
                                navigator?.push(TabFourScreenManage(
                                    number = number
                                ))
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete icon"
                            )
                        }
                    }

                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxSize()
                        .fillMaxWidth()
                ) {
                    KamelImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .fillMaxWidth(),
                        resource = painterResource,
                        contentDescription = "Meme image",
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = selectedMeme?.category ?: "",
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = selectedMeme?.title ?: "",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = selectedMeme?.description ?: ""
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = selectedMeme?.creator ?: "",
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.alpha(0.5f),
                    text = selectedMeme?.tags ?: "",
                )
                /* TODO-FIXME
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    selectedMeme?.tags?.forEach { tag ->
                        Text(
                            modifier = Modifier.alpha(0.5f),
                            text = selectedMeme?.tags ?: "",
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }
                */
            }
        }
    }
}
