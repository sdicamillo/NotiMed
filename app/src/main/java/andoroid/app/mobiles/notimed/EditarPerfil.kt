package andoroid.app.mobiles.notimed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditarPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        setup()
    }

    private fun setup(){

        //variables desde el perfil
        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")

        //controles
        val nameView = findViewById<TextView>(R.id.nombre)
        val emailView = findViewById<TextView>(R.id.email)
        val guardarbtn = findViewById<Button>(R.id.guardarBtn)
        val arrowBack = findViewById<ImageButton>(R.id.backButton)

        arrowBack.setOnClickListener{
            finish()
        }

        nameView.text = name
        emailView.text = email

        guardarbtn.setOnClickListener {
            if (name != null && email != null){
                println("perisitir fun")
                persistirPerfil(nameView.text.toString())
                showMain()
            } else{
                println("error")
            }
        }
    }

    private fun persistirPerfil(name:String){

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users")

            // Guardar el medicamento en la base de datos
            usersRef.child(uid).child("name").setValue(name)
                .addOnSuccessListener {
                    println("Medicamento agregado correctamente")
                }
                .addOnFailureListener { exception ->
                    println("Error al agregar el medicamento: $exception")
                }

        } else {
            println("El usuario no ha iniciado sesión")
        }

    }

    private fun showMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

}