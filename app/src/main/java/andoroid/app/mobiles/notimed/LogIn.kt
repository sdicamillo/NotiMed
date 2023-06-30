package andoroid.app.mobiles.notimed

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


class LogIn : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }

    //Inicio de sesión
        fun logIn(view: View){
            val email = findViewById<EditText>(R.id.email).text
            val pass = findViewById<EditText>(R.id.contra).text
            println(email)
            println(pass)
            if (email.isNotEmpty() && pass.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(), pass.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showPerfil(it.result?.user?.email ?:"")
                        finish()
                    } else{
                        showAlert(it.exception?.message.toString())
                    }
                }
            }
            else{
                showAlert("Por favor, rellene todos los campos")
            }
        }

        //configuración de google
        fun googleLogin(view: View){
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        //Registrarse si no tiene cuenta
        fun registrate(view: View) {
            val registrarse = Intent(this, Registro::class.java)
            startActivity(registrarse)
        }
    private fun showAlert(mensaje: String){
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showPerfil(email: String){
        //cambiar AltaMedicamento por Perfil
        val perfil = Intent(this, AltaMedicamento::class.java)//.apply {
            //putExtra("email", email)

        startActivity(perfil)
    }


    //Inicio de sesión de google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            showPerfil(account.email ?: "")
                            persistirUsuario()
                        } else {
                            showAlert(it.exception?.message.toString())
                        }
                    }
                }
            } catch (e: ApiException){
                showAlert(e.toString())
            }
        }
    }

    private fun persistirUsuario(){
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
        val userName = user?.displayName

        // Crea un objeto para guardar los datos del usuario
        val userData = HashMap<String, Any>()
        userData["email"] = userEmail ?: ""
        userData["name"] = userName ?: ""

        // Guarda los datos del usuario en la base de datos
        usersRef.child(userId ?: "").setValue(userData)

    }

}
