package br.com.antoniocdp.literalura.repository;

import br.com.antoniocdp.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    Livro findByTitulo(String titulo);

    @Query("SELECT l FROM Livro l WHERE l.idioma LIKE %:idioma%")
    List<Livro> findLivrosPorIdioma(String idioma);
}
