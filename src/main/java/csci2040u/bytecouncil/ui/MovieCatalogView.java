package csci2040u.bytecouncil.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.backend.MovieCsvWriter;

//Anonymous allowed tells springboot you don't need to login to access this page
//route("") tells its default page, we can change this later
@Route("")
@AnonymousAllowed
public class MovieCatalogView extends VerticalLayout {
    public MovieCatalogView(MovieCsvWriter movieCsvWriter){
        add(new H1("Movie Catalog View"));

        Div catalogGrid = new Div();
        catalogGrid.getStyle().set("display", "grid");
        catalogGrid.getStyle().set("grid-template-columns", "repeat(3, minmax(0, 1fr))");
        catalogGrid.getStyle().set("gap", "10px");
        catalogGrid.getStyle().set("width", "100%");

        for (Movie movie : movieCsvWriter.readMovies()) {
            catalogGrid.add(createMovieCard(movie));
        }

        add(catalogGrid);
    }

    private VerticalLayout createMovieCard(Movie movie) {
        VerticalLayout movieCard = new VerticalLayout(
                new Paragraph("Name: " + valueOrNA(movie.getName())),
                new Paragraph("Actors: " + valueOrNA(movie.getActors())),
                new Paragraph("Genre: " + valueOrNA(movie.getGenre())),
                new Paragraph("Ratings: " + valueOrNA(movie.getRatings())),
                new Paragraph("Release Year: " + yearOrNA(movie.getReleaseYear()))
        );
        movieCard.setSpacing(false);
        movieCard.setPadding(false);
        movieCard.getStyle().set("border", "1px solid #d3d3d3");
        movieCard.getStyle().set("border-radius", "8px");
        movieCard.getStyle().set("padding", "8px");
        movieCard.getStyle().set("font-size", "0.9rem");
        movieCard.getStyle().set("min-height", "160px");
        return movieCard;
    }

    private String valueOrNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }

    private String yearOrNA(int releaseYear) {
        return releaseYear <= 0 ? "N/A" : Integer.toString(releaseYear);
    }
}
