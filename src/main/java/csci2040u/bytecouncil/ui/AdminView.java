package csci2040u.bytecouncil.ui;


import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import csci2040u.bytecouncil.backend.Movie;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.crudui.crud.impl.GridCrud;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {
    public AdminView(){
        var crud=new GridCrud<>(Movie.class);
        crud.getGrid().setColumns( "name",  "actors",  "genre",  "ratings", "year", "posterURL");
        crud.getCrudFormFactory().setVisibleProperties("name",  "actors",  "genre",  "ratings", "year","posterURL");
        add(
          new H1("Admin View"),
                crud
        );
    }
}
