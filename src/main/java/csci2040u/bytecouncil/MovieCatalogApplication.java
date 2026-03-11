package csci2040u.bytecouncil;
//This class is the start of the project. Running this class starts the webpage at localhost:8080 and then allows for the website
//to be seen on local host

/* localhost:8080 will get you to the default movie catalog view
localhost:8080/login will get you to default login screen. Temp account Admin1 with password. This will take you to login
*/

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import csci2040u.bytecouncil.ui.LoginView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

//@springBootApplication tells that this a webServer
@SpringBootApplication
@EnableMethodSecurity(jsr250Enabled = true)
public class MovieCatalogApplication extends VaadinWebSecurity {
    public static void main(String[] args) {
        //code that opens the webpage on localhost
        SpringApplication.run(MovieCatalogApplication.class);
    }

    //tells the program to use the loginView Class for security
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //tells to use
        setLoginView(http,LoginView.class,"/admin");
        super.configure(http);
    }

    //create temporary user variables
    @Bean
    public UserDetailsService userDetailsServiceBean()throws  Exception{
        return new InMemoryUserDetailsManager(
                User.withUsername("Admin1")
                        .password("{noop}password")
                        .roles("ADMIN")
                        .build(),
                User.withUsername("User1")
                        .password("{noop}password")
                        .roles("USER")
                        .build()
        );
    }
}
