package com.August.voidrestrict;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public record VoidListener(VoidRestrictPlugin plugin) implements Listener {

    private boolean isUnderBedrock(Location loc) {
        World.Environment env = loc.getWorld().getEnvironment();

        if (env == World.Environment.NORMAL && plugin.getConfig().getBoolean("worlds.overworld")) {
            return loc.getY() < -64.0;
        } else if (env == World.Environment.NETHER && plugin.getConfig().getBoolean("worlds.nether")) {
            return loc.getY() < 0.0;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getConfig().getBoolean("settings.prevent-break")) return;

        if (isUnderBedrock(event.getBlock().getLocation()) || isUnderBedrock(event.getPlayer().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getConfig().getBoolean("settings.prevent-place")) return;

        if (isUnderBedrock(event.getBlock().getLocation()) || isUnderBedrock(event.getPlayer().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!plugin.getConfig().getBoolean("settings.prevent-place")) return;

        if (isUnderBedrock(event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (!plugin.getConfig().getBoolean("settings.prevent-place")) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        ItemStack item = event.getItem();
        if (item == null) return;

        Material type = item.getType();

        if (type == Material.END_CRYSTAL || type.name().endsWith("_BED")) {
            Location placeLoc = clickedBlock.getRelative(event.getBlockFace()).getLocation();
            if (isUnderBedrock(placeLoc)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!plugin.getConfig().getBoolean("settings.prevent-attack")) return;

        Entity victim = event.getEntity();
        Entity damager = event.getDamager();

        if (isUnderBedrock(victim.getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (isUnderBedrock(damager.getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();
            if (shooter instanceof Entity) {
                if (isUnderBedrock(((Entity) shooter).getLocation())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}