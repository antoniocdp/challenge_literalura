package br.com.antoniocdp.literalura.util;

public class NomeUtil {
    public static String formatarNome(String nome){
        String[] partes = nome.split(", ");
        if(partes.length > 1){
            return partes[1] + " " + partes[0];
        } else {
            return nome;
        }
    }
}
