package andoroid.app.mobiles.notimed

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LogIn : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        setup()
        sesion()

    }

    override fun onStart() {
        super.onStart()
        val layout = findViewById<RelativeLayout>(R.id.logInLayout)
        layout.visibility = View.VISIBLE
    }

    private fun sesion(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        if (email != null){
            val layout = findViewById<RelativeLayout>(R.id.logInLayout)
            layout.visibility = View.INVISIBLE
            showPerfil(email)
        }
    }

    private fun setup(){
        val btn = findViewById<Button>(R.id.logInBtn)
        val email = findViewById<EditText>(R.id.email).text
        val pass = findViewById<EditText>(R.id.contra).text
        val registroBtn = findViewById<TextView>(R.id.registrate)
        val googleBtn = findViewById<Button>(R.id.googleBtn)

        //Inicio de sesión
        btn.setOnClickListener{
            if (email.isNotEmpty() && pass.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.toString(), pass.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showPerfil(it.result?.user?.email ?:"")

                    } else{
                        showAlert()
                    }
                }
            }
        }

        //configuración de google
        googleBtn.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        //Registrarse si no tiene cuenta
        registroBtn.setOnClickListener {
            val registrarse = Intent(this, Registro::class.java)
            startActivity(registrarse)
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

                        } else {
                            showAlert()
                        }
                    }
                }
            } catch (e: ApiException){
                showAlert()
            }
        }
    }
}
