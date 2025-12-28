package com.playerframe;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import java.awt.Color;

@ConfigGroup("playerframe")
public interface PlayerFrameConfig extends Config
{
	@ConfigItem(
		keyName = "showPortrait",
		name = "Show Portrait",
		description = "Display the character portrait/chathead",
		position = 1
	)
	default boolean showPortrait()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showLevel",
		name = "Show Combat Level",
		description = "Display combat level badge on portrait",
		position = 2
	)
	default boolean showLevel()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPlayerName",
		name = "Show Player Name",
		description = "Display the player's name above the bars",
		position = 3
	)
	default boolean showPlayerName()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showHealthText",
		name = "Show Health Values",
		description = "Display current/max health numbers on the health bar",
		position = 4
	)
	default boolean showHealthText()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showPrayerText",
		name = "Show Prayer Values",
		description = "Display current/max prayer numbers on the prayer bar",
		position = 5
	)
	default boolean showPrayerText()
	{
		return true;
	}

	@ConfigItem(
		keyName = "portraitStyle",
		name = "Portrait Style",
		description = "Choose between simple chathead or detailed character representation",
		position = 6
	)
	default PortraitStyle portraitStyle()
	{
		return PortraitStyle.SIMPLE;
	}

	enum PortraitStyle
	{
		SIMPLE,
		DETAILED
	}

	@ConfigItem(
		keyName = "barWidth",
		name = "Bar Width",
		description = "Width of the health and prayer bars",
		position = 10
	)
	@Range(min = 150, max = 400)
	default int barWidth()
	{
		return 220;
	}

	@ConfigItem(
		keyName = "healthBarHeight",
		name = "Health Bar Height",
		description = "Height of the health bar",
		position = 11
	)
	@Range(min = 20, max = 50)
	default int healthBarHeight()
	{
		return 28;
	}

	@ConfigItem(
		keyName = "prayerBarHeight",
		name = "Prayer Bar Height",
		description = "Height of the prayer bar",
		position = 12
	)
	@Range(min = 15, max = 40)
	default int prayerBarHeight()
	{
		return 22;
	}

	@ConfigItem(
		keyName = "portraitSize",
		name = "Portrait Size",
		description = "Size of the character portrait",
		position = 13
	)
	@Range(min = 50, max = 100)
	default int portraitSize()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "healthColorHigh",
		name = "Health Color (High)",
		description = "Health bar color when above 60%",
		position = 20
	)
	default Color healthColorHigh()
	{
		return new Color(0, 220, 0); // Bright green like WoW
	}

	@ConfigItem(
		keyName = "healthColorMid",
		name = "Health Color (Mid)",
		description = "Health bar color when between 30-60%",
		position = 21
	)
	default Color healthColorMid()
	{
		return new Color(255, 220, 0); // Bright yellow
	}

	@ConfigItem(
		keyName = "healthColorLow",
		name = "Health Color (Low)",
		description = "Health bar color when below 30%",
		position = 22
	)
	default Color healthColorLow()
	{
		return new Color(220, 0, 0); // Bright red
	}

	@ConfigItem(
		keyName = "prayerColor",
		name = "Prayer Bar Color",
		description = "Color of the prayer bar (like WoW's mana)",
		position = 23
	)
	default Color prayerColor()
	{
		return new Color(0, 120, 255); // Bright blue like WoW mana
	}

	@ConfigItem(
		keyName = "backgroundColor",
		name = "Background Color",
		description = "Background color of the unit frame",
		position = 24
	)
	default Color backgroundColor()
	{
		return new Color(30, 30, 30, 220);
	}

	@ConfigItem(
		keyName = "borderColor",
		name = "Border Color",
		description = "Border color of the bars and frame",
		position = 25
	)
	default Color borderColor()
	{
		return new Color(60, 60, 60);
	}
}
