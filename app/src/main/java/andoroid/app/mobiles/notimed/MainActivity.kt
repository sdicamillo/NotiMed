package andoroid.app.mobiles.notimed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    // Acción para el elemento "Inicio"

                    val menuPrincipal = Intent(this, MainActivity::class.java)
                    startActivity(menuPrincipal)

                    true
                }

                R.id.navigation_home -> {
                    // Acción para el elemento "Medicamentos"

                    val menuMedicamentos = Intent(this, ListaMedicamentos::class.java)
                    startActivity(menuMedicamentos)

                    true
                }

                R.id.navigation_profile -> {
                    // Acción para el elemento "Perfil"

                    val menuPerfil = Intent(this, Perfil::class.java)
                    startActivity(menuPerfil)
                    true
                }

                else -> false
            }
        }

        obtenerMedicamentos()
    }


    private fun obtenerMedicamentos() {
        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val medicamentosRef = database.getReference("users").child(uid).child("medicamentos")

            val medicamentosList = mutableListOf<Medicamento>()

            medicamentosRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (medicamentoSnapshot in dataSnapshot.children) {

                        val medId = medicamentoSnapshot.key
                        val medName = medicamentoSnapshot.child("name").getValue(String::class.java)
                        val medDosis = medicamentoSnapshot.child("dosis").getValue(String::class.java)
                        val medStock = medicamentoSnapshot.child("stock").getValue(String::class.java)


                        if (medId != null && medName != null && medDosis != null && medStock != null){
                            val medicamento = Medicamento(medId, medName, medDosis, medStock)
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

    private fun mostrarMedicamentos(medicamentosList: List<Medicamento>){

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = MedicamentoAdapter(medicamentosList)
        recyclerView.adapter = adapter

    }



}