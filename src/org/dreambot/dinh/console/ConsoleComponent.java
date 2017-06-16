package org.dreambot.dinh.console;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by: Niklas
 * Date: 12.06.2017
 * Time: 01:21
 */

class ConsoleComponent extends JPanel {

    private volatile JTextArea textArea;

    ConsoleComponent() {
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JScrollPane second = new JScrollPane(textArea);
        second.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        second.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        textArea.setLineWrap(true);

        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setText("DreamBot Console V1.0 by dinh\n\nPlease login to DreamBot:\nusername: ");

        textArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        textArea.setCaret(new CustomCaret());
        textArea.setCaretPosition(textArea.getDocument().getEndPosition().getOffset() - 1);

        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                handleInput(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleInput(e);
            }
        });

        Filter filter = new Filter();
        ((AbstractDocument) textArea.getDocument()).setDocumentFilter(filter);

        add(second, BorderLayout.CENTER);
    }

    private void handleInput(KeyEvent e) {
        if (e.getKeyCode() == 17 || e.getKeyCode() == 67) {
            return;
        }
        if (e.getKeyCode() != 39 && e.getKeyCode() != 37) {
            textArea.setCaretPosition(textArea.getDocument().getEndPosition().getOffset() - 1);
        } else {
            int caretOffset = textArea.getCaretPosition();
            int lineNumber = 0;
            try {
                lineNumber = textArea.getLineOfOffset(caretOffset);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
            Document doc = textArea.getDocument();
            Element root = doc.getDefaultRootElement();
            Element element = root.getElement(lineNumber);
            int start = element.getStartOffset();
            int end = element.getEndOffset();
            String lineString = null;
            try {
                lineString = doc.getText(start, end - start);
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
            int indexOf = lineString != null ? lineString.indexOf(" ") : 0;
            int requiredCaretPosition = start + indexOf + 1;
            if (requiredCaretPosition >= textArea.getCaretPosition()) {
                textArea.setCaretPosition(start + indexOf + 1);
            }
        }
    }

    JTextArea getText() {
        return textArea;
    }

}
