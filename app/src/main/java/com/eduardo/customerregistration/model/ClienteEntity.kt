package com.eduardo.customerregistration.model

import java.io.Serializable
import java.util.*

data class ClienteEntity(

    var codeId: Long? = null,
    var name: String? = null,
    var date: Long? = null,
    var userName: String? = null,
    var password: String? = null,
    var gender: String? = null,
    var cpfOrCnpj: String? = null,
    var email: String? = null,
    var adress: String? = null,
    var picture: String? = null

) : Serializable {

    fun calculateAge(): Int {
        date?.let {
            val dob = Calendar.getInstance()
            dob.timeInMillis = it

            val today = Calendar.getInstance()
            var age = today[Calendar.YEAR] - dob[Calendar.YEAR]

            if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
                age--
            }

            return age
            }

        return 0 // Se a data de nascimento não estiver definida, retorna 0.
        }

}