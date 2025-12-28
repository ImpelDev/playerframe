package com.playerframe;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Player Frame",
	description = "Displays health and prayer in a WoW-style player frame with character portrait",
	tags = {"health", "prayer", "overlay", "wow", "player", "frame", "unit"}
)
public class PlayerFramePlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private PlayerFrameConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PlayerFrameOverlay overlay;

	@Override
	protected void startUp()
	{
		log.info("Player Frame started!");
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		log.info("Player Frame stopped!");
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			log.debug("Player logged in");
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		// Plugin updates automatically via overlay render
	}

	@Provides
	PlayerFrameConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PlayerFrameConfig.class);
	}

	public Player getPlayer()
	{
		return client.getLocalPlayer();
	}

	public int getCurrentHealth()
	{
		return client.getBoostedSkillLevel(Skill.HITPOINTS);
	}

	public int getMaxHealth()
	{
		return client.getRealSkillLevel(Skill.HITPOINTS);
	}

	public int getCurrentPrayer()
	{
		return client.getBoostedSkillLevel(Skill.PRAYER);
	}

	public int getMaxPrayer()
	{
		return client.getRealSkillLevel(Skill.PRAYER);
	}

	public String getPlayerName()
	{
		Player player = getPlayer();
		return player != null ? player.getName() : "Unknown";
	}

	public int getCombatLevel()
	{
		return client.getLocalPlayer() != null ? client.getLocalPlayer().getCombatLevel() : 3;
	}
}
