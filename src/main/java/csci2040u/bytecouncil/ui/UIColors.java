package csci2040u.bytecouncil.ui;

import com.vaadin.flow.component.Component;

//holds the ui color palette

public class UIColors {
    //used for background
    public static String DARKMODEBACKGROUND ="#1d1e26";
    public static String MOVIECARDBACKGROUND = "#212124";

    //secondary colors, things like headings and elements
    public static String SECONDARYCOLOR ="#726E97";
    public static String SECONDARYOUTLINE="#483457";

    public static String TEXTCOLORHEADER="#E8E4F2";
    public static String TEXTCOLORONBACKGROUND="#BDBDBD";

    public static String IMGNOTFOUND="\"url('https://cdn-icons-png.flaticon.com/128/1665/1665664.png')\"";

    public static void setMainBackground(Component component){
        component.getStyle().setBackground(DARKMODEBACKGROUND);

    }

    public static void setSecondary(Component component){
        component.getStyle().setBackground(SECONDARYCOLOR);
    }
            ;
}
