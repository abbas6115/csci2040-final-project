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
    public MovieCatalogView(MovieCsvWriter movieCsvWriter, AuthenticationContext authCont) {

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidth("100%"); // Make the header take full width
        Button adminButton = new Button("Admin View", event -> {
            UI.getCurrent().navigate(AdminView.class);
        });
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
                catalogGrid.add(new MovieCard(movie));
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

}
