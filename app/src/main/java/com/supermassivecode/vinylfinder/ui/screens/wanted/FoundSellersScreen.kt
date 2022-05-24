package com.supermassivecode.vinylfinder.ui.screens.wanted

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import org.koin.androidx.compose.getViewModel

@Composable
fun FoundSellersScreen(
    uid: String,
    viewModel: FoundSellersViewModel = getViewModel()
) {
    viewModel.loadFound(uid)

    val state by viewModel.state.observeAsState()
    when (val s = state) {
        is FoundSellersUiState.ShowFound -> {
            SellersList(sellers = s.data) { url ->
                viewModel.loadUrl(url = url)
            }
        }
        is FoundSellersUiState.LoadWebView -> {
            StartWebView(url = s.url)
        }
        null -> {}
    }
}

@Composable
fun SellersList(sellers: List<FoundRecordDTO>, loadUrl: (url: String) -> Unit) {
    LazyColumn(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(items = sellers) { record: FoundRecordDTO ->
            SellerItem(record = record, loadUrl)
        }
    }
}

@Composable
fun SellerItem(record: FoundRecordDTO, loadUrl: (url: String) -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = { loadUrl(record.url) })
            .background(Color.LightGray),
    ) {
        Row(
            Modifier.padding(
              start = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape),
                model = record.shop.imageId,
                contentDescription = "shop logo"
            )

            Column(
                Modifier.padding(8.dp),
            ) {
                Text("${record.currency}${record.price}", fontWeight = FontWeight.Bold)
                Text(record.shop.shopName, fontWeight = FontWeight.Medium)
                Text(record.notes)
            }
        }
    }
}

@Composable
fun StartWebView(url: String) {
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(LocalContext.current, webIntent, null)
}