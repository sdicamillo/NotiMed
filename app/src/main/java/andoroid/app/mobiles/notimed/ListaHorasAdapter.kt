package andoroid.app.mobiles.notimed

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Time
import java.util.Timer

class ListaHorasAdapter(private val horas: List<String>) : RecyclerView.Adapter<ListaHorasAdapter.HoraViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoraViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hora, parent, false)
        return HoraViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HoraViewHolder, position: Int) {
        val hora = horas[position]
        holder.bind(hora)
    }

    override fun getItemCount(): Int {
        return horas.size
    }

    inner class HoraViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val horaTextView: TextView = itemView.findViewById(R.id.hora)


        fun bind(hora: String){
            horaTextView.text = hora.toString()
        }
    }

}