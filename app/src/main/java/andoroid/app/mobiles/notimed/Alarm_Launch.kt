package andoroid.app.mobiles.notimed

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Alarm_Launch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_notify)
        val nombreMed =  intent.getStringExtra("medicamento")
        val textViewMessage: TextView = findViewById(R.id.message)
        textViewMessage.text = "Es hora de tomar $nombreMed"
        val cancelBTN: Button = findViewById(R.id.CancelBTN)
        cancelBTN.setOnClickListener{
            val bajoStock = intent.getStringExtra("FaltaStock")
            println(nombreMed)
            println(bajoStock)
            if (bajoStock == "true" && nombreMed != null){
                GlobalScope.launch{
                    enviarNotificacion(this@Alarm_Launch, nombreMed)
                }
            }
            onPause()
            finish()
        }
    }
    var ringtone: Ringtone? = null
    override fun onResume() {
        super.onResume()
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
        ringtone?.play()
    }
    override fun onPause() {
        super.onPause()
        ringtone?.stop()
    }
    private fun enviarNotificacion(context: Context, nombre: String){
        println("Notificado")
        val canalID= "canalID"
        val canalNombre= "bajoStockChannel"
        crearCanalNotificacion(canalID, canalNombre, context)
        val resultIntent = Intent(context, ListaMedicamentos::class.java)
        val resultPendingIntent = TaskStackBuilder.create(context).run{
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }
        val notificacion = NotificationCompat.Builder(context, canalID).also{
            it.setContentTitle("Notificacion")
            it.setContentText("El stock de $nombre es bajo!")
            it.setSmallIcon(R.drawable.pill_capsule)
            it.priority = NotificationCompat.PRIORITY_HIGH
            it.setContentIntent(resultPendingIntent)
            it.setAutoCancel(true)
        }.build()

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(0, notificacion)
    }

    private fun crearCanalNotificacion(canalId: String, canalNombre: String, context: Context){
        val canalImportancia = NotificationManager.IMPORTANCE_HIGH
        val canal = NotificationChannel(canalId, canalNombre, canalImportancia)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(canal)
    }
}