package csci2040u.bytecouncil.backend;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieDatabaseCommandsTest {

    @Test
    void findAllReturnsDatabaseMoviesWhenDatabaseIsNotEmpty() {
        MovieRepository movieRepository = mock(MovieRepository.class);
        MovieCsvWriter movieCsvWriter = mock(MovieCsvWriter.class);
        MovieDatabaseCommands commands = new MovieDatabaseCommands(movieRepository, movieCsvWriter);

        List<Movie> databaseMovies = List.of(new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021));
        when(movieRepository.findAll()).thenReturn(databaseMovies);

        assertSame(databaseMovies, commands.findAll());
        verify(movieRepository).findAll();
        verify(movieCsvWriter, never()).readMovies();
    }

    @Test
    void findAllFallsBackToCsvWhenDatabaseIsEmpty() {
        MovieRepository movieRepository = mock(MovieRepository.class);
        MovieCsvWriter movieCsvWriter = mock(MovieCsvWriter.class);
        MovieDatabaseCommands commands = new MovieDatabaseCommands(movieRepository, movieCsvWriter);

        List<Movie> csvMovies = List.of(new Movie("Interstellar", "poster", "Matthew", "Sci-Fi", "8.6", 2014));
        when(movieRepository.findAll()).thenReturn(List.of());
        when(movieCsvWriter.readMovies()).thenReturn(csvMovies);

        assertSame(csvMovies, commands.findAll());
        verify(movieRepository).findAll();
        verify(movieCsvWriter).readMovies();
    }

    @Test
    void addSavesMovieAndAppendsSavedMovieToCsv() {
        MovieRepository movieRepository = mock(MovieRepository.class);
        MovieCsvWriter movieCsvWriter = mock(MovieCsvWriter.class);
        MovieDatabaseCommands commands = new MovieDatabaseCommands(movieRepository, movieCsvWriter);

        Movie newMovie = new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.0", 2016);
        Movie savedMovie = new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.0", 2016);
        savedMovie.setId(7L);

        when(movieRepository.save(newMovie)).thenReturn(savedMovie);

        Movie result = commands.add(newMovie);

        assertSame(savedMovie, result);
        verify(movieRepository).save(newMovie);
        verify(movieCsvWriter).appendMovie(savedMovie);
    }
}