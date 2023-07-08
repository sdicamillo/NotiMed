package andoroid.app.mobiles.notimed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth

class ReestablerContra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reestabler_contra)
    }

    override fun onBackPressed() {
        finish()
    }

    fun reestablecerContra(view:View){

        val email = findViewById<EditText>(R.id.email).text.toString()

        try {
            if (email.isNotEmpty()) {
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.setLanguageCode("es")
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Se envió un correo para reestablecer su contraseña",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "No se pudo enviar el correo de reestablecer contraseña",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else {
                Toast.makeText(this, "Debe ingresar el email", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException){
            val text = e.toString()
            Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
        }

    }

}