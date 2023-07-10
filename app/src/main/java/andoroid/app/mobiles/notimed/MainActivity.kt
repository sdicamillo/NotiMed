package andoroid.app.mobiles.notimed

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Locale


class MainActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            BottomNavigationHandler.handleNavigationItemSelected(this,bottomNavigationView, item)
            true
        }

        obtenerMedicamentos()

        //guardado de datos de la sesión
        guardarDatos()
    }


    private fun guardarDatos(){
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()

        val firebaseAuth = FirebaseAuth.getInstance()
        val email = firebaseAuth.currentUser?.email
        prefs.putString("email", email)
        prefs.apply()
    }

    private fun obtenerMedicamentos() {
        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val medicamentosRef = database.getReference("users").child(uid).child("medicamentos")

            val medicamentosList = mutableListOf<Medicamento>()

            medicamentosRef.addListenerForSingleValueEvent(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (medicamentoSnapshot in dataSnapshot.children) {

                        val medId = medicamentoSnapshot.key
                        val medName = medicamentoSnapshot.child("name").getValue(String::class.java)
                        val medDosis = medicamentoSnapshot.child("dosis").getValue(String::class.java)
                        val medStock = medicamentoSnapshot.child("stock").getValue(String::class.java)

                        val horariosList = mutableListOf<String>()

                        val horariosSnapshot = medicamentoSnapshot.child("horarios")
                        for (horarioSnapshot in horariosSnapshot.children) {
                            val medHorario = horarioSnapshot.getValue(String::class.java)
                            if (medHorario != null) {
                                horariosList.add(medHorario)
                            }
                        }

                        if (medId != null && medName != null && medDosis!= null && medStock != null){
                            val medicamento = Medicamento(medId, medName, medDosis, medStock, horariosList)
                            medicamentosList.add(medicamento)
                        }
                    }
                    adaptarLista(medicamentosList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Manejar el error en caso de que ocurra
                }
            })

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun adaptarLista(medicamentosList: List<Medicamento>){
        val nuevaListaMedicamentos: List<MedicamentoHorario> = medicamentosList.flatMap { medicamento ->
            medicamento.horarios.map { horario ->
                MedicamentoHorario(medicamento.id, medicamento.name, medicamento.dosis, medicamento.stock, horario)
            }
        }

        println("hola")
        println(nuevaListaMedicamentos)


        val currentTime = LocalTime.now()

        println(currentTime)

        val listaOrdenada = nuevaListaMedicamentos.sortedBy { medicamento ->
            val horario = LocalTime.parse(medicamento.horario)
            val diferencia = horario.hour * 60 + horario.minute - (currentTime.hour * 60 + currentTime.minute)
            if (diferencia < 0) diferencia + 24 * 60 else diferencia
        }


        mostrarMedicamentos(listaOrdenada)

    }

    // Función para convertir una cadena de horario a un timestamp
    fun convertToTimestamp(horario: String): Long {
        // Implementa la lógica para convertir la cadena de horario a un timestamp
        // Puedes utilizar SimpleDateFormat u otras clases de manejo de fechas y horarios en Kotlin
        // En este ejemplo, se utiliza un formato de horario "HH:mm" y se convierte a un timestamp
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = format.parse(horario)
        return date?.time ?: 0
    }

    private fun mostrarMedicamentos(medicamentosList: List<MedicamentoHorario>){

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = MedicamentoAdapter(medicamentosList)
        recyclerView.adapter = adapter

    }



}