package andoroid.app.mobiles.notimed

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


        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?: "")
    }

    private fun setup(email:String){
        var nombre = findViewById<TextView>(R.id.nombre)
        nombre.text = email
    }
}