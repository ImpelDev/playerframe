package com.playerframe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.SpritePixels;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ComponentConstants;

@Slf4j
public class PlayerFrameOverlay extends Overlay
{
	private final Client client;
	private final PlayerFramePlugin plugin;
	private final PlayerFrameConfig config;
	private final SpriteManager spriteManager;

	private static final int PADDING = 10;
	private static final int BAR_SPACING = 3;
	private static final int PORTRAIT_BORDER = 4;
	private static final int NAME_SPACING = 5;

	@Inject
	private PlayerFrameOverlay(Client client, PlayerFramePlugin plugin,
									PlayerFrameConfig config, SpriteManager spriteManager)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.spriteManager = spriteManager;
		setPosition(OverlayPosition.TOP_LEFT);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (client.getGameState() != net.runelite.api.GameState.LOGGED_IN)
		{
			return null;
		}

		Player player = plugin.getPlayer();
		if (player == null)
		{
			return null;
		}

		// Enable anti-aliasing for smooth rendering
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int currentHealth = plugin.getCurrentHealth();
		int maxHealth = plugin.getMaxHealth();
		int currentPrayer = plugin.getCurrentPrayer();
		int maxPrayer = plugin.getMaxPrayer();

		int portraitSize = config.showPortrait() ? config.portraitSize() : 0;
		int barWidth = config.barWidth();
		int healthBarHeight = config.healthBarHeight();
		int prayerBarHeight = config.prayerBarHeight();

		// Calculate layout - WoW style with portrait on left, bars on right
		int barsStartX = portraitSize > 0 ? portraitSize + PADDING : PADDING;
		int totalWidth = barsStartX + barWidth + PADDING;

		int nameHeight = config.showPlayerName() ? 18 : 0;
		int barsHeight = healthBarHeight + prayerBarHeight + BAR_SPACING;
		int contentHeight = Math.max(nameHeight + barsHeight, portraitSize);
		int totalHeight = contentHeight + PADDING * 2;

		// Draw main background with darker, more solid WoW-style frame
		graphics.setColor(config.backgroundColor());
		graphics.fillRoundRect(0, 0, totalWidth, totalHeight, 6, 6);

		// Draw outer border (darker)
		graphics.setColor(new Color(20, 20, 20, 230));
		graphics.drawRoundRect(0, 0, totalWidth, totalHeight, 6, 6);

		// Draw inner highlight border (lighter, for depth)
		graphics.setColor(new Color(100, 100, 100, 150));
		graphics.drawRoundRect(1, 1, totalWidth - 2, totalHeight - 2, 6, 6);

		int yOffset = PADDING;

		// Draw portrait if enabled (starts at left edge)
		if (config.showPortrait())
		{
			drawPortrait(graphics, PADDING, yOffset, portraitSize, player);
		}

		// Draw player name if enabled
		int barsY = yOffset;
		if (config.showPlayerName())
		{
			graphics.setColor(new Color(255, 209, 0)); // WoW gold
			graphics.setFont(new Font("Arial", Font.BOLD, 13));
			String playerName = plugin.getPlayerName();

			// Add shadow for better readability
			graphics.setColor(new Color(0, 0, 0, 180));
			graphics.drawString(playerName, barsStartX + 1, barsY + 13 + 1);
			graphics.setColor(new Color(255, 209, 0));
			graphics.drawString(playerName, barsStartX, barsY + 13);

			barsY += nameHeight + NAME_SPACING;
		}

		// Draw health bar
		drawBar(graphics, barsStartX, barsY, barWidth, healthBarHeight,
			currentHealth, maxHealth, getHealthColor(currentHealth, maxHealth),
			config.showHealthText(), "HP");

		barsY += healthBarHeight + BAR_SPACING;

		// Draw prayer bar
		drawBar(graphics, barsStartX, barsY, barWidth, prayerBarHeight,
			currentPrayer, maxPrayer, config.prayerColor(),
			config.showPrayerText(), "Prayer");

		return new Dimension(totalWidth, totalHeight);
	}

	private void drawPortrait(Graphics2D graphics, int x, int y, int size, Player player)
	{
		// WoW-style portrait frame - darker outer border
		graphics.setColor(new Color(30, 30, 30, 230));
		graphics.fillRoundRect(x - 1, y - 1, size + 2, size + 2, 8, 8);

		// Gold/bronze frame border
		graphics.setColor(new Color(139, 101, 28)); // Dark bronze
		graphics.fillRoundRect(x, y, size, size, 6, 6);

		// Inner darker border for depth
		int innerBorder = 2;
		graphics.setColor(new Color(20, 20, 20));
		graphics.fillRoundRect(x + innerBorder, y + innerBorder,
			size - innerBorder * 2, size - innerBorder * 2, 4, 4);

		// Portrait background (before chathead)
		int portraitInset = PORTRAIT_BORDER;
		graphics.setColor(new Color(40, 40, 40));
		graphics.fillRoundRect(x + portraitInset, y + portraitInset,
			size - portraitInset * 2, size - portraitInset * 2, 4, 4);

		// Try to get and draw the actual player chathead
		BufferedImage chathead = getPlayerChathead(player, size - portraitInset * 2);

		if (chathead != null)
		{
			graphics.drawImage(chathead,
				x + portraitInset, y + portraitInset,
				size - portraitInset * 2, size - portraitInset * 2, null);
		}
		else
		{
			// Fallback - draw player name initial with better styling
			graphics.setColor(new Color(80, 120, 150)); // Blue-grey background
			graphics.fillRoundRect(x + portraitInset, y + portraitInset,
				size - portraitInset * 2, size - portraitInset * 2, 4, 4);

			// Draw initial
			graphics.setColor(new Color(255, 255, 255, 230));
			graphics.setFont(new Font("Arial", Font.BOLD, size / 2));
			String initial = plugin.getPlayerName().substring(0, 1).toUpperCase();
			int textWidth = graphics.getFontMetrics().stringWidth(initial);

			// Text shadow
			graphics.setColor(new Color(0, 0, 0, 180));
			graphics.drawString(initial,
				x + (size - textWidth) / 2 + 2,
				y + size / 2 + size / 6 + 2);

			// Actual text
			graphics.setColor(new Color(255, 255, 255, 230));
			graphics.drawString(initial,
				x + (size - textWidth) / 2,
				y + size / 2 + size / 6);
		}

		// Add inner highlight for a glossy WoW look
		graphics.setColor(new Color(255, 255, 255, 30));
		graphics.fillRoundRect(x + portraitInset, y + portraitInset,
			size - portraitInset * 2, (size - portraitInset * 2) / 3, 4, 4);

		// Draw combat level badge if enabled
		if (config.showLevel())
		{
			int badgeSize = size / 3 + 2;
			int badgeX = x + size - badgeSize - 4;
			int badgeY = y + size - badgeSize - 4;

			// Badge shadow
			graphics.setColor(new Color(0, 0, 0, 180));
			graphics.fillOval(badgeX + 2, badgeY + 2, badgeSize, badgeSize);

			// Badge background - WoW gold
			graphics.setColor(new Color(255, 209, 0));
			graphics.fillOval(badgeX, badgeY, badgeSize, badgeSize);

			// Badge border (darker gold)
			graphics.setColor(new Color(139, 101, 28));
			graphics.drawOval(badgeX, badgeY, badgeSize, badgeSize);
			graphics.drawOval(badgeX + 1, badgeY + 1, badgeSize - 2, badgeSize - 2);

			// Combat level text
			graphics.setColor(new Color(0, 0, 0, 255));
			graphics.setFont(new Font("Arial", Font.BOLD, 11));
			String level = String.valueOf(plugin.getCombatLevel());
			int levelWidth = graphics.getFontMetrics().stringWidth(level);
			graphics.drawString(level,
				badgeX + (badgeSize - levelWidth) / 2,
				badgeY + badgeSize / 2 + 5);
		}
	}

	private void drawBar(Graphics2D graphics, int x, int y, int width, int height,
						 int current, int max, Color barColor, boolean showText, String label)
	{
		// Calculate percentage
		float percentage = max > 0 ? (float) current / max : 0;
		int fillWidth = (int) (width * percentage);

		// Draw outer shadow/border
		graphics.setColor(new Color(0, 0, 0, 180));
		graphics.fillRoundRect(x + 1, y + 1, width, height, 5, 5);

		// Draw bar background (darker, like WoW)
		graphics.setColor(new Color(15, 15, 15, 230));
		graphics.fillRoundRect(x, y, width, height, 5, 5);

		// Draw bar fill with gradient
		if (fillWidth > 0)
		{
			// Main bar color
			graphics.setColor(barColor);
			graphics.fillRoundRect(x + 1, y + 1, fillWidth - 1, height - 2, 4, 4);

			// Top highlight (glossy effect like WoW)
			Color highlightColor = new Color(
				Math.min(255, barColor.getRed() + 60),
				Math.min(255, barColor.getGreen() + 60),
				Math.min(255, barColor.getBlue() + 60),
				120
			);
			graphics.setColor(highlightColor);
			graphics.fillRoundRect(x + 1, y + 1, fillWidth - 1, height / 3, 4, 4);

			// Inner darker edge for depth
			graphics.setColor(new Color(0, 0, 0, 80));
			graphics.drawRoundRect(x + 1, y + 1, fillWidth - 1, height - 2, 4, 4);
		}

		// Draw main border
		graphics.setColor(new Color(60, 60, 60, 230));
		graphics.drawRoundRect(x, y, width, height, 5, 5);

		// Draw inner border highlight
		graphics.setColor(new Color(100, 100, 100, 100));
		graphics.drawRoundRect(x + 1, y + 1, width - 2, height - 2, 4, 4);

		// Draw text if enabled
		if (showText)
		{
			String text = current + " / " + max;
			graphics.setFont(new Font("Arial", Font.BOLD, 12));
			int textWidth = graphics.getFontMetrics().stringWidth(text);

			// Text shadow for readability
			graphics.setColor(new Color(0, 0, 0, 220));
			graphics.drawString(text,
				x + (width - textWidth) / 2 + 1,
				y + height / 2 + 5 + 1);

			// Actual text
			graphics.setColor(Color.WHITE);
			graphics.drawString(text,
				x + (width - textWidth) / 2,
				y + height / 2 + 5);
		}
	}

	private Color getHealthColor(int current, int max)
	{
		float percentage = max > 0 ? (float) current / max : 0;

		if (percentage > 0.6f)
		{
			return config.healthColorHigh();
		}
		else if (percentage > 0.3f)
		{
			return config.healthColorMid();
		}
		else
		{
			return config.healthColorLow();
		}
	}

	// Cache for chathead images
	private BufferedImage cachedChathead = null;
	private String lastPlayerName = "";

	private BufferedImage getPlayerChathead(Player player, int size)
	{
		if (player == null)
		{
			return null;
		}

		String playerName = plugin.getPlayerName();
		if (playerName == null || playerName.equals("Unknown"))
		{
			return null;
		}

		// Check if we already have a cached chathead for this player
		if (cachedChathead != null && playerName.equals(lastPlayerName))
		{
			return cachedChathead;
		}

		try
		{
			// Use RuneScape's avatar service
			// Format: https://secure.runescape.com/m=avatar-rs/PLAYERNAME/chat.png
			String avatarUrl = "https://secure.runescape.com/m=avatar-rs/" +
				playerName.replace(" ", "%20") + "/chat.png";

			java.net.URL url = new java.net.URL(avatarUrl);
			BufferedImage img = javax.imageio.ImageIO.read(url);

			if (img != null)
			{
				// Scale it to the desired size
				BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = scaled.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.drawImage(img, 0, 0, size, size, null);
				g.dispose();

				// Cache it
				cachedChathead = scaled;
				lastPlayerName = playerName;

				return scaled;
			}
		}
		catch (Exception e)
		{
			// If fetching fails, return null to use fallback
			log.debug("Failed to fetch chathead for " + playerName, e);
		}

		return null;
	}
}
