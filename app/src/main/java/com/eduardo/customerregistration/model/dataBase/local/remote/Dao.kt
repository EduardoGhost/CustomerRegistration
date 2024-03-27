package com.eduardo.customerregistration.model.dataBase.local.remote

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import android.widget.Toast
import com.eduardo.customerregistration.model.ClienteEntity
import com.eduardo.customerregistration.model.dataBase.local.Interface
import java.lang.Exception
import java.util.regex.Pattern
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Dao(context: Context) : Interface {
    //escrever e ler os dados
    var sqlWrite: SQLiteDatabase
    var sqlRead: SQLiteDatabase
    var context: Context

    init {
        val base = SQLite(context)
        sqlWrite = base.writableDatabase
        sqlRead = base.readableDatabase
        this.context = context
    }

    override fun cadastroCliente(mCliente: ClienteEntity): Boolean {
        val senha = mCliente.password

        return if (!isValidEmail(mCliente.email!!)) {
            showToast("Email inválido. Insira um email válido.")
            false
        } else if (mCliente.name?.length ?: 0 <= 15) {
            showToast("Nome deve ter mais de 15 caracteres.")
            false
        } else if (checkIfUsernameExists(mCliente.userName!!)) {
            showToast("Username já está em uso. Escolha outro.")
            false
        } else if (mCliente.calculateAge() < 18) {
            showToast("É necessário ter mais de 18 anos para se cadastrar.")
            false
        } else if (!isPasswordValid(senha!!)) {
            showToast("Senha inválida. Deve ter pelo menos 8 caracteres, um número e uma letra maiúscula.")
            false
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                val success = try {
                    val values = ContentValues().apply {
                        put("clienteNome", mCliente.name)
                        put("clienteUserName", mCliente.userName)
                        put("password", senha)
                        put("adress", mCliente.adress)
                        put("email", mCliente.email)
                        put("date", mCliente.date)
                        put("cpfOrCnpj", mCliente.cpfOrCnpj)
                        put("gender", mCliente.gender)
                        put("picture", mCliente.picture)
                    }
                    sqlWrite.insert(SQLite.TABELA_CLIENTE, null, values)
                    Log.i("Cliente Dados", SQLite.TABELA_CLIENTE + values)
                    true
                } catch (e: SQLiteException) {
                    e.printStackTrace()
                    false
                }
                withContext(Dispatchers.Main) {
                    if (success) {
                        Log.d(TAG, "Cadastro realizado com sucesso!")

                    } else {
                        Log.e(TAG, "Erro ao realizar o cadastro!")

                    }
                }
            }
            true
        }
    }

    override fun alterarCliente(mCliente: ClienteEntity): Boolean {
        val senha = mCliente.password

        //validador de email
        return if (isValidEmail(mCliente.email!!)) {

            //verifica se o nome tem mais de 15 caracteres
            if (mCliente.name?.length ?: 0 <= 15) {
                showToast("Nome deve ter mais de 15 caracteres.")
                return false
            }
            //verifica se é maior do que 18 anos
            val idade = mCliente.calculateAge()
            if (idade < 18) {
                showToast("É necessário ter mais de 18 anos para se cadastrar.")
                return false
            }
            if (isPasswordValid(senha!!)) {
                val values = ContentValues()
                values.put("clienteNome", mCliente.name)
                values.put("clienteUserName", mCliente.userName)
                values.put("password", mCliente.password)
                values.put("adress", mCliente.adress)
                values.put("email", mCliente.email)
                values.put("date", mCliente.date)
                values.put("cpfOrCnpj", mCliente.cpfOrCnpj)
                //    values.put("gender", mCliente.getGender());
                //  values.put("picture", mCliente.getPicture());
                try {
                    val id = arrayOf(mCliente.codeId.toString())
                    sqlWrite.update(SQLite.TABELA_CLIENTE, values, "cliCodigo = ?", id)
                    sqlWrite.close()
                    true
                } catch (e: Exception) {
                    Log.i("Informação: ", "Erro ao atualizar dados: " + e.message)
                    false
                }
            } else {
                showToast("Senha inválida. Deve ter pelo menos 8 caracteres, um número e uma letra maiúscula.")
                false
            }
        } else {
            showToast("Email inválido. Insira um email válido.")
            false
        }
    }

    override fun deleteCliente(mCliente: ClienteEntity): Boolean {
        return try {
            val id = arrayOf(mCliente.codeId.toString())
            sqlWrite.delete(SQLite.TABELA_CLIENTE, "cliCodigo = ?", id)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    @SuppressLint("Range")
    fun listClientes(): List<ClienteEntity> {
        val listClientes: MutableList<ClienteEntity> = ArrayList()
        val sqlSelect = "select * from " + SQLite.TABELA_CLIENTE + ";"
        val cursor = sqlRead.rawQuery(sqlSelect, null)
        while (cursor.moveToNext()) {
            val cliente = ClienteEntity()
            val codigo = cursor.getLong(cursor.getColumnIndexOrThrow("cliCodigo"))
            var nome: String?
            var userName: String?
            var password: String?
            var adress: String?
            var email: String?
            var date: String
            var cpfOrCnpj: String?
            var gender: String?
            var picture: String?
            nome = cursor.getString(cursor.getColumnIndex("clienteNome"))
            userName = cursor.getString(cursor.getColumnIndex("clienteUserName"))
            password = cursor.getString(cursor.getColumnIndex("password"))
            adress = cursor.getString(cursor.getColumnIndex("adress"))
            email = cursor.getString(cursor.getColumnIndex("email"))
            date = cursor.getLong(cursor.getColumnIndex("date")).toString()
            cpfOrCnpj = cursor.getString(cursor.getColumnIndex("cpfOrCnpj"))
            gender = cursor.getString(cursor.getColumnIndex("gender"))
            picture = cursor.getString(cursor.getColumnIndex("picture"))
            cliente.codeId = codigo
            cliente.name = nome
            cliente.userName = userName
            cliente.password = password
            cliente.adress = adress
            cliente.email = email
            cliente.date = date.toLong()
            cliente.cpfOrCnpj = cpfOrCnpj
            cliente.gender = gender
            cliente.picture = picture
            listClientes.add(cliente)
        }
        return listClientes
    }

    @SuppressLint("Range")
    fun getClienteById(clienteId: Long): ClienteEntity? {
        val sqlSelect = "SELECT * FROM " + SQLite.TABELA_CLIENTE + " WHERE cliCodigo = ?;"
        val selectionArgs = arrayOf(clienteId.toString())
        val cursor = sqlRead.rawQuery(sqlSelect, selectionArgs)
        var cliente: ClienteEntity? = null
        if (cursor != null && cursor.moveToFirst()) {
            cliente = ClienteEntity()
            cliente.codeId = cursor.getLong(cursor.getColumnIndexOrThrow("cliCodigo"))
            cliente.name = cursor.getString(cursor.getColumnIndex("clienteNome"))
            cliente.userName = cursor.getString(cursor.getColumnIndex("clienteUserName"))
            cliente.password = cursor.getString(cursor.getColumnIndex("password"))
            cliente.adress = cursor.getString(cursor.getColumnIndex("adress"))
            cliente.email = cursor.getString(cursor.getColumnIndex("email"))
            cliente.date = cursor.getLong(cursor.getColumnIndex("date"))
            cliente.cpfOrCnpj = cursor.getString(cursor.getColumnIndex("cpfOrCnpj"))
            cliente.picture = cursor.getString(cursor.getColumnIndex("picture"))
            cursor.close()
        }
        return cliente
    }

    //metodo para checkar o userName existente
    private fun checkIfUsernameExists(username: String): Boolean {
        val sqlSelect = "select * from " + SQLite.TABELA_CLIENTE + " where clienteUserName = ?"
        val cursor = sqlRead.rawQuery(sqlSelect, arrayOf(username))
        val usernameExists = cursor.count > 0
        cursor.close()
        return usernameExists
    }

    //metodo para validar senha
    fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}\$"
        val pattern = Regex(passwordRegex)
        return pattern.matches(password)
    }

    //metodo para validar email
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
        val pattern = Pattern.compile(emailRegex)
        return pattern.matcher(email).matches()
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
