package csci2040u.bytecouncil.backend;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.crudui.crud.CrudListener;

@Service
//This command creates the movie repository attribute with the required arguments
public class MovieDatabaseCommands implements CrudListener<Movie> {
    //if this is red underlined, go to settings and turn on annotated processing in File->settings->build,Execute->compiler, and download the Lombok plugin in settings->plugins
    private final JpaRepository<Movie, Long> movieRepo;
    private final MovieCsvWriter movieCsvWriter;

    public MovieDatabaseCommands(JpaRepository<Movie, Long> movieRepo, MovieCsvWriter movieCsvWriter) {
        this.movieRepo = movieRepo;
        this.movieCsvWriter = movieCsvWriter;
    }




    @Override
    public Collection<Movie> findAll() {
        Collection<Movie> databaseMovies = movieRepo.findAll();
        // Prefer DB values during the same runtime session when they exist.
        if (!databaseMovies.isEmpty()) {
            return databaseMovies;
        }

        // Fall back to CSV so admin/catalog can still show persisted movies after restarts
        Collection<Movie> csvMovies = movieCsvWriter.readMovies();
        if (!csvMovies.isEmpty()) {
            movieRepo.saveAll(csvMovies);
            return movieRepo.findAll();
        }

        return databaseMovies;
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
        Movie updatedMovie = movieRepo.save(movie);
        movieCsvWriter.overwriteMovies(movieRepo.findAll());
        return updatedMovie;
    }

    @Override
    @Transactional
    public void delete(Movie movie) {
        if (movie.getId() != null) {
            movieRepo.deleteById(movie.getId());
        } else {
            movieRepo.delete(movie);
        }
        movieCsvWriter.overwriteMovies(movieRepo.findAll());
    }
}
