package com.eduardo.customerregistration.view

import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eduardo.customerregistration.R
import com.eduardo.customerregistration.model.ClienteEntity
import com.eduardo.customerregistration.model.dataBase.local.remote.Dao
import com.eduardo.customerregistration.utils.DateUtils
import com.eduardo.customerregistration.viewModel.CadastroViewModel
import com.squareup.picasso.Picasso
import com.vicmikhailau.maskededittext.MaskedFormatter
import com.vicmikhailau.maskededittext.MaskedWatcher
import java.io.File

class CadastroActivity : AppCompatActivity() {

    private lateinit var cadastroViewModel: CadastroViewModel
    private lateinit var editTextName: EditText
    private lateinit var editTextUserName: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editAdress: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextCpfOrCnpj: EditText
    private lateinit var botaoAdicionarCliente: Button
    private lateinit var radioGroup: RadioGroup
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioCPF: RadioButton
    private lateinit var radioCNPJ: RadioButton
    private lateinit var radioMasculino: RadioButton
    private lateinit var radioFeminino: RadioButton
    private lateinit var imageViewPhoto: ImageView
    private lateinit var selectedImageUri: Uri

    companion object {
        private const val PICK_PHOTO_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        initializeComponents()

        cadastroViewModel = ViewModelProvider(this).get(CadastroViewModel::class.java)

        cadastroViewModel.cadastroResult.observe(this) { resultado ->
            if (resultado) {
                // Sucesso no cadastro
                showToast("Cliente cadastrado com sucesso!")
                clearFields()
            }
        }

        // Observa mensagens de erro
        cadastroViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                showToast(errorMessage)
            }
        }

        editTextCpfOrCnpj = findViewById(R.id.editTextCpfOrCnpj)

        // Criar método separado para evitar a duplicação
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioCPF) {
                // CPF foi selecionado
                editTextCpfOrCnpj.hint = "CPF"
                setMaskCpf() // Se necessário, aplique a máscara de CPF
            } else if (checkedId == R.id.radioCNPJ) {
                // CNPJ foi selecionado
                editTextCpfOrCnpj.hint = "CNPJ"
                setMaskCnpj() // Se necessário, aplique a máscara de CNPJ
            }
        }

        imageViewPhoto = findViewById(R.id.imageViewProfile)
        imageViewPhoto.setOnClickListener {
            openGallery()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_PHOTO_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            val imagePath = getRealPathFromUri(selectedImageUri)

            if (imagePath != null) {
                val selectedBitmap = BitmapFactory.decodeFile(imagePath)
                imageViewPhoto.setImageBitmap(selectedBitmap)
            } else {
                Toast.makeText(this, "Falha ao obter caminho do arquivo da imagem", Toast.LENGTH_SHORT).show()
            }
            Picasso.get().load(File(imagePath)).into(imageViewPhoto)
        }
    }

    private fun getRealPathFromUri(contentUri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri, projection, null, null, null)

        if (cursor == null) {
            return contentUri.path!!
        }

        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val filePath: String = cursor.getString(column_index)
        cursor.close()

        return filePath
    }

    private fun initializeComponents() {
        editTextName = findViewById(R.id.editTextName)
        editTextUserName = findViewById(R.id.editTextUserName)
        editTextPassword = findViewById(R.id.editTextPassword)
        editAdress = findViewById(R.id.editTextAdress)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextDate = findViewById(R.id.editTextDate)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        botaoAdicionarCliente = findViewById(R.id.idBtnCadastro)
        radioGroup = findViewById(R.id.radioGroup)
        radioCPF = findViewById(R.id.radioCPF)
        radioCNPJ = findViewById(R.id.radioCNPJ)
        editTextCpfOrCnpj = findViewById(R.id.editTextCpfOrCnpj)

        // Configurar o RadioGroup para inicialmente desmarcar todos os RadioButtons
        radioGroup.clearCheck()

        // Configurar o ouvinte de alteração no RadioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == R.id.radioCPF) {
                // CPF foi selecionado
                editTextCpfOrCnpj.hint = "CPF"
                setMaskCpf() // Se necessário, aplique a máscara de CPF
            } else if (checkedId == R.id.radioCNPJ) {
                // CNPJ foi selecionado
                editTextCpfOrCnpj.setHint("CNPJ")
                setMaskCnpj() // Se necessário, aplique a máscara de CNPJ
            }
        }
        radioGroupGender.clearCheck()
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioMasculino = findViewById(R.id.radioMasculine)
        radioFeminino = findViewById(R.id.radioFeminine)
        imageViewPhoto = findViewById(R.id.imageViewProfile)
    }

    fun cadastroCliente(view: View?) {
        val genderSelect = genderSelect
        val dao = Dao(baseContext)
        val setCliente = ClienteEntity()

      //   Obtém o CPF digitado e aplica a desmascaração
        val cpfOrCnpj = editTextCpfOrCnpj!!.text.toString()
        val cpfUnmasked = unmask(cpfOrCnpj)
        setCliente.name = getText(editTextName)
        setCliente.userName = getText(editTextUserName)
        setCliente.password = getText(editTextPassword)
        setCliente.adress = getText(editAdress)
        setCliente.email = getText(editTextEmail)
        setCliente.date =
            DateUtils.getTimestampFromDateString(getText(editTextDate))
        setCliente.cpfOrCnpj = cpfUnmasked
        setCliente.gender = genderSelect
        val imagePath = getRealPathFromUri(selectedImageUri)
        setCliente.picture = imagePath

        // Chama o método da ViewModel para cadastrar o cliente
        cadastroViewModel!!.cadastrarCliente(dao, setCliente)
        Log.i(" Nome: ", setCliente.name!!)
        val formattedDate = DateUtils.formatDateFromTimestamp(setCliente.date!!)
        Log.i(
            "Resultado: ", " Nome: " + setCliente.name + " UserName: "
                    + setCliente.userName + " Senha: " + setCliente.password + " Endereço: " + setCliente.adress
                    + " Email: " + setCliente.email + " Data de Nascimento: " + formattedDate + " Documento: " + setCliente.cpfOrCnpj
                    + " Genero: " + setCliente.gender + " Image: " + setCliente.picture
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getText(editText: EditText?): String {
        return editText!!.text.toString()
    }

    fun clearFields() {
        editTextName!!.setText("")
        editTextUserName!!.setText("")
        editTextPassword!!.setText("")
        editAdress!!.setText("")
        editTextEmail!!.setText("")
        editTextDate!!.setText("")
        editTextCpfOrCnpj!!.setText("")
        imageViewPhoto!!.setImageBitmap(null)
    }

    // Método para definir a máscara do CPF
    private fun setMaskCpf() {
        val formatter = MaskedFormatter("###.###.###-##")
        editTextCpfOrCnpj!!.addTextChangedListener(MaskedWatcher(formatter, editTextCpfOrCnpj!!))
    }

    // Método para remover a máscara do CPF (retorna apenas os números)
    private fun unmask(cpf: String): String {
        return cpf.replace("[^0-9]".toRegex(), "")
    }

    // Método para definir a máscara do CNPJ
    private fun setMaskCnpj() {
        val formatter = MaskedFormatter("##.###.###/####-##")
        editTextCpfOrCnpj!!.addTextChangedListener(MaskedWatcher(formatter, editTextCpfOrCnpj!!))
    }

    private val genderSelect: String
        private get() {
            val radioButtonId = radioGroupGender!!.checkedRadioButtonId
            return when (radioButtonId) {
                R.id.radioMasculine -> "Masculino"
                R.id.radioFeminine -> "Feminino"
                else -> "Outro"
            }
        }

}
