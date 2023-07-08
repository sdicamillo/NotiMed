package andoroid.app.mobiles.notimed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AltaMedicamento : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alta_medicamentos)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
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
        val guardarBtn = findViewById<Button>(R.id.guardarBtn)

        guardarBtn.setOnClickListener {
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