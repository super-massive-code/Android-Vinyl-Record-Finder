package com.supermassivecode.vinylfinder.ui.screens

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.supermassivecode.vinylfinder.data.local.model.RecordInfo

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchScreenViewModel
) {
    Box(
        Modifier.fillMaxSize()
    ) {
        Column(Modifier.fillMaxSize()) {
            SearchBar { viewModel.search(it) }
            List(viewModel.records.observeAsState().value)
        }

        viewModel.isLoading.observeAsState().value.run {
            if (this!!) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
private fun SearchBar(onSearch: (term: String) -> Unit) {
    Column(
        Modifier
            .padding(16.dp)
            .width(IntrinsicSize.Max)
    ) {
        val textState = remember { mutableStateOf("") }
        TextField(
            value = textState.value,
            onValueChange = { textState.value = it},
            singleLine = true,
            label = { Text(text = "Search") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(textState.value)
            }),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            },
        )
    }
}

@Composable
private fun List(records: List<RecordInfo>?) {
    if (records != null) {
        LazyColumn(
            Modifier.fillMaxSize()
        ) {
            items(items = records) { record ->
                RecordListItem(record = record, onClick = {
                    //TODO: callback to viewModel, then?
                })
            }
        }
    }
}

@Composable
private fun RecordListItem(record: RecordInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                AsyncImage(
                    modifier = Modifier.size(150.dp),
                    model = record.thumbUrl,
                    contentDescription = record.title,
                    onLoading = {
                        //TODO: add loading image - how?
                    }
                )
            }
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(record.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(record.year)
                Text(record.label)
                Text(record.catno)
            }
        }
    }
}
