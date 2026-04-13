package csci2040u.bytecouncil.backend;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieCatalogSystemsTest {

    private Path tempCsvPath;
    private MovieCsvWriter csvWriter;

    @BeforeEach
    void setUp() throws IOException {
        tempCsvPath = Files.createTempFile("movie_catalog_system", ".csv");
        csvWriter = new MovieCsvWriter(tempCsvPath.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempCsvPath);
    }

    @Test
    void ST05_userCatalogDisplaysAllMoviesWithCorrectDetails() {
        Movie dune = new Movie("Dune", "poster-url", "Timothee Chalamet", "Sci-Fi", "8.0", 2021);
        Movie avatar = new Movie("Avatar", "poster-url-2", "Sam Worthington", "Action", "7.8", 2009);

        csvWriter.appendMovie(dune);
        csvWriter.appendMovie(avatar);

        List<Movie> catalog = csvWriter.readMovies();
        assertFalse(catalog.isEmpty(), "Catalog should display at least one movie");

        Movie first = catalog.get(0);
        assertFalse(first.getName().isBlank(), "Movie title should not be blank");
        assertFalse(first.getGenre().isBlank(), "Movie genre should not be blank");
        assertFalse(first.getRatings().isBlank(), "Movie rating should not be blank");
        assertTrue(first.getReleaseYear() > 0, "Release year should be present");
    }

    @Test
    void ST06_addToWatchlistAppearsAtTopOfWatchlist() {
        CustomUser user = new CustomUser("User1", "password", "USER");
        Movie movie = new Movie("Dune", "poster-url", "Timothee Chalamet", "Sci-Fi", "8.0", 2021);

        user.addToWatchlist(movie);

        LinkedList<Movie> watchlist = user.getRecentlyWatched();
        assertFalse(watchlist.isEmpty(), "Watchlist should have at least one movie after add");
        assertEquals(movie.getName(), watchlist.getFirst().getName(),
            "Most recently added movie should appear at the top of the watchlist");
    }

    @Test
    void ST07_mostRecentlyAddedMovieIsListedFirst() {
        CustomUser user = new CustomUser("User1", "password", "USER");

        Movie firstAdded = new Movie("Dune", "poster-url", "Timothee Chalamet", "Sci-Fi", "8.0", 2021);
        Movie secondAdded = new Movie("Avatar", "poster-url-2", "Sam Worthington", "Action", "7.8", 2009);

        user.addToWatchlist(firstAdded);
        user.addToWatchlist(secondAdded);

        LinkedList<Movie> watchlist = user.getRecentlyWatched();
        assertEquals(secondAdded.getName(), watchlist.get(0).getName(),
            "Second added movie should be at the top");
        assertEquals(firstAdded.getName(), watchlist.get(1).getName(),
            "First added movie should be second in the list");
    }

    @Test
    void ST08_removedMovieIsNoLongerInWatchlist() {
        CustomUser user = new CustomUser("User1", "password", "USER");

        Movie dune = new Movie("Dune", "poster-url", "Timothee Chalamet", "Sci-Fi", "8.0", 2021);
        Movie avatar = new Movie("Avatar", "poster-url-2", "Sam Worthington", "Action", "7.8", 2009);

        user.addToWatchlist(dune);
        user.addToWatchlist(avatar);

        int sizeBefore = user.getRecentlyWatched().size();
        user.removeMovieWatchList(dune);

        LinkedList<Movie> watchlist = user.getRecentlyWatched();
        assertEquals(sizeBefore - 1, watchlist.size(),
            "Watchlist should have one fewer entry after removal");
        assertTrue(watchlist.stream().noneMatch(movie -> movie.getName().equals(dune.getName())),
            "Removed movie should no longer appear in the watchlist");
    }
}
