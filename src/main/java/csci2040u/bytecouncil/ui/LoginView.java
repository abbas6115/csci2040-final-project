package csci2040u.bytecouncil.ui;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.extern.slf4j.Slf4j;

/*
the @route tells the Spring that this is the login component of the web
 */
//This is for temporary login page will turn later
//Anonymous allowed tells springboot you dont need to login to access this page
@Slf4j
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
        UIAssests.setMainBackground(this);
        this.setPadding(false);

        //header
        H1 header =new H1("Filmbase");
        header.setWidth("100%");
        header.setHeight("10%");
        header.getStyle().set("padding-left", "100px");
        header.getStyle().set("color", UIAssests.TEXTCOLORHEADER);
        header.getStyle().set("text-shadow", "2px 2px 10px rgba(0, 0, 0, 0.2)");
        UIAssests.setSecondary(header);

        this.add(header);

        //Login
        VerticalLayout loginPanel=new VerticalLayout();
        loginPanel.getStyle().setBackground("#ffffff");
        loginPanel.getStyle().set("border", "1px solid #d3d3d3");
        loginPanel.getStyle().set("border-radius", "8px");
        loginPanel.getStyle().set("margin-bottom", "20px");
        loginPanel.setAlignItems(Alignment.CENTER);
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

