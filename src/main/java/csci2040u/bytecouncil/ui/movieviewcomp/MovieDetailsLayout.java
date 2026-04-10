package csci2040u.bytecouncil.ui.movieviewcomp;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.component.page.Push;
import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.ui.UIColors;


public class MovieDetailsLayout extends VerticalLayout {
    public MovieDetailsLayout(Movie movie, Button addToWatchHistoryButton){
        this.setWidthFull();
        this.setPadding(true);
        this.setSpacing(true);
        this.setAlignItems(Alignment.STRETCH);

        this.getStyle().setBackground(UIColors.DARKMODEBACKGROUND);

        TMBDRequest tmdb = new TMBDRequest();
        String movieId = String.valueOf(movie.getTmdbID());

//        movie immage
        HorizontalLayout topSection = new HorizontalLayout();
        topSection.setWidthFull();
        topSection.setAlignItems(Alignment.START);

        // 1. Movie Poster
        Image poster = new Image(movie.getPosterURL(), "Poster");
        poster.setHeight("280px");
        poster.setWidth("187px");
        poster.getStyle().set("border-radius", "4px");
        poster.getStyle().set("flex-shrink", "0");

        // 2. Video Carousel (Top Right)
        Div videoCarousel = new Div();
        videoCarousel.setWidthFull();
        videoCarousel.setHeight("280px");
        videoCarousel.getStyle().set("overflow-x", "auto");
        videoCarousel.getStyle().set("display", "flex");
        videoCarousel.getStyle().set("gap", "10px");

        // Fetch top 5 videos from TMDB
        tmdb.getAllVideos(movieId).thenAccept(videoUrls -> {
            getUI().ifPresent(ui -> ui.access(() -> {
                videoUrls.stream().limit(5).forEach(url -> {
                    // Convert watch URL to embed URL
                    String embedUrl = url.replace("watch?v=", "embed/");
                    IFrame iframe = new IFrame(embedUrl);
                    iframe.setWidth("450px");
                    iframe.setHeight("250px");
                    iframe.getStyle().set("border-radius", "8px");
                    videoCarousel.add(iframe);
                });
            }));
        });

        topSection.add(poster, videoCarousel);


        // bottom half
        //box settings
        HorizontalLayout bottomSection = new HorizontalLayout();
        bottomSection.setWidthFull();
        bottomSection.getStyle().set("background-color", UIColors.MOVIECARDBACKGROUND);
        bottomSection.getStyle().set("border-radius", "8px");
        bottomSection.setPadding(true);

        // Details Box (Title, Genre, Actors, etc.)
        VerticalLayout detailsBox = new VerticalLayout();
        detailsBox.setSpacing(false);
        detailsBox.setPadding(false);

        // Add Content to the Box
        Paragraph name = new Paragraph(movie.getName());
        name.getStyle().set("color", UIColors.TEXTCOLORHEADER);
        name.getStyle().set("font-weight", "bold");
        name.getStyle().set("margin", "0");

        HorizontalLayout nameRow = new HorizontalLayout(name);
        nameRow.setWidthFull();
        nameRow.setAlignItems(Alignment.CENTER);
        nameRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Span genre = new Span("Genre: " + movie.getGenre());
        Span actors = new Span("Actors: " + movie.getActors());
        Span year = new Span("Year: " + movie.getReleaseYear());
        Span ratings = new Span("Rating: " + movie.getRatings());

        detailsBox.add(nameRow, genre, actors, year, ratings);
        detailsBox.getChildren().forEach(c -> {
            if(c instanceof Span) c.getStyle().set("color", UIColors.TEXTCOLORHEADER).set("font-size", "0.9em");
        });

        // Logos Grid (Bottom Right)
        Div logoGrid = new Div();
        logoGrid.getStyle().set("display", "grid");
        logoGrid.getStyle().set("grid-template-columns", "repeat(3, 1fr)");
        logoGrid.getStyle().set("gap", "8px");
        logoGrid.getStyle().set("margin-left", "auto");

        tmdb.getStreamingLogos(movieId, "US").thenAccept(logos -> {
            getUI().ifPresent(ui -> ui.access(() -> {
                logos.forEach(logoUrl -> {
                    Image img = new Image(logoUrl, "provider");
                    img.setWidth("40px");
                    img.setHeight("40px");
                    img.getStyle().set("border-radius", "4px");
                    logoGrid.add(img);
                });
            }));
        });

        bottomSection.add(detailsBox, logoGrid,addToWatchHistoryButton);

        // Assemble Final Layout
        add(topSection, bottomSection);
    }

}
