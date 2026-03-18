package csci2040u.bytecouncil.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;

@Route("register")
@AnonymousAllowed
public class RegisterView extends Composite {
    private final AuthenticationContext authContext;

    public RegisterView(AuthenticationContext authenticationContext){
        this.authContext=authenticationContext;
    }

    @Override
    protected Component initContent(){
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");

        VerticalLayout registerPanel=new VerticalLayout();
        registerPanel.getStyle().set("border", "1px solid #d3d3d3");
        registerPanel.getStyle().set("border-radius", "8px");
        registerPanel.setHeightFull();
        registerPanel.setWidth("30%");

        registerPanel.add(
                usernameField,
                passwordField,
                new Button("Create Account",
                event->{register(
                        usernameField.getValue(),
                        passwordField.getValue()
                );
                }));

     return new VerticalLayout(registerPanel);
    }

    private void register(String username, String password){
        if(username.trim().isEmpty()){
            Notification.show("Enter a Username");
        } else if (password.trim().isEmpty()){
            Notification.show("Enter a Password");
        }
    }
}
