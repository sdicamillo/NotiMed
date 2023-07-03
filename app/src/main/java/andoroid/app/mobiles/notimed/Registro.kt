package andoroid.app.mobiles.notimed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
    }

    fun atras(view: View) {
        finish()
    }


    fun registrarse(view: View){
        println("registrarse")
        val email = findViewById<EditText>(R.id.email).text
        val pass = findViewById<EditText>(R.id.contra).text
        val name = findViewById<EditText>(R.id.nombre).text
        val surname = findViewById<EditText>(R.id.apellido).text
        println(email)
        println(pass)
        println(name)
        println(surname)
        if (email.isNotEmpty() && pass.isNotEmpty() && name.isNotEmpty() && surname.isNotEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.toString(), pass.toString()).addOnCompleteListener{
                if (it.isSuccessful){
                    showMain()
                    persistirUsuario(name.toString())
                }
                else{
                    showAlert(it.exception?.message.toString())
                }
            }
        }
        else{
            showAlert("Todos los campos deben ser llenados")
        }
    }

    private fun showAlert(mensaje: String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No se pudo registrar al usuario")
        if (mensaje == "The email address is already in use by another account."){
            builder.setMessage("El correo ya está en uso")
        }
        if (mensaje == "The email address is badly formatted."){
            builder.setMessage("El correo no tiene un formato válido")
        }
        if (mensaje == "The given password is invalid. [ Password should be at least 6 characters ]"){
            builder.setMessage("La contraseña debe tener al menos 6 caracteres")
        }
        if (mensaje == "Todos los campos deben ser llenados"){
            builder.setMessage(mensaje)
        }
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMain(){
        val intent = Intent(this, Perfil::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun persistirUsuario(name: String){
        // Obtén una instancia de FirebaseAuth
        val firebaseAuth = FirebaseAuth.getInstance()

        // Obtén el usuario autenticado actual
        val user = firebaseAuth.currentUser

        // Crea una referencia a la base de datos
        val database = FirebaseDatabase.getInstance()
        val usersRef = database.getReference("users")

        // Obtén los datos del usuario
        val userId = user?.uid
        val userEmail = user?.email

        // Crea un objeto para guardar los datos del usuario
        val userData = HashMap<String, Any>()
        userData["email"] = userEmail ?: ""
        userData["name"] = name

        // Guarda los datos del usuario en la base de datos
        usersRef.child(userId ?: "").setValue(userData)

    }

}