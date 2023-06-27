package andoroid.app.mobiles.notimed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
    }

    fun registrarse(view: View) {
        val registrarse = Intent(this, Registro::class.java)
        startActivity(registrarse)
    }

}