package csci2040u.bytecouncil.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Route(value = "login", autoLayout = false)
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    public static final String login_path = "login";
    private final AuthenticationContext authContext;
    private final LoginForm login;

    public LoginView(AuthenticationContext authenticationContext) {
        this.authContext = authenticationContext;

        // Root setup: Force the body to be exactly 100% of the screen
        setSizeFull();

        setPadding(false);
        setSpacing(false);
        getStyle().set("overflow", "hidden");
        UIColors.setMainBackground(this);

        // header
        H1 header = new H1("Filmbase");
        header.setWidthFull();
        header.setHeight("60px");
        header.getStyle().set("min-height", "60px");
        header.getStyle().set("max-height", "60px");
        header.getStyle().set("margin", "0");
        header.getStyle().set("padding", "0 20px");
        header.getStyle().set("display", "flex");
        header.getStyle().set("align-items", "center");
        header.getStyle().set("color", "#f8f9fa");
        // This stops the 20px padding from pushing the header past the screen width
        header.getStyle().set("box-sizing", "border-box");
        UIColors.setSecondary(header);
        add(header);

        //  Main Content Container (Image + Login Side-by-Side)
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setSpacing(false);
        mainContent.setPadding(false);
        mainContent.getStyle().set("overflow", "hidden");

        // Image Section
        Image movieImg = new Image("https://imglink.cc/cdn/GKS4pZ-SRT.png", "Movie Backdrop");
        movieImg.getStyle().set("object-fit", "cover");
        movieImg.setWidthFull();
        movieImg.setHeightFull();

        Div imgWrapper = new Div(movieImg);
        imgWrapper.setWidth("70%");
        imgWrapper.setHeightFull();
        imgWrapper.getStyle().set("flex-shrink", "0");

        // Login Panel Section
        VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setWidth("30%");
        loginPanel.setHeightFull();
        loginPanel.setPadding(false); // No extra padding here to keep it tight
        loginPanel.setJustifyContentMode(JustifyContentMode.CENTER);
        loginPanel.setAlignItems(Alignment.CENTER);
        loginPanel.getStyle().set("background", "#0d0216");

        // The Login Card
        VerticalLayout formCard = new VerticalLayout();
        formCard.setWidth("420px"); // Keep original horizontal size


        formCard.setHeight("90%");
        formCard.setMaxHeight("850px");

        formCard.getStyle().set("background", "white");
        formCard.getStyle().set("border-radius", "24px");
        formCard.getStyle().set("box-shadow", "0 20px 60px rgba(0,0,0,0.7)");
        formCard.setPadding(true);
        formCard.setJustifyContentMode(JustifyContentMode.CENTER);

        login = new LoginForm();
        login.setAction(login_path);
        login.setForgotPasswordButtonVisible(false);

        formCard.add(login);
        formCard.setAlignItems(Alignment.CENTER);

        loginPanel.add(formCard);

        // Assembly
        mainContent.add(imgWrapper, loginPanel);
        add(mainContent);
        setFlexGrow(1, mainContent);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authContext.isAuthenticated()) {
            event.forwardTo("");
            return;
        }
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            login.setError(true);
        }
    }
}