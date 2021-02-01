package BuildAHouse;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class BuildAHouse extends JavaPlugin implements Listener {
    boolean isPlacedCraftingTable = false;
    boolean isSandBroken = false;
    int logsPlaced = 0;

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

        // Break sand
        if (block.getType().equals(Material.SAND)) {
            event.setDropItems(false);
            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.GLASS, 1));

            if (!isSandBroken) {
                isSandBroken = true;

                var wall76 = new HashMap<String, Integer[]>();
                wall76.put("x", new Integer[]{
                        -91, -92, -93, -94, -95, -96, -97, -90, -90, -90, -98, -98, -98, -91, -92, -93, -94, -95, -96, -97
                });
                wall76.put("z", new Integer[]{
                        8, 8, 8, 8, 8, 8, 8, 5, 6, 7, 5, 6, 7, 4, 4, 4, 4, 4, 4, 4,
                });

                var wall75 = new HashMap<String, Integer[]>();
                wall75.put("x", new Integer[]{
                        -91, -94, -97, -98, -98, -90, -90, -91, -93, -95, -97
                });
                wall75.put("z", new Integer[]{
                        8, 8, 8, 7, 5, 7, 5, 4, 4, 4, 4,
                });

                for (int i = 0; i < 11; i++) {
                    new Location(player.getWorld(), wall75.get("x")[i], 75, wall75.get("z")[i]).getBlock().setType(Material.OAK_PLANKS);
                }

                for (int i = 0; i < 20; i++) {
                    new Location(player.getWorld(), wall76.get("x")[i], 76, wall76.get("z")[i]).getBlock().setType(Material.OAK_PLANKS);
                }

                var left = new Location(player.getWorld(), -94, 74, 7).getBlock();
                left.setType(Material.CHEST);
                var leftData = left.getBlockData();
                if (leftData instanceof Chest) {
                    ((Chest) leftData).setType(Chest.Type.LEFT);
                    left.setBlockData(leftData);
                }

                var right = new Location(player.getWorld(), -93, 74, 7).getBlock();
                right.setType(Material.CHEST);
                var rightData = left.getBlockData();
                if (rightData instanceof Chest) {
                    ((Chest) rightData).setType(Chest.Type.RIGHT);
                    right.setBlockData(rightData);
                }


                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 3; j++) {
                        var location = new Location(player.getWorld(), -97 + i, 76, 5 + j);
                        location.getBlock().setType(Material.OAK_SLAB);
                        var data = location.getBlock().getBlockData();
                        if (data instanceof Slab) {
                            ((Slab) data).setType(Slab.Type.TOP);
                        }
                        location.getBlock().setBlockData(data);
                    }
                }

            }
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        var player = event.getPlayer();
        if (event.getInventory().getType().equals(InventoryType.CHEST)) {
            event.setCancelled(true);
            Material[] items = {Material.OAK_DOOR, Material.CRAFTING_TABLE, Material.FURNACE, Material.RED_BED};
            for (var item : items) {
                player.getInventory().addItem(new ItemStack(item, 1));
            }

            // Build roof
            var stairs = new HashMap<String, Integer[]>();
            stairs.put("y", new Integer[]{
                    76, 77, 78
            });
            stairs.put("z", new Integer[]{
                    3, 4, 5
            });

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    var location = new Location(player.getWorld(), -98 + j, stairs.get("y")[i], stairs.get("z")[i]);
                    location.getBlock().setType(Material.OAK_STAIRS);
                    var data = location.getBlock().getBlockData();
                    if (data instanceof Stairs) {
                        ((Stairs) data).setFacing(BlockFace.SOUTH);
                    }
                    location.getBlock().setBlockData(data);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        var pillar = new HashMap<String, Integer[]>();
        pillar.put("y", new Integer[]{
                74, 75, 76
        });
        pillar.put("z", new Integer[]{
                4, 8
        });

        var base = new HashMap<String, Integer[]>();
        base.put("x", new Integer[]{
                -91, -92, -93, -95, -96, -97, -91, -92, -93, -94, -95, -96, -97, -90, -90, -90, -98, -98, -98
        });
        base.put("z", new Integer[]{
                4, 4, 4, 4, 4, 4, 8, 8, 8, 8, 8, 8, 8, 5, 6, 7, 5, 6, 7,
        });

        var sand = new HashMap<String, Integer[]>();
        sand.put("x", new Integer[]{
                -108, -108, -109, -109, -109, -109, -109, -110
        });
        sand.put("y", new Integer[]{
                73, 73, 73, 73, 73, 74, 74, 73
        });
        sand.put("z", new Integer[]{
                8, 9, 8, 9, 10, 8, 9, 9
        });


        var player = event.getPlayer();
        var block = event.getBlock();
        if (block.getType().equals(Material.OAK_PLANKS) && !isPlacedCraftingTable) {
            block.getLocation().getBlock().setType(Material.CRAFTING_TABLE);
            isPlacedCraftingTable = true;
        }

        if (block.getType().equals(Material.OAK_LOG)) {
            logsPlaced += 1;
            if (logsPlaced == 2) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        new Location(player.getWorld(), -98, pillar.get("y")[j], pillar.get("z")[i]).getBlock().setType(Material.OAK_LOG);
                    }
                }

                for (int i = 0; i < 8; i++) {
                    new Location(player.getWorld(), sand.get("x")[i], sand.get("y")[i], sand.get("z")[i]).getBlock().setType(Material.SAND);
                }


            }

            if (logsPlaced == 3) {
                try {
                    for (int y : pillar.get("y")) {
                        new Location(player.getWorld(), -90, y, 8).getBlock().setType(Material.OAK_LOG);
                    }
                    player.getInventory().remove(Material.OAK_LOG);

                    player.teleport(new Location(player.getWorld(), -97.6, 77, 8.4, -10, 90));

                    Thread.sleep(1000);

                    for (int i = 0; i < 19; i++) {
                        new Location(player.getWorld(), base.get("x")[i], 74, base.get("z")[i]).getBlock().setType(Material.COBBLESTONE);
                    }
                    player.getInventory().remove(Material.COBBLESTONE);
                } catch (InterruptedException ex) {
                }
            }
        }

        if (event.getBlock().getType().equals(Material.OAK_DOOR)) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 3; j++) {
                    var location = new Location(player.getWorld(), -97 + i, 73, 5 + j);
                    location.getBlock().setType(Material.OAK_PLANKS);
                }
            }
            var location = new Location(player.getWorld(), -94, 73, 4);
            location.getBlock().setType(Material.OAK_PLANKS);
        }

        if (event.getBlock().getType().equals(Material.RED_BED)) {
            var location = new Location(player.getWorld(), -96, 74, 7);
            location.getBlock().setType(Material.OAK_STAIRS);
            var data = location.getBlock().getBlockData();
            if (data instanceof Stairs) {
                ((Stairs) data).setFacing(BlockFace.SOUTH);
                ((Stairs) data).setHalf(Bisected.Half.TOP);
            }
            location.getBlock().setBlockData(data);

            new Location(player.getWorld(), -96, 75, 7).getBlock().setType(Material.POTTED_CORNFLOWER);
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

    @EventHandler
    public void onDamageEvent(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            event.setCancelled(true);
        }
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    public void herobrineInteraction(Location loc, Player player) {
        var herobrineTask = new HerobrineTask(loc.clone(), player, (int) (loc.clone().getY() / 2)).runTaskTimer(this, 60, 1);
        Material[] items = {Material.STONE_SWORD, Material.STONE_PICKAXE, Material.STONE_AXE,
                Material.STONE_SHOVEL, Material.OAK_LOG, Material.COBBLESTONE};
        int[] amounts = {1, 1, 1, 1, 12, 19};

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(7000);
                    Bukkit.broadcastMessage("<Herobrine> Sorry wrong guy");
                    Thread.sleep(2000);
                    Bukkit.broadcastMessage("<Herobrine> Hope this helps!");

                    for (int i = 0; i < items.length; i++) {
                        player.getInventory().addItem(new ItemStack(items[i], amounts[i]));
                        Thread.sleep(500);
                    }

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }.runTaskAsynchronously(this);

    }
}