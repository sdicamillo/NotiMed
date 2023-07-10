package andoroid.app.mobiles.notimed

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Button

class Alarm_Launch : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.alarm_notify)

        val message = intent.getStringExtra("ExtraMessage")
        val textViewMessage: TextView = findViewById(R.id.message)
        textViewMessage.text = message
        val cancelBTN: Button = findViewById(R.id.CancelBTN)
        cancelBTN.setOnClickListener{
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
}