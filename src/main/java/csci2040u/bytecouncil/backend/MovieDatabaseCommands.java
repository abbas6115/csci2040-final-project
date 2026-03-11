package csci2040u.bytecouncil.backend;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.crudui.crud.CrudListener;

import lombok.RequiredArgsConstructor;

@Service
//This command creates the movie repository attribute with the required arguments
@RequiredArgsConstructor
public class MovieDatabaseCommands implements CrudListener<Movie> {
    //if this is red underlined, go to settings and turn on annotated processing in File->settings->build,Execute->compiler, and download the Lombok plugin in settings->plugins
    private final MovieRepository movieRepo;
    private final MovieCsvWriter movieCsvWriter;




    @Override
    public Collection<Movie> findAll() {
        Collection<Movie> databaseMovies = movieRepo.findAll();
        // Prefer DB values during the same runtime session when they exist.
        if (!databaseMovies.isEmpty()) {
            return databaseMovies;
        }

        // Fall back to CSV so admin/catalog can still show persisted movies after restarts
        return movieCsvWriter.readMovies();
    }

    @Override
    @Transactional
    public Movie add(Movie movie) {
        // Keep DB as source of truth and mirror the saved record to CSV storage
        Movie savedMovie = movieRepo.save(movie);
        movieCsvWriter.appendMovie(savedMovie);
        return savedMovie;
    }

    @Override
    public Movie update(Movie movie) {
        return movieRepo.save(movie);
    }

    @Override
    public void delete(Movie movie) {
        movieRepo.delete(movie);
    }
}
