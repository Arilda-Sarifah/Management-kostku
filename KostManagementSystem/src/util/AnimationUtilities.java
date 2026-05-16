package util;

import javax.swing.*;
import java.awt.*;

/**
 * Utility class untuk animasi pada Swing components
 */
public class AnimationUtilities {
    
    /**
     * Animasi button hover
     */
    public static void addHoverEffect(JButton button, Color hoverColor, Color normalColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }
    
    /**
     * Slide animasi dari kiri ke kanan
     */
    public static void slideInFromLeft(JComponent component, int duration) {
        int startX = -component.getWidth();
        int endX = component.getX();
        
        Timer timer = new Timer(10, null);
        long[] startTime = {System.currentTimeMillis()};
        
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime[0];
            float progress = Math.min(1f, (float) elapsed / duration);
            
            int currentX = (int) (startX + (endX - startX) * progress);
            component.setLocation(currentX, component.getY());
            
            if (progress >= 1f) {
                ((Timer) e.getSource()).stop();
                component.setLocation(endX, component.getY());
            }
        });
        
        timer.start();
    }
    
    /**
     * Pulse animasi untuk highlight
     */
    public static void pulseAnimation(JComponent component, int duration) {
        Timer timer = new Timer(50, null);
        float[] pulse = {0f};
        
        timer.addActionListener(e -> {
            pulse[0] += 0.05f;
            if (pulse[0] > 1f) {
                pulse[0] = 0f;
            }
            
            float alpha = 0.3f + (0.4f * (float)Math.abs(Math.sin(pulse[0] * Math.PI)));
            component.repaint();
            
            if (System.currentTimeMillis() > startTime + duration) {
                ((Timer) e.getSource()).stop();
            }
        });
        
        timer.start();
    }
    
    static long startTime;
    
    /**
     * Animasi zoom in
     */
    public static void zoomIn(JComponent component, int duration) {
        startTime = System.currentTimeMillis();
        float[] scale = {0.7f};
        
        Timer timer = new Timer(10, null);
        
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1f, (float) elapsed / duration);
            
            scale[0] = 0.7f + (0.3f * progress);
            component.repaint();
            
            if (progress >= 1f) {
                ((Timer) e.getSource()).stop();
            }
        });
        
        timer.start();
    }
    
    /**
     * Animasi fade in text
     */
    public static void fadeInText(JLabel label, int duration) {
        float[] alpha = {0f};
        Timer timer = new Timer(20, null);
        
        timer.addActionListener(e -> {
            alpha[0] += 0.05f;
            if (alpha[0] >= 1f) {
                alpha[0] = 1f;
                ((Timer) e.getSource()).stop();
            }
        });
        
        timer.start();
    }
    
    /**
     * Animasi typing text
     */
    public static void typeText(JLabel label, String text, int speed) {
        Timer timer = new Timer(speed, null);
        int[] index = {0};
        
        timer.addActionListener(e -> {
            if (index[0] <= text.length()) {
                label.setText(text.substring(0, index[0]));
                index[0]++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        
        timer.start();
    }
    
    /**
     * Animasi rotating icon (loading spinner)
     */
    public static Timer createRotatingAnimation(JLabel iconLabel, int duration) {
        Timer timer = new Timer(30, null);
        float[] angle = {0f};
        
        timer.addActionListener(e -> {
            angle[0] += 10;
            if (angle[0] >= 360) {
                angle[0] = 0;
            }
            iconLabel.repaint();
        });
        
        timer.start();
        return timer;
    }
}
