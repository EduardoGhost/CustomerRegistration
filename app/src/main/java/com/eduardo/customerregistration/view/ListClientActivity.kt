package com.eduardo.customerregistration.view

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eduardo.customerregistration.R
import com.eduardo.customerregistration.model.ClienteEntity
import com.eduardo.customerregistration.model.dataBase.local.remote.Dao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class listClientActivity : AppCompatActivity() {
    private var listClientes: List<ClienteEntity> = ArrayList()
    private var recyclerViewClientes: RecyclerView? = null
    private var adapter: Adapter? = null

    fun initViews() {
        recyclerViewClientes = findViewById<View>(R.id.idRecyclerViewListedClients) as RecyclerView
    }

    fun loadListClients() {
        val daoCliente = Dao(baseContext)
        val activity = this@listClientActivity // Referência à atividade atual

        CoroutineScope(Dispatchers.Main).launch {
            // Carrega a lista de clientes de forma assíncrona
            listClientes = withContext(Dispatchers.IO) {
                daoCliente.listClientes()
            }
            // Atualiza o RecyclerView com a lista de clientes carregada
            adapter = Adapter(listClientes as MutableList<ClienteEntity>, applicationContext)
            activity.recyclerViewClientes?.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        applicationContext,
                        LinearLayout.VERTICAL
                    )
                )
                adapter = activity.adapter
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_client)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initViews()
    }

    override fun onStart() {
        loadListClients()
        super.onStart()
    }
}