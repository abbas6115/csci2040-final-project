package csci2040u.bytecouncil.ui.mainviewcomp;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import csci2040u.bytecouncil.ui.AdminView;
import csci2040u.bytecouncil.ui.LoginView;
import csci2040u.bytecouncil.ui.UIAssests;
import org.springframework.security.core.userdetails.UserDetails;

//Class for custom header components
public class Header extends HorizontalLayout {
    public Header(AuthenticationContext authCont){
        this.setWidthFull();

        setWidth("100%"); // Make the header take full width
        Button adminButton = new Button("Admin View", event -> {
            UI.getCurrent().navigate(AdminView.class);
        });
        adminButton.getStyle().set("color", UIAssests.TEXTCOLORHEADER);
        adminButton.setVisible(authCont.hasRole("ADMIN"));

        Button signInButton = new Button("Sign in", event -> {
            UI.getCurrent().navigate(LoginView.class);
        });

        signInButton.getStyle().set("padding-top", "10px");
        signInButton.getStyle().set("padding-right", "20px");
        signInButton.getStyle().set("color", UIAssests.TEXTCOLORHEADER);

        signInButton.setVisible(!authCont.isAuthenticated());
        Span spacer=new Span();
        expand(spacer);

        H1 title = new H1("Filmbase");
        title.getStyle().set("color", UIAssests.TEXTCOLORHEADER);
        title.getStyle().set("padding-top", "10px");
        title.getStyle().set("padding-left", "20px"); // Inner spacing for the text

        //show current user one logout panel
        if(authCont.isAuthenticated()){
            HorizontalLayout userPanel=new HorizontalLayout();
            String username = authCont.getAuthenticatedUser(UserDetails.class).get().getUsername();
            Text userlabel=new Text(username);
            userPanel.add(new Icon(VaadinIcon.USER),userlabel);
            add(title, adminButton,spacer,userPanel);
            userPanel.getStyle().set("padding-top", "10px");
            userPanel.getStyle().set("padding-right", "20px");
            ContextMenu menu = new ContextMenu(userPanel);

            menu.setOpenOnClick(true);

            menu.addItem("Logout", e -> {
                authCont.logout();
            });
        }
        else {
            add(title, adminButton, spacer, signInButton);
        }


    }
}
