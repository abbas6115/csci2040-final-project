package csci2040u.bytecouncil.backend;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
        List<Movie> persistedMovies = List.of(new Movie("Interstellar", "poster", "Matthew", "Sci-Fi", "8.6", 2014));
        doReturn(List.of()).doReturn(persistedMovies).when(movieRepository).findAll();
        when(movieCsvWriter.readMovies()).thenReturn(csvMovies);

        assertSame(persistedMovies, commands.findAll());
        verify(movieRepository, times(2)).findAll();
        verify(movieRepository).saveAll(csvMovies);
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

    @Test
    void updateSavesMovieAndOverwritesCsvWithDatabaseState() {
        MovieRepository movieRepository = mock(MovieRepository.class);
        MovieCsvWriter movieCsvWriter = mock(MovieCsvWriter.class);
        MovieDatabaseCommands commands = new MovieDatabaseCommands(movieRepository, movieCsvWriter);

        Movie movieToUpdate = new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.0", 2016);
        movieToUpdate.setId(7L);
        Movie updatedMovie = new Movie("Arrival", "new-poster", "Amy Adams", "Sci-Fi", "8.1", 2016);
        updatedMovie.setId(7L);

        List<Movie> allMoviesAfterUpdate = List.of(updatedMovie);
        when(movieRepository.save(movieToUpdate)).thenReturn(updatedMovie);
        when(movieRepository.findAll()).thenReturn(allMoviesAfterUpdate);

        Movie result = commands.update(movieToUpdate);

        assertSame(updatedMovie, result);
        verify(movieRepository).save(movieToUpdate);
        verify(movieRepository).findAll();
        verify(movieCsvWriter).overwriteMovies(allMoviesAfterUpdate);
    }

    @Test
    void deleteByIdAndOverwritesCsvWithRemainingDatabaseState() {
        MovieRepository movieRepository = mock(MovieRepository.class);
        MovieCsvWriter movieCsvWriter = mock(MovieCsvWriter.class);
        MovieDatabaseCommands commands = new MovieDatabaseCommands(movieRepository, movieCsvWriter);

        Movie movieToDelete = new Movie("Arrival", "poster", "Amy Adams", "Sci-Fi", "8.0", 2016);
        movieToDelete.setId(7L);

        List<Movie> remainingMovies = List.of(new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021));
        when(movieRepository.findAll()).thenReturn(remainingMovies);

        commands.delete(movieToDelete);

        verify(movieRepository).deleteById(7L);
        verify(movieRepository).findAll();
        verify(movieCsvWriter).overwriteMovies(remainingMovies);
    }
}
