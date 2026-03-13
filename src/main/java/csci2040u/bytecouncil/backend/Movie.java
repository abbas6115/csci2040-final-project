package csci2040u.bytecouncil.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;


//For CRUD to store movies, it has to become and entity. Entities require an ID to be used in a repository
@Entity
@EqualsAndHashCode
public class  Movie {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;
    String name;
    String posterURL;
    String actors;
    String genre;
    String ratings;
    int releaseYear;



    public Movie() {
        name = "NA";
        posterURL = "NA";
        actors = "NA";
        genre = "NA";
        ratings = "0.0";
        releaseYear = 0;
    }

    public Movie(String name, String posterURL, String actors, String genre, String ratings,int year) {
        this.name = name;
        this.posterURL = posterURL;
        this.actors = actors;
        this.genre = genre;
        this.ratings = ratings;
        this.releaseYear = year;
    }


    //getters
    public String getName() { return name; }
    public String getPosterURL() { return posterURL; }
    public String getActors() { return actors; }
    public String getGenre() { return genre; }
    public String getRatings() { return ratings; }
    public int getReleaseYear() {return releaseYear; }
    public Long getId() { return id; }

    //setters
    public void setName(String name) { this.name = name; }
    public void setPosterURL(String posterURL) { this.posterURL = posterURL; }
    public void setActors(String actors) { this.actors = actors; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setRatings(String ratings) { this.ratings = ratings; }
    public void setReleaseYear(int year) {this.releaseYear = year; }
    public void setId(Long id) { this.id = id; }

}
