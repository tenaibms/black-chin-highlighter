package me.tenai.blackchinhighlighter;

import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import java.awt.*;

/*
 * Credits:
 *
 * https://github.com/MoreBuchus/buchus-plugins/blob/tzhaar-hp-tracker/src/main/java/com/tzhaarhptracker/TzhaarHPTrackerOverlay.java
 *  For the highlighting code
 *
 */

public class BlackChinHighlightOverlay extends Overlay {
    private final BlackChinHighlighterPlugin plugin;
    private final BlackChinHighlighterConfig config;
    private final Client client;
    private final ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private BlackChinHighlightOverlay(BlackChinHighlighterPlugin plugin, BlackChinHighlighterConfig config,
                                      Client client, ModelOutlineRenderer modelOutlineRenderer)
    {
        this.plugin = plugin;
        this.config = config;
        this.client = client;
        this.modelOutlineRenderer = modelOutlineRenderer;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(Overlay.PRIORITY_HIGHEST);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        WorldView wv = client.getTopLevelWorldView();

        if(plugin.isCalibrating()) {
            var loadedNpcs = wv.npcs();
            for(var npc : loadedNpcs) {
                if(npc.isDead()) continue;
                if(plugin.getNpcs().contains(npc)) continue;
                if(npc.getName().equals("Black chinchompa")) {
                    NPCComposition npcComposition = npc.getTransformedComposition();
                    if(npcComposition != null) {
                        Shape hull = npc.getConvexHull();
                        if (hull != null) {
                            renderPoly(graphics, new Color(0, 255, 0, 255), new Color(0, 0, 0, 0), hull, config.highlightOutlineWidth());
                        }

                    }
                }
            }
        }

        for(var npc : plugin.getNpcs()) {
            if(npc.isDead()) continue;
            NPCComposition npcComposition = npc.getTransformedComposition();
            if (npcComposition != null) {
                if(config.highlightStyle().contains(BlackChinHighlighterConfig.HighlightStyle.HULL)) {
                    Shape hull = npc.getConvexHull();
                    if (hull != null) {
                        renderPoly(graphics, config.highlightOutlineColor(), config.highlightFillColor(), hull, config.highlightOutlineWidth());
                    }
                }

                if(config.highlightStyle().contains(BlackChinHighlighterConfig.HighlightStyle.TILE)) {
                    LocalPoint lp = npc.getLocalLocation();

                    if (lp != null) {
                        Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, lp, 1);
                        if(tilePoly != null) {
                            renderPoly(graphics, config.highlightOutlineColor(), config.highlightFillColor(), tilePoly, config.highlightOutlineWidth());
                        }
                    }
                }

                if(config.highlightStyle().contains(BlackChinHighlighterConfig.HighlightStyle.TRUE_TILE)) {
                    LocalPoint lp = LocalPoint.fromWorld(wv, npc.getWorldLocation());

                    if (lp != null) {
                        lp = new LocalPoint(lp.getX() + 128 / 2 - 64, lp.getY() + 128 / 2 - 64, wv);
                        Polygon tilePoly = Perspective.getCanvasTileAreaPoly(client, lp, 1);
                        if(tilePoly != null) {
                            renderPoly(graphics, config.highlightOutlineColor(), config.highlightFillColor(), tilePoly, config.highlightOutlineWidth());
                        }
                    }
                }

                if(config.highlightStyle().contains(BlackChinHighlighterConfig.HighlightStyle.OUTLINE)) {
                    modelOutlineRenderer.drawOutline(npc, (int) config.highlightOutlineWidth(), config.highlightOutlineColor(), 4);
                }
            }
        }
        return null;
    }

    private void renderPoly(Graphics2D g, Color outlineColor, Color fillColor, Shape polygon, double width)
    {
        if (polygon != null)
        {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(outlineColor);
            g.setStroke(new BasicStroke((float) width));
            g.draw(polygon);
            g.setColor(fillColor);
            g.fill(polygon);
        }
    }
}
