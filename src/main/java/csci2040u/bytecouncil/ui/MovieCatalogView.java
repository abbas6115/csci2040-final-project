package csci2040u.bytecouncil.ui;

import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
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
import csci2040u.bytecouncil.backend.CustomUser;
import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.backend.MovieCsvWriter;
import csci2040u.bytecouncil.ui.movieviewcomp.Header;
import csci2040u.bytecouncil.ui.movieviewcomp.MovieCard;
import csci2040u.bytecouncil.ui.movieviewcomp.MovieDetailsLayout;
import csci2040u.bytecouncil.ui.movieviewcomp.WatchHistorySidebar;

import java.util.ArrayList;
import java.util.List;

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

        // Filter State Holders
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
        Button genreFilterButton = createGenreButton(genreFilter, applyFilters);
        Button ratingFilterButton = createRatingButton(minRatingFilter, maxRatingFilter, applyFilters);
        Button yearFilterButton = createYearButton(minYearFilter, maxYearFilter, applyFilters);
        Button clearAllFiltersButton = new Button("Clear All Filters", event -> {
            genreFilter[0] = "";
            minRatingFilter[0] = null;
            maxRatingFilter[0] = null;
            minYearFilter[0] = null;
            maxYearFilter[0] = null;
            searchField.clear();
            applyFilters.run();
        });

        HorizontalLayout filterButtons = new HorizontalLayout(genreFilterButton, ratingFilterButton, yearFilterButton, clearAllFiltersButton);
        filterButtons.setWidthFull();
        filterButtons.setJustifyContentMode(JustifyContentMode.CENTER);

        add(filterButtons, catalogGrid);

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
            movieCard.addClickListener(event -> {
                openMovieDetails(movie);
                addMovieToHistory(movie, authCont);
            });
            // Insert before the sentinel
            catalogGrid.addComponentAtIndex(catalogGrid.getComponentCount() - 1, movieCard);
        }
        currentPage++;
    }



    private Button createGenreButton(String[] genreFilter, Runnable applyFilters) {
        return new Button("Filter Genre", event -> {
            Dialog dialog = new Dialog();
            TextField genreField = new TextField("Genre");
            genreField.setValue(genreFilter[0]);
            Button apply = new Button("Apply", e -> {
                genreFilter[0] = genreField.getValue().trim();
                applyFilters.run();
                dialog.close();
            });
            dialog.add(genreField, apply);
            dialog.open();
        });
    }

    private Button createRatingButton(Double[] min, Double[] max, Runnable applyFilters) {
        return new Button("Filter Rating", event -> {
            Dialog dialog = new Dialog();
            NumberField minF = new NumberField("Min");
            NumberField maxF = new NumberField("Max");
            minF.setValue(min[0]); maxF.setValue(max[0]);
            Button apply = new Button("Apply", e -> {
                min[0] = minF.getValue();
                max[0] = maxF.getValue();
                applyFilters.run();
                dialog.close();
            });
            dialog.add(new VerticalLayout(minF, maxF, apply));
            dialog.open();
        });
    }

    private Button createYearButton(Integer[] min, Integer[] max, Runnable applyFilters) {
        return new Button("Filter Year", event -> {
            Dialog dialog = new Dialog();
            IntegerField minF = new IntegerField("Min Year");
            IntegerField maxF = new IntegerField("Max Year");
            minF.setValue(min[0]); maxF.setValue(max[0]);
            Button apply = new Button("Apply", e -> {
                min[0] = minF.getValue();
                max[0] = maxF.getValue();
                applyFilters.run();
                dialog.close();
            });
            dialog.add(new VerticalLayout(minF, maxF, apply));
            dialog.open();
        });
    }

    private void sidebarInit() {
        WatchHistorySidebar historySidebar = new WatchHistorySidebar();
        Button historyBtn = new Button("Watch History", e -> historySidebar.toggle());
        historyBtn.getStyle().set("position", "fixed").set("left", "0").set("top", "10%").set("z-index", "999");
        UIColors.setSecondary(historyBtn);
        historyBtn.getStyle().set("color", UIColors.TEXTCOLORHEADER).set("border-radius", "0 5px 5px 0");
        add(historySidebar.getBackdrop(), historySidebar, historyBtn);
    }

    public static void openMovieDetails(Movie movie) {
        Dialog detailDialog = new Dialog();
        detailDialog.setCloseOnOutsideClick(true);
        detailDialog.setModal(true);
        detailDialog.getElement().getStyle().set("--lumo-base-color", UIColors.SECONDARYCOLOR);
        detailDialog.add(new MovieDetailsLayout(movie));
        detailDialog.setWidth("80%"); detailDialog.setHeight("80%");
        detailDialog.open();
    }

    private void addMovieToHistory(Movie movie, AuthenticationContext authCont) {
        if(authCont.isAuthenticated()){
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof CustomUser currentUser) {
                currentUser.addToWatchlist(movie);
            }
        }
    }

    private boolean matchesAllFilters(Movie movie, String searchTerm, String genre, Double minR, Double maxR, Integer minY, Integer maxY) {
        if (!matchesSearch(movie, searchTerm)) return false;
        if (genre != null && !genre.isEmpty() && !contains(movie.getGenre(), genre.toLowerCase())) return false;

        Double rating = parseRating(movie.getRatings());
        if (minR != null && (rating == null || rating < minR)) return false;
        if (maxR != null && (rating == null || rating > maxR)) return false;

        return isInRange(movie.getReleaseYear(), minY, maxY);
    }

    private boolean matchesSearch(Movie movie, String searchTerm) {
        String norm = searchTerm == null ? "" : searchTerm.trim().toLowerCase();
        return norm.isEmpty() || contains(movie.getName(), norm);
    }

    private boolean contains(String value, String search) {
        return value != null && value.toLowerCase().contains(search);
    }

    private Double parseRating(String value) {
        try { return Double.valueOf(value.trim()); } catch (Exception e) { return null; }
    }

    private boolean isInRange(int val, Integer min, Integer max) {
        if (min != null && val < min) return false;
        return max == null || val <= max;
    }
}