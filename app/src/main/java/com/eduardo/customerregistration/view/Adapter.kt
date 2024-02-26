package com.eduardo.customerregistration.view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.eduardo.customerregistration.R
import com.eduardo.customerregistration.model.ClienteEntity
import com.eduardo.customerregistration.model.dataBase.local.remote.Dao

class Adapter(private val listClientes: MutableList<ClienteEntity>, private val context: Context) :
    RecyclerView.Adapter<Adapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ClienteViewHolder {
        val listaDeClientes = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.adapter, viewGroup, false
        )
        return ClienteViewHolder(listaDeClientes)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, i: Int) {
        val cliente = listClientes[i]
        holder.nome.text = cliente.name
        holder.nome.setOnClickListener { v ->
            Toast.makeText(context, "details client", Toast.LENGTH_LONG).show()
            val getDetails = listClientes[i]
            val intent = Intent(context.applicationContext, DetailsClientActivity::class.java)
            intent.putExtra("keyDetails", getDetails)
            v.context.startActivity(intent)
        }
        holder.toAlter.setOnClickListener { v ->
            Toast.makeText(context, "edit client", Toast.LENGTH_LONG).show()
            val editCliente = listClientes[i]
            val intent = Intent(v.context, EditClientActivity::class.java)
            intent.putExtra("keyEdit", editCliente)
            v.context.startActivity(intent)
        }
        holder.delete.setOnClickListener {
            val builder = AlertDialog.Builder(holder.activity)
            builder.setCancelable(false)
            builder.setTitle("Excluir Cliente?")
            builder.setPositiveButton("Confirmar") { dialog, which ->
                val daoCliente = Dao(context)
                Toast.makeText(context, "Usuario Deletado", Toast.LENGTH_LONG).show()
                val deleteCliente = listClientes[i]
                deleteCliente.codeId
                daoCliente.deleteCliente(deleteCliente)
                listClientes.removeAt(i)
                notifyItemRemoved(i)
                notifyItemRangeChanged(i, itemCount)
            }
            builder.setNegativeButton("Cancelar", null)
            builder.create().show()
        }
    }

    override fun getItemCount(): Int {
        return listClientes.size
    }

    inner class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var activity: Context
        var nome: TextView
        var toAlter: TextView
        var delete: TextView

        init {
            activity = itemView.context
            nome = itemView.findViewById(R.id.idNomeCliente)
            toAlter = itemView.findViewById(R.id.idTxtAlter)
            delete = itemView.findViewById(R.id.idTxtDelete)
        }
    }
}