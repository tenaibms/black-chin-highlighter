package me.tenai.blackchinhighlighter;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;

public class CalibrationOverlay extends OverlayPanel {
    private final BlackChinHighlighterPlugin plugin;
    private Long startTime;

    @Inject
    CalibrationOverlay(BlackChinHighlighterPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();

        int calibratedChins = plugin.getNpcIndexes().size();

        panelComponent.getChildren().add(LineComponent.builder()
                .left(String.format("Chinchompas %d / 4", calibratedChins))
                .build()
        );

        setPosition(OverlayPosition.TOP_LEFT);

        if(calibratedChins == 0) panelComponent.setBackgroundColor(new Color(255, 0, 0, 64));
        else if (calibratedChins < 4) panelComponent.setBackgroundColor(new Color(255, 128, 0, 64));
        else {
            panelComponent.setBackgroundColor(new Color(0, 255, 0, 64));
        }

        if(plugin.isCalibrated()) {
            if(startTime == null) startTime = System.currentTimeMillis();
            long timeSinceStart = System.currentTimeMillis() - startTime;
            if(timeSinceStart > 5000) return null;
        } else {
            startTime = null;
        }

        return panelComponent.render(graphics);
    }
}
