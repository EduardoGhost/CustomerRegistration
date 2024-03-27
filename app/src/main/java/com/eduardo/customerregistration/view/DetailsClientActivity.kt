package com.eduardo.customerregistration.view

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import android.util.Log
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import com.eduardo.customerregistration.R
import com.eduardo.customerregistration.model.ClienteEntity
import com.eduardo.customerregistration.model.dataBase.local.remote.Dao
import com.eduardo.customerregistration.utils.DateUtils
import com.eduardo.customerregistration.utils.MaskUtils
import com.eduardo.customerregistration.viewModel.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class DetailsClientActivity : AppCompatActivity() {
    private var textNome: TextView? = null
    private var textUserName: TextView? = null
    private var textAdress: TextView? = null
    private var textEmail: TextView? = null
    private var textDate: TextView? = null
    private var textCpfOrCnpj: TextView? = null
    private var textGender: TextView? = null
    private var detailViewModel: DetailViewModel? = null
    private var imageViewPhoto: ImageView? = null
    private var detalhes: ClienteEntity? = ClienteEntity()

    fun initViews() {
        textNome = findViewById(R.id.txtNome)
        textUserName = findViewById(R.id.txtUserName)
        textAdress = findViewById(R.id.txtAdress)
        textEmail = findViewById(R.id.txtEmail)
        textDate = findViewById(R.id.txtDate)
        textCpfOrCnpj = findViewById(R.id.txtCpfOrCnpj)
        textGender = findViewById(R.id.txtGender)
        imageViewPhoto = findViewById(R.id.imageViewDetails)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_client)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        detailViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[DetailViewModel::class.java]
        initViews()
        detalhes = intent.getSerializableExtra("keyDetails") as ClienteEntity?
        if (detalhes != null) {
            updateUIWithDetails(detalhes!!)

            //viewModel
            detalhes!!.codeId?.let { detailViewModel!!.detailCliente(Dao(baseContext), it) }
        } else {
            Toast.makeText(this@DetailsClientActivity, "Vazio", Toast.LENGTH_LONG).show()
        }
        detailViewModel!!.detailResult.observe(this) { detailResult: Boolean ->
            if (detailResult) {
                // Lógica para lidar com os detalhes obtidos com sucesso
                Toast.makeText(
                    this@DetailsClientActivity,
                    "Detalhes obtidos com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        detailViewModel!!.errorMessage.observe(this) { errorMessage: String ->
            if (!errorMessage.isEmpty()) {
                // Lógica para lidar com mensagens de erro
                Toast.makeText(this@DetailsClientActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    // Interface com os detalhes
    private fun updateUIWithDetails(detalhes: ClienteEntity) {
        textNome!!.text = detalhes.name
        textUserName!!.text = detalhes.userName
        textAdress!!.text = detalhes.adress
        textEmail!!.text = detalhes.email
        val timestamp = detalhes.date
        val formattedDate = DateUtils.formatDateFromTimestamp(timestamp!!)
        textDate!!.text = formattedDate
        val cpfOrCnpj = detalhes.cpfOrCnpj
        if (MaskUtils.isCpf(cpfOrCnpj)) {
            textCpfOrCnpj!!.text = MaskUtils.formatCpf(cpfOrCnpj!!)
        } else {
            textCpfOrCnpj!!.text = MaskUtils.formatCnpj(cpfOrCnpj!!)
        }
        textGender!!.text = detalhes.gender

     //    Verifica se a permissão de leitura externa não foi concedida
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
            loadImageView(detalhes)
        }
    }

    private fun loadImageView(detalhes: ClienteEntity) {
        // Verificar se a permissão já foi concedida
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val imagePath = detalhes.picture
            if (imagePath != null && !imagePath.isEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        val bitmap = withContext(Dispatchers.IO) {
                            // Carregar a imagem de forma assíncrona
                            Picasso.get().load(File(imagePath)).get()
                        }
                        imageViewPhoto?.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DetailsClientActivity,
                            "Erro ao carregar a imagem",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("Picasso", "erro", e)
                        e.printStackTrace()
                    }
                }
            } else {
                Toast.makeText(
                    this@DetailsClientActivity,
                    "Sem imagem disponível",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Permissão não concedida, solicite permissão novamente
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 1
        private const val REQUEST_READ_EXTERNAL_STORAGE = 1
    }
}