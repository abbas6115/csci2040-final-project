package csci2040u.bytecouncil.backend;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

public class ViewWatchListTest {

    @Test
    void WatchListUpdated() {
        //Initialize user
        CustomUser user = new CustomUser("user1", "password");

        //Create and add movie to watch list
        Movie dune = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        user.addToWatchlist(dune);

        Movie transformers = new Movie("Transformers", "poster", "Megan", "Fantasy", "9.0", 2002);
        user.addToWatchlist(transformers);

        //Initialize results
        LinkedList<Movie> result = user.getRecentlyWatched();

        //Test size of queue (2)
        assertEquals(2, result.size());
        assertEquals(dune, result.get(0));
        assertEquals(transformers, result.get(0));




    }

    @Test
    void WatchListEmpty() {
        //initialize components
        CustomUser user = new CustomUser("user1", "password");
        Movie dune = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        user.addToWatchlist(dune);

        //Test if watch list is empty
        assertTrue(user.getRecentlyWatched().isEmpty());
    }

    @Test
    void QueueNotModifiedAfterView() {
        // Arrange
        CustomUser user = new CustomUser("user1", "password");

        Movie dune = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        Movie transformers = new Movie("Transformers", "poster", "Megan", "Fantasy", "9.0", 2002);

        user.addToWatchlist(dune);
        user.addToWatchlist(transformers);

        LinkedList<Movie> result = user.getRecentlyWatched();

        assertEquals(2, result.size());
        assertEquals(dune, result.get(0));

        user.removeMovieWatchList(transformers);

        result = user.getRecentlyWatched();

        assertEquals(1, result.size());
        assertEquals(transformers, result.get(0));
    }
}
