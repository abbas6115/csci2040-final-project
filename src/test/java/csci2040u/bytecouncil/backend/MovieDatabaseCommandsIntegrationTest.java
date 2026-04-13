package csci2040u.bytecouncil.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieDatabaseCommandsIntegrationTest {

    private MovieRepository repo;
    private MovieCsvWriter csvWriter;
    private MovieDatabaseCommands commands;

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        repo = mock(MovieRepository.class);

        tempFile = Files.createTempFile("movies_integration", ".csv");
        csvWriter = new MovieCsvWriter(tempFile.toString());

        commands = new MovieDatabaseCommands(repo, csvWriter);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // IT-03-TB: Add movie -> verify CSV written
    @Test
    void add_writesMovieToCsv() throws IOException {
        Movie movie = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);

        when(repo.save(any(Movie.class))).thenReturn(movie);

        commands.add(movie);

        List<String> lines = Files.readAllLines(tempFile);

        assertEquals(1, lines.size());
        assertTrue(lines.get(0).contains("Dune"));
    }

    // IT-04-TB: Delete movie -> CSV updated correctly
    @Test
    void delete_updatesCsvCorrectly() throws IOException {
        Movie movie1 = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        Movie movie2 = new Movie("Avatar", "poster2", "Sam", "Action", "7.5", 2009);
        Movie movie3 = new Movie("Interstellar", "poster3", "Matthew", "Sci-Fi", "9.0", 2014);

        // Populate CSV
        csvWriter.overwriteMovies(List.of(movie1, movie2, movie3));

        // After delete the repo returns 2 movies
        when(repo.findAll()).thenReturn(List.of(movie1, movie2));

        commands.delete(movie3);

        List<String> lines = Files.readAllLines(tempFile);

        assertEquals(3, lines.size());

        String content = String.join("\n", lines);

        assertFalse(content.contains("Interstellar"));
        assertTrue(content.contains("Dune"));
        assertTrue(content.contains("Avatar"));
    }

    // IT-05-TB: CSV fallback -> database gets populated
    @Test
    void findAll_csvFallback_populatesDatabase() throws IOException {
        Movie movie1 = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        Movie movie2 = new Movie("Avatar", "poster2", "Sam", "Action", "7.5", 2009);


        // Write CSV manually
        csvWriter.overwriteMovies(List.of(movie1, movie2));

        // database empty -> triggers CSV fallback
        when(repo.findAll())
                .thenReturn(Collections.emptyList())
                .thenReturn(List.of(movie1, movie2));

        Collection<Movie> result = commands.findAll();

        // Verify database populated from CSV
        verify(repo, times(1)).saveAll(anyCollection());

        assertEquals(2, result.size());
    }

    @Test
    void deleteByMovieObjectWhenIdIsNull() {
        MovieRepository movieRepository = mock(MovieRepository.class);
        MovieCsvWriter movieCsvWriter = mock(MovieCsvWriter.class);
        MovieDatabaseCommands commands = new MovieDatabaseCommands(movieRepository, movieCsvWriter);

        Movie movieWithoutId = new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.0", 2016);
        // id is null — never assigned

        when(movieRepository.findAll()).thenReturn(List.of());

        commands.delete(movieWithoutId);

        verify(movieRepository).delete(movieWithoutId);
        verify(movieRepository, never()).deleteById(any());
    }

    @Test
    void findAllReturnsEmptyWhenBothDatabaseAndCsvAreEmpty() {
        MovieRepository movieRepository = mock(MovieRepository.class);
        MovieCsvWriter movieCsvWriter = mock(MovieCsvWriter.class);
        MovieDatabaseCommands commands = new MovieDatabaseCommands(movieRepository, movieCsvWriter);

        when(movieRepository.findAll()).thenReturn(List.of());
        when(movieCsvWriter.readMovies()).thenReturn(List.of());

        Collection<Movie> result = commands.findAll();

        assertTrue(result.isEmpty());
        verify(movieRepository, never()).saveAll(any());
    }
}