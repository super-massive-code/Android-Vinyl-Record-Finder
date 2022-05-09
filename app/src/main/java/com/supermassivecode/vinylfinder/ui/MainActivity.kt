package com.supermassivecode.vinylfinder.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.supermassivecode.vinylfinder.ui.theme.VinylFinderTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("SMC", "onCreate()")

        setContent {
            VinylFinderTheme {
                VinylFinderUI()
            }
        }
    }
}