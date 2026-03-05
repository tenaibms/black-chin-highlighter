package me.tenai.blackchinhighlighter;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@PluginDescriptor(
	name = "Black Chinchompa Highlighter"
)
public class BlackChinHighlighterPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private BlackChinHighlighterConfig config;

	@Inject
	private BlackChinHighlightOverlay highlightOverlay;

	@Inject
	private CalibrationOverlay calibrationOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Getter
	private boolean calibrated = false;

	@Getter
	private boolean calibrating = false;

	private final ArrayList<WorldPoint> coordinateList = new ArrayList<>(Arrays.asList(
			new WorldPoint(3139, 3780, 0),
			new WorldPoint(3136, 3782, 0),
			new WorldPoint(3140, 3784, 0),
			new WorldPoint(3134, 3786, 0)
	));

	private HashSet<Integer> removedNpcIndexes = new HashSet<>();

	@Getter
	private HashSet<Integer> npcIndexes = new HashSet<>();

	@Getter
	private HashSet<NPC> npcs = new HashSet<>();

	@Inject
	private ClientToolbar clientToolbar;

	private BlackChinHighlighterPanel panel;
	private NavigationButton navButton;

	@Subscribe
	private void onNpcSpawned(NpcSpawned event) {
		var npc = event.getNpc();
		if(calibrating
				&& npc.getName().equals("Black chinchompa")
				&& coordinateList.contains(npc.getWorldLocation())
				&& removedNpcIndexes.contains(npc.getIndex())
		) {
			npcIndexes.add(npc.getIndex());
			if(npcIndexes.size() >= 4) {
				calibrated = true;
				calibrating = false;
			}
		}
		if(npcIndexes.contains(npc.getIndex())) {
			npcs.add(npc);
		}
	}

	@Subscribe
	private void onNpcDespawned(NpcDespawned e) {
		npcs.removeIf(n -> n.getIndex() == e.getNpc().getIndex());
		if(calibrating) {
			removedNpcIndexes.add(e.getNpc().getIndex());
		}
	}

	@Subscribe
	private void onGameTick(GameTick event) {
		if(!calibrated) {
			var chinArea = new WorldArea(3125, 3765, 25, 30, 0);
			var playerLocation = client.getLocalPlayer().getWorldLocation();
			if(playerLocation.isInArea(chinArea)) {
				overlayManager.add(calibrationOverlay);
				calibrating = true;
			} else {
				overlayManager.remove(calibrationOverlay);
				calibrating = false;
			}
		}
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event) {
		npcs.clear();
	}

	public void recalibrate() {
		npcIndexes.clear();
		npcs.clear();

		calibrated = false;
		calibrating = false;
	}

	@Override
	protected void startUp() throws Exception
	{
		panel = injector.getInstance(BlackChinHighlighterPanel.class);
		panel.Init();

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), "/blackchin.png");

		navButton = NavigationButton.builder()
				.tooltip("Black Chinchompa Highlighter")
				.icon(icon)
				.priority(6)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);

		npcIndexes.clear();
		npcs.clear();
		overlayManager.add(highlightOverlay);
		overlayManager.add(calibrationOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		npcIndexes.clear();
		npcs.clear();
		overlayManager.remove(highlightOverlay);
		overlayManager.remove(calibrationOverlay);

		clientToolbar.removeNavigation(navButton);

		calibrated = false;
		calibrating = false;
	}

	@Provides
	BlackChinHighlighterConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(BlackChinHighlighterConfig.class);
	}
}
