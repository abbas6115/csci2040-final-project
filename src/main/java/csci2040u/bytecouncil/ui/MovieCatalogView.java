package csci2040u.bytecouncil.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import com.vaadin.flow.spring.security.AuthenticationContext;
import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.backend.MovieCsvWriter;

//Anonymous allowed tells springboot you don't need to login to access this page
//route("") tells its default page, we can change this later
@Route("")
@RouteAlias("user")
@AnonymousAllowed
public class MovieCatalogView extends VerticalLayout {
    public MovieCatalogView(MovieCsvWriter movieCsvWriter, AuthenticationContext authCont){

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%"); // Make the header take full width
        Button adminButton = new Button("Admin View", event -> {UI.getCurrent().navigate(AdminView.class);});
        adminButton.setVisible(authCont.hasRole("ADMIN"));
        H1 title = new H1("Movie Catalog View");


        headerLayout.add(title, adminButton);


        add(headerLayout);

        // Create a search field to filter movies by name in the grid
        TextField searchField = new TextField("Search movies");
        searchField.setPlaceholder("Search by movie name");
        searchField.setClearButtonVisible(true);
        searchField.setWidthFull();
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        Div catalogGrid = new Div();
        // Render a compact 3-column card grid on the main page
        catalogGrid.getStyle().set("display", "grid");
        catalogGrid.getStyle().set("grid-template-columns", "repeat(3, minmax(0, 1fr))");
        catalogGrid.getStyle().set("gap", "10px");
        catalogGrid.getStyle().set("width", "100%");

        List<Movie> allMovies = new ArrayList<>(movieCsvWriter.readMovies());
        refreshCatalog(catalogGrid, allMovies, "");

        searchField.addValueChangeListener(event ->
                refreshCatalog(catalogGrid, allMovies, event.getValue())
        );

        add(searchField, catalogGrid);
    }

    // Helper method to refresh the catalog grid based on the current search term
    private void refreshCatalog(Div catalogGrid, List<Movie> allMovies, String searchTerm) {
        catalogGrid.removeAll();
        for (Movie movie : allMovies) {
            if (matchesSearch(movie, searchTerm)) {
                catalogGrid.add(createMovieCard(movie));
            }
        }
    }

    // Helper method to check if a movie matches the search term
    private boolean matchesSearch(Movie movie, String searchTerm) {
        String normalizedSearch = searchTerm == null ? "" : searchTerm.trim().toLowerCase(Locale.ROOT);
        if (normalizedSearch.isEmpty()) {
            return true;
        }

        return contains(movie.getName(), normalizedSearch);
    }

    private boolean contains(String value, String searchTerm) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(searchTerm);
    }

    // Helper method to create a card layout for each movie
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
