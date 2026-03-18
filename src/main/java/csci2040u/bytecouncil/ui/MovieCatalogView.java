package csci2040u.bytecouncil.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
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

        Button signInButton = new Button("Sign in", event -> {
            UI.getCurrent().navigate(LoginView.class);
        });
        signInButton.setVisible(!authCont.isAuthenticated());
        Span spacer=new Span();
        headerLayout.expand(spacer);

        H1 title = new H1("Movie Catalog View");


        headerLayout.add(title, adminButton,spacer,signInButton);


        add(headerLayout);

        // Create a search field to filter movies by name in the grid
        TextField searchField = new TextField("Search movies");
        searchField.setPlaceholder("Search by movie name");
        searchField.setClearButtonVisible(true);
        searchField.setWidthFull();
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        // Keep  filter selections inmutable holders so button handlers can update them
        String[] genreFilter = {""};
        Double[] minRatingFilter = {null};
        Double[] maxRatingFilter = {null};
        Integer[] minYearFilter = {null};
        Integer[] maxYearFilter = {null};

        Div catalogGrid =new Div();
        // Render a compact 3-column card grid on the main page
        catalogGrid.getStyle().set("display", "grid");
        catalogGrid.getStyle().set("grid-template-columns", "repeat(3, minmax(0, 1fr))");
        catalogGrid.getStyle().set("gap", "10px");
        catalogGrid.getStyle().set("width", "100%");

        List<Movie> allMovies = new ArrayList<>(movieCsvWriter.readMovies());

    // Reuse one refresh action so search and all filter buttons stay in sync
        Runnable applyFilters = () -> refreshCatalog(
                catalogGrid,
                allMovies,
                searchField.getValue(),
                genreFilter[0],
                minRatingFilter[0],
                maxRatingFilter[0],
                minYearFilter[0],
                maxYearFilter[0]
        );

            // Prompt for a genre value and apply it as a case-insensitive contains filter
        Button genreFilterButton = new Button("Filter Genre", event -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Filter by Genre");

            TextField genreField = new TextField("Genre");
            genreField.setWidthFull();
            genreField.setPlaceholder("e.g. Action");
            genreField.setValue(genreFilter[0] == null ? "" : genreFilter[0]);

            Button applyButton = new Button("Apply", click -> {
                // Save the latest genre input and rerender the catalog
                genreFilter[0] = genreField.getValue() == null ? "" : genreField.getValue().trim();
                applyFilters.run();
                dialog.close();
            });
            Button clearButton = new Button("Clear", click -> {
                // Remove only the genre filter while keeping other filters intact
                genreFilter[0] = "";
                applyFilters.run();
                dialog.close();
            });
            Button cancelButton = new Button("Cancel", click -> dialog.close());

            dialog.add(genreField, new HorizontalLayout(applyButton, clearButton, cancelButton));
            dialog.open();
        });

        // Prompt for minimum and maximum rating values and enforce a valid range
        Button ratingFilterButton = new Button("Filter Rating Range", event -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Filter by Rating Range");

            NumberField minRatingField = new NumberField("Minimum Rating");
            minRatingField.setMin(0);
            minRatingField.setMax(10);
            minRatingField.setStep(0.1);
            minRatingField.setWidthFull();
            if (minRatingFilter[0] != null) {
                minRatingField.setValue(minRatingFilter[0]);
            }

            NumberField maxRatingField = new NumberField("Maximum Rating");
            maxRatingField.setMin(0);
            maxRatingField.setMax(10);
            maxRatingField.setStep(0.1);
            maxRatingField.setWidthFull();
            if (maxRatingFilter[0] != null) {
                maxRatingField.setValue(maxRatingFilter[0]);
            }

            Button applyButton = new Button("Apply", click -> {
                Double min = minRatingField.getValue();
                Double max = maxRatingField.getValue();
                if (min != null && max != null && min > max) {
                    // Prevent an inverted range so filtering logic remains deterministic
                    Notification.show("Minimum rating cannot be greater than maximum rating.");
                    return;
                }

                // Save rating boundaries and apply
                minRatingFilter[0] = min;
                maxRatingFilter[0] = max;
                applyFilters.run();
                dialog.close();
            });
            Button clearButton = new Button("Clear", click -> {
                // Remove only the rating filter while keeping the others as-is
                minRatingFilter[0] = null;
                maxRatingFilter[0] = null;
                applyFilters.run();
                dialog.close();
            });
            Button cancelButton = new Button("Cancel", click -> dialog.close());

            dialog.add(minRatingField, maxRatingField, new HorizontalLayout(applyButton, clearButton, cancelButton));
            dialog.open();
        });

        // Prompt for release-year boundaries and apply them as an inclusive range
        Button yearFilterButton = new Button("Filter Release Year Range", event -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Filter by Release Year Range");

            IntegerField minYearField = new IntegerField("Minimum Year");
            minYearField.setWidthFull();
            if (minYearFilter[0] != null) {
                minYearField.setValue(minYearFilter[0]);
            }

            IntegerField maxYearField = new IntegerField("Maximum Year");
            maxYearField.setWidthFull();
            if (maxYearFilter[0] != null) {
                maxYearField.setValue(maxYearFilter[0]);
            }

            Button applyButton = new Button("Apply", click -> {
                Integer min = minYearField.getValue();
                Integer max = maxYearField.getValue();
                if (min != null && max != null && min > max) {
                    // Prevent an inverted range so year filtering behaves predictably
                    Notification.show("Minimum year cannot be greater than maximum year.");
                    return;
                }

                // Save year boundaries and apply
                minYearFilter[0] = min;
                maxYearFilter[0] = max;
                applyFilters.run();
                dialog.close();
            });
            Button clearButton = new Button("Clear", click -> {
                // Remove only the year filter while leaving other filters untouched
                minYearFilter[0] = null;
                maxYearFilter[0] = null;
                applyFilters.run();
                dialog.close();
            });
            Button cancelButton = new Button("Cancel", click -> dialog.close());

            dialog.add(minYearField, maxYearField, new HorizontalLayout(applyButton, clearButton, cancelButton));
            dialog.open();
        });

        // Reset search text and every active filter in one click
        Button clearAllFiltersButton = new Button("Clear All Filters", event -> {
            genreFilter[0] = "";
            minRatingFilter[0] = null;
            maxRatingFilter[0]= null;
            minYearFilter[0] = null;
            maxYearFilter[0] = null;
            searchField.clear();
            applyFilters.run();
        });

        // Keep all filter actions in one row directly under the search field
        HorizontalLayout filterButtons = new HorizontalLayout(
                genreFilterButton,
                ratingFilterButton,
                yearFilterButton,
                clearAllFiltersButton
        );
        filterButtons.setWidthFull();
        filterButtons.getStyle().set("flex-wrap", "wrap");

        // Render initial catalog state before user interaction
        applyFilters.run();

        // Route search typing through the same combined filter pipeline
        searchField.addValueChangeListener(event ->
                applyFilters.run()
        );

        add(searchField, filterButtons, catalogGrid);
    }

    // Helper method to refresh the catalog grid based on all activ filters
    private void refreshCatalog(
            Div catalogGrid,
            List<Movie> allMovies,
            String  searchTerm,
            String genreFilter,
            Double minRating,
            Double maxRating,
            Integer minYear,
            Integer maxYear
    ) {
        catalogGrid.removeAll();
        for (Movie movie : allMovies){
            if (matchesAllFilters(movie, searchTerm, genreFilter, minRating, maxRating, minYear, maxYear)) {
                catalogGrid.add(new MovieCard(movie));
            }
        }
    }

    private boolean matchesAllFilters(
            Movie movie,
            String searchTerm,
            String genreFilter,
            Double minRating,
            Double maxRating,
            Integer minYear,
            Integer maxYear
    ) {
        //Search term is always applied first
        if (!matchesSearch(movie, searchTerm)) {
            return false;
        }

        // Genre filter is a case-insensitive contains match
        String normalizedGenre = genreFilter == null ? "" : genreFilter.trim().toLowerCase(Locale.ROOT);
        if (!normalizedGenre.isEmpty() && !contains(movie.getGenre(), normalizedGenre)) {
            return false;
        }

        // Ratings are stored as text in the model, so parse before comparison
        Double rating = parseRating(movie.getRatings());
        if (minRating != null && (rating == null || rating < minRating)) {
            return false;
        }
        if (maxRating != null && (rating == null || rating > maxRating)) {
            return false;
        }

        // Year boundaries are inclusive
        return isInRange(movie.getReleaseYear(), minYear, maxYear);
    }

    // Helper method to check if a movie matches the search ter
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

    // Safely parse rating text and ignore malformed values
    private Double parseRating(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Double.valueOf(value.trim());
        }catch (NumberFormatException ex) {
            return null;
        }
    }

    // Evaluate an inclusive integer range where each bound is optional
    private boolean isInRange(int value, Integer min, Integer max) {
        if (min != null && value < min) {
            return false;
        }

        return max == null || value <= max;
    }

}
