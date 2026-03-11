package csci2040u.bytecouncil.ui;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
        // Render a compact 3-column card grid on the main page
        catalogGrid.getStyle().set("display", "grid");
        catalogGrid.getStyle().set("grid-template-columns", "repeat(3, minmax(0, 1fr))");
        catalogGrid.getStyle().set("gap", "10px");
        catalogGrid.getStyle().set("width", "100%");

        // Build cards from CSV-backed movie objects
        for (Movie movie : movieCsvWriter.readMovies()) {
            catalogGrid.add(createMovieCard(movie));
        }

        add(catalogGrid);
    }

    private HorizontalLayout createMovieCard(Movie movie) {
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
        HorizontalLayout movieCard=new HorizontalLayout(movieDetails,poster);

        movieCard.setSpacing(true);
        movieCard.getStyle().set("border", "1px solid #d3d3d3");
        movieCard.getStyle().set("border-radius", "8px");
        movieCard.getStyle().set("padding", "8px");
        movieCard.getStyle().set("min-height", "160px");
        movieCard.setJustifyContentMode(JustifyContentMode.BETWEEN);
        movieCard.setAlignItems(Alignment.CENTER);

        return movieCard;
    }

    private String valueOrNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }

    private String yearOrNA(int releaseYear) {
        return releaseYear <= 0 ? "N/A" : Integer.toString(releaseYear);
    }
}
