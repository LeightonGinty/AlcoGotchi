package com.example.alcagotchi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.alcagotchi.ui.theme.AlcaGotchiTheme
import java.lang.reflect.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun GetAmiiboItems(modifier: Modifier) {
    val amiiboList = remember { mutableStateOf<List<AmiiboItem>>(listOf()) }
    Column {
        Button(onClick = {
            asyncGetHttpRequest(
                endpoint = "https://www.amiiboapi.com/api/amiibo/",
                onSuccess = {
                    amiiboList.value = it.response.amiibo
                    Log.d("SUCCESS", amiiboList.toString())
                },
                onError = {
                    Log.d("ERROR", it.message.toString())
                }
            )
        })
        {
            Text(
                text = "Get Amiibos"
            )
        }
    }
    Column(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp)
    ) {
        Text(text = amiiboList.value.toString())
    }
}
class RequestTest : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlcaGotchiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Let's create a composable function named GetAmiiboItems
                    GetAmiiboItems(modifier = Modifier())
                }
            }
        }
    }
}