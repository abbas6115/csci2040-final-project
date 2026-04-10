package csci2040u.bytecouncil;
//This class is the start of the project. Running this class starts the webpage at localhost:8080 and then allows for the website
//to be seen on local host

/* localhost:8080 will get you to the default movie catalog view
localhost:8080/login will get you to default login screen. Temp account Admin1 with password. This will take you to login
*/

import com.vaadin.flow.component.page.Push;
import csci2040u.bytecouncil.backend.CustomUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import csci2040u.bytecouncil.ui.LoginView;

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
        http.headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                        .policyDirectives("frame-src 'self' https://www.youtube.com https://youtube.com; " +
                                "img-src 'self' https://image.tmdb.org https://imglink.cc https://www.youtube.com data:;")
                )
        );

        super.configure(http);
        setLoginView(http, LoginView.class, "/user");

        http.formLogin(form -> form.successHandler((request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));

            String targetPath = isAdmin ? "/admin" : "/user";
            response.sendRedirect(request.getContextPath() + targetPath);
        }));
    }

    //create temporary user variables
    @Bean
    public UserDetailsService userDetailsServiceBean() {
        // Create our CustomUsers
        CustomUser admin = new CustomUser("Admin1", "{noop}password", "ADMIN");
        CustomUser user = new CustomUser("User1", "{noop}password", "USER");

        // We use a custom implementation of UserDetailsService
        // that returns our CustomUser objects
        return username -> {
            if ("Admin1".equals(username)) return admin;
            if ("User1".equals(username)) return user;
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found");
        };
    }
}
