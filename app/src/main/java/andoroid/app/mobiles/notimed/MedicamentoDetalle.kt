package andoroid.app.mobiles.notimed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MedicamentoDetalle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_medicamentos)
        //SETUP
        setup()
    }

    private fun setup(){
        val editarBtn = findViewById<Button>(R.id.editarBtn)
        val backArrow = findViewById<ImageButton>(R.id.backButton)
        val borrarBtn = findViewById<ImageButton>(R.id.borrarBtn)

        //variables desde el item medicamento
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val dosis = intent.getStringExtra("dosis")
        val stock = intent.getStringExtra("stock")
        //controles
        val nameView = findViewById<TextView>(R.id.nombreDetalle)
        val dosisView = findViewById<TextView>(R.id.dosisDetalle)
        val stockView = findViewById<TextView>(R.id.stockDetalle)
        nameView.text = name
        dosisView.text = dosis
        stockView.text = stock

        //cambio de pantalla a editar
        editarBtn.setOnClickListener{
            val intent = Intent(this, EditarMedicamento::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("dosis", dosis)
            intent.putExtra("stock", stock)
            startActivity(intent)
        }

        backArrow.setOnClickListener{
            finish()
        }

        //borrar medicamento
        borrarBtn.setOnClickListener {
            // Obtén una instancia de FirebaseAuth
            val firebaseAuth = FirebaseAuth.getInstance()

            // Obtén el usuario autenticado actual
            val user = firebaseAuth.currentUser

            // Verifica si el usuario está autenticado
            if (user != null) {
                val userId = user.uid

                // Crea una referencia a la base de datos
                val database = FirebaseDatabase.getInstance()
                val usersRef = database.getReference("users")

                // Obtén una referencia al nodo del usuario en la base de datos
                val userRef = usersRef.child(userId)

                // Obtén una referencia al nodo del medicamento específico
                val medicationRef = userRef.child("medicamentos").child(id.toString())

                // Elimina el medicamento de la base de datos
                medicationRef.removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // El medicamento se ha eliminado exitosamente
                            println("Medicamento eliminado correctamente")
                        } else {
                            // Hubo un error al eliminar el medicamento
                            println("Error al eliminar el medicamento: ${task.exception?.message}")
                        }
                    }
            }

        }



    }
}