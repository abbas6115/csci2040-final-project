package csci2040u.bytecouncil.backend;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminSystemTest {

    private MovieRepository repo;
    private MovieCsvWriter csvWriter;
    private MovieDatabaseCommands commands;

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        repo = mock(MovieRepository.class);

        tempFile = Files.createTempFile("movies_system", ".csv");
        csvWriter = new MovieCsvWriter(tempFile.toString());

        commands = new MovieDatabaseCommands(repo, csvWriter);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // ST-09-OB: Admin adds a new movie
    @Test
    void adminAddsMovie() {
        Movie movie = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);

        when(repo.save(any(Movie.class))).thenReturn(movie);
        when(repo.findAll()).thenReturn(List.of(movie));

        commands.add(movie);

        Collection<Movie> result = commands.findAll();

        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(movie1 -> movie1.getName().equals("Dune")));
    }

    // ST-10-OB: Admin edits a movie
    @Test
    void adminEditsMovie() {
        Movie movie = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);

        when(repo.save(any(Movie.class))).thenReturn(movie);

        // initial add
        commands.add(movie);

        // simulate edit
        movie.setRatings("9.0");

        when(repo.findAll()).thenReturn(List.of(movie));

        commands.update(movie);

        Collection<Movie> result = commands.findAll();

        assertEquals("9.0", result.iterator().next().getRatings());
    }

    // ST-11-OB: Admin deletes a movie
    @Test
    void adminDeletesMovie() {
        Movie m1 = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        Movie m2 = new Movie("Avatar", "poster2", "Sam", "Action", "7.5", 2009);

        when(repo.findAll()).thenReturn(List.of(m1, m2))  // before delete
                .thenReturn(List.of(m2));     // after delete

        commands.delete(m1);

        Collection<Movie> result = commands.findAll();

        assertEquals(1, result.size());
        assertFalse(result.stream().anyMatch(m -> m.getName().equals("Dune")));
    }

    // ST-12-OB: Admin bulk import from CSV
    @Test
    void adminImportsMoviesFromCsv() throws IOException {
        // Populate CSV
        List<String> lines = List.of(
                "id,name,posterURL,actors,genre,ratings,releaseYear",
                "1,Dune,poster,Timothee,Sci-Fi,8.0,2021",
                "2,Avatar,poster2,Sam,Action,7.5,2009"
        );

        Files.write(tempFile, lines);

        // simulate empty database
        when(repo.findAll()).thenReturn(Collections.emptyList()) // first call
                .thenReturn(List.of(
                        new Movie("Dune","poster","Timothee","Sci-Fi","8.0",2021),
                        new Movie("Avatar","poster2","Sam","Action","7.5",2009)
                ));

        Collection<Movie> result = commands.findAll();

        // verify database was populated
        verify(repo, times(1)).saveAll(anyCollection());

        assertEquals(2, result.size());
    }
}