package com.eduardo.customerregistration.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardo.customerregistration.model.dataBase.local.remote.Dao
import java.lang.Exception

class DetailViewModel : ViewModel() {
    val detailResult = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    fun detailCliente(dao: Dao, clienteId: Long) {
        try {
            val cliente = dao.getClienteById(clienteId)
            if (cliente != null) {
                // Detalhes obtidos com sucesso
                detailResult.setValue(true)
            } else {
                // Não foi possível obter os detalhes do cliente
                errorMessage.setValue("Detalhes do cliente não encontrados.")
            }
        } catch (e: Exception) {
            // Lidar com exceções, por exemplo, erro no acesso ao banco de dados
            errorMessage.setValue("Erro ao obter detalhes do cliente: " + e.message)
        }
    }
}