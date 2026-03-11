package csci2040u.bytecouncil.ui;


import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.backend.MovieDatabaseCommands;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.crudui.crud.impl.GridCrud;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {
    public AdminView(MovieDatabaseCommands commands){
        //creates the grid for the movie databases
        var crud=new GridCrud<>(Movie.class,commands);

        //lists columns that can be viewed
        crud.getGrid().setColumns( "name",  "actors",  "genre",  "ratings", "releaseYear", "posterURL","id");

        //lists editable columns for new entries
        crud.getCrudFormFactory().setVisibleProperties("name",  "actors",  "genre",  "ratings", "releaseYear","posterURL");


        add(
          new H1("Admin View"),
                crud
        );
    }
}
