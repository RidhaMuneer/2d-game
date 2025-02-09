package com.dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;

import com.panel.GamePanel;
import com.network.GeminiApiRequest;

public class DialogBox {
    private Rectangle dialogRect;
    private Rectangle actionBox1;
    private Rectangle actionBox2;
    private boolean isVisible = false;
    private final GeminiApiRequest chat = new GeminiApiRequest();
    private String promot;
    private boolean activePerformed = false;

    /**
     * Draws the dialog box on the screen, including the prompt text and the two action boxes.
     * 
     * @param g2 the graphics context to draw on
     * @param gp the game panel that the dialog box is associated with
     */
    public void draw(Graphics2D g2, GamePanel gp){
        if (!isVisible) return;
        try {
            if(promot == null) promot = chat.sendRequest("Hello, let's have a philosophical discussion");
    
            // Entire dialog container
            g2.setColor(new Color(0, 0, 0, 200)); // Slightly more opaque
            dialogRect = new Rectangle(50, gp.getHeight() - 250, gp.getWidth() - 100, 200);
            g2.fill(dialogRect);
    
            // Prompt text
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));
            
            // Wrap long text with more space
            int maxWidth = dialogRect.width - 40;
            drawWrappedString(g2, promot, dialogRect.x + 20, dialogRect.y + 30, maxWidth);
    
            // Action Boxes
            g2.setColor(Color.GRAY);
            actionBox1 = new Rectangle(dialogRect.x + 20, dialogRect.y + dialogRect.height - 60, 150, 40);
            actionBox2 = new Rectangle(dialogRect.x + dialogRect.width - 170, dialogRect.y + dialogRect.height - 60, 150, 40);
    
            g2.fill(actionBox1);
            g2.fill(actionBox2);
    
            // Text in boxes
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.drawString("A", actionBox1.x + 40, actionBox1.y + 25);
            g2.drawString("B", actionBox2.x + 40, actionBox2.y + 25);
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            activePerformed = false;
        }
    }

    
    /**
     * Draw a string that is wrapped to a maximum width. The wrapping is simple, it just
     * splits the string into words and draws each word on a new line if the word would
     * go beyond the maximum width.
     * 
     * @param g2 the graphics object to draw with
     * @param text the string to draw
     * @param x the x position of the top left of the string
     * @param y the y position of the top left of the string
     * @param maxWidth the maximum width of the string
     */
    private void drawWrappedString(Graphics2D g2, String text, int x, int y, int maxWidth) {
        Font font = g2.getFont();
        // Simple wrapping logic
        if (g2.getFontMetrics().stringWidth(text) <= maxWidth) {
            g2.drawString(text, x, y);
            return;
        }

        // Basic wrapping
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int lineY = y;

        for (String word : words) {
            if (g2.getFontMetrics().stringWidth(line + " " + word) < maxWidth) {
                line.append(line.length() == 0 ? "" : " ").append(word);
            } else {
                g2.drawString(line.toString(), x, lineY);
                line = new StringBuilder(word);
                lineY += g2.getFontMetrics().getHeight();
            }
        }
        
        if (line.length() > 0) {
            g2.drawString(line.toString(), x, lineY);
        }
    }

    /**
     * Handle a mouse click event. If the dialog box is not visible, do nothing.
     * If the mouse click is within one of the action boxes, perform the
     * appropriate action and set activePerformed to true.
     * 
     * @param e the MouseEvent
     */
    public void handleMouseClick(MouseEvent e) {
        if (!isVisible) return;
        if(activePerformed) return;

        int x = e.getX();
        int y = e.getY();

        if (actionBox1.contains(x, y)) {
            try {
                performAction1();
                activePerformed = true;
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
            }
        } else if (actionBox2.contains(x, y)) {
            try {
                performAction2();
                activePerformed = true;
            } catch (IOException | InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }

        /**
         * Sends a request to the Gemini API with the user input as "A".
         * @throws IOException if there was an error communicating with the Gemini API
         * @throws InterruptedException if the thread was interrupted while waiting for the response
         */
    private void performAction1() throws IOException, InterruptedException{
        promot = chat.sendRequest("A");
    }

        /**
         * Sends a request to the Gemini API with the user input as "B".
         * @throws IOException if there was an error communicating with the Gemini API
         * @throws InterruptedException if the thread was interrupted while waiting for the response
         */
    private void performAction2() throws IOException, InterruptedException{
        promot = chat.sendRequest("B");
    }

    /**
     * Sets whether the dialog box is visible or not.
     * 
     * @param visible if true, the dialog box is visible. If false, it is not visible.
     */
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
}