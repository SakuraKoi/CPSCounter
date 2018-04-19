package ldcr.CpsCounter;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CpsCounter extends JavaPlugin implements Listener {
    public static CpsCounter instance;
    private static HashMap<UUID, Counter> counter = new HashMap<UUID, Counter>();
    private static HashMap<UUID, MonitorThread> monitors = new HashMap<UUID,MonitorThread>();
    public static Counter getCounter(final Player p) {
	Counter c = counter.get(p.getUniqueId());
	if (c == null) {
	    c = new Counter(p);
	    counter.put(p.getUniqueId(), c);
	}
	return c;
    }
    @Override
    public void onEnable() {
	instance = this;
	Bukkit.getPluginManager().registerEvents(this, this);
	getCommand("cps").setExecutor(new CPSCommand());
    }

    @EventHandler
    public void onClick(final PlayerInteractEvent e) {
	if (e.getAction().toString().startsWith("LEFT_CLICK_")) {
	    final Counter c = getCounter(e.getPlayer());
	    c.countCPS();
	}
    }
    @EventHandler
    public void onLogout(final PlayerQuitEvent e) {
	counter.remove(e.getPlayer().getUniqueId());
    }

    public static void startMonitor(final Player player,final Player target) {
	if (monitors.containsKey(player.getUniqueId())) {
	    monitors.get(player.getUniqueId()).stopMonitor();
	}
	final MonitorThread mon = new MonitorThread(player,target);
	monitors.put(player.getUniqueId(), mon);
	mon.startMonitor();
    }
    public static boolean isMoniting(final Player player) {
	return monitors.containsKey(player.getUniqueId());
    }
    public static void stopMoniting(final Player player) {
	if (monitors.containsKey(player.getUniqueId())) {
	    monitors.get(player.getUniqueId()).stopMonitor();
	    monitors.remove(player.getUniqueId());
	}
    }
}
