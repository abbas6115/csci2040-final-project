package csci2040u.bytecouncil.ui;


import java.util.Locale;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.backend.MovieDatabaseCommands;
import jakarta.annotation.security.RolesAllowed;

@Route("admin")
@RolesAllowed({"ADMIN"})
public class AdminView extends VerticalLayout implements BeforeEnterObserver {
    AuthenticationContext authCont;

    public AdminView(MovieDatabaseCommands commands, AuthenticationContext authenticationContext) {
        this.authCont=authenticationContext;

        setSizeFull();

        setPadding(false);
        setSpacing(false);
        getStyle().set("overflow", "hidden");

        //creates the grid for the movie databases
        var crud = new GridCrud<>(Movie.class,commands);
        crud.getStyle().set("margin", "20px auto");
        crud.getStyle().set("width", "95%");

        //lists columns that can be viewed
        crud.getGrid().setColumns( "name",  "actors",  "genre",  "ratings", "releaseYear", "posterURL", "id");

                // Keep edit form fields aligned with add form fields.
                String[] editableFields = {"name", "actors", "genre", "ratings", "releaseYear", "posterURL"};
                crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD, editableFields);
                crud.getCrudFormFactory().setVisibleProperties(CrudOperation.UPDATE, editableFields);



                // Explicitly wire button operations to backend command handlers.
                crud.setAddOperation(commands::add);
                crud.setUpdateOperation(commands::update);
                crud.setDeleteOperation(commands::delete);

        // Create a search field to filter movies by name in the grid
        TextField searchField = new TextField("Search movies");
        searchField.setPlaceholder("Search by movie name");
        searchField.setClearButtonVisible(true);
        searchField.getStyle().set("width", "95%");
        searchField.getStyle().set("margin", "20px auto");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> {
            String searchTerm = event.getValue() == null ? "" : event.getValue();
            crud.getGrid().setItems(
                    commands.findAll().stream()
                            .filter(movie -> matchesSearch(movie, searchTerm))
                            .toList()
            );
        });

        // Make header
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.setHeight("60px");
        headerLayout.getStyle().set("min-height", "60px");
        headerLayout.getStyle().set("max-height", "60px");
        headerLayout.getStyle().set("margin", "0");
        headerLayout.getStyle().set("padding", "0 20px");
        headerLayout.getStyle().set("display", "flex");
        headerLayout.getStyle().set("align-items", "center");
        headerLayout.getStyle().set("color", "#f8f9fa");

        Button catalogButton = new Button("MovieCatalogView", event -> {UI.getCurrent().navigate(MovieCatalogView.class);});
        catalogButton.getStyle().setColor(UIColors.TEXTCOLORHEADER);

        H1 title = new H1("FilmBase");
        title.getStyle().setColor(UIColors.TEXTCOLORHEADER);

        // This stops the 20px padding from pushing the header past the screen width
        headerLayout.getStyle().set("box-sizing", "border-box");
        UIColors.setSecondary(headerLayout);
        add(headerLayout);
        headerLayout.add(title, catalogButton);

        //logout from admin
        HorizontalLayout userPanel=new HorizontalLayout();
        String username="";
        if(authCont.getAuthenticatedUser(UserDetails.class).isPresent()) {
            username = authCont.getAuthenticatedUser(UserDetails.class).get().getUsername();
        }
        Span userlabel=new Span(username);
        userlabel.getStyle().set("color",UIColors.TEXTCOLORHEADER);
        Span spacer=new Span();
        headerLayout.expand(spacer);

        Icon userIcon=new Icon(VaadinIcon.USER);
        userIcon.getStyle().set("color","white");
        userPanel.add(userIcon,userlabel);
        headerLayout.add(spacer,userPanel);

        ContextMenu menu = new ContextMenu(userPanel);

        menu.setOpenOnClick(true);

        menu.addItem("Logout", e -> {
            authCont.logout();
            UI.getCurrent().getPage().setLocation("");
        });

        //add components to page
        if(authCont.hasRole("ADMIN")) {
            add(
                    headerLayout,
                    searchField,
                    crud
            );
        }
    }

    // Helper method to check if a movie matches the search term
    private boolean matchesSearch(Movie movie, String searchTerm) {
        String normalizedSearch = searchTerm.trim().toLowerCase(Locale.ROOT);
        if (normalizedSearch.isEmpty()) {
            return true;
        }

        return contains(movie.getName(), normalizedSearch);
    }

    private boolean contains(String value, String searchTerm) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(searchTerm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!authCont.hasRole("ADMIN")) {
            event.rerouteTo(""); // Or another page
        }
    }
}
