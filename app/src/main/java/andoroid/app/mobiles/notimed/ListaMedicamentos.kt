package andoroid.app.mobiles.notimed

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ListaMedicamentos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_medicamentos)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            BottomNavigationHandler.handleNavigationItemSelected(this,bottomNavigationView, item)
            true
        }
        obtenerMedicamentos()
        val btnAltaMedicamento = findViewById<Button>(R.id.altaMedicamento_btn)
        btnAltaMedicamento.setOnClickListener {
            altaMedicamento(this)
        }
    }
    private lateinit var recyclerView: RecyclerView

    private fun obtenerMedicamentos() {
        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val medicamentosRef = database.getReference("users").child(uid).child("medicamentos")

            val medicamentosList = mutableListOf<Medicamento>()

            medicamentosRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    val listType = object : GenericTypeIndicator<List<String>>() {}

                    for (medicamentoSnapshot in dataSnapshot.children) {

                        val medId = medicamentoSnapshot.key
                        val medName = medicamentoSnapshot.child("name").getValue(String::class.java)
                        val medDosis = medicamentoSnapshot.child("dosis").getValue(String::class.java)
                        val medStock = medicamentoSnapshot.child("stock").getValue(String::class.java)
                        val medHorarios = medicamentoSnapshot.child("horarios").getValue(listType)


                        if (medId != null && medName != null && medDosis != null && medStock != null){
                            val medicamento = Medicamento(medId, medName, medDosis, medStock, medHorarios?: emptyList())
                            medicamentosList.add(medicamento)
                        }
                    }

                    mostrarMedicamentos(medicamentosList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar el error en caso de que ocurra
                }
            })

        }
    }

    private fun altaMedicamento(context: Context){
        println("Alta")
        val intent = Intent(context, AltaMedicamento::class.java)
        println(intent)
        startActivity(intent)
    }

    private fun mostrarMedicamentos(medicamentosList: List<Medicamento>){

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ListaMedicamentoAdapter(medicamentosList)
        recyclerView.adapter = adapter

    }
}