package br.com.antoniocdp.literalura.util;

public class ConversorIdioma {

    public static String converterIdioma(String idioma) {

        return switch (idioma) {
            case "portugues" -> "pt";
            case "ingles" -> "en";
            case "espanhol" -> "es";
            case "frances" -> "fr";
            case "italiano" -> "it";
            case "alemao" -> "de";
            default -> idioma;
        };
    }
}
