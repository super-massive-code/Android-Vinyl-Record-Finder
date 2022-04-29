package com.supermassivecode.vinylfinder.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.supermassivecode.vinylfinder.data.remote.model.Result

@Composable
fun SearchScreen(navController: NavController,
                 viewModel: SearchScreenViewModel) {
    Column(Modifier.fillMaxSize()) {
        SearchBar {
            viewModel.search(it)
        }
        List(viewModel.records.observeAsState().value)
    }
}

@Composable
private fun SearchBar(onSearch: (term: String) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        val textState = remember { mutableStateOf(TextFieldValue) }
        TextField(
            value = textState.value.Saver.toString(),
            onValueChange = {
                onSearch(it)
            }
        )
    }
}

@Composable
private fun List(records: List<Result>?) {
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
private fun RecordListItem(record: Result, onClick: () -> Unit) {
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
                    model = record.thumb,
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
                record.year?.let { Text(it) }
                record.label?.first()?.let { Text(it) }
                record.catno?.let { Text(it) }
            }
        }
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    VinylFinderTheme {
//        SearchScreen(listOf(
//            Result(1,
//                "https://api.discogs.com/releases/231834",
//                "Carl Taylor - Static / Compulsion",
//                "https://i.discogs.com/9C_asSjqF_K8JhTix42gssh8_zPB1KuZigFR0Mbc_yQ/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTIzMTgz/NC0xNjQzMTMzMDg0/LTg3MDIuanBlZw.jpeg",
//                "UK",
//                "2004",
//                listOf(
//                    "Bugged Out! Recordings"
//                ),
//                "BUG013"
//            ),
//            Result(2,
//                "https://api.discogs.com/releases/2273922",
//                "Carl Taylor - Foundation",
//                "https://i.discogs.com/WyqtOF-QX0sQkcl7Qam8OlWl2kBiemtvxSiAnL1Uia8/rs:fit/g:sm/q:90/h:248/w:250/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTIyNzM5/MjItMTI3Mzc2MjIz/Ni5qcGVn.jpeg",
//                "UK",
//                "2009",
//                listOf(
//                    "Auterform Records"
//                ),
//                "AUFCD001"
//            ),
//            Result(2,
//                "https://api.discogs.com/releases/16812750",
//                "Carl Taylor - Consumer:Producer",
//                "https://i.discogs.com/djP5YIca7hGeIwMkaI8ewemgYw5uLgLa8AftUynSGdc/rs:fit/g:sm/q:90/h:600/w:600/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTE2ODEy/NzUwLTE2MDk5NzI5/NjUtMjYzNS5qcGVn.jpeg",
//                "UK",
//                "2020",
//                listOf(
//                    "Auterform Recordings"
//                ),
//                "LR-1726494"
//            )
//        ))
//    }
//}
