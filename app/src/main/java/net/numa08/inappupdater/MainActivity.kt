package net.numa08.inappupdater

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.OnCompleteListener
import com.google.android.play.core.tasks.OnFailureListener
import com.google.android.play.core.tasks.OnSuccessListener
import com.google.android.play.core.tasks.Task
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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
    val inAppUpdateManager =
        InAppUpdateManager(AppUpdateManagerFactory.create(LocalContext.current))
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity
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
            Button(onClick = {
                scope.launch {
                    try {
                        inAppUpdateManager.requestUpdate(AppUpdateType.FLEXIBLE, activity)
                    } catch (e: Throwable) {
                        Log.e("InAppUpdater", "failed", e)
                    }
                }
            }) {
                Text(text = "Update flexible")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                scope.launch {
                    try {
                        inAppUpdateManager.requestUpdate(AppUpdateType.IMMEDIATE, activity)
                    } catch (e: Throwable) {
                        Log.e("InAppUpdater", "failed", e)
                    }
                }
            }) {
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

class InAppUpdateManager(
    private val appUpdateManager: AppUpdateManager
) {

    suspend fun requestUpdate(
        @AppUpdateType updateType: Int,
        activity: Activity
    ) {
        val appUpdateInfo = appUpdateManager
            .appUpdateInfo
            .asFlow()
            .first()
        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            appUpdateManager
                .startUpdateFlow(
                    appUpdateInfo,
                    activity,
                    AppUpdateOptions.defaultOptions(updateType)
                )
                .asFlow()
                .first()
        }
    }
}

fun <T> Task<T>.asFlow(): Flow<T> = callbackFlow {
    val successListener = OnSuccessListener<T> {
        trySend(it)
    }
    val onFailureListener = OnFailureListener {
        close(it)
    }
    val onCompleteListener = OnCompleteListener<T> {
        close()
    }

    addOnSuccessListener(successListener)
    addOnFailureListener(onFailureListener)
    addOnCompleteListener(onCompleteListener)
    awaitClose {  }
}