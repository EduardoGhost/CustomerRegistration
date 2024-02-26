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

class listClientActivity : AppCompatActivity() {
    private var listClientes: List<ClienteEntity> = ArrayList()
    private var recyclerViewClientes: RecyclerView? = null
    private var adapter: Adapter? = null

    fun initViews() {
        recyclerViewClientes = findViewById<View>(R.id.idRecyclerViewListedClients) as RecyclerView
    }

    fun loadListclients() {
        val daoCliente = Dao(baseContext)
        listClientes = daoCliente.listClientes()
        adapter = Adapter(listClientes as MutableList<ClienteEntity>, applicationContext)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerViewClientes!!.layoutManager = layoutManager
        recyclerViewClientes!!.setHasFixedSize(true)
        recyclerViewClientes!!.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayout.VERTICAL
            )
        )
        recyclerViewClientes!!.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_client)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        initViews()
    }

    override fun onStart() {
        loadListclients()
        super.onStart()
    }
}