package com.supermassivecode.vinylfinder.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.supermassivecode.vinylfinder.ui.theme.VinylFinderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = 0xFF455a64.toInt()
        //TODO: get this from the theme

        setContent {
            VinylFinderTheme {
                VinylFinderUI()
            }
        }
    }
}
