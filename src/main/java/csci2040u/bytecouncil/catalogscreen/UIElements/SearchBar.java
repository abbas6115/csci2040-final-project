package csci2040u.bytecouncil.catalogscreen.UIElements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SearchBar extends JPanel {
    final static String defaultText = "Search";
    private JTextField searchField;
    private JButton searchButton;

    public SearchBar() {
        // panel settings
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(Integer.MAX_VALUE,50));
        Dimension preferredSize = this.getPreferredSize();
        this.setMaximumSize(new Dimension(preferredSize.width, preferredSize.height));

        // textfield for search button
        searchField = new JTextField(20);
        searchField.setText(defaultText);

        // temporary text in textfield logic
        TempText tempTextLogic = new TempText();
        searchField.addFocusListener(tempTextLogic);
        searchField.addKeyListener(tempTextLogic);
        // search button
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> {searchItem(); });

        // adding components to searchbar
        this.add(searchField);
        this.add(searchButton);
    }

    // focus listener to make temporary text appear in text field at appropriate times
    private class TempText implements FocusListener, KeyListener {
        @Override
        public void focusGained(FocusEvent e) {
            // replace
            if (searchField.getText().equals(defaultText)) {
                searchField.setText("");
            }

        }

        // while focus is lost and field is empty, display default text
        @Override
        public void focusLost(FocusEvent e) {
            if (searchField.getText().isEmpty()) {
                searchField.setText(defaultText);
            }
        }

        // run searchItem on enter
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                searchItem();
            }
        }

        // wasnt used
        @Override
        public void keyTyped(KeyEvent e) {

        }

        // not used
        @Override
        public void keyReleased(KeyEvent e) {
        }
    }


    // will call the search function eventually
    public void searchItem() {
        // does nothing for now.
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getSearchButton(){
        return searchButton;
    }


}
