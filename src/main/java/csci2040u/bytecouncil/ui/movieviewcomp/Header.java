package csci2040u.bytecouncil.ui.movieviewcomp;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.security.AuthenticationContext;
import csci2040u.bytecouncil.ui.AdminView;
import csci2040u.bytecouncil.ui.LoginView;
import csci2040u.bytecouncil.ui.UIColors;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
public class Header extends HorizontalLayout {
    private final TextField minYear;
    private final TextField maxYear;
    private final TextField minRating;
    private final TextField maxRating;
    private final TextField searchField;
    private final Button applyFilter;
    private final Button removeFilter;
    // ComboBox is much more reliable for "breaking out" of absolute-positioned menus
    ComboBox<String> genreSelect;

    private static final List<String> GENRE_OPTIONS = List.of(
            "Drama", "Documentary", "Comedy", "Animation", "Horror",
            "Romance", "Music", "Thriller", "Action", "Crime",
            "Family", "Fantasy", "Adventure", "TV Movie",
            "Science Fiction", "Mystery", "History", "War", "Western"
    );

    public Header(AuthenticationContext authCont) {
        //   Header layout
        setHeight("60px");
        setWidthFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        getStyle().set("background-color", UIColors.SECONDARYCOLOR);
        getStyle().set("padding", "0 25px");
        getStyle().set("box-sizing", "border-box");
        getStyle().set("overflow", "visible");

        HorizontalLayout leftSection = new HorizontalLayout();
        leftSection.setAlignItems(Alignment.CENTER);
        leftSection.getStyle().set("flex-shrink", "0");
        H1 title = new H1("Filmbase");
        title.getStyle().set("color", UIColors.TEXTCOLORHEADER).set("margin", "0");
        leftSection.add(title);

        if (authCont.hasRole("ADMIN")) {
            Button adminBtn = new Button("Admin", e -> UI.getCurrent().navigate(AdminView.class));
            adminBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            adminBtn.getStyle().set("color", "white").set("margin-left", "15px");
            leftSection.add(adminBtn);
        }
        add(leftSection);

        //   Search bar
        searchField = new TextField();
        searchField.setPlaceholder("Search movies...");
        searchField.setWidthFull();
        searchField.getStyle().set("height", "32px");
        searchField.getStyle().set("min-height", "32px");
        searchField.getStyle().set("--lumo-text-field-size", "32px");
        searchField.getStyle().set("--lumo-contrast-10pct", "white");
        searchField.getStyle().set("background-color", "white");
        searchField.getStyle().set("border-radius", "8px");

        Div searchWrapper = new Div();
        searchWrapper.getStyle().set("position", "relative");
        searchWrapper.getStyle().set("margin", "0 40px");
        searchWrapper.getStyle().set("display", "flex");
        searchWrapper.getStyle().set("align-items", "center");
        searchWrapper.getStyle().set("flex-grow", "1");
        searchWrapper.getStyle().set("max-width", "1200px");
        searchWrapper.getStyle().set("overflow", "visible");
        setFlexGrow(1.0, searchWrapper);

        Icon filterIcon = new Icon(VaadinIcon.FILTER);
        filterIcon.getStyle().set("color", "#444");
        filterIcon.getStyle().set("width", "22px");
        filterIcon.getStyle().set("height", "22px");

        Button filterButton = new Button(filterIcon);
        filterButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        filterButton.getStyle().set("padding", "0").set("margin", "0 5px 0 0").set("min-width", "36px").set("height", "32px");
        searchField.setSuffixComponent(filterButton);

        // Filter dropdown
        VerticalLayout filterDropdown = new VerticalLayout();
        filterDropdown.setVisible(false);
        filterDropdown.setSpacing(false);
        filterDropdown.setPadding(true);
        filterDropdown.setWidth("280px");
        filterDropdown.getStyle()
                .set("position", "absolute").set("top", "38px").set("right", "0")
                .set("background-color", "white").set("border", "1px solid #d1d1d1")
                .set("border-radius", "8px 0 8px 8px").set("z-index", "10000")
                .set("box-shadow", "0 10px 30px rgba(0, 0, 0, 0.3)");

        // Genre filter
        genreSelect = new ComboBox<>();
        genreSelect.setItems(GENRE_OPTIONS);
        genreSelect.setWidthFull();
        genreSelect.setPlaceholder("Select genre");


        genreSelect.getElement().executeJs("this.$.overlay.style.zIndex = '11000'");
        Details genreDetails = new Details("Genre", genreSelect);
        genreDetails.getStyle().set("position", "relative").set("z-index", "10001");
        genreDetails.setWidthFull();

        // Display Genre dropdown over the field
        genreSelect.getElement().executeJs(
                "this.$.overlay.style.zIndex = '20000';" +
                        "this.$.overlay.setAttribute('modeless', true);"
        );

        // Year filter
        minYear = new TextField("Min Year");
        maxYear = new TextField("Max Year");
        minYear.setWidthFull(); maxYear.setWidthFull();
        Details yearDetails = new Details("Year Range", new VerticalLayout(minYear, maxYear));
        yearDetails.setWidthFull();

        // Rating filter
        minRating = new TextField("Min Rating");
        maxRating = new TextField("Max Rating");
        minRating.setWidthFull(); maxRating.setWidthFull();
        Details ratingDetails = new Details("Rating Range", new VerticalLayout(minRating, maxRating));
        ratingDetails.setWidthFull();

        // actions
        HorizontalLayout actions = new HorizontalLayout();
        actions.setWidthFull();
        actions.getStyle().set("margin-top", "15px");

        applyFilter = new Button("Apply", e -> filterDropdown.setVisible(false));
        applyFilter.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

        removeFilter = new Button("Remove", e -> {
            genreSelect.clear();
            minYear.clear(); maxYear.clear();
            minRating.clear(); maxRating.clear();
        });
        removeFilter.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);

        actions.add(applyFilter, removeFilter);
        actions.setFlexGrow(1, applyFilter, removeFilter);

        filterDropdown.add(genreDetails, yearDetails, ratingDetails, actions);
        filterDropdown.getStyle().set("overflow", "visible");
        filterButton.addClickListener(e -> filterDropdown.setVisible(!filterDropdown.isVisible()));
        searchWrapper.add(searchField, filterDropdown);
        add(searchWrapper);

        filterDropdown.getStyle()
                .set("position", "absolute")
                .set("z-index", "10000")
                .set("overflow", "visible")
                .set("top", "38px").set("right", "0")
                .set("background-color", "white").set("border", "1px solid #d1d1d1")
                .set("border-radius", "8px 0 8px 8px").set("z-index", "10000")
                .set("box-shadow", "0 10px 30px rgba(0, 0, 0, 0.3)")
                .set("overflow", "visible");

        getStyle().set("overflow", "visible");
        getStyle().set("z-index", "10");

        searchWrapper.getStyle().set("overflow", "visible");




        // Auth section
        HorizontalLayout authSection = new HorizontalLayout();
        authSection.setAlignItems(Alignment.CENTER);
        authSection.getStyle().set("flex-shrink", "0");

        if (authCont.isAuthenticated()) {
            String username = authCont.getAuthenticatedUser(UserDetails.class).get().getUsername();
            Button userMenuBtn = new Button(username, new Icon(VaadinIcon.USER));
            userMenuBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            userMenuBtn.getStyle().set("color", "white");
            ContextMenu userMenu = new ContextMenu(userMenuBtn);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Logout", e -> authCont.logout());
            authSection.add(userMenuBtn);
        } else {
            Button signIn = new Button("Sign in", e -> UI.getCurrent().navigate(LoginView.class));
            signIn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            signIn.getStyle().set("color", "white");
            authSection.add(signIn);
        }
        add(authSection);
    }

    public String getGenre(){
        return genreSelect.getValue();
    }

    public String getMinYear(){
        return minYear.getValue();
    }

    public String getMaxYear(){
        return maxYear.getValue();
    }

    public String getMinRating(){
        return minRating.getValue();
    }

    public String getMaxRating(){
        return maxRating.getValue();
    }

    public TextField getSearchField() {
        return searchField;
    }

    public Button getApplyFilter() {
        return applyFilter;
    }

    public Button getRemoveFilter() {
        return removeFilter;
    }
}