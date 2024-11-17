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

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.alcagotchi.ui.theme.AlcaGotchiTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlcaGotchiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingImage(
                        stringResource(R.string.happy_birthday_text),
                        stringResource(R.string.signature_text)
                    )
                }
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private val alcoGotchi = AlcoGotchi.getInstance()
    val connecting = mutableStateOf(true)

    fun connect(context: Context) {
        viewModelScope.launch {
            run {
                try {
                    alcoGotchi.getState()

                    connecting.value = false
                } catch (e: Exception) {
                    val alertDialog = AlertDialog.Builder(context)

                    alertDialog.apply {
                        setTitle("rip")
                        setMessage("can't connect: ${e.message}")
                    }.create().show()
                }
            }
        }
    }
}


@Composable
fun GreetingText(viewModel: MainViewModel, message: String, from: String, modifier: Modifier = Modifier) {
    // Create a column so that texts don't overlap
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        val context = LocalContext.current
        val connecting by remember { viewModel.connecting }

        viewModel.connect(context)

        if (!connecting) {
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
            Button(onClick = {
                val intent = Intent(context, CasinoActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("Casino")
            }
            Button(onClick = {
                val intent = Intent(context, DrinkingActivity::class.java)
                context.startActivity(intent)
            }) {
                Text("Drinking")
            }
        } else {
            Text(
                text = "connecting... plz wait",
                fontSize = 36.sp,
                color = Color(0xFF00FF00),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(end = 16.dp)
                    .align(alignment = Alignment.End)

            )
        }
        Button(onClick = {
            val intent = Intent(context, ShopActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Go to shop")
        }
    }
}
@Composable
fun GreetingImage(message: String, from: String, modifier: Modifier = Modifier,
        navController: NavHostController = rememberNavController(),
//                  viewModel: OrderViewModel = viewModel(),

) {
    // Create a box to overlap image and texts
    Box {
        Image(
            painter = painterResource(id = R.drawable.alcogotchi),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 1F,
            modifier = Modifier.fillMaxSize()


        )
        GreetingText(
            viewModel = MainViewModel(),
            message = message,
            from = from,
//            colorResource(id = )
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun BirthdayCardPreview() {
    AlcaGotchiTheme {
        GreetingImage(
            stringResource(R.string.happy_birthday_text),
            stringResource(R.string.signature_text)
        )
    }
}
