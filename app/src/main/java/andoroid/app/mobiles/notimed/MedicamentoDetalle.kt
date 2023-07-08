package andoroid.app.mobiles.notimed

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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
        val editarBtn = findViewById<Button>(R.id.editarBtn)
        val backArrow = findViewById<ImageButton>(R.id.backButton)
        //variables desde el item medicamento
        val id = intent.getStringExtra("id")
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

        //cambio de pantalla a editar
        editarBtn.setOnClickListener{
            val intent = Intent(this, EditarMedicamento::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)
            intent.putExtra("dosis", dosis)
            intent.putExtra("stock", stock)
            startActivity(intent)
        }
        backArrow.setOnClickListener{
            finish()
        }

    }
}