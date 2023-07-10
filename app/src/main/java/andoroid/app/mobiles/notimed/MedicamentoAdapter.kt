package andoroid.app.mobiles.notimed

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicamentoAdapter(private val medicamentos: List<Medicamento>) : RecyclerView.Adapter<MedicamentoAdapter.MedicamentoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_medicamento, parent, false)
        return MedicamentoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedicamentoViewHolder, position: Int) {
        val medicamento = medicamentos[position]

        holder.bind(medicamento)
    }

    override fun getItemCount(): Int {
        return medicamentos.size
    }

    inner class MedicamentoViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val nombreTextView: TextView = itemView.findViewById(R.id.nombreMed)
        val dosisTextView: TextView = itemView.findViewById(R.id.dosisMed)
        val stockTextView: TextView = itemView.findViewById(R.id.stockMed)
        val horaTextView: TextView = itemView.findViewById(R.id.hora)


        fun bind(medicamento: Medicamento){
            nombreTextView.text = medicamento.name
            dosisTextView.text = medicamento.dosis
            stockTextView.text = medicamento.stock
            //horaTextView.text = medicamento.hora

            //abre el detalle del medicamento
            itemView.setOnClickListener{
                val context = itemView.context
                val intent = Intent(context, MedicamentoDetalle::class.java)
                intent.putExtra("id", medicamento.id)
                intent.putExtra("name", medicamento.name)
                intent.putExtra("dosis", medicamento.dosis)
                intent.putExtra("stock", medicamento.stock)
                context.startActivity(intent)
            }

        }
    }

}