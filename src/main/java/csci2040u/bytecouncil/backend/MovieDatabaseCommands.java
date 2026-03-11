package csci2040u.bytecouncil.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.Collection;
import java.util.List;

@Service
//This command creates the movie repository attribute with the required arguments
@RequiredArgsConstructor
public class MovieDatabaseCommands implements CrudListener<Movie> {
    //if this is red underlined, go to settings and turn on annotated processing in File->settings->build,Execute->compiler, and download the Lombok plugin in settings->plugins
    private final MovieRepository movieRepo;




    @Override
    public Collection<Movie> findAll() {
        return movieRepo.findAll();
    }

    @Override
    public Movie add(Movie movie) {
        return movieRepo.save(movie);
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
