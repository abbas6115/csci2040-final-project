package csci2040u.bytecouncil.backend;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MovieCsvWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void appendMovieWritesHeaderAndEscapedMovieValues() throws IOException {
        Path csvPath = tempDir.resolve("movies.csv");
        MovieCsvWriter writer = new MovieCsvWriter(csvPath.toString());

        Movie movie = new Movie("A, \"Great\" Movie", "poster", "Actor One", "Drama", "9.0", 2026);
        movie.setId(42L);

        writer.appendMovie(movie);

        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        assertEquals(2, lines.size());
        assertEquals("id,name,posterURL,actors,genre,ratings,releaseYear", lines.get(0));
        assertTrue(lines.get(1).contains("\"42\""));
        assertTrue(lines.get(1).contains("\"A, \"\"Great\"\" Movie\""));
        assertTrue(lines.get(1).contains("\"2026\""));
    }

    @Test
    void overwriteMoviesReplacesFileContentWithProvidedMovies() throws IOException {
        Path csvPath = tempDir.resolve("movies.csv");
        MovieCsvWriter writer = new MovieCsvWriter(csvPath.toString());

        Movie first = new Movie("Dune", "poster-1", "Timothee", "Sci-Fi", "8.0", 2021);
        first.setId(1L);
        Movie second = new Movie("Arrival", "poster-2", "Amy Adams", "Sci-Fi", "8.1", 2016);
        second.setId(2L);

        writer.appendMovie(first);
        writer.overwriteMovies(List.of(second));

        List<String> lines = Files.readAllLines(csvPath, StandardCharsets.UTF_8);
        assertEquals(2, lines.size());
        assertEquals("id,name,posterURL,actors,genre,ratings,releaseYear", lines.get(0));
        assertTrue(lines.get(1).contains("\"Arrival\""));
        assertTrue(lines.get(1).contains("\"2\""));
        assertTrue(lines.stream().noneMatch(line -> line.contains("\"Dune\"")));
    }

    @Test
    void readMoviesMapsCsvValuesAndDefaultsMissingInfoToNA() throws IOException {
        Path csvPath = tempDir.resolve("movies.csv");
        MovieCsvWriter writer = new MovieCsvWriter(csvPath.toString());

        String csvContent = String.join(
                System.lineSeparator(),
                "id,name,posterURL,actors,genre,ratings,releaseYear",
                "\"1\",\"Interstellar\",\"\",\"Matthew\",\"Sci-Fi\",\"\",\"2014\"",
                "\"2\",\"OnlyName\""
        ) + System.lineSeparator();

        Files.writeString(csvPath, csvContent, StandardCharsets.UTF_8);

        List<Movie> movies = writer.readMovies();

        assertEquals(2, movies.size());

        Movie first = movies.get(0);
        assertNull(first.getId());
        assertEquals("Interstellar", first.getName());
        assertEquals("N/A", first.getPosterURL());
        assertEquals("Matthew", first.getActors());
        assertEquals("Sci-Fi", first.getGenre());
        assertEquals("N/A", first.getRatings());
        assertEquals(2014, first.getReleaseYear());

        Movie second = movies.get(1);
        assertEquals("OnlyName", second.getName());
        assertEquals("N/A", second.getPosterURL());
        assertEquals("N/A", second.getActors());
        assertEquals("N/A", second.getGenre());
        assertEquals("N/A", second.getRatings());
        assertEquals(0, second.getReleaseYear());
    }

    @Test
    void importMoviesBulkCopiesRowsFromInputCsv() throws IOException {
        Path outputCsv = tempDir.resolve("movies.csv");
        MovieCsvWriter writer = new MovieCsvWriter(outputCsv.toString());

        Path inputCsv = tempDir.resolve("bulk-import.csv");
        String inputContent = String.join(
                System.lineSeparator(),
                "id,name,posterURL,actors,genre,ratings,releaseYear",
                "\"10\",\"Blade Runner 2049\",\"poster-a\",\"Ryan Gosling\",\"Sci-Fi\",\"8.0\",\"2017\"",
                "\"11\",\"The Martian\",\"poster-b\",\"Matt Damon\",\"Sci-Fi\",\"8.0\",\"2015\""
        ) + System.lineSeparator();
        Files.writeString(inputCsv, inputContent, StandardCharsets.UTF_8);

        writer.importMoviesBulk(inputCsv.toFile());

        List<String> lines = Files.readAllLines(outputCsv, StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals("id,name,posterURL,actors,genre,ratings,releaseYear", lines.get(0));
        assertTrue(lines.get(1).contains("\"Blade Runner 2049\""));
        assertTrue(lines.get(2).contains("\"The Martian\""));
    }
}
