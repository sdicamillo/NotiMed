package andoroid.app.mobiles.notimed

import android.app.Application
import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration

class MyApp : Application() {
    companion object {
        var isDarkModeEnabled: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        isDarkModeEnabled = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        // Evitar el modo oscuro en la aplicación
        if (!isDarkModeEnabled) {
            val uiModeManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            uiModeManager.nightMode = UiModeManager.MODE_NIGHT_NO
        }
    }
}




