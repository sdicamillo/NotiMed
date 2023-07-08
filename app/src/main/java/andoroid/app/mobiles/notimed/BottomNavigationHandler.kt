package andoroid.app.mobiles.notimed

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.app.Activity

object BottomNavigationHandler {
        fun handleNavigationItemSelected(context: Context, item: MenuItem) {
            val currentActivity = context as Activity

            when (item.itemId) {
                R.id.navigation_search -> {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    currentActivity.startActivity(intent)
                    currentActivity.finishAffinity()
                }
                R.id.navigation_home -> {
                    val intent = Intent(context, ListaMedicamentos::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    currentActivity.startActivity(intent)
                    currentActivity.finishAffinity()
                }
                R.id.navigation_profile -> {
                    val intent = Intent(context, Perfil::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    currentActivity.startActivity(intent)
                    currentActivity.finishAffinity()
                }
            }
        }
    }
