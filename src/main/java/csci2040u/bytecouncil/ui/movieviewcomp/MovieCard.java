package csci2040u.bytecouncil.ui.movieviewcomp;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import csci2040u.bytecouncil.backend.Movie;
import csci2040u.bytecouncil.ui.UIColors;

public class MovieCard extends VerticalLayout {
    public MovieCard(Movie movie){

        //image
        Image poster = new Image(movie.getPosterURL(),"");
        poster.getStyle().set("object-fit", "contain");
        poster.setWidth("180px");
        poster.setHeight("270px");
        poster.getStyle().set("background-image", "url('https://cdn-icons-png.flaticon.com/128/1665/1665664.png')");
        poster.getStyle().set("object-fit", "cover");
        poster.getStyle().set("background-repeat", "no-repeat");
        poster.getStyle().set("background-position", "center");
        poster.getStyle().set("background-size", "contain");



        //make it horizontal layout so you can place the image beside the details
        this.add(poster);

        Paragraph title=new Paragraph(valueOrNA(movie.getName()));
        title.getStyle().setColor(UIColors.TEXTCOLORHEADER);

        this.add(title);

        this.addClassName("movie-card");
        this.setSpacing(true);
        this.addClassName("sharp-corners");
        this.getStyle().setBackground(UIColors.MOVIECARDBACKGROUND);
        this.getStyle().set("border-radius", "8px");
        this.getStyle().set("padding", "8px");
        this.getStyle().set("min-height", "160px");
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.setAlignItems(Alignment.CENTER);

        this.getStyle().set("transition", "transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out");
        this.getStyle().set("cursor", "pointer"); // Changes mouse to a hand
        this.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");

        // life card when hover
        this.getElement().addEventListener("mouseenter", e -> {
            this.getStyle().set("transform", "translateY(-5px)");
            this.getStyle().set("box-shadow", "var(--lumo-box-shadow-m)");
        });

        // return when left
        this.getElement().addEventListener("mouseleave", e -> {
            this.getStyle().set("transform", "translateY(0)");
            this.getStyle().set("box-shadow", "var(--lumo-box-shadow-s)");
        });


    }



    private String valueOrNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }
    private String yearOrNA(int releaseYear) {
        return releaseYear <= 0 ? "N/A" : Integer.toString(releaseYear);
    }
}
