package br.com.neomind.util;

//INFORMATIVO
/*https://www.linkedin.com/pulse/como-validar-o-novo-formato-do-cnpj-brasil-welyab-paula-imtvf/*/

import java.util.InputMismatchException;

public class CnpjValidator {
    private CnpjValidator() {

    }
    public static boolean isValid(String cnpj) {
        cnpj = cnpj.replace(".","").replace("-", "").replace("/", "");
        if(cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: "00000000000000"), o que é inválido
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        char dig13, dig14;
        int sm, i, r, num;
        String cnpjCalc;

        try {
            sm = 0;
            int peso = 2;
            for (i = 11; i >= 0; i--) {
                num = (int)(cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            dig13 = (char)((r == 0) || (r == 1) ? '0' : (11 - r) + 48);

            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (int)(cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10) {
                    peso = 2;
                }
            }

            r = sm % 11;
            dig14 = (char)((r == 0) || (r == 1) ? '0' : (11 - r) + 48);

            cnpjCalc = cnpj.substring(0, 12) + dig13 + dig14;
            return cnpjCalc.equals(cnpj);
        } catch (InputMismatchException erro) {
            return false;
        }
    }
}

