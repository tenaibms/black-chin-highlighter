package me.tenai.blackchinhighlighter;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class BlackChinHighlighterTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(BlackChinHighlighterPlugin.class);
		RuneLite.main(args);
	}
}