package util;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.net.URL;

public class UIUtilities {
    // Color Palette - Earthy warm palette
    public static final Color PRIMARY_COLOR    = new Color(167, 111, 75);
    public static final Color SECONDARY_COLOR  = new Color(210, 137, 62);
    public static final Color ACCENT_COLOR     = new Color(248, 200, 118);
    public static final Color DARK_BG          = new Color(106, 73, 52);
    public static final Color LIGHT_BG         = new Color(250, 242, 228);
    public static final Color CARD_BG          = new Color(255, 248, 237);
    public static final Color TEXT_PRIMARY     = new Color(69, 44, 20);
    public static final Color TEXT_SECONDARY   = new Color(112, 84, 64);
    public static final Color ERROR_COLOR      = new Color(179, 73, 60);
    public static final Color SUCCESS_COLOR    = new Color(132, 158, 103);
    public static final Color WARNING_COLOR    = new Color(217, 145, 81);
    public static final Color BORDER_COLOR     = new Color(222, 205, 186);
    public static final Color MUTED_COLOR      = new Color(203, 177, 144);

    // Fonts
    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADING  = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_REGULAR  = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 12);

    /**
     * Load icon dari classpath (Maven resources: src/main/resources/assets/icon/)
     */
    public static ImageIcon loadIcon(String fileName, int width, int height) {
        // 1. Coba dari classpath (Maven jar / resources folder)
        URL resource = UIUtilities.class.getClassLoader().getResource("assets/icon/" + fileName);
        if (resource == null) {
            resource = UIUtilities.class.getResource("/assets/icon/" + fileName);
        }

        // 2. Fallback ke path file langsung (untuk development)
        if (resource == null) {
            String[] fallbackPaths = {
                "src/main/resources/assets/icon/" + fileName,
                "assets/icon/" + fileName
            };
            for (String path : fallbackPaths) {
                File f = new File(path);
                if (f.exists()) {
                    ImageIcon icon = new ImageIcon(path);
                    Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                }
            }
            return new ImageIcon(); // kosong jika tidak ditemukan
        }

        ImageIcon icon = new ImageIcon(resource);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public static JPanel createRoundedPanel(Color backgroundColor, int arcSize) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcSize, arcSize);
            }
        };
    }

    public static JButton createModernButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.setColor(fgColor);
                g2.setFont(FONT_HEADING);
                g2.drawString(getText(), x, y);
            }
        };
        button.setPreferredSize(new Dimension(200, 45));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFont(FONT_HEADING);
        button.setForeground(fgColor);
        button.setBackground(bgColor);
        button.setOpaque(false);
        return button;
    }

    public static JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                super.paintComponent(g);
            }
        };
        field.setPreferredSize(new Dimension(300, 45));
        field.setBackground(CARD_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setFont(FONT_REGULAR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        field.setCaretColor(PRIMARY_COLOR);
        return field;
    }

    public static JPasswordField createModernPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                super.paintComponent(g);
            }
        };
        field.setPreferredSize(new Dimension(300, 45));
        field.setBackground(CARD_BG);
        field.setForeground(TEXT_PRIMARY);
        field.setFont(FONT_REGULAR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        field.setCaretColor(PRIMARY_COLOR);
        return field;
    }

    public static JComboBox<String> createModernComboBox(String[] options) {
        JComboBox<String> combo = new JComboBox<>(options);
        combo.setFont(FONT_REGULAR);
        combo.setBackground(CARD_BG);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        combo.setPreferredSize(new Dimension(300, 36));
        return combo;
    }

    public static JTextArea createModernTextArea(int rows, int cols) {
        JTextArea area = new JTextArea(rows, cols);
        area.setFont(FONT_REGULAR);
        area.setForeground(TEXT_PRIMARY);
        area.setBackground(CARD_BG);
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    public static JPanel createCardPanel(int arcSize) {
        JPanel panel = createRoundedPanel(CARD_BG, arcSize);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        panel.setOpaque(false);
        return panel;
    }

    public static void fadeInPanel(JPanel panel, int duration) {
        Timer timer = new Timer(10, null);
        float[] alpha = {0f};
        timer.addActionListener(e -> {
            alpha[0] += 0.02f;
            if (alpha[0] >= 1f) { alpha[0] = 1f; ((Timer) e.getSource()).stop(); }
            panel.setOpaque(false);
            panel.repaint();
        });
        timer.start();
    }

    public static void slideDownAnimation(JComponent component, int targetY, int duration) {
        int startY = component.getY() - 50;
        Timer timer = new Timer(10, null);
        long[] startTime = {System.currentTimeMillis()};
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime[0];
            float progress = Math.min(1f, (float) elapsed / duration);
            int newY = (int) (startY + (targetY - startY) * progress);
            component.setLocation(component.getX(), newY);
            if (progress >= 1f) ((Timer) e.getSource()).stop();
        });
        timer.start();
    }

    public static void showErrorMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccessMessage(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static JDialog createLoadingDialog(JFrame parent, String message) {
        JDialog dialog = new JDialog(parent, true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 200));
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(parent);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 200));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel label = new JLabel(message);
        label.setForeground(Color.WHITE);
        label.setFont(FONT_REGULAR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        dialog.add(panel);
        return dialog;
    }

    public static JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        return separator;
    }
}
