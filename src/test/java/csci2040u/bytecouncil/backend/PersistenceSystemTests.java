package csci2040u.bytecouncil.backend;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersistenceSystemTests {

    private static final String CSV_PATH = "movies_test.csv";
    private MovieCsvWriter writer;

    private Path tempFile;


    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("movies_integration", ".csv");
        writer = new MovieCsvWriter(tempFile.toString());

    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // ST-13-OB
    @Test
    void moviesPersistAcrossRestart() {

        MovieRepository repo1 = mock(MovieRepository.class);
        MovieRepository repo2 = mock(MovieRepository.class);

        List<Movie> movies = List.of(
                new Movie("A", "p", "actor", "genre", "5", 2000),
                new Movie("B", "p", "actor", "genre", "6", 2001),
                new Movie("C", "p", "actor", "genre", "7", 2002)
        );

        when(repo1.findAll()).thenReturn(movies);

        // Write using real instance
        writer.overwriteMovies(repo1.findAll());

        // Simulate restart
        List<Movie> loaded = writer.readMovies();

        for (Movie m : loaded) {
            repo2.save(m);
        }

        verify(repo2, times(3)).save(any(Movie.class));
    }

    // ST-14-OB
    @Test
    void csvMirrorsDatabaseAfterChanges() {

        MovieRepository repo = mock(MovieRepository.class);

        Movie m1 = new Movie("A", "p", "actor", "genre", "5", 2000);
        Movie m2 = new Movie("B", "p", "actor", "genre", "6", 2001);

        List<Movie> finalState = List.of(m2);
        when(repo.findAll()).thenReturn(finalState);

        // Write Database -> CSV
        writer.overwriteMovies(repo.findAll());

        // Read back
        List<Movie> fromCsv = writer.readMovies();

        assertEquals(1, fromCsv.size());
        assertEquals(m2.getName(), fromCsv.get(0).getName());
    }
}
