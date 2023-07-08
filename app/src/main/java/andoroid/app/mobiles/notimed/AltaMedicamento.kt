package andoroid.app.mobiles.notimed

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AltaMedicamento : AppCompatActivity() {

    private val horasList = mutableListOf<String>()
    private lateinit var horasAdapter: ListaHorasAdapter

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
        val timePicker: TimePicker = findViewById(R.id.timePicker)

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
                                setearAlarmas()
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

        agregarBtn.setOnClickListener {
            val horaSeleccionada = String.format(
                "%02d:%02d",
                timePicker.currentHour,
                timePicker.currentMinute
            )
            horasList.add(horaSeleccionada)
            setupRecyclerView(horasList)
        }
    }
    private fun setearAlarmas() {
        for (horaSeleccionada in horasList) {
            val formatoHora = SimpleDateFormat("HH:mm", Locale.getDefault())
            val calendar = Calendar.getInstance()
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            try {
                val date = formatoHora.parse(horaSeleccionada)
                calendar.time = date
                val intent = Intent(this, AlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

                Toast.makeText(
                    this,
                    "Alarma configurada para $horaSeleccionada",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }
    private fun setupRecyclerView(horasList : List<String>) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyclerView_hora)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ListaHorasAdapter(horasList)
        recyclerView.adapter = adapter
    }
}