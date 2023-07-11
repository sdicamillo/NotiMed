package andoroid.app.mobiles.notimed

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getStringExtra("ExtraMessage") ?: return
        val newIntent = Intent(context, Alarm_Launch::class.java).apply {
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
                            if (stockActual/dosis <= 5){
                                println("Falta Stock")
                                enviarNotificacionAsync(context)
                            }
                            else{
                                println("el stock es suficiente")
                            }
                            // Actualizar el campo "stock" en Firebase
                            medicamentoRef.child("stock").setValue(stockActual.toString())
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
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(newIntent)
        }
    }

fun enviarNotificacionAsync(context: Context?) {
    CoroutineScope(Dispatchers.Main).launch {
        enviarNotificacion(context)
    }
}

fun enviarNotificacion(context: Context?) {
    val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "mi_canal"
    val channelName = "Mi Canal"
    val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
    notificationManager.createNotificationChannel(channel)
    val intent = Intent(context, ListaMedicamentos::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("Alerta")
        .setContentText("El stock del medicamento es bajo")
        .setSmallIcon(R.drawable.pill_capsule)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    notificationManager.notify(0, notification)
}