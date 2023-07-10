package andoroid.app.mobiles.notimed

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("ExtraMessage") ?: return
        val newIntent = Intent(context, Alarm_Launch::class.java).apply {
            putExtra("ExtraMessage", message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context?.startActivity(newIntent)
    }
}
