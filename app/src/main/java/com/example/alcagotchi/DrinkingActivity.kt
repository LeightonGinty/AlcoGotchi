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
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.alcagotchi.ui.theme.AlcaGotchiTheme
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpRequest
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
//import java.net.HttpURLConnection
//import java.net.URL
//

//val myBuilder = CronetEngine.Builder(context)
//val cronetEngine: CronetEngine = myBuilder.build()
//var client: OkHttpClient? = OkHttpClient()

fun PostHttpRequest(url_name: String = "http://httpbin.org/post", json_string: String = """{"name":"John Doe", "age": 30}"""){
    val url = URL(url_name)
    val json = json_string
    val connection = url.openConnection() as HttpURLConnection
    connection.apply {
        requestMethod = "POST"
        doOutput = true
        setRequestProperty("Content-Type", "application/json")
    }

    val outputStream = connection.outputStream
    outputStream.write(json.toByteArray())
    outputStream.flush()
    outputStream.close()

    val response = connection.inputStream.use { it.reader().use { reader -> reader.readText() } }
    println(response)
    connection.disconnect()
}
fun asyncGetHttpRequest(
    endpoint: String,
    onSuccess: (ApiResponse<AmiiboResponse>) -> Unit,
    onError: (Exception) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        val url = URL(endpoint)
        val openedConnection = url.openConnection() as HttpURLConnection
        openedConnection.requestMethod = "GET"

        val responseCode = openedConnection.responseCode
        try {
            val reader = BufferedReader(InputStreamReader(openedConnection.inputStream))
            val response = reader.readText()
            val apiResponse = ApiResponse(
                responseCode,
                parseJson<AmiiboResponse>(response)
            )
            print(response)
            reader.close()
            // Call the success callback on the main thread
            launch(Dispatchers.Main) {
                onSuccess(apiResponse)
            }
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
            // Handle error cases and call the error callback on the main thread
            launch(Dispatchers.Main) {
                onError(Exception("HTTP Request failed with response code $responseCode"))
            }
        } finally {

        }
    }
}


//fun sendPost(urlString: String = "http://127.0.0.1:8080/test",
//             bodyContent: String = "test") {
//
//    val client = HttpClient.newBuilder().build();
//    val request = HttpRequest.newBuilder()
//        .uri(URI.create(urlString))
//        //.header("Content-Type", "application/json")
//        .POST(HttpRequest.BodyPublishers.ofString(bodyContent))
//        .build()
//    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
//    val answer = response.body()
//    println(answer)
//}

private inline fun <reified T>parseJson(text: String): T =
    Gson().fromJson(text, T::class.java)

data class ApiResponse<T>(
    val responseCode: Int,
    val response: T
)

data class AmiiboResponse(
    val amiibo: List<AmiiboItem>
)

data class AmiiboItem(
    val amiiboSeries: String,
    val character: String,
    val gameSeries: String,
    val head: String,
    val image: String,
    val name: String,
    val tail: String,
    val type: String
)
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

@Composable
fun DrinkingOptions(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "What would you like to drink?",
            fontSize = 36.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier
                .padding(top = 16.dp)
                .padding(end = 16.dp)
        )
        Button(onClick = {
            PostHttpRequest("http://192.168.4.1/drink")
        })
        {
            Text(
                text = "Drink Beer"
            )
        }
        Button(onClick = {
            PostHttpRequest("http://192.168.4.1/drink")
        })
        {
            Text(
                text = "Drink Whisky"
            )
        }
        Button(onClick = {
            PostHttpRequest("http://192.168.4.1/drink")
        })
        {
            Text(
                text = "Drink Wine"
            )
        }
        Button(onClick = {
            PostHttpRequest("http://192.168.4.1/drink")
        })
        {
            Text(
                text = "Drink Lemonade"
            )
        }
    }
}

@Composable
fun DrinkingGreeting(message: String, modifier: Modifier = Modifier) {
    // Create a box to overlap image and texts
    val context = LocalContext.current

    Box(modifier) {
        Image(
            painter = painterResource(id = R.drawable.spoons),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 1F,
            modifier = Modifier.fillMaxSize()


        )
        Button(onClick = {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Go back")
        }
        DrinkingOptions()
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
