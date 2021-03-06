package com.supermassivecode.vinylfinder.ui.screens.search

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.supermassivecode.vinylfinder.data.local.model.RecordInfoDTO
import com.supermassivecode.vinylfinder.navigation.NavigationScreen
import com.supermassivecode.vinylfinder.ui.GenericAlertDialog
import com.supermassivecode.vinylfinder.ui.theme.standardPadding
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    context: Context,
    viewModel: SearchScreenViewModel = getViewModel()
) {
    val state by viewModel.state.observeAsState()

    Column(
        Modifier.fillMaxSize(),
    ) {
        SearchBar { viewModel.search(it) }
        Box(
            Modifier.fillMaxSize()
        ) {
            when (val s = state) {
                is SearchUiState.Loading ->
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                is SearchUiState.Success -> {

                    RecordList(s.data) { selectedRecord ->
                        navController.navigate(
                            NavigationScreen.Detail.createRoute(
                                Uri.encode(
                                    selectedRecord.toJson()
                                )
                            )
                        )
                    }

                }
                is SearchUiState.Error -> GenericAlertDialog(context, s.alertStringId)
                null -> {}
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchBar(onSearch: (term: String) -> Unit) {
    Column(
        Modifier
            .padding(12.dp)
            .fillMaxWidth()
    ) {
        val textState = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it },
            singleLine = true,
            label = {
                Text(
                    text = "Artist / Title",
                    fontWeight = FontWeight.Light,
                    fontStyle = FontStyle.Italic
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    onSearch(textState.value)
                }),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
        )
    }
}

@Composable
private fun RecordList(records: List<RecordInfoDTO>, onClick: (record: RecordInfoDTO) -> Unit) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        items(items = records) { record ->
            RecordItem(record = record, onClick = {
                onClick(record)
            })
        }
    }
}

@Composable
fun RecordItem(record: RecordInfoDTO, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(standardPadding)
            ) {
                AsyncImage(
                    modifier = Modifier.size(150.dp),
                    model = record.imageUrl,
                    contentDescription = record.title,
                    onLoading = {
                        //TODO: add loading image - how?
                    }
                )
            }
            Column(
                modifier = Modifier.padding(standardPadding)
            ) {
                Text(record.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(record.year)
                Text(record.label)
                Text(record.catno)
            }
        }
    }
}
