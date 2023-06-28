package andoroid.app.mobiles.notimed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        setup();
    }

    fun atras() {
        val login = Intent(this, LogIn::class.java)
        startActivity(login)
    }


    private fun setup(){
        val btn = findViewById<Button>(R.id.registrarBtn)
        val email = findViewById<EditText>(R.id.email).text
        val pass = findViewById<EditText>(R.id.contra).text

        btn.setOnClickListener{
            if (email.isNotEmpty() && pass.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.toString(), pass.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showPerfil(it.result?.user?.email ?:"")

                    } else{
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No se pudo registrar al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showPerfil(email: String){
        val perfil = Intent(this, Perfil::class.java).apply {
            putExtra("email", email)
        }
        startActivity(perfil)
    }


}