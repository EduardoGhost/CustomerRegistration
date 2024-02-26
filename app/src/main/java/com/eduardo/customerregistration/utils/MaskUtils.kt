package com.eduardo.customerregistration.utils

object MaskUtils {
    @JvmStatic
    fun formatCnpj(cnpj: String): String {
        // Remove caracteres não numéricos do CNPJ
        val digits = cnpj.replace("[^0-9]".toRegex(), "")

        // Aplica a máscara do CNPJ (##.###.###/####-##)
        return String.format(
            "%s.%s.%s/%s-%s",
            digits.substring(0, 2),
            digits.substring(2, 5),
            digits.substring(5, 8),
            digits.substring(8, 12),
            digits.substring(12)
        )
    }

    // Método para definir a máscara do CPF
    @JvmStatic
    fun formatCpf(cpf: String): String {
        // Remove caracteres não numéricos do CPF
        val digits = cpf.replace("[^0-9]".toRegex(), "")

        // Aplica a máscara do CPF (###.###.###-##)
        return String.format(
            "%s.%s.%s-%s",
            digits.substring(0, 3),
            digits.substring(3, 6),
            digits.substring(6, 9),
            digits.substring(9)
        )
    }

    fun unmaskCnpj(cnpj: String): String {
        // Remove caracteres não numéricos do CNPJ
        return cnpj.replace("[^0-9]".toRegex(), "")
    }

    // Método para verificar se é CPF
    @JvmStatic
    fun isCpf(documento: String?): Boolean {
        return documento != null && documento.length == 11
    }
}