package io.github.loshine.constructor

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alibaba.fastjson.JSON
import io.github.loshine.constructor.model.TestModel
import io.github.loshine.constructor.ui.theme.ConstuctorcompilerpluginTheme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        testFastjson()
        testKotlinSerialization()
        setContent {
            ConstuctorcompilerpluginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun testFastjson() {
        Log.d("MainActivity", "test fastjson")
        val model = TestModel(foo = "fooValue", bar = 2)
        Log.d("MainActivity", "toJSON: ${JSON.toJSON(model)}")
        val fromJson = JSON.parseObject(
            """
            {"testFoo" :"testFooValue", "testBar":3}
        """.trimIndent(), TestModel::class.java
        )
        Log.d("MainActivity", "fromJSON: $fromJson")
    }

    private fun testKotlinSerialization() {
        Log.d("MainActivity", "test kotlin serialization")
        val model = TestModel(foo = "fooValue", bar = 2)
        val toJson = Json.encodeToString(model)
        Log.d("MainActivity", "toJson: $toJson")
        val fromJson: TestModel = Json.decodeFromString(
            """
            {"testFoo" :"testFooValue", "testBar":3}
        """.trimIndent()
        )
        Log.d("MainActivity", "fromJson: $fromJson")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ConstuctorcompilerpluginTheme {
        Greeting("Android")
    }
}
