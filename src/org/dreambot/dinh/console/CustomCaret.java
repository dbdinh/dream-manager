package org.dreambot.dinh.console;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * Created by: Niklas
 * Date: 13.06.2017
 * Time: 16:03
 */

public class CustomCaret extends DefaultCaret {

    @Override
    protected synchronized void damage(Rectangle r) {
        if (r == null) {
            return;
        }

        JTextComponent comp = getComponent();
        FontMetrics fm = comp.getFontMetrics(comp.getFont());
        int textWidth = fm.stringWidth(">");
        int textHeight = fm.getHeight();
        x = r.x;
        y = r.y;
        width = textWidth;
        height = textHeight;
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        JTextComponent comp = getComponent();
        if (comp == null) {
            return;
        }

        int dot = getDot();
        Rectangle r;
        try {
            r = comp.modelToView(dot);
        } catch (BadLocationException e) {
            return;
        }
        if (r == null) {
            return;
        }

        if ((x != r.x) || (y != r.y)) {
            repaint();
            damage(r);
        }

        if (isVisible()) {
            g.setColor(Color.GREEN);
            g.drawRect(x, y, 6, 13);
        }
    }


}
