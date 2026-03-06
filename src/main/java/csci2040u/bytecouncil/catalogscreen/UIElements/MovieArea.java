package csci2040u.bytecouncil.catalogscreen.UIElements;

import javax.swing.*;
import java.awt.*;

public class MovieArea extends JPanel {


    public MovieArea(int row, int col) {
        this.setLayout(new GridLayout(row, col, 5, 8));

        testArea(18);
    }

    private void testArea(int numButtons) {
        for(int i = 0; i < numButtons; i++) {
            this.add(new JButton("Movie " + i));
        }
    }
}
