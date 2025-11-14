<div align="center">
  <img src="assets/easyrtp.png" alt="EasyRTP Logo" width="200">
  <h1>EasyRTP</h1>
  <p><strong>Random teleports: Simple. Safe. Fast</strong></p>
  
  [![Minecraft](https://img.shields.io/badge/Minecraft-1.19.4--1.21.X-brightgreen)](https://www.minecraft.net/)
  [![License](https://img.shields.io/badge/License-MIT-blue.svg)](license)
  [![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://www.java.com/)
</div>

## Features

- **Random Teleportation** - Teleport to random safe locations with `/rtp`
- **Multi-World Support** - Specify different worlds for teleportation
- **Player Teleportation** - Teleport other players with `/rtpo`
- **Visual Effects** - Titles, blindness effects, and sound feedback
- **Cost System** - Configurable hunger and XP costs
- **Smart Location Finding** - Safe location detection with chunk preloading
- **Performance Optimized** - Chunk preloading for instant world loading
- **Anti-Spam Protection** - Prevents multiple simultaneous teleports
- **Fully Customizable** - Extensive configuration options
- **Permission System** - Granular permission control
- **Multi-version Support** - Compatible with Minecraft 1.19.4 through 1.21.X

## Commands

| Command                  | Description                                  | Permission      |
| ------------------------ | -------------------------------------------- | --------------- |
| `/rtp`                   | Teleport yourself to a random location       | `easyrtp.rtp`   |
| `/rtp <player>`          | Teleport another player to a random location | `easyrtp.rtp`   |
| `/rtp <world>`           | Teleport yourself to a specific world        | `easyrtp.rtp`   |
| `/rtp <player> <world>`  | Teleport player to a specific world          | `easyrtp.rtp`   |
| `/rtpo <player>`         | Teleport another player (admin command)      | `easyrtp.rtpo`  |
| `/rtpo <player> <world>` | Teleport player to specific world (admin)    | `easyrtp.rtpo`  |
| `/easyrtp reload`        | Reload the plugin configuration              | `easyrtp.admin` |

## Permissions

### Basic Permissions

- `easyrtp.rtp` - Use random teleport (default: true)
- `easyrtp.rtpo` - Teleport other players (default: op)

### Advanced Permissions

- `easyrtp.admin` - Access admin commands (default: op)
- `easyrtp.world.bypass` - Bypass world blacklist restrictions (default: op)
- `easyrtp.cooldown.bypass` - Bypass cooldown timers (default: op)

### Permission Groups

- `easyrtp.*` - All EasyRTP permissions (default: op)

## Configuration

### Basic Settings

```yaml
settings:
  cooldown: 30 # Time in seconds between random teleports
  teleport-delay: 3 # Delay before teleporting (0 = instant)
  max-attempts: 100 # Maximum attempts to find a safe location
  min-distance: 1000 # Minimum distance from spawn
  max-distance: 10000 # Maximum distance from spawn
  safe-location-check: true # Check for safe locations
  world-blacklist: [] # Worlds where RTP is disabled
  preload-chunks: true # Preload chunks around teleport location
  preload-radius: 2 # Radius of chunks to preload
```

### Cost System

```yaml
settings:
  hunger-cost: 0 # Hunger points to consume (0-20, 0 = disabled)
  xp-cost: 0 # Experience levels to consume (0 = disabled)
  require-costs: false # Require costs even with bypass permission
```

### Visual Effects

```yaml
settings:
  enable-sounds: true # Play teleport sounds
  enable-titles: true # Show teleport titles
  title-fade-in: 10 # Title fade in duration (ticks)
  title-stay: 40 # Title display duration (ticks)
  title-fade-out: 10 # Title fade out duration (ticks)
  blindness-duration: 1 # Blindness effect duration (seconds, 0 = disabled)
```

### Messages

All messages are fully customizable with color codes and placeholders:

```yaml
messages:
  prefix: "&7[&6EasyRTP&7] &r"
  teleport-success: "&aYou have been teleported to a random location!"
  teleport-location: "&7Location: &e%x%, %y%, %z% &7in &e%world%"
  teleport-time: "&7Teleport took &e%time%ms"
  # ... and many more customizable messages
```

## Features in Detail

### Smart Location Finding

- **Safe Location Detection**: Avoids lava, void, and dangerous blocks
- **Configurable Distance**: Set minimum and maximum distances from spawn
- **Y-Level Constraints**: Control teleport height range
- **Multiple Attempts**: Configurable attempts to find suitable locations

### Performance Optimizations

- **Chunk Preloading**: Automatically loads chunks around teleport location
- **Configurable Radius**: Adjust preload area for performance vs. speed
- **Efficient Algorithms**: Optimized location finding with early termination

### Visual & Audio Effects

- **Animated Titles**: "TELEPORTING..." and "TELEPORTED!" with custom timing
- **Blindness Effect**: Temporary blindness for immersive teleportation
- **Sound Effects**: Configurable teleport sounds
- **Location Information**: Shows exact coordinates and teleport timing

### Economic Balance

- **Hunger Costs**: Consume food points for teleportation
- **XP Costs**: Require experience levels for teleportation
- **Bypass Options**: Admins can skip costs with permissions
- **Flexible Configuration**: Enable/disable costs per server needs

### Safety & Anti-Abuse

- **Cooldown System**: Prevent teleport spam
- **Multiple RTP Prevention**: Block simultaneous teleport attempts
- **World Blacklisting**: Disable RTP in specific worlds
- **Permission-Based Access**: Granular control over features

## Building from Source

```bash
git clone https://github.com/Membercat-Studios/EasyRTP.git
cd EasyRTP

# Build the plugin
./gradlew shadowJar

# Find the compiled jar in build/libs/
```

## Support

If you encounter issues or have suggestions, please [open an issue](https://github.com/Membercat-Studios/EasyRTP/issues) with:

- Server version and type
- Plugin version
- Java version
- Error messages (if any)
- Steps to reproduce
- Configuration file (if relevant)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Feel free to submit pull requests or open issues for bugs and feature requests.

---

Made with ❤️ by [maybeizen](https://maybeizen.space)
