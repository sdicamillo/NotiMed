package andoroid.app.mobiles.notimed

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MedicamentoDetalle : AppCompatActivity() {

    private val horariosList = mutableListOf<String>()

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

        if (id != null) {
            mostrarHoras(id)
        }

        //cambio de pantalla a editar
        editarBtn.setOnClickListener{
            val intent = Intent(this, EditarMedicamento::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("dosis", dosis)
            intent.putExtra("stock", stock)
            intent.putExtra("listaHorarios", ArrayList(horariosList))
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

                // Mostrar el diálogo de confirmación
                MaterialAlertDialogBuilder(this)
                    .setTitle("Confirmación")
                    .setMessage("¿Estás seguro de que deseas eliminar este medicamento?")
                    .setPositiveButton("Aceptar") { dialog, which ->
                        // Eliminar el medicamento de la base de datos
                        medicationRef.removeValue()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // El medicamento se ha eliminado exitosamente
                                    println("Medicamento eliminado correctamente")
                                    showMain()
                                } else {
                                    // Hubo un error al eliminar el medicamento
                                    println("Error al eliminar el medicamento: ${task.exception?.message}")
                                }
                            }
                    }
                    .setNegativeButton("Cancelar") { dialog, which ->
                        // Acción a realizar si el usuario cancela la eliminación del medicamento
                    }
                    .show()

            }

        }
    }

    private fun mostrarHoras(medId : String){
        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val horariosRef = database.getReference("users").child(uid).child("medicamentos").child(medId).child("horarios")


            horariosRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (horarioSnapshot in dataSnapshot.children) {

                        val horario = horarioSnapshot.getValue(String::class.java)
                        if (horario != null) {
                            horariosList.add(horario)
                        }
                    }
                    setupRecyclerView(horariosList)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar el error en caso de que ocurra
                }
            })

        }

    }

    private fun setupRecyclerView(horasList : List<String>) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView_hora)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ListaHorasAdapter(horasList)
        recyclerView.adapter = adapter
    }

    private fun showMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}