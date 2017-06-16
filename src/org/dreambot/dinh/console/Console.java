package org.dreambot.dinh.console;

import javax.swing.*;
import java.awt.*;

/**
 * Created by: Niklas
 * Date: 12.06.2017
 * Time: 01:12
 */

public class Console extends JFrame {

    private static ConsoleComponent console;

    public Console(String title) {
        super(title);
        setLayout(new BorderLayout());

        getContentPane().setPreferredSize(new Dimension(600, 300));
        getContentPane().add(console = new ConsoleComponent(), BorderLayout.CENTER);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    static void setCaret() {
        console.getText().setCaretPosition(console.getText().getDocument().getEndPosition().getOffset() - 1);
    }
}
