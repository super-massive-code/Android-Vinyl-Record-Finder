package com.supermassivecode.vinylfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.supermassivecode.vinylfinder.data.remote.DiscogsService
import com.supermassivecode.vinylfinder.ui.theme.VinylFinderTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VinylFinderTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            searchDiscogs("Carl Taylor Static")
        }
    }

    suspend fun searchDiscogs(query: String) {
        //TODO move this into viewModel etc
        val token: String = getString(R.string.discogs_token)
        val response = DiscogsService.getService().search(token = token, query = query)
        if (response.isSuccessful) {

        }
    }

    @Composable
    fun MainList() {

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    VinylFinderTheme {
        Greeting("Android")
    }
}

