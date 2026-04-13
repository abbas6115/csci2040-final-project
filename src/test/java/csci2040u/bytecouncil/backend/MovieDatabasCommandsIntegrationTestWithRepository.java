package csci2040u.bytecouncil.backend;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest(classes = MovieDatabasCommandsIntegrationTestWithRepository.TestApplication.class)
@Transactional
class MovieDatabasCommandsIntegrationTestWithRepository {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EnableJpaRepositories(basePackageClasses = MovieRepository.class)
    @EntityScan(basePackageClasses = Movie.class)
    @Import(MovieDatabaseCommands.class)
    static class TestApplication {
        @Bean
        MovieCsvWriter movieCsvWriter() {
            return mock(MovieCsvWriter.class);
        }
    }

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MovieDatabaseCommands commands;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void IT02_updatedMovieReflectsNewNameInFindAll() {
        Movie movie = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);

        Movie saved = movieRepository.save(movie);
        saved.setName("Dune: Part Two");
        movieRepository.save(saved);

        Collection<Movie> result = commands.findAll();

        assertEquals(1, result.size());
        assertEquals("Dune: Part Two", result.iterator().next().getName());
        assertTrue(result.stream().noneMatch(m -> m.getName().equals("Dune")));
    }

    @Test
    void IT03_deletedMovieIsAbsentFromFindAll() {
        Movie movie1 = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        Movie movie2 = new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.1", 2016);

        Movie saved1 = movieRepository.save(movie1);
        movieRepository.save(movie2);

        commands.delete(saved1);
        entityManager.flush();
        entityManager.clear();

        Collection<Movie> result = commands.findAll();

        assertEquals(1, result.size());
        assertEquals("Arrival", result.iterator().next().getName());
        assertTrue(result.stream().noneMatch(m -> m.getName().equals("Dune")));
    }
}
