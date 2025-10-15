package com.maybeizen.EasyRTP.managers;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import com.maybeizen.EasyRTP.EasyRTP;

import java.util.Random;

public class LocationFinder {
    private final EasyRTP plugin;
    private final Random random;

    public LocationFinder(EasyRTP plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    public Location findRandomLocation(World world) {
        int maxAttempts = plugin.getConfigManager().getMaxAttempts();
        int minDistance = plugin.getConfigManager().getMinDistance();
        int maxDistance = plugin.getConfigManager().getMaxDistance();
        int centerX = plugin.getConfigManager().getCenterX();
        int centerZ = plugin.getConfigManager().getCenterZ();
        int minY = plugin.getConfigManager().getMinY();
        int maxY = plugin.getConfigManager().getMaxY();
        boolean safeLocationCheck = plugin.getConfigManager().getSafeLocationCheck();

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            int x = centerX + random.nextInt(maxDistance * 2) - maxDistance;
            int z = centerZ + random.nextInt(maxDistance * 2) - maxDistance;
            
            double distance = Math.sqrt(x * x + z * z);
            if (distance < minDistance) {
                continue;
            }

            Location location = findHighestSolidBlock(world, x, z, minY, maxY);
            
            if (location == null) {
                continue;
            }

            if (safeLocationCheck && !isLocationSafe(location)) {
                continue;
            }

            return location;
        }

        return null;
    }

    private Location findHighestSolidBlock(World world, int x, int z, int minY, int maxY) {
        for (int y = maxY; y >= minY; y--) {
            Location location = new Location(world, x, y, z);
            Material blockType = location.getBlock().getType();
            
            if (blockType.isSolid() && blockType != Material.LAVA && blockType != Material.WATER) {
                Location playerLocation = location.clone().add(0, 1, 0);
                Location headLocation = location.clone().add(0, 2, 0);
                
                if (playerLocation.getBlock().getType().isAir() && headLocation.getBlock().getType().isAir()) {
                    return playerLocation;
                }
            }
        }
        
        return null;
    }

    private boolean isLocationSafe(Location location) {
        Location[] checkLocations = {
            location.clone().add(0, -1, 0), // Block below feet
            location.clone().add(1, 0, 0),  // Block to the right
            location.clone().add(-1, 0, 0), // Block to the left
            location.clone().add(0, 0, 1),  // Block in front
            location.clone().add(0, 0, -1), // Block behind
            location.clone().add(0, 1, 0),  // Block at head level
            location.clone().add(0, 2, 0)   // Block above head
        };

        for (Location checkLoc : checkLocations) {
            Material blockType = checkLoc.getBlock().getType();
            
            if (blockType == Material.LAVA || 
                blockType == Material.FIRE || 
                blockType == Material.CACTUS ||
                blockType == Material.MAGMA_BLOCK ||
                blockType == Material.WITHER_ROSE) {
                return false;
            }
        }

        if (location.getBlock().getType() == Material.WATER || 
            location.getBlock().getType() == Material.LAVA) {
            return false;
        }

        return true;
    }

    public void preloadChunks(Location location) {
        int radius = plugin.getConfigManager().getPreloadRadius();
        World world = location.getWorld();

        int centerChunkX = location.getBlockX() >> 4;
        int centerChunkZ = location.getBlockZ() >> 4;

        for (int x = centerChunkX - radius; x <= centerChunkX + radius; x++) {
            for (int z = centerChunkZ - radius; z <= centerChunkZ + radius; z++) {
                Chunk chunk = world.getChunkAt(x, z);
                if (!chunk.isLoaded()) {
                    chunk.load(true);
                }
            }
        }
    }
}
