package andoroid.app.mobiles.notimed

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.app.Activity
import com.google.android.material.bottomnavigation.BottomNavigationView

object BottomNavigationHandler {
    fun handleNavigationItemSelected(context: Context, bottomNavigationView: BottomNavigationView, item: MenuItem) {
        val currentActivity = context as Activity

        when (item.itemId) {
            R.id.navigation_search -> {
                if (currentActivity is MainActivity) {
                    return
                }
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                currentActivity.startActivity(intent)
                currentActivity.finishAffinity()
            }
            R.id.navigation_home -> {
                if (currentActivity is ListaMedicamentos) {
                    return
                }

                val intent = Intent(context, ListaMedicamentos::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                currentActivity.startActivity(intent)
                currentActivity.finishAffinity()
            }
            R.id.navigation_profile -> {
                if (currentActivity is Perfil) {
                    return
                }

                val intent = Intent(context, Perfil::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                currentActivity.startActivity(intent)
                currentActivity.finishAffinity()
            }
        }
        bottomNavigationView.postDelayed({
            bottomNavigationView.setOnItemSelectedListener { menuItem ->
                handleNavigationItemSelected(context, bottomNavigationView, menuItem) // Volver a habilitar el escuchador de selección
                true
            }
        }, 100)
    }
}

