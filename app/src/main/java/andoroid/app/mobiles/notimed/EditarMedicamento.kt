package andoroid.app.mobiles.notimed

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditarMedicamento : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editar_medicamento)

        //SETUP
        setup()
    }

    private fun setup(){

        //variables desde el medicamento detalle
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val dosis = intent.getStringExtra("dosis")
        val stock = intent.getStringExtra("stock")

        //controles
        val nameView = findViewById<TextView>(R.id.nombreEditar)
        val dosisView = findViewById<TextView>(R.id.dosisEditar)
        val stockView = findViewById<TextView>(R.id.stockEditar)

        nameView.text = name
        dosisView.text = dosis
        stockView.text = stock

        if (id != null && name != null && dosis != null && stock != null) {
            persistirMedicamento(id, name, dosis, stock)
        } else{
            println("error")
        }
    }

    private fun persistirMedicamento(medId: String, medName:String, medDosis:String, medStock:String ){

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val database = FirebaseDatabase.getInstance()
            val usersRef = database.getReference("users")
            val medicamentosRef = usersRef.child(uid).child("medicamentos").child(medId)

            //Crea un objeto para guardar los datos del medicamento editado
            val medData = HashMap<String, Any>()
            medData["name"] = medName
            medData["stock"] = medStock
            medData["dosis"] = medDosis

            // Guardar el medicamento en la base de datos
            medicamentosRef.setValue(medData)
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




}