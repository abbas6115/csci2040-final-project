package csci2040u.bytecouncil.ui;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/*
the @route tells the Spring that this is the login component of the web
 */
//This is for temporary login page will turn later
//Anonymous allowed tells springboot you dont need to login to access this page
@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends Composite<LoginOverlay> {
    public LoginView() {
        //tells the original login Container to turn its self off and to use this one
        getContent().setOpened(true);
        getContent().setAction("login");
        getContent().setTitle("Movie Catalog");
        getContent().setDescription("");
    }
}
