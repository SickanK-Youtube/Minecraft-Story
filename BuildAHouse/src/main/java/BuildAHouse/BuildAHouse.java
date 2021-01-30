package BuildAHouse;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BuildAHouse extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.registerEvents();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var player = event.getPlayer();
        var block = event.getBlock();

        // Break tree log
        if ((player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) &&
                block.getType().equals(Material.OAK_LOG)) {
            event.setDropItems(false);
            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.OAK_PLANKS, 4));
            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.STICK, 2));
        }

        // Break stone with wooden pickaxe
        if ((player.getInventory().getItemInMainHand().getType().equals(Material.WOODEN_PICKAXE)) &&
                block.getType().equals(Material.STONE)) {
            player.getInventory().remove(Material.WOODEN_PICKAXE);
            this.herobrineInteraction(player.getLocation().add(0, 0, 0), player);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var block = event.getBlock();
        if (block.getType().equals(Material.OAK_PLANKS)) {
            block.getLocation().getBlock().setType(Material.CRAFTING_TABLE);
        }
    }

    @EventHandler
    public void onClickCraftingTable(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.CRAFTING_TABLE)) {
            event.setCancelled(true);
            player.getInventory().removeItem(new ItemStack(Material.OAK_PLANKS, 3), new ItemStack(Material.STICK, 2));
            player.getInventory().addItem(new ItemStack(Material.WOODEN_PICKAXE, 1));
        }
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void herobrineInteraction(Location loc, Player player) {
        var herobrineTask = new HerobrineTask(loc.clone(), player, (int) (loc.clone().getY() / 2)).runTaskTimer(this, 60, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                    Bukkit.broadcastMessage("<Herobrine> Sorry wrong guy");
                    Thread.sleep(1000);
                    Bukkit.broadcastMessage("<Herobrine> Hope this helps!");
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }.runTaskAsynchronously(this);

    }
}
