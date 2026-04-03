package csci2040u.bytecouncil.ui.movieviewcomp;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.security.AuthenticationContext;
import csci2040u.bytecouncil.ui.AdminView;
import csci2040u.bytecouncil.ui.LoginView;
import csci2040u.bytecouncil.ui.UIColors;
import org.springframework.security.core.userdetails.UserDetails;

public class Header extends HorizontalLayout {
    TextField searchField;
    public Header(AuthenticationContext authCont){
        setHeight("60px");
        getStyle().set("min-height", "60px"); // Prevents squishing
        getStyle().set("max-height", "60px"); // Prevents stretching

        setWidthFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);

        getStyle().set("background-color", UIColors.SECONDARYCOLOR);
        getStyle().set("box-sizing", "border-box");
        getStyle().set("padding", "0 25px");

        // Title
        H1 title = new H1("Filmbase");
        title.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        title.getStyle().set("margin", "0"); // REMOVES BROWSER DEFAULT SPACING
        title.getStyle().set("line-height", "60px"); // Matches text height to header height

        //  Admin Button
        Button adminButton = new Button("Admin View", event -> {
            UI.getCurrent().navigate(AdminView.class);
        });
        adminButton.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        adminButton.getStyle().set("margin-left", "20px");
        adminButton.setVisible(authCont.hasRole("ADMIN"));

        add(title, adminButton);

        // 3. Search TextField (Added between Admin and Sign In)
        searchField = new TextField();
        searchField.setPlaceholder("Search movies...");
        searchField.getStyle().set("margin", "0 20px");

        // Styling for white background
        searchField.getElement().getStyle().set("--lumo-contrast-10pct", "white"); // Sets the field background
        searchField.getStyle().set("background-color", "white");
        searchField.getStyle().set("border-radius", "5px");

        add(searchField);

        // This makes the field "Flexible". It will grow to occupy all available space.
        setFlexGrow(1.0, searchField);
        // 5. Right side logic
        if(authCont.isAuthenticated()){
            HorizontalLayout userPanel = new HorizontalLayout();
            userPanel.setAlignItems(Alignment.CENTER);
            userPanel.setSpacing(true);

            String username = authCont.getAuthenticatedUser(UserDetails.class).get().getUsername();
            Span userlabel = new Span(username);
            userlabel.getStyle().set("color", UIColors.TEXTCOLORHEADER);

            Icon userIcon = new Icon(VaadinIcon.USER);
            userIcon.setColor(UIColors.TEXTCOLORHEADER);

            userPanel.add(userIcon, userlabel);
            add(userPanel);

            ContextMenu menu = new ContextMenu(userPanel);
            menu.setOpenOnClick(true);
            menu.addItem("Logout", e -> authCont.logout());
        } else {
            Button signInButton = new Button("Sign in", event -> {
                UI.getCurrent().navigate(LoginView.class);
            });
            signInButton.getStyle().set("color", UIColors.TEXTCOLORHEADER);
            add(signInButton);
        }
    }

    public TextField getSearchField() {
        return searchField;
    }
}