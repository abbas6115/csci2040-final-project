package csci2040u.bytecouncil.ui;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

/*
the @route tells the Spring that this is the login component of the web
 */
//This is for temporary login page will turn later
//Anonymous allowed tells springboot you dont need to login to access this page
@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
public class  LoginView extends VerticalLayout implements BeforeEnterObserver {
    public static final String login_path="login";
    private final AuthenticationContext authContext;
    private final LoginForm login;

    public LoginView(AuthenticationContext authenticationContext) {
        this.authContext=authenticationContext;

        setAlignItems(Alignment.END);

        //Login
        VerticalLayout loginPanel=new VerticalLayout();
        loginPanel.getStyle().set("border", "1px solid #d3d3d3");
        loginPanel.getStyle().set("border-radius", "8px");
        loginPanel.setHeightFull();
        loginPanel.setWidth("30%");

        login=new LoginForm();
        login.setAction(login_path);
        login.setForgotPasswordButtonVisible(false);


        //Login form settings
        LoginI18n customForm=LoginI18n.createDefault();
        LoginI18n.Form editForm= customForm.getForm();


        setSizeFull();
        var contentDiv=new Div(login);
        contentDiv.addClassName("content-div");
        loginPanel.add(contentDiv);
        add(loginPanel);

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(authContext.isAuthenticated()){
            event.forwardTo("");
            return;
        }

        if(event.getLocation().getQueryParameters().getParameters().containsKey("error")){
            login.setError(true);
        }
    }
}

