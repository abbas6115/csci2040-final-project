package csci2040u.bytecouncil.backend;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Movie {
    String name;
    String posterURL;
    String actors;
    String genre;
    String ratings;
    int year;


    public Movie(){
        name = "NA";
        posterURL = "NA";
        actors = "NA";
        genre = "NA";
        ratings="0.0";
    }

    public Movie(String name, String posterURL, String actors, String genre, String ratings,int year) {
        this.name = name;
        this.posterURL = posterURL;
        this.actors = actors;
        this.genre = genre;
        this.ratings = ratings;
        this.year=year;
    }


    //getters
    public String getName() { return name; }
    public String getPosterURL() { return posterURL; }
    public String getActors() { return actors; }
    public String getGenre() { return genre; }
    public String getRatings() { return ratings; }
    public int getYear(){return year;}

    //setters
    public void setName(String name) { this.name = name; }
    public void setPosterURL(String posterURL) { this.posterURL = posterURL; }
    public void setActors(String actors) { this.actors = actors; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setRatings(String ratings) { this.ratings = ratings; }
    public void setYear(int year){this.year=year;}

}
