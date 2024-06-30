package br.com.antoniocdp.literalura.principal;

import br.com.antoniocdp.literalura.repository.AutorRepository;
import br.com.antoniocdp.literalura.repository.LivroRepository;
import br.com.antoniocdp.literalura.service.ApiService;
import br.com.antoniocdp.literalura.service.ConverteDados;
import br.com.antoniocdp.literalura.util.ConversorIdioma;
import br.com.antoniocdp.literalura.util.NomeUtil;
import br.com.antoniocdp.literalura.model.*;

import java.util.*;

public class Principal {
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    private final Scanner scanner = new Scanner(System.in);
    private final ApiService apiService = new ApiService();
    private final ConverteDados converteDados = new ConverteDados();

    private final List<DadosAutor> dadosAutores = new ArrayList<>();
    private final List<DadosLivros> dadosLivros = new ArrayList<>();

    public void salvarLivro(DadosLivros dadosLivros) {
        var livroExistente = livroRepository.findByTitulo(dadosLivros.getTitulo());
        if(livroExistente == null){
            var livro = new Livro();
            livro.setTitulo(dadosLivros.getTitulo());
            livro.setAutor(dadosLivros.getAutores().get(0).getNome());
            livro.setIdioma(String.join(", ", dadosLivros.getIdiomas()));
            livro.setDownloads(dadosLivros.getDownloads());
            livroRepository.save(livro);
        }
    }
    private void salvarAutores(List<DadosAutor> dadosAutores){
        for (var dadosAutor : dadosAutores) {
            var autorExistente = autorRepository.findByNome(dadosAutor.getNome());
            if (autorExistente == null) {
                var autor = new Autor();
                autor.setNome(dadosAutor.getNome());
                autor.setAnoNascimento(dadosAutor.getAnoNascimento());
                autor.setAnoFalecimento(dadosAutor.getAnoFalecimento());
                autorRepository.save(autor);
            }
        }
    }
    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }
    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                 ******************************
                     Escolha o número de sua opção:
                 ******************************
                
                1 - Buscar livros pelo título
                2 - Listar livros registrados no banco de dados
                3 - Listar autores registrados no banco de dados
                4 - Listar autores vivos em um determinado ano
                5 - Listar livros em um determinado idioma
                
                0 - Sair
                """;

            System.out.println(menu);
            System.out.print("Digite a opção desejada: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> buscarLivrosPeloTitulo();
                case 2 -> listarLivros();
                case 3 -> listarAutores();
                case 4 -> listarAutoresVivos();
                case 5 -> listarLivrosPorIdioma();
                case 0 -> System.out.println("Saindo do aplicativo!");
                default -> System.out.println("Opção inválida");
            }
        }
        scanner.close();
    }

    private void buscarLivrosPeloTitulo() {
        var livro = buscarLivros();
        if (livro != null) {
            System.out.println("----------LIVRO----------");
            System.out.println("Título: " + livro.getTitulo());
            System.out.println("Autor: " + NomeUtil.formatarNome(livro.getAutores().get(0).getNome()));
            String idioma = String.join(", ", livro.getIdiomas());
            System.out.println("Idiomas: " + idioma); ;
            System.out.println("Quantidade de Downloads: " + livro.getDownloads());
            System.out.println("-------------------------");
            System.out.println();
        } else {
            System.out.println("Nenhum livro encontrado com o título fornecido.");
        }
    }

    private DadosLivros buscarLivros(){
        System.out.println("Insira o nome do livro que você deseja procurar ");
        var titulo = scanner.nextLine();
        String URL = "https://gutendex.com/books";
        var json = apiService.obterDados(URL + "?search="
                + titulo.replace(" ", "%20"));
        Response response = converteDados.obterDados(json, Response.class);
        if (!response.getResults().isEmpty()) {
            var dadosLivros = response.getResults().get(0);
            salvarLivro(dadosLivros);
            salvarAutores(dadosLivros.getAutores());
            return dadosLivros;
        } else {
            return null;
        }
    }

    private void listarLivros() {
        var livros = livroRepository.findAll();
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro registrado no banco de dados.");
        } else {
            System.out.println("----------LIVROS----------");
            for (var livro : livros) {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + livro.getAutor());
                System.out.println("Idioma: " + livro.getIdioma());
                System.out.println("Downloads: " + livro.getDownloads());
                System.out.println("-------------------------");
            }
            System.out.println();
        }
    }

    private void listarAutores() {
        var autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor registrado no banco de dados.");
        } else {
            System.out.println("----------AUTORES----------");
            for (var autor : autores) {
                System.out.println("Nome: " + autor.getNome());
                System.out.println("Ano de Nascimento: " + autor.getAnoNascimento());
                System.out.println("Ano de Falecimento: " + autor.getAnoFalecimento());
                System.out.println("-------------------------");
            }
            System.out.println();
        }
    }

    private void listarAutoresVivos() {
        System.out.println("Digite o ano: ");
        var ano = scanner.nextInt();
        scanner.nextLine();
        var autores = autorRepository.findAutoresVivos(ano);
        if (autores.isEmpty()) {
            System.out.println("Nenhum autor vivo registrado no banco de dados para o ano " + ano + ".");
        } else {
            System.out.println("---------->AUTORES VIVOS EM " + ano + "<----------");
            for (var autor : autores) {
                System.out.println("Nome: " + autor.getNome());
                System.out.println("Ano de Nascimento: " + autor.getAnoNascimento());
                System.out.println("Ano de Falecimento: " + autor.getAnoFalecimento());
                System.out.println("-------------------------");
            }
            System.out.println();
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("Insira o idoma para realizar a busca: ");
        System.out.println("de- alemão ");
        System.out.println("en- inglês ");
        System.out.println("es- espanhol ");
        System.out.println("fr- francês ");
        System.out.println("it- italiano");
        System.out.println("pt- português ");
        var idioma = ConversorIdioma.converterIdioma(scanner.nextLine());
        var livros = livroRepository.findLivrosPorIdioma(idioma);
        if (livros.isEmpty()) {
            System.out.println("Não existem livros nesse idioma no banco de dados. " );
        } else {
            System.out.println("---------->LIVROS NO IDIOMA " + idioma + "<----------");
            for (var livro : livros) {
                System.out.println("Título: " + livro.getTitulo());
                System.out.println("Autor: " + livro.getAutor());
                System.out.println("-------------------------");
            }
            System.out.println();
        }
    }
}
