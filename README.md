# Player Frame

A RuneLite plugin that displays your health and prayer in a WoW-style player frame with your character's portrait.

![Player Frame Screenshot](https://i.imgur.com/placeholder.png)

## Features

- **WoW-Style Unit Frame**: Classic World of Warcraft-inspired player frame design
- **Real Character Portrait**: Displays your actual RuneScape character's chathead
- **Dynamic Health Bar**: Color-coded health bar (green → yellow → red based on HP percentage)
- **Prayer/Mana Bar**: Blue prayer bar styled like WoW's mana bar
- **Combat Level Badge**: Shows your combat level in a gold badge
- **Highly Customizable**: Extensive configuration options for colors, sizes, and display preferences

## Configuration Options

### Display Options
- **Show Portrait**: Toggle character portrait display
- **Show Combat Level**: Display combat level badge on portrait
- **Show Player Name**: Display your character name above the bars
- **Show Health Values**: Display current/max health numbers on the health bar
- **Show Prayer Values**: Display current/max prayer numbers on the prayer bar

### Size Options
- **Bar Width**: Adjust the width of health and prayer bars (150-400px)
- **Health Bar Height**: Customize health bar height (20-50px)
- **Prayer Bar Height**: Customize prayer bar height (15-40px)
- **Portrait Size**: Adjust character portrait size (50-100px)

### Color Customization
- **Health Color (High)**: Health bar color when above 60% (default: bright green)
- **Health Color (Mid)**: Health bar color when between 30-60% (default: yellow)
- **Health Color (Low)**: Health bar color when below 30% (default: red)
- **Prayer Bar Color**: Prayer bar color (default: bright blue)
- **Background Color**: Frame background color
- **Border Color**: Frame border color

## Installation

### From Plugin Hub (Recommended)
1. Open RuneLite
2. Click the **Plugin Hub** button (wrench icon)
3. Search for **"Player Frame"**
4. Click **Install**

### Manual Installation (Development)
1. Clone this repository
2. Build the plugin:
   ```bash
   gradle build
   ```
3. The JAR will be in `build/libs/`

## Building from Source

### Requirements
- Java 11 or higher
- Gradle 8.x

### Build Steps
```bash
# Clone the repository
git clone https://github.com/YOUR_USERNAME/player-frame.git
cd player-frame

# Build the plugin
gradle build

# The output JAR will be in build/libs/
```

## Development

To run the plugin in development mode:

1. Clone the RuneLite repository
2. Copy the plugin source into RuneLite's plugins directory
3. Run RuneLite from IntelliJ IDEA:
   ```
   runelite-client/src/main/java/net/runelite/client/RuneLite.java
   ```

## Screenshots

*Coming soon*

## Credits

- Inspired by World of Warcraft's unit frames
- Uses RuneScape's avatar service for character portraits
- Built for [RuneLite](https://runelite.net/)

## License

This project is licensed under the BSD 2-Clause License - see the [LICENSE](LICENSE) file for details.

## Support

If you encounter any bugs or have feature requests, please open an issue on the [GitHub repository](https://github.com/YOUR_USERNAME/player-frame/issues).

## Changelog

### Version 1.0.0
- Initial release
- WoW-style player frame with portrait
- Real character chathead from RuneScape avatar service
- Customizable colors and sizes
- Dynamic health bar colors
- Combat level badge
- Extensive configuration options
