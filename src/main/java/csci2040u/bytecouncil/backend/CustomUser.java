package csci2040u.bytecouncil.backend;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CustomUser implements UserDetails {

    private String username;
    private String password;
    private LinkedList<Movie> recentlyWatched = new LinkedList<>();

    public CustomUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addToWatchlist(Movie movie) {
        recentlyWatched.add(movie);
    }

    public LinkedList<Movie> getRecentlyWatched() { return recentlyWatched; }

    public void removeMovieWatchList(Movie movie) {recentlyWatched.remove(movie);}

    @Override
    public String getUsername() { return username; }

    @Override
    public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}