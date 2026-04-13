package csci2040u.bytecouncil.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MovieDatabaseCommandsTest {

    @Autowired
    private MovieRepository movieRepository;

    private Path tempCsvPath;
    private MovieCsvWriter movieCsvWriter;
    private MovieDatabaseCommands commands;

    @BeforeEach
    void setUp() throws IOException {
        tempCsvPath = Files.createTempFile("movie_db_commands", ".csv");
        movieCsvWriter = new MovieCsvWriter(tempCsvPath.toString());
        commands = new MovieDatabaseCommands(movieRepository, movieCsvWriter);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempCsvPath);
    }

    @Test
    void findAllReturnsDatabaseMoviesWhenDatabaseIsNotEmpty() {
        Movie dbMovie = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        movieRepository.save(dbMovie);

        // Seed CSV with a different movie to prove DB values are preferred.
        movieCsvWriter.appendMovie(new Movie("Interstellar", "poster2", "Matthew", "Sci-Fi", "8.6", 2014));

        List<Movie> result = (List<Movie>) commands.findAll();

        assertEquals(1, result.size());
        assertEquals("Dune", result.get(0).getName());
    }

    @Test
    void findAllFallsBackToCsvWhenDatabaseIsEmpty() {
        movieCsvWriter.appendMovie(new Movie("Interstellar", "poster", "Matthew", "Sci-Fi", "8.6", 2014));

        List<Movie> result = (List<Movie>) commands.findAll();

        assertEquals(1, result.size());
        assertEquals("Interstellar", result.get(0).getName());
        assertEquals(1, movieRepository.findAll().size(), "CSV movie should be persisted into DB");
    }

    @Test
    void addSavesMovieAndAppendsSavedMovieToCsv() {
        Movie newMovie = new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.0", 2016);

        Movie result = commands.add(newMovie);

        assertTrue(result.getId() != null, "Saved movie should receive a database id");
        assertEquals(1, movieRepository.findAll().size());

        List<Movie> csvMovies = movieCsvWriter.readMovies();
        assertEquals(1, csvMovies.size());
        assertEquals("Arrival", csvMovies.get(0).getName());
    }

    @Test
    void updateSavesMovieAndOverwritesCsvWithDatabaseState() {
        Movie existing = movieRepository.save(
            new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.0", 2016)
        );

        existing.setPosterURL("new-poster");
        existing.setRatings("8.1");

        Movie result = commands.update(existing);

        assertEquals("new-poster", result.getPosterURL());
        assertEquals("8.1", result.getRatings());

        List<Movie> csvMovies = movieCsvWriter.readMovies();
        assertEquals(1, csvMovies.size());
        assertEquals("new-poster", csvMovies.get(0).getPosterURL());
        assertEquals("8.1", csvMovies.get(0).getRatings());
    }

    @Test
    void deleteByIdAndOverwritesCsvWithRemainingDatabaseState() {
        Movie toDelete = movieRepository.save(
            new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.0", 2016)
        );
        movieRepository.save(new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021));

        commands.delete(toDelete);

        List<Movie> remaining = movieRepository.findAll();
        assertEquals(1, remaining.size());
        assertEquals("Dune", remaining.get(0).getName());

        List<Movie> csvMovies = movieCsvWriter.readMovies();
        assertEquals(1, csvMovies.size());
        assertEquals("Dune", csvMovies.get(0).getName());
        assertFalse(csvMovies.stream().anyMatch(movie -> "Arrival".equals(movie.getName())));
    }
}
