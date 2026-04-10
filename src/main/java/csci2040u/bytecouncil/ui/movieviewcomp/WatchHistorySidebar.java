package csci2040u.bytecouncil.ui.movieviewcomp;

import java.util.Queue;

import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import csci2040u.bytecouncil.backend.CustomUser;
import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.ui.MovieCatalogView;
import csci2040u.bytecouncil.ui.UIColors;

public class WatchHistorySidebar extends VerticalLayout {
    private final VerticalLayout backdrop = new VerticalLayout();
    public WatchHistorySidebar() {
        // Position and Size
        this.setWidth("350px");
        this.setHeightFull();
        this.getStyle().set("position", "fixed");
        this.getStyle().set("top", "0");
        this.getStyle().set("left", "0");
        this.getStyle().set("bottom", "0");
        this.getStyle().set("z-index", "1000");

        // Visual Styling
        this.getStyle().set("background-color", UIColors.SECONDARYOUTLINE); // Dark Background
        this.getStyle().set("border-right", "3px solid #6200ee");
        this.getStyle().set("box-shadow", "5px 0 15px rgba(0,0,0,0.5)"); // Shadow on the right
        this.setPadding(true);
        this.setSpacing(true);

        // collapse logic
        this.getStyle().set("transition", "transform 0.4s ease-in-out");
        this.getStyle().set("transform", "translateX(-100%)"); // Start off-screen to the LEFT

        this.getStyle().set("overflow-y", "auto");

        // Prevent the main page from scrolling when the mouse is over the sidebar
        this.getStyle().set("pointer-events", "auto");

        backdrop.setSizeFull();
        backdrop.getStyle().set("position", "fixed");
        backdrop.getStyle().set("top", "0");
        backdrop.getStyle().set("left", "0");
        backdrop.getStyle().set("z-index", "999"); // Just below the sidebar
        backdrop.getStyle().set("background-color", "rgba(0, 0, 0, 0.5)"); // Dimmed effect
        backdrop.setVisible(false); // Hidden by default

        // THE MAGIC LOGIC: If user clicks the background, close the sidebar
        backdrop.addClickListener(e -> this.close());

        renderHeader();
    }

    private void renderHeader() {
        this.removeAll();

        // close button
        Button closeBtn = new Button("✕", e -> this.close());
        closeBtn.getStyle().set("color", "white");
        closeBtn.getStyle().set("align-self", "flex-end");
        closeBtn.getStyle().set("background", "transparent");

        H3 title = new H3("Watch History");
        title.getStyle().set("color", "white");
        title.getStyle().set("margin-top", "0");


        add(closeBtn, title);
    }

    //open side bar
    public void open() {
        refreshHistory();
        this.getStyle().set("transform", "translateX(0)");
        backdrop.setVisible(true);
    }

    // close side bar
    public void close() {
        this.getStyle().set("transform", "translateX(-100%)");
        backdrop.setVisible(false);
    }

    public void toggle() {
        String transform = this.getStyle().get("transform");
        // If it's at 0, it's open, so close it.
        if ("translateX(0)".equals(transform) || "translateX(0px)".equals(transform)) {
            close();
        } else {
            open();
        }
    }

    private void refreshHistory() {
        renderHeader();
        var auth = SecurityContextHolder.getContext().getAuthentication();


        if (auth != null && auth.getPrincipal() instanceof CustomUser currentUser) {
            Queue<Movie> history = currentUser.getRecentlyWatched();

            if (history == null || history.isEmpty()) {
                Span emptyMsg = new Span("No movies watched recently.");
                emptyMsg.getStyle().set("color", "#999");
                add(emptyMsg);
            } else {
                for (Movie movie : history) {
                    MovieCard card = new MovieCard(movie);

                    card.setWidthFull();
                    card.getStyle().set("margin-bottom", "10px");
                    card.getStyle().set("flex-shrink", "0");
                    card.setWidthFull();
                    card.setHeight("290");

                    // Add click listener to close sidebar when a movie is selected
                    card.addClickListener(e -> {
                        this.close();
                        MovieCatalogView.openMovieDetails(movie);
                    });

                    this.add(card);
                }
            }
        }
        else {
            Span emptyMsg = new Span("Sign in for watch history");
            emptyMsg.getStyle().set("color", "#999");
            add(emptyMsg);
        }
    }

    public VerticalLayout getBackdrop() {
        return backdrop;
    }
}