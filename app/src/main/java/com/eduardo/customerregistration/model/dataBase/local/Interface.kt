package com.eduardo.customerregistration.model.dataBase.local

import com.eduardo.customerregistration.model.ClienteEntity

interface Interface {
    fun cadastroCliente(mCliente: ClienteEntity): Boolean
    fun alterarCliente(mCliente: ClienteEntity): Boolean
    fun deleteCliente(mCliente: ClienteEntity): Boolean
}
