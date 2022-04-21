package net.numa08.inappupdater

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.numa08.inappupdater.ui.theme.InAppUpdaterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InAppUpdaterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Current version name: ${BuildConfig.VERSION_NAME}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Current version code: ${BuildConfig.VERSION_CODE}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Update flexible")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Update Immediate")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PreviewMainScreen() {
    InAppUpdaterTheme {
        MainScreen()
    }
}