package andoroid.app.mobiles.notimed

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import andoroid.app.mobiles.notimed.MainActivity
import andoroid.app.mobiles.notimed.ListaMedicamentos
import andoroid.app.mobiles.notimed.Perfil

object BottomNavigationHandler {
    fun handleNavigationItemSelected(context: Context, item: MenuItem) {
        when (item.itemId) {
            R.id.navigation_search -> {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }
            R.id.navigation_home -> {
                val intent = Intent(context, ListaMedicamentos::class.java)
                context.startActivity(intent)
            }
            R.id.navigation_profile -> {
                val intent = Intent(context, Perfil::class.java)
                context.startActivity(intent)
            }
        }
    }


}
