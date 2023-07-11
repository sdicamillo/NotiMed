package andoroid.app.mobiles.notimed

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

@SuppressLint("Registered")

class AlarmService: Service() {
    companion object {
        private const val CHANNEL_ID = "ForegroundServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        // Aquí puedes realizar cualquier inicialización necesaria para tu servicio
    }

    fun Alarma(context: Context?, intent: Intent?){
        val message = intent?.getStringExtra("ExtraMessage") ?: return
        val newIntent = Intent(context, Alarm_Launch::class.java).apply {
            putExtra("ExtraMessage", message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context?.startActivity(newIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification: Notification = createNotification()
        startForeground(1, notification)
        // Aquí puedes realizar la tarea que deseas ejecutar en segundo plano

        // Si deseas detener el servicio después de completar la tarea, utiliza el siguiente código:
        // stopForeground(true)
        // stopSelf()

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Executing background task")
            .setSmallIcon(androidx.appcompat.R.drawable.abc_btn_check_material_anim)
        return notificationBuilder.build()
    }
}