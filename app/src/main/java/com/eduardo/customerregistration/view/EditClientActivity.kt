package com.eduardo.customerregistration.view

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import com.eduardo.customerregistration.R
import com.eduardo.customerregistration.model.ClienteEntity
import com.eduardo.customerregistration.model.dataBase.local.remote.Dao
import com.eduardo.customerregistration.utils.DateUtils
import com.eduardo.customerregistration.viewModel.EditViewModel

class EditClientActivity : AppCompatActivity() {
    private var editTextNome: EditText? = null
    private var editTextUserName: EditText? = null
    private var editTextPassword: EditText? = null
    private var editTextAdress: EditText? = null
    private var editTextEmail: EditText? = null
    private var editTextDate: EditText? = null
    private var editTextCpfOrCnpj: EditText? = null
    private var btnSave: Button? = null
    private var editViewModel: EditViewModel? = null
    private var detalhes: ClienteEntity? = ClienteEntity()
    fun initViews() {
        editTextNome = findViewById(R.id.idNome)
        editTextUserName = findViewById(R.id.idUserName)
        editTextPassword = findViewById(R.id.idPassword)
        editTextAdress = findViewById(R.id.idAdress)
        editTextEmail = findViewById(R.id.idEmail)
        editTextDate = findViewById(R.id.idDate)
        editTextCpfOrCnpj = findViewById(R.id.idCpfOrCnpj)
        btnSave = findViewById(R.id.idBtnSave)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_client)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Inicialize o EditViewModel
        editViewModel = ViewModelProvider(this).get(EditViewModel::class.java)

        // Observa o resultado da edição e mensagens de erro
        editViewModel!!.editResult.observe(this) { editResult: Boolean ->
            if (editResult) {
                showToast("Dados atualizados com sucesso!")
                finish()
            }
        }
        editViewModel!!.errorMessage.observe(this) { errorMessage: String ->
            if (!errorMessage.isEmpty()) {
                showToast(errorMessage)
            }
        }
        initViews()
        detalhes = intent.getSerializableExtra("keyEdit") as ClienteEntity?
        if (detalhes != null) {
            editTextNome!!.setText(detalhes!!.name)
            editTextUserName!!.setText(detalhes!!.userName)
            editTextPassword!!.setText(detalhes!!.password)
            editTextAdress!!.setText(detalhes!!.adress)
            editTextEmail!!.setText(detalhes!!.email)
            val timestamp = detalhes!!.date
            val formattedDate = DateUtils.formatDateFromTimestamp(timestamp!!)
            editTextDate!!.setText(formattedDate)
            editTextCpfOrCnpj!!.setText(detalhes!!.cpfOrCnpj)

//            String cpfOrCnpj = detalhes.getCpfOrCnpj();
//            if (MaskUtils.isCpf(cpfOrCnpj)) {
//                editTextCpfOrCnpj.setText(MaskUtils.formatCpf(cpfOrCnpj));
//            } else {
//                editTextCpfOrCnpj.setText(MaskUtils.formatCnpj(cpfOrCnpj));
//            }
        } else {
            Toast.makeText(
                this@EditClientActivity,
                "Vazio", Toast.LENGTH_LONG
            ).show()
        }
    }

    fun alterarCliente(view: View?) {
        val setNewNameClient: String
        val setNewUserNameClient: String
        val setNewPassword: String
        val setNewAdress: String
        val setNewEmail: String
        val setNewDate: String
        val setNewCpfOrCnpj: String
        setNewNameClient = editTextNome!!.text.toString()
        setNewUserNameClient = editTextUserName!!.text.toString()
        setNewPassword = editTextPassword!!.text.toString()
        setNewAdress = editTextAdress!!.text.toString()
        setNewEmail = editTextEmail!!.text.toString()
        setNewDate = editTextDate!!.text.toString()
        setNewCpfOrCnpj = editTextCpfOrCnpj!!.text.toString()
        val dao = Dao(baseContext)
        val setAlterar = ClienteEntity()
        setAlterar.codeId = detalhes!!.codeId
        setAlterar.name = setNewNameClient
        setAlterar.userName = setNewUserNameClient
        setAlterar.password = setNewPassword
        setAlterar.adress = setNewAdress
        setAlterar.email = setNewEmail
        setAlterar.date = DateUtils.getTimestampFromDateString(setNewDate)
        setAlterar.cpfOrCnpj = setNewCpfOrCnpj
        editViewModel!!.alterarCliente(Dao(baseContext), setAlterar)
        val resultado = dao.alterarCliente(setAlterar)
        if (resultado) {
            Toast.makeText(
                this@EditClientActivity,
                "Dados atualizados com sucesso!",
                Toast.LENGTH_LONG
            ).show()
            finish()
        } else {
            Toast.makeText(this@EditClientActivity, "Erro ao atualizar dados!", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}