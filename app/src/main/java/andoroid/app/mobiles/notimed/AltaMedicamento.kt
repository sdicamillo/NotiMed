package andoroid.app.mobiles.notimed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AltaMedicamento : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alta_medicamentos)
        setup()
    }

    private fun showAlert(mensaje: String){
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun setup(){
        val arrowBack = findViewById<ImageButton>(R.id.backButton)
        val nombre = findViewById<EditText>(R.id.nombreMed).text
        val stock = findViewById<EditText>(R.id.cantidad).text
        val agregarBtn = findViewById<Button>(R.id.agregarBtn)
        val guardarBtn = findViewById<Button>(R.id.guardarBtn)

        arrowBack.setOnClickListener{
            finish()
        }

        guardarBtn.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid

            if (uid != null) {
                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("users")
                val medicamentosRef = usersRef.child(uid).child("medicamentos")

                //Crea un objeto para guardar los datos del nuevo medicamento
                val medData = HashMap<String, Any>()

                if (nombre.isEmpty() || stock.isEmpty()) {
                    showAlert("Debe ingresar los datos")
                } else {
                    medData["name"] = nombre.toString()
                    medData["stock"] = stock.toString()


                    // Generar una clave única para el nuevo medicamento
                    val medicamentoKey = medicamentosRef.push().key

                    // Guardar el medicamento en la base de datos
                    if (medicamentoKey != null) {
                        medicamentosRef.child(medicamentoKey.toString()).setValue(medData)
                            .addOnSuccessListener {
                                println("Medicamento agregado correctamente")
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                println("Error al agregar el medicamento: $exception")
                                showAlert(exception.toString())
                            }
                    } else {
                        println("El usuario no ha iniciado sesión")
                    }
                }
            }
        }

    }

}