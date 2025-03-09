package app.saaslaunchpad.saaslaunchpadapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import app.saaslaunchpad.saaslaunchpadapp.di.initializeKoin
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        try {
            initializeKoin()
        } catch (e: Exception) {
            Timber.e(e, "Error initializing Koin")
        }

        enableEdgeToEdge()
        setContent {
            App(
                prefs = rememberSaveable {
                    createDataStore(applicationContext)
                }
            )
            val darkTheme = isSystemInDarkTheme()
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = this.window
                    window.statusBarColor = Color.Transparent.toArgb()
                    window.navigationBarColor = Color.Transparent.toArgb()
                    val wic = WindowCompat.getInsetsController(window, view)
                    wic.isAppearanceLightStatusBars = false
                    wic.isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }
}

/* TODO-FIXME
@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
*/