package andoroid.app.mobiles.notimed

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.sql.Time
import java.util.Timer

class ListaHorasBorrarAdapter(private val horasParametro: List<String>) : RecyclerView.Adapter<ListaHorasBorrarAdapter.HoraViewHolder>() {

    private val horas = horasParametro.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoraViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hora_borrar, parent, false)
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
        val borrarBtn : ImageButton = itemview.findViewById(R.id.borrarBtn)


        fun bind(hora: String){
            horaTextView.text = hora.toString()

            borrarBtn.setOnClickListener {
                val posicion = adapterPosition
                if (posicion != RecyclerView.NO_POSITION) {
                    // Obtener la hora en la posición correspondiente
                    val hora = horas[posicion]
                    // Realizar la lógica de eliminación de la hora de la lista
                    eliminarHora(posicion)
                }
            }

        }

        fun eliminarHora(posicion: Int) {
            horas.apply {
                removeAt(posicion)
                notifyDataSetChanged()
            }
        }

    }

}