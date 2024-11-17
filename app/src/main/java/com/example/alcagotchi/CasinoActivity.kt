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

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alcagotchi.ui.theme.AlcaGotchiTheme
import com.example.alcagotchi.ui.theme.AlcaGotchiTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CasinoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlcaGotchiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CasinoGreeting(
                        stringResource(R.string.welcome_to_casino),
                    )
                }
            }
        }
    }
}

class CasinoViewModel : ViewModel() {
    private val alcoGotchi = AlcoGotchi.getInstance()
    val coin = mutableIntStateOf(alcoGotchi.coins)

    fun gamble(amount: Int, context: Context) {
        viewModelScope.launch {
            run {
                try {
                    alcoGotchi.postGamble(amount)
                } catch (e: Exception) {
                    val alertDialog = AlertDialog.Builder(context)

                    alertDialog.apply {
                        setTitle("no")
                        setMessage("ur broke")
                    }.create().show()
                }

                coin.intValue = alcoGotchi.coins
            }
        }
    }
}


@Composable
fun CasinoOptions(viewModel: CasinoViewModel, modifier: Modifier = Modifier) {
    val coin by viewModel.coin

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current as Activity

        Text(
            text = "Your coins:",
            fontSize = 36.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(end = 16.dp)
        )
        Text(
            text = coin.toString(),
            fontSize = 36.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(end = 16.dp)
        )
        Button(onClick = { viewModel.gamble(10, context) }) {
            Text("Gamble 10 coins")
        }
        Button(onClick = { viewModel.gamble(100, context) }) {
            Text("Gamble 100 coins")
        }
        Button(onClick = { viewModel.gamble(1000, context) }) {
            Text("Gamble 1000 coins")
        }
        Button(onClick = { context.finish() }) {
            Text("Exit casino")
        }
    }
}

@Composable
fun CasinoGreeting(message: String, modifier: Modifier = Modifier) {
    // Create a box to overlap image and texts
    Box(modifier) {
        Image(
            painter = painterResource(id = R.drawable.genting),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 1F,
            modifier = Modifier.fillMaxSize()


        )
        CasinoOptions(viewModel = CasinoViewModel())
    }
}

@Preview(showBackground = false)
@Composable
private fun CasinoPreview() {
    AlcaGotchiTheme {
        CasinoGreeting(
            stringResource(R.string.welcome_to_casino),
        )
    }
}

