package com.supermassivecode.vinylfinder.ui.screens.wanted

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import com.supermassivecode.vinylfinder.data.local.model.FoundRecordDTO
import com.supermassivecode.vinylfinder.ui.theme.standardPadding
import org.koin.androidx.compose.getViewModel
import androidx.core.net.toUri

@Composable
fun FoundSellersScreen(
    uid: String,
    viewModel: FoundSellersViewModel = getViewModel()
) {
    LaunchedEffect(uid) {
        viewModel.loadFound(uid)
    }

    val state by viewModel.state.collectAsState()
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
        Modifier
            .fillMaxSize()
            .padding(standardPadding),
        verticalArrangement = Arrangement.spacedBy(standardPadding)
    ) {
        items(items = sellers) { record: FoundRecordDTO ->
            SellerItem(record = record, loadUrl)
        }
    }
}

@Composable
fun SellerItem(record: FoundRecordDTO, loadUrl: (url: String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { loadUrl(record.url) })
    ) {
        Row(
            modifier = Modifier.padding(
                start = standardPadding
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(25.dp),
                model = record.shop.imageId,
                contentDescription = "Shop logo for ${record.shop.shopName}"
            )
            Column(
                modifier = Modifier.padding(standardPadding),
            ) {
                Text(
                    text = "${record.currency}${record.price}",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = record.shop.shopName,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = record.notes,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StartWebView(url: String) {
    val context = LocalContext.current
    LaunchedEffect(url) {
        val webIntent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(context, webIntent, null)
    }
}