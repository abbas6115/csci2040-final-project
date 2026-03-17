package csci2040u.bytecouncil.ui;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import csci2040u.bytecouncil.backend.Movie;

public class MovieCard extends HorizontalLayout {
    public MovieCard(Movie movie){
        VerticalLayout movieDetails = new VerticalLayout(
                new Paragraph("Name: " + valueOrNA(movie.getName())),
                new Paragraph("Actors: " + valueOrNA(movie.getActors())),
                new Paragraph("Genre: " + valueOrNA(movie.getGenre())),
                new Paragraph("Ratings: " + valueOrNA(movie.getRatings())),
                new Paragraph("Release Year: " + yearOrNA(movie.getReleaseYear()))

        );
        movieDetails.setSpacing(false);
        movieDetails.setPadding(false);
        movieDetails.getStyle().set("font-size", "0.9rem");
        movieDetails.setFlexGrow(1);

        //image
        Image poster = new Image(movie.getPosterURL(),"");
        poster.getStyle().set("object-fit", "contain");
        poster.setWidth("400px");
        poster.setHeight("240px");
        poster.getStyle().set("background-image", "url('https://cdn-icons-png.flaticon.com/128/1665/1665664.png')");
        poster.getStyle().set("object-fit", "cover");
        poster.getStyle().set("border-radius", "4px");
        poster.getStyle().set("background-repeat", "no-repeat");
        poster.getStyle().set("background-position", "center");
        poster.getStyle().set("background-size", "contain");

        //make it horizontal layout so you can place the image beside the details
        this.add(movieDetails);
        this.add(poster);

        this.setSpacing(true);
        this.getStyle().set("border", "1px solid #d3d3d3");
        this.getStyle().set("border-radius", "8px");
        this.getStyle().set("padding", "8px");
        this.getStyle().set("min-height", "160px");
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.setAlignItems(Alignment.CENTER);

    }

    private String valueOrNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
    private String yearOrNA(int releaseYear) {
        return releaseYear <= 0 ? "N/A" : Integer.toString(releaseYear);
    }
}
