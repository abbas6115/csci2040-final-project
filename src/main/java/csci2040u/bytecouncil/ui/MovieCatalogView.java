package csci2040u.bytecouncil.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

import csci2040u.bytecouncil.backend.CustomUser;
import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.backend.MovieCsvWriter;
import csci2040u.bytecouncil.ui.movieviewcomp.Header;
import csci2040u.bytecouncil.ui.movieviewcomp.MovieCard;
import csci2040u.bytecouncil.ui.movieviewcomp.MovieDetailsLayout;
import csci2040u.bytecouncil.ui.movieviewcomp.WatchHistorySidebar;

//Anonymous allowed tells springboot you don't need to login to access this page
//route("") tells its default page, we can change this later
@Route("")
@RouteAlias("user")
@AnonymousAllowed
public class MovieCatalogView extends VerticalLayout {
    private final AuthenticationContext authCont;
    private final Div catalogGrid;
    private final Span sentinel;

    // Lazy Loading State
    private int currentPage = 0;
    private final int PAGE_SIZE = 20;
    private List<Movie> filteredMovies = new ArrayList<>();

    public MovieCatalogView(MovieCsvWriter movieCsvWriter, AuthenticationContext authenticationContext) {
        this.authCont = authenticationContext;

        // Layout Setup
        this.getStyle().set("min-height", "100vh");
        UIColors.setMainBackground(this);
        this.getStyle().set("padding-top", "0");

        Header header = new Header(authCont);
        UIColors.setSecondary(header);
        header.setHeight("10%");
        header.getStyle().set("margin-left", "-16px");
        header.getStyle().set("margin-right", "-16px");
        header.getStyle().set("width", "calc(100% + 32px)");
        add(header);

        // Sidebar Initialization
        sidebarInit();

        // Search Field Setup
        TextField searchField = header.getSearchField();
        searchField.setValueChangeMode(ValueChangeMode.EAGER);

        // Grid Setup
        catalogGrid = new Div();
        catalogGrid.getStyle().set("margin", "20px auto");
        catalogGrid.getStyle().set("display", "grid");
        catalogGrid.getStyle().set("grid-template-columns", "repeat(5, minmax(0, 1fr))");
        catalogGrid.getStyle().set("gap", "15px");
        catalogGrid.getStyle().set("width", "85%");

        // Infinite Scroll Sentinel
        sentinel = new Span();
        sentinel.setId("sentinel");
        sentinel.getStyle().set("grid-column", "1 / -1");
        sentinel.getStyle().set("height", "10px");

        List<Movie> allMovies = new ArrayList<>(movieCsvWriter.readMovies());


        // Keep  filter selections inmutable holders so button handlers can update them
        String[] genreFilter = {""};
        Double[] minRatingFilter = {null};
        Double[] maxRatingFilter = {null};
        Integer[] minYearFilter = {null};
        Integer[] maxYearFilter = {null};

        Runnable applyFilters = () -> refreshCatalog(
                allMovies,
                searchField.getValue(),
                genreFilter[0],
                minRatingFilter[0],
                maxRatingFilter[0],
                minYearFilter[0],
                maxYearFilter[0]
        );

        // Filter Buttons
        Button clearAllFiltersButton = header.getRemoveFilter();
        clearAllFiltersButton.addClickListener(
                event -> {
            genreFilter[0] = "";
            minRatingFilter[0] = null;
            maxRatingFilter[0] = null;
            minYearFilter[0] = null;
            maxYearFilter[0] = null;
            searchField.clear();
            applyFilters.run();
        });

        Button applyFiltersButton = header.getApplyFilter();
        applyFiltersButton.addClickListener(
                event -> {
                    genreFilter[0] = header.getGenre();
                    minRatingFilter[0] = parseRating(header.getMinRating());
                    maxRatingFilter[0] = parseRating(header.getMaxRating());
                    minYearFilter[0] = parseYear(header.getMinYear());
                    maxYearFilter[0] = parseYear(header.getMinYear());

                    applyFilters.run();
                });


        add(catalogGrid);

        // Setup JS Infinite Scroll
        setupInfiniteScroll();

        // Initial Load
        applyFilters.run();

        searchField.addValueChangeListener(event -> applyFilters.run());
    }

    private void setupInfiniteScroll() {
        getElement().executeJs(
                "const observer = new IntersectionObserver((entries) => {" +
                        "  if (entries[0].isIntersecting) {" +
                        "    $0.$server.loadMore();" +
                        "  }" +
                        "}, { threshold: 0.1 });" +
                        "observer.observe(document.getElementById('sentinel'));",
                getElement()
        );
    }

    @ClientCallable
    public void loadMore() {
        if (currentPage * PAGE_SIZE < filteredMovies.size()) {
            loadNextPage();
        }
    }

    private void refreshCatalog(List<Movie> allMovies, String searchTerm, String genreFilter,
                                Double minRating, Double maxRating, Integer minYear, Integer maxYear) {
        catalogGrid.removeAll();
        catalogGrid.add(sentinel);
        currentPage = 0;

        filteredMovies = allMovies.stream()
                .filter(movie -> matchesAllFilters(movie, searchTerm, genreFilter, minRating, maxRating, minYear, maxYear))
                .toList();

        loadNextPage();
    }

    private void loadNextPage() {
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, filteredMovies.size());

        if (start >= filteredMovies.size()) return;

        for (int i = start; i < end; i++) {
            Movie movie = filteredMovies.get(i);
            MovieCard movieCard = new MovieCard(movie);
            movieCard.addClickListener(event -> openMovieDetails(movie));
            // Insert before the sentinel
            catalogGrid.addComponentAtIndex(catalogGrid.getComponentCount() - 1, movieCard);
        }
        currentPage++;
    }

    private void sidebarInit() {
        // 1. Initialize the sidebar
        WatchHistorySidebar historySidebar = new WatchHistorySidebar();
        Button historyBtn = new Button("Watch History", e -> historySidebar.toggle());
        historyBtn.getStyle().set("position", "fixed").set("left", "0").set("top", "10%").set("z-index", "999");
        UIColors.setSecondary(historyBtn);
        historyBtn.getStyle().set("color", UIColors.TEXTCOLORHEADER).set("border-radius", "0 5px 5px 0");
        add(historySidebar.getBackdrop(), historySidebar, historyBtn);
    }

    public static void openMovieDetails(Movie movie) {

        Dialog detailDialog = new Dialog();

        // Closes the dialog when los focus
        detailDialog.setCloseOnOutsideClick(true);
        // Closes the dialog if the user presses the 'Esc' key
        detailDialog.setCloseOnEsc(true);
        // Modal ensures the background is dimmed and inactive
        detailDialog.setModal(true);

        // Setting behaviors
        detailDialog.setCloseOnOutsideClick(true);
        detailDialog.setCloseOnEsc(true);
        detailDialog.getElement().getStyle().set("--lumo-base-color", UIColors.SECONDARYCOLOR);

        Button addToWatchHistoryButton = new Button("Add to Watch History", event -> {
            boolean added = addMovieToHistory(movie);
            if (added) {
                Notification.show("Added to watch history", 1800, Notification.Position.BOTTOM_END);
            }
        });
        UIColors.setSecondary(addToWatchHistoryButton);
        addToWatchHistoryButton.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        addToWatchHistoryButton.getStyle().set("margin-top", "8px");

        detailDialog.add(new MovieDetailsLayout(movie, addToWatchHistoryButton));
        detailDialog.setWidth("80%"); detailDialog.setHeight("80%");
        detailDialog.open();
    }

    private static boolean addMovieToHistory(Movie movie) {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUser currentUser) {
            boolean alreadyInHistory = currentUser.getRecentlyWatched().stream()
                    .anyMatch(existingMovie -> isSameMovie(existingMovie, movie));
            if (alreadyInHistory) {
                return false;
            }
            currentUser.addToWatchlist(movie);
            return true;
        }

        return false;
    }

    private static boolean isSameMovie(Movie first, Movie second) {
        if (first == null || second == null) {
            return false;
        }

        return Objects.equals(normalize(first.getName()), normalize(second.getName()))
                && first.getReleaseYear() == second.getReleaseYear()
                && Objects.equals(normalize(first.getGenre()), normalize(second.getGenre()));
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
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

    private Integer parseYear(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Integer.valueOf(value.trim());
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
