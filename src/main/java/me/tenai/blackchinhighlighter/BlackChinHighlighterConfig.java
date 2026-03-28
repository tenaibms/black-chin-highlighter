package me.tenai.blackchinhighlighter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;
import java.util.Set;

/*
* Credits:
*
* https://github.com/MoreBuchus/buchus-plugins/blob/tzhaar-hp-tracker/src/main/java/com/tzhaarhptracker/TzhaarHPTrackerConfig.java
* For the highlight style enum
*
*/

@ConfigGroup("Black Chinchompa Highlighter")
public interface BlackChinHighlighterConfig extends Config
{
    @ConfigItem(
            position = 1,
            keyName = "highlightStyle",
            name = "Highlight Style",
            description = "Picks the highlight style you want for the chinchompas"
    )
    default Set<HighlightStyle> highlightStyle()
    {
        return Set.of(HighlightStyle.HULL);
    }
    @Alpha
    @ConfigItem(
            position = 2,
            keyName = "outlineColor",
            name = "Outline Color",
            description = "Outline color for the highlighted chinchompas"
    )
    default Color highlightOutlineColor()
    {
        return new Color(255, 255, 255, 255);
    }

    @Alpha
    @ConfigItem(
            position = 3,
            keyName = "fillColor",
            name = "Fill Color",
            description = "Fill color for the highlighted chinchompas"
    )
    default Color highlightFillColor()
    {
        return new Color(255, 255, 255, 64);
    }

    @Alpha
    @ConfigItem(
            position = 4,
            keyName = "outlineColorDead",
            name = "Fill Color",
            description = "Outline color for the dying chinchompas"
    )
    default Color highlightOutlineColorDead()
    {
        return new Color(255, 0, 0, 255);
    }

    @Alpha
    @ConfigItem(
            position = 5,
            keyName = "fillColorDead",
            name = "Fill Color",
            description = "Fill color for the dying chinchompas"
    )
    default Color highlightFillColorDead()
    {
        return new Color(255, 0, 0, 64);
    }

    @ConfigItem(
            position = 6,
            keyName = "outlineWidth",
            name = "Outline Width",
            description = "Outline width for the highlighted chinchompas"
    )
    default double highlightOutlineWidth() {
        return 2.0;
    }

    @ConfigItem(
            position = 7,
            keyName = "recolorDeadChins",
            name = "Recolor Dying Chinchompas",
            description = "Changes the color for Chinchompas that will die soon (after being shot). May be somewhat buggy but errors should correct themselves."
    )
    default boolean recolorDeadChinchompas() { return true; }

    @ConfigItem(
            position = 8,
            keyName = "highlightFourthChinchompa",
            name = "Highlight North West Spawn",
            description = "Highlights the North West Chinchompa, this is the only one that isn't insta-lured, so it's optional to kill and it's usually better to prioritize the other three Chinchompas."
    )
    default boolean highlightFourthChinchompa() { return false; }

    @Getter
    @RequiredArgsConstructor
    enum HighlightStyle
    {
        TILE("Tile"),
        TRUE_TILE("True Tile"),
        HULL("Hull"),
        OUTLINE("Outline");

        @Getter
        private final String group;

        @Override
        public String toString()
        {
            return group;
        }
    }
}
