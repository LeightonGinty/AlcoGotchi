/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.alcagotchi

//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcagotchi.ui.theme.AlcaGotchiTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

//import java.net.HttpURLConnection
//import java.net.URL
//

//val myBuilder = CronetEngine.Builder(context)
//val cronetEngine: CronetEngine = myBuilder.build()
//var client: OkHttpClient? = OkHttpClient()

fun PostHttpRequest(url_name: String = "http://httpbin.org/post", json_string: String = """{"name":"John Doe", "age": 30}"""){
}

class DrinkingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlcaGotchiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrinkingGreeting(
                        stringResource(R.string.welcome_to_casino),
                    )
                }
            }
        }
    }
}



@Composable
fun DrinkingGreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    // Create a column so that texts don't overlap
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = message,
            fontSize = 100.sp,
            lineHeight = 116.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF00FF00),
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = from,
            fontSize = 36.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(end = 16.dp)
                .align(alignment = Alignment.End)

        )
    }
}
class DrinkingViewModel : ViewModel() {
    val drunk = mutableIntStateOf(0)

    fun drink(drink_choice: String, context: Activity) {
        val alcoGotchi = AlcoGotchi.getInstance()
        viewModelScope.launch {
            runBlocking {
                try {
                    alcoGotchi.postDrink(drink_choice)
                } catch (e: Exception) {
                    val alertDialog = AlertDialog.Builder(context)

                    alertDialog.apply {
                        setTitle("no")
                        setMessage(e.toString())
                    }.create().show()
                }

                drunk.intValue = alcoGotchi.drunk
            }
        }

    }
}

@Composable
fun DrinkingOptions(viewModel: DrinkingViewModel, modifier: Modifier = Modifier) {
    val drunk by viewModel.drunk

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current as Activity
        Text(
            text = "Number of units drank:",
            fontSize = 36.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = drunk.toString(),
            fontSize = 36.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = "What would you like to drink?",
            fontSize = 36.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(end = 16.dp)
        )
        Button(onClick = {
            viewModel.drink("beer", context)
        })
        {
            Text(
                text = "Drink Beer"
            )
        }
        Button(onClick = {
            viewModel.drink("whisky", context)
        })
        {
            Text(
                text = "Drink Whisky"
            )
        }
        Button(onClick = {
            viewModel.drink("wine", context)
        })
        {
            Text(
                text = "Drink Wine"
            )
        }
        Button(onClick = {
            viewModel.drink("lemonade", context)
        })
        {
            Text(
                text = "Drink Lemonade"
            )
        }
        Button(onClick = { context.finish() }) {
            Text("Go back")
        }
    }
}

@Composable
fun DrinkingGreeting(message: String, modifier: Modifier = Modifier) {
    // Create a box to overlap image and texts
    val context = LocalContext.current as Activity

    Box(modifier) {
        Image(
            painter = painterResource(id = R.drawable.spoons),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 1F,
            modifier = Modifier.fillMaxSize()


        )
        DrinkingOptions(viewModel = DrinkingViewModel())
    }
}

@Preview(showBackground = false)
@Composable
private fun DrinkingPreview() {
    AlcaGotchiTheme {
        DrinkingGreeting(
            stringResource(R.string.welcome_to_casino),
        )
    }
}
