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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class AltaMedicamento : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alta_medicamentos)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            BottomNavigationHandler.handleNavigationItemSelected(this, item)
            true
        }

        setup()

    }

    private fun setup(){
        val nombre = findViewById<EditText>(R.id.nombreMed).text
        val stock = findViewById<EditText>(R.id.cantidad).text
        val dosis = findViewById<EditText>(R.id.dosis).text
        val agregarBtn = findViewById<Button>(R.id.agregarBtn)

        agregarBtn.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid

            if (uid != null) {
                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("users")
                val medicamentosRef = usersRef.child(uid).child("medicamentos")

                //Crea un objeto para guardar los datos del nuevo medicamento
                val medData = HashMap<String, Any>()
                medData["name"] = nombre.toString()
                medData["stock"] = stock.toString()
                medData["dosis"] = dosis.toString()


                // Generar una clave única para el nuevo medicamento
                val medicamentoKey = medicamentosRef.push().key

                // Guardar el medicamento en la base de datos
                if (medicamentoKey != null) {
                    medicamentosRef.child(medicamentoKey.toString()).setValue(medData)
                        .addOnSuccessListener {
                            println("Medicamento agregado correctamente")
                        }
                        .addOnFailureListener { exception ->
                            println("Error al agregar el medicamento: $exception")
                        }
                }

            } else {
                println("El usuario no ha iniciado sesión")
            }
        }

    }

}