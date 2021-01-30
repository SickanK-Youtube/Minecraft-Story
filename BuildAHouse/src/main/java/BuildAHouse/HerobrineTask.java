package BuildAHouse;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class HerobrineTask extends BukkitRunnable {
    private final Location location;
    private final Location initLocation;
    private final Player player;
    private int counter;

    public HerobrineTask(Location location, Player player, int counter) {
        this.location = location;
        this.initLocation = location.clone();
        this.counter = counter;
        this.player = player;
    }

    @Override
    public void run() {
        if (counter > 0) {
            location.add(0, -1, 0);
            location.getBlock().setType(Material.AIR);
            location.add(0, -1, 0);
            location.getBlock().setType(Material.AIR);
            counter -= 1;
        } else {
            player.teleport(initLocation.add(3, 10, 0));
            this.cancel();
        }
    }
}
