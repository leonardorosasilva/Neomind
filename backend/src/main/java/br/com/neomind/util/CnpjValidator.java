package br.com.neomind.util;

//INFORMATIVO
/*https://www.linkedin.com/pulse/como-validar-o-novo-formato-do-cnpj-brasil-welyab-paula-imtvf/*/

import java.util.InputMismatchException;

public class CnpjValidator {
    private CnpjValidator() {

    }
    public static boolean isValid(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;
        try {
            int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int soma = 0;
            for (int i = 0; i < 12; i++) soma += (cnpj.charAt(i) - '0') * pesos1[i];
            int dig1 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

            soma = 0;
            for (int i = 0; i < 13; i++) soma += (cnpj.charAt(i) - '0') * pesos2[i];
            int dig2 = soma % 11 < 2 ? 0 : 11 - (soma % 11);

            return dig1 == (cnpj.charAt(12) - '0') && dig2 == (cnpj.charAt(13) - '0');
        } catch (Exception e) {

            return false;
        }
    }
}

