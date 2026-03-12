package csci2040u.bytecouncil.ui;


import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.backend.MovieDatabaseCommands;
import jakarta.annotation.security.RolesAllowed;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {
    public AdminView(MovieDatabaseCommands commands){
        //creates the grid for the movie databases
        var crud=new GridCrud<>(Movie.class,commands);

        //lists columns that can be viewed
        crud.getGrid().setColumns( "name",  "actors",  "genre",  "ratings", "releaseYear", "posterURL","id");

                // Keep edit form fields aligned with add form fields.
                String[] editableFields = {"name", "actors", "genre", "ratings", "releaseYear", "posterURL"};
                crud.getCrudFormFactory().setVisibleProperties(CrudOperation.ADD, editableFields);
                crud.getCrudFormFactory().setVisibleProperties(CrudOperation.UPDATE, editableFields);

                // Explicitly wire button operations to backend command handlers.
                crud.setAddOperation(commands::add);
                crud.setUpdateOperation(commands::update);
                crud.setDeleteOperation(commands::delete);


        add(
          new H1("Admin View"),
                crud
        );
    }
}
