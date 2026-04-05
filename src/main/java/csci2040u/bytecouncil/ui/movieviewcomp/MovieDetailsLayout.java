package csci2040u.bytecouncil.ui.movieviewcomp;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.ui.UIColors;

public class MovieDetailsLayout extends VerticalLayout {
    public MovieDetailsLayout(Movie movie){
        this.setWidthFull();
        this.setPadding(true);
        this.setSpacing(true);
        this.setAlignItems(Alignment.STRETCH);

        this.getStyle().setBackground(UIColors.DARKMODEBACKGROUND);

//        movie immage
        Div moviePoster = new Div();
        Image poster = new Image(movie.getPosterURL(), "");
        poster.getStyle().set("background-image", "url('https://cdn-icons-png.flaticon.com/128/1665/1665664.png')");
        poster.setHeight("280px");
        poster.setWidth("187");
        poster.getStyle().set("border-radius", "4px");
        moviePoster.add(poster);

        //box settings
        VerticalLayout detailsBox = new VerticalLayout();
        detailsBox.setWidthFull();
        detailsBox.setPadding(true);
        detailsBox.setSpacing(false);

        // border
        detailsBox.getStyle().set("border-radius", "8px");
        detailsBox.getStyle().set("background-color", UIColors.MOVIECARDBACKGROUND);

        // Add Content to the Box
        Paragraph name = new Paragraph(movie.getName());
        name.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        name.getStyle().set("font-weight", "bold");
        name.getStyle().set("margin-bottom", "5px");

        Span actors = new Span("Actors: " + movie.getActors());
        actors.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        actors.getStyle().set("font-size", "0.9em");

        Span genre = new Span("Genre: " + movie.getGenre());
        genre.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        genre.getStyle().set("font-size", "0.9em");

        Span year = new Span("Year: " + movie.getReleaseYear());
        year.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        year.getStyle().set("font-size", "0.9em");

        Span ratings = new Span("Rating: " + movie.getRatings());
        ratings.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        ratings.getStyle().set("font-size", "0.9em");

        detailsBox.add(name, genre, actors, year, ratings);

        // Assemble the layout
        add(moviePoster, detailsBox);
    }

}
