package andoroid.app.mobiles.notimed

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class Perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil)

        //SETUP
        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?: "")

        //guardado de datos
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()

    }

    private fun setup(email:String){
        var nombre = findViewById<TextView>(R.id.nombre)
        nombre.text = email

        val btn = findViewById<Button>(R.id.logOutBtn)

        //cerrar sesión
        btn.setOnClickListener {

            //borrado de datos de sesión
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            showLogIn()
        }
    }

    private fun showLogIn(){
        val logIn = Intent(this, LogIn::class.java)
        startActivity(logIn)
    }

}