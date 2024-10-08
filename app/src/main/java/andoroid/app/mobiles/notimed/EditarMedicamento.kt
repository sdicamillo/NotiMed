package andoroid.app.mobiles.notimed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        val guardarbtn = findViewById<Button>(R.id.guardarBtn)

        //variables desde el medicamento detalle
        val id = intent.getStringExtra("id")
        val name = intent.getStringExtra("name")
        val dosis = intent.getStringExtra("dosis")
        val stock = intent.getStringExtra("stock")
        val horariosList = intent.getStringArrayListExtra("listaHorarios") as MutableList<String>


        if (horariosList != null) {
            setupRecyclerView(horariosList)
        }

        //controles
        val nameView = findViewById<TextView>(R.id.nombreEditar)
        val dosisView = findViewById<TextView>(R.id.dosisEditar)
        val stockView = findViewById<TextView>(R.id.stockEditar)
        val arrowBack = findViewById<ImageButton>(R.id.backButton)
        val agregarBtn = findViewById<Button>(R.id.agregarBtn)
        val timePicker: TimePicker = findViewById(R.id.timePicker)

        arrowBack.setOnClickListener{
            finish()
        }

        nameView.text = name
        dosisView.text = dosis
        stockView.text = stock

        agregarBtn.setOnClickListener {
            val horaSeleccionada = String.format(
                "%02d:%02d",
                timePicker.currentHour,
                timePicker.currentMinute
            )

            horariosList.add(horaSeleccionada)
            setupRecyclerView(horariosList)
        }

        guardarbtn.setOnClickListener {
            println(id)
            println(name)
            println(dosis)
            println(stock)
            if (id != null && name != null && dosis != null && stock != null) {
                println("perisitir fun")
                persistirMedicamento(id, nameView.text.toString(), dosisView.text.toString(), stockView.text.toString(), horariosList)
            } else{
                println("error")
            }
        }

    }

    private fun setupRecyclerView(horasList : MutableList<String>) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView_hora)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ListaHorasBorrarAdapter(horasList)
        recyclerView.adapter = adapter
    }

    private fun persistirMedicamento(medId: String, medName:String, medDosis:String, medStock:String, horariosList: MutableList<String> ){

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
            medData["horarios"] = horariosList

            // Guardar el medicamento en la base de datos
            medicamentosRef.setValue(medData)
                .addOnSuccessListener {
                    println("Medicamento agregado correctamente")
                    showMed()
                }
                .addOnFailureListener { exception ->
                    println("Error al agregar el medicamento: $exception")
                }

        } else {
            println("El usuario no ha iniciado sesión")
        }

    }

    private fun showMed(){
        val intent = Intent(this, ListaMedicamentos::class.java)
        startActivity(intent)
        finish()
    }



}