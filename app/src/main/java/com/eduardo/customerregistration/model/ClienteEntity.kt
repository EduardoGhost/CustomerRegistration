package com.eduardo.customerregistration.model

import java.io.Serializable

data class ClienteEntity(

  //  var name: String,
    var codeId: Long? = null,
    var name: String? = null
 //   var date: Long? = null,

) : Serializable {

//    fun calculateAge(): Int {
//        date?.let {
//            val dob = Calendar.getInstance()
//            dob.timeInMillis = it
//
//            val today = Calendar.getInstance()
//            var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
//
//            if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
//                age--
//            }
//
//            return age
//            }
//
//        return 0 // Se a data de nascimento não estiver definida, retorna 0.
//        }
}