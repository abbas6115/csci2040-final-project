package csci2040u.bytecouncil.catalogscreen.UIElements;

import javax.swing.*;
import java.awt.*;

public class CatalogScreen extends JPanel {
    SearchBar searchBar;
    MovieArea movieArea;

    //movie area modifiers
    private final int movieRows=0;
    private final int movieCol=3;

    public CatalogScreen(){
        this.setLayout(new BorderLayout());

        initCatalogElements();
    }

    private void initCatalogElements(){
        searchBar=new SearchBar();
        movieArea=new MovieArea(movieRows,movieCol);
        movieArea.requestFocusInWindow();

        ScrollPane scrollPane=new ScrollPane();
        scrollPane.add(movieArea);

        this.add(searchBar,BorderLayout.PAGE_START);
        this.add(scrollPane,BorderLayout.CENTER);
    }

    public MovieArea getMovieArea(){
        return movieArea;
    }

    public SearchBar searchBar(){
        return searchBar;
    }




}
