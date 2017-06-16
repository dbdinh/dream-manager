package org.dreambot.dinh.console;

import org.dreambot.dinh.DreamBot;
import org.dreambot.dinh.command.Execute;

import javax.swing.text.*;
import java.util.ArrayList;
import java.util.List;

public class Filter extends DocumentFilter {

    private Execute execution = new Execute();
    private boolean filter = true;

    public boolean isFilter() {
        return filter;
    }

    void setFilter(boolean filter) {
        this.filter = filter;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
            throws BadLocationException {
        if (filter) {
            if (string == null) {
                return;
            } else {
                replace(fb, offset, 0, string, attr);
            }
        }
        super.insertString(fb, offset, string, attr);
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if (filter) {
            replace(fb, offset, length, "", null);
        }
    }

    private String prompt;
    private boolean isLogged = false;
    private List<String> detailList = new ArrayList<>();


    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
            throws BadLocationException {
        if (filter) {
            Document doc = fb.getDocument();
            Element root = doc.getDefaultRootElement();
            int count = root.getElementCount();
            int index = root.getElementIndex(offset);
            Element cur = root.getElement(index);

            int promptPosition = cur.getStartOffset();
            if (!isLogged) promptPosition += 10;
            else promptPosition += prompt.length();

            if (index == count - 1 && offset - promptPosition >= 0) {
                String input = doc.getText(promptPosition, offset - promptPosition);
                if (!isLogged) {
                    if (detailList.size() >= 2) return;
                    handleLogin(text, input, fb, offset, length, attrs);
                    return;
                } else {
                    if (text.equals("\n")) {
                        String cmd = doc.getText(promptPosition, offset - promptPosition);
                        if (cmd.isEmpty()) {
                            text = "\n" + prompt;
                        } else {
                            text = "\n" + getReply(cmd) + "\n\n" + prompt;
                        }
                    }
                }
                fb.replace(offset, length, text, attrs);
            }
        }
    }

    private String getReply(String cmd) {
        return execution.executeCommand(cmd);
    }

    private void handleLogin(String text, String input, FilterBypass fb, int offset, int length, AttributeSet attrs) throws BadLocationException {
        if (text.equals("\n")) {
            detailList.add(input);
            if (detailList.size() >= 2) {
                prompt = detailList.get(0) + ":~$ ";
                text += "\nAttempting to login...";
                String finalText = text;
                new Thread(() -> {
                    String extraText = "\n";
                    if (DreamBot.login(detailList.get(0), detailList.get(1))) {
                        extraText += "Successfully logged in as " + detailList.get(0) + "\n\n" + prompt;
                        try {
                            fb.replace(offset + finalText.length(), length, extraText, attrs);
                            isLogged = true;
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                    } else {
                        extraText += "Failed to login as: " + detailList.get(0) + "\n\nPlease login to DreamBot:\nusername: ";
                        try {
                            fb.replace(offset + finalText.length(), length, extraText, attrs);
                        } catch (BadLocationException e) {
                            e.printStackTrace();
                        }
                        isLogged = false;
                        detailList.clear();
                    }
                    Console.setCaret();
                }).start();
            } else text += "password: ";
        }
        fb.replace(offset, length, text, attrs);
    }
}
