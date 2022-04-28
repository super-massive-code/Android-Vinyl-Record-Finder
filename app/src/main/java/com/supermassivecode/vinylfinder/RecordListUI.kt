package com.supermassivecode.vinylfinder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.supermassivecode.vinylfinder.data.remote.model.Result
import com.supermassivecode.vinylfinder.ui.theme.VinylFinderTheme

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VinylFinderTheme {
        MainRecordList(listOf(
            Result(1,
                "https://api.discogs.com/releases/231834",
                "Carl Taylor - Static / Compulsion",
                "https://i.discogs.com/9C_asSjqF_K8JhTix42gssh8_zPB1KuZigFR0Mbc_yQ/rs:fit/g:sm/q:40/h:150/w:150/czM6Ly9kaXNjb2dz/LWRhdGFiYXNlLWlt/YWdlcy9SLTIzMTgz/NC0xNjQzMTMzMDg0/LTg3MDIuanBlZw.jpeg",
                "UK",
                "2004",
                listOf(
                    "Bugged Out! Recordings"
                ),
                "BUG013"
            )
        ))
    }
}

@Composable
fun MainRecordList(records: List<Result>) {
    Column(Modifier.fillMaxSize()) {
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
fun RecordListItem(record: Result, onClick: () -> Unit) {
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
                modifier = Modifier.padding(10.dp)
            ) {
                AsyncImage(
                    model = record.thumb,
                    contentDescription = record.title,
                    onLoading = {
                        //TODO: add loading image - how?
                    },
                    modifier = Modifier.size(150.dp)
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
