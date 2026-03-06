package csci2040u.bytecouncil.catalogscreen;

import csci2040u.bytecouncil.catalogscreen.UIElements.CatalogScreen;

import javax.swing.*;

public class MovieCatalogGUI extends JFrame{
    CatalogScreen screen;

    public MovieCatalogGUI(){
        this.setTitle("Movie Catalog");
        this.setSize(800, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        screen=new CatalogScreen();
        this.add(screen);

        this.setVisible(true);
        screen.getMovieArea().requestFocusInWindow();
    }

//    public static void main(String []args){
//        MovieCatalogGUI test=new MovieCatalogGUI();
//    }

}
