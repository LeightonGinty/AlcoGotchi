package com.example.alcagotchi

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcagotchi.ui.theme.AlcaGotchiTheme
import kotlinx.coroutines.launch

class DrivingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlcaGotchiTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DrivingGreeting(
                        stringResource(R.string.welcome_to_casino),
                    )
                }
            }
        }
    }
}

class DrivingViewModel : ViewModel() {
    val coin = mutableIntStateOf(0)

    fun drive(context: Context) {
        val alcoGotchi = AlcoGotchi.getInstance()
        viewModelScope.launch {
            kotlin.run {
                try {
                    alcoGotchi.getDrive()
                } catch (e: Exception) {
                    val alertDialog = AlertDialog.Builder(context)

                    alertDialog.apply {
                        setTitle("no")
                        setMessage(e.toString())

                    }.create().show()
                }

                coin.intValue = alcoGotchi.coins
            }
        }
    }
}


@Composable
fun DrivingOptions(viewModel: DrivingViewModel, modifier: Modifier = Modifier) {
    val coin by viewModel.coin

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current as Activity

        Text(
            text = "Would you like to drive?",
            fontSize = 36.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(end = 16.dp)
        )
        Button(onClick = { viewModel.drive(context) }) {
            Text("Yes please")
        }
        Button(onClick = { context.finish() }) {
            Text("\n\n\n\n\n\nNo, as I am over the limit to drive safely. I believe that I am under the influence of alcohol that would inhibit my ability to drive safely on the road. Despite my best efforts to remain composed and aware of my surroundings, I can feel that my coordination and reflexes are not functioning at their usual level. The clarity I typically rely on is clouded, and I can sense that my judgment is becoming impaired. The familiar confidence I usually have behind the wheel is replaced by a growing uncertainty and hesitation. Simple tasks, like maintaining my lane or responding to changes in traffic, are becoming more challenging, and I am aware that my reaction time has slowed considerably. Even though I may still feel like I am in control, deep down I know that the risk of making a mistake—something as minor as misjudging the distance to the car ahead, or as serious as not noticing a pedestrian crossing—could have dire consequences. The last thing I want is to put anyone’s life in danger, including my own, and the best choice in this moment is to acknowledge that I am not in a condition to drive safely, no matter how capable I may think I am. It is better to wait, find another way to get home, or call for help than to risk the kind of harm that could occur from driving while impaired.\n" +
                    "\n" +
                    "\n" +
                    "\n\n\n")
        }
    }
}

@Composable
fun DrivingGreeting(message: String, modifier: Modifier = Modifier) {
    // Create a box to overlap image and texts
    Box(modifier) {
        Image(
            painter = painterResource(id = R.drawable.driving),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 1F,
            modifier = Modifier.fillMaxSize()


        )
        DrivingOptions(viewModel = DrivingViewModel())
    }
}

@Preview(showBackground = false)
@Composable
private fun DrivingPreview() {
    AlcaGotchiTheme {
        DrivingGreeting(
            stringResource(R.string.welcome_to_casino),
        )
    }
}

