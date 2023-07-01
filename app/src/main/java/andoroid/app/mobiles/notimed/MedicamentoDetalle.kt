package andoroid.app.mobiles.notimed

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MedicamentoDetalle : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalle_medicamentos)

        //SETUP
        setup()
    }

    private fun setup(){

        //variables desde el item medicamento
        val name = intent.getStringExtra("name")
        val dosis = intent.getStringExtra("dosis")
        val stock = intent.getStringExtra("stock")

        //controles
        val nameView = findViewById<TextView>(R.id.nombreDetalle)
        val dosisView = findViewById<TextView>(R.id.dosisDetalle)
        val stockView = findViewById<TextView>(R.id.stockDetalle)

        nameView.text = name
        dosisView.text = dosis
        stockView.text = stock
    }
}