package andoroid.app.mobiles.notimed

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AlarmReceiver : BroadcastReceiver() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getStringExtra("ExtraMessage") ?: return
        val database = FirebaseDatabase.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        if (uid != null) {
            val medicamentoRef = database.getReference("users").child(uid).child("medicamentos").child(id)

            medicamentoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val stock = dataSnapshot.child("stock").getValue(String::class.java)?.toIntOrNull() ?: 0
                        val dosis = dataSnapshot.child("dosis").getValue(String::class.java)?.toIntOrNull() ?: 0
                        val stockActual = stock - dosis
                        val newIntent = Intent(context, Alarm_Launch::class.java)
                        newIntent.flags= Intent.FLAG_ACTIVITY_NEW_TASK
                        if (stockActual / dosis <= 5) {
                            newIntent.putExtra("FaltaStock", "true")
                        } else {
                            newIntent.putExtra("FaltaStock", "false")
                        }
                        val nombre = dataSnapshot.child("name").getValue(String::class.java)
                        newIntent.putExtra("medicamento", nombre)
                        context?.startActivity(newIntent)
                        val stockInt = stockActual.toString()
                        // Actualizar el campo "stock" en Firebase
                        medicamentoRef.child("stock").setValue(stockInt)
                            .addOnSuccessListener {
                                println("Stock del medicamento actualizado correctamente")
                            }
                            .addOnFailureListener { exception ->
                                println("Error al actualizar el stock del medicamento: $exception")
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("Error al obtener el medicamento: $databaseError")
                }
            })
        }
    }
}
