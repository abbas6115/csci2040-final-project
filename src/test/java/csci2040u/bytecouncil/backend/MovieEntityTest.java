package csci2040u.bytecouncil.backend;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MovieEntityTest {

    // UT-13-OB: Default constructor
    @Test
    void defaultConstructor_setsDefaultValues() {
        Movie movie = new Movie();

        assertEquals("NA", movie.getName());
        assertEquals("NA", movie.getPosterURL());
        assertEquals("NA", movie.getActors());
        assertEquals("NA", movie.getGenre());
        assertEquals("0.0", movie.getRatings());
        assertEquals(0, movie.getReleaseYear());
    }

    // UT-14-OB: Parameterized constructor
    @Test
    void parameterizedConstructor_setsAllFieldsCorrectly() {
        Movie movie = new Movie(
                "Dune",
                "poster",
                "Timothee",
                "Sci-Fi",
                "8.0",
                2021
        );

        assertEquals("Dune", movie.getName());
        assertEquals("poster", movie.getPosterURL());
        assertEquals("Timothee", movie.getActors());
        assertEquals("Sci-Fi", movie.getGenre());
        assertEquals("8.0", movie.getRatings());
        assertEquals(2021, movie.getReleaseYear());
    }

    // UT-15-CB: Equals and HashCode based on ID only
    @Test
    void shouldReturnTrue_WhenMovieIDsAreNotEqual() {
        Movie movie1 = new Movie("Dune", "poster", "Timothee", "Sci-Fi", "8.0", 2021);
        Movie movie2 = new Movie("Avatar", "poster2", "Sam", "Action", "7.5", 2009);

        assertNotEquals(movie1, movie2);
        assertEquals(movie1 == null, movie2 == null);
    }

    // UT-16-OB: Setters and getters
    @Test
    void settersAndGetters_workCorrectly() {
        Movie movie = new Movie();

        movie.setName("Interstellar");
        movie.setPosterURL("posterURL");
        movie.setActors("Matthew McConaughey");
        movie.setGenre("Sci-Fi");
        movie.setRatings("9.0");
        movie.setReleaseYear(2014);

        assertEquals("Interstellar", movie.getName());
        assertEquals("posterURL", movie.getPosterURL());
        assertEquals("Matthew McConaughey", movie.getActors());
        assertEquals("Sci-Fi", movie.getGenre());
        assertEquals("9.0", movie.getRatings());
        assertEquals(2014, movie.getReleaseYear());
    }
}
