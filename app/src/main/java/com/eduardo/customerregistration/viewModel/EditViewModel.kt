package com.eduardo.customerregistration.viewModel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eduardo.customerregistration.model.ClienteEntity
import com.eduardo.customerregistration.model.dataBase.local.remote.Dao

class EditViewModel : ViewModel() {
    val editResult = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun alterarCliente(dao: Dao, cliente: ClienteEntity?) {
        // Lógica de validação e cadastro
        if (isValidCliente(cliente)) {
            val resultado: Boolean = dao.alterarCliente(cliente!!)
            if (resultado) {
                editResult.setValue(true)
            } else {
                errorMessage.setValue("Erro ao atualizar cliente.")
            }
        } else {
            errorMessage.setValue("Dados do cliente inválidos.")
        }
    }

    private fun isValidCliente(cliente: ClienteEntity?): Boolean {

        // Cliente não pode ser nulo
        if (cliente == null) {
            return false
        }

        // nome não pode está vazio
        return !TextUtils.isEmpty(cliente.name)
    }
}