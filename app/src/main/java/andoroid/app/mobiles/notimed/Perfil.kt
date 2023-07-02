package andoroid.app.mobiles.notimed

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.perfil)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            BottomNavigationHandler.handleNavigationItemSelected(this, item)
            true
        }

        //muestro los datos del usuario
        mostrarDatos()

        //SETUP
        setup()

        //guardado de datos de la sesión
        guardarDatos()

    }

    private fun setup(){
        var nombreTxt = findViewById<TextView>(R.id.nombre)
        val logOutBtn = findViewById<Button>(R.id.logOutBtn)


        //cerrar sesión
        logOutBtn.setOnClickListener {

            //borrado de datos de sesión
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()
            showLogIn()
        }
    }

    //cargar vista con los datos del usuario
    private fun mostrarDatos(){
        var nombreTxt = findViewById<TextView>(R.id.nombre)
        var emailTxt = findViewById<TextView>(R.id.apellido)

        // Obtengo una instancia de FirebaseAuth
        val firebaseAuth = FirebaseAuth.getInstance()

        // Obtén el usuario autenticado actual
        val user = firebaseAuth.currentUser

        if (user != null){
            // Crea una referencia a la base de datos
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users")

            // Obtengo una referencia al nodo del usuario en la base de datos
            val userRef = usersRef.child(user.uid)

            // Leo los datos del usuario desde la base de datos
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userName = snapshot.child("name").getValue(String::class.java)
                        val userEmail = snapshot.child("email").getValue(String::class.java)

                        nombreTxt.text = userName
                        emailTxt.text = userEmail

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error al traer datos de la bd")
                }

            })
        }
    }

    private fun guardarDatos(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()

        val firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email
        prefs.putString("email", email)
        prefs.apply()
    }

    private fun showLogIn(){
        val logIn = Intent(this, LogIn::class.java)
        startActivity(logIn)
    }

}