package br.com.antoniocdp.literalura.service;

public interface IConverteDados {
    <T> T  obterDados(String json, Class<T> classe);
}
