package com.eduardo.customerregistration.model.dataBase.local.remote

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception

class SQLite(context: Context?) : SQLiteOpenHelper(context, BASEDADOS, null, VERSAO_BANCO) {
    override fun onCreate(db: SQLiteDatabase) {
        val sqlCliente =
            "create table if not exists " + TABELA_CLIENTE + "(cliCodigo integer primary key autoincrement," +
                    "clienteNome text not null, clienteUserName text not null, password text not null, adress text not null," +
                    " email text not null, date Long not null, cpfOrCnpj text not null, gender text not null, picture text not null)"
        try {
            db.execSQL(sqlCliente)
            Log.i("Banco de dados criado", "Banco de dados: $sqlCliente")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("Erro", "Banco de dados: ")
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("create table if not exists " + TABELA_CLIENTE)
        onCreate(db)
    }

    companion object {
        const val BASEDADOS = "baseDados.db"
        const val TABELA_CLIENTE = "tb_cliente"
        private const val VERSAO_BANCO = 1
    }
}
