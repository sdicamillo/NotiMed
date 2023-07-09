package andoroid.app.mobiles.notimed

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.ZoneId
import java.util.Calendar

class AndroidAlarmScheduler(private val context: Context) : AlarmScheduler {
    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ExtraMessage", item.message)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, item.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, item.hour)
        calendar.set(Calendar.MINUTE, item.minute)
        calendar.set(Calendar.SECOND, 0)
        val triggerTimeMillis = calendar.timeInMillis
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
        }
    }
    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(context, item.hashCode(), Intent(context, AlarmReceiver::class.java), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        )
    }
}

