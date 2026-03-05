package me.tenai.blackchinhighlighter;

import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;

public class BlackChinHighlighterPanel extends PluginPanel {
    BlackChinHighlighterPlugin plugin;
    Client client;

    @Inject
    BlackChinHighlighterPanel(BlackChinHighlighterPlugin plugin, Client client) {
        this.plugin = plugin;
        this.client = client;
    }

    void Init() {
        getParent().setLayout(new BorderLayout());
        getParent().add(this, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JButton button = new JButton("Recalibrate");
        button.setBackground(ColorScheme.DARK_GRAY_COLOR);
        button.addActionListener(e -> plugin.recalibrate());

        JPanel frame = new JPanel();
        frame.add(button);
        frame.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        container.add(frame, BorderLayout.CENTER);

        JLabel calibrationLabel = new JLabel("<html><center><h1>Black Chinchompa Highlighter</h1><p><strong>Instructions</strong></p></Center>To use this plugin, go to the Black Chinchompa area and begin shooting the marked Chinchompas until the plugin detects the four spawns. You will need to do this at the beginning of every session, but it should persist on log-outs and server hops. If the Chinchompas are highlighted incorrectly, see the next section.<br><br><center><p><strong>Recalibration</strong></p></center>Pressing the button will require you to recalibrate which Chinchompas to highlight, but if a Chinchompa is being highlighted incorrectly pressing this should fix it.<br><br></center></html>");
        add(calibrationLabel, BorderLayout.PAGE_START);
        add(container, BorderLayout.CENTER);
    }
}