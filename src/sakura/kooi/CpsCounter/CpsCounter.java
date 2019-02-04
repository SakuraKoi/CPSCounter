package sakura.kooi.CpsCounter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class CpsCounter extends JavaPlugin implements Listener {
	@Getter private static CpsCounter instance;
	private final HashMap<UUID, Counter> counter = new HashMap<>();
	private final HashMap<UUID, MonitorThread> monitors = new HashMap<>();
	private final HashSet<Player> silentPlayer = new HashSet<>();
	public static Counter getCounter(final Player p) {
		Counter c = instance.counter.get(p.getUniqueId());
		if (c == null) {
			c = new Counter(p);
			instance.counter.put(p.getUniqueId(), c);
		}
		return c;
	}

	@Override
	public void onEnable() {
		instance = this;
		new Metrics(this);
		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("cps").setExecutor(new CPSCommand());
	}

	@EventHandler
	public void onClick(final PlayerInteractEvent e) {
		if (e.getAction().toString().startsWith("LEFT_CLICK_")) {
			if (e.isCancelled()) return;
			final Counter c = getCounter(e.getPlayer());
			c.countLeftCPS();
		} else if (e.getAction().toString().startsWith("RIGHT_CLICK_")) {
			final Counter c = getCounter(e.getPlayer());
			c.countRightCPS();
		}
	}
	@EventHandler
	public void onLogout(final PlayerQuitEvent e) {
		counter.remove(e.getPlayer().getUniqueId());
	}

	public static void startMonitor(final Player player,final Player target) {
		if (instance.monitors.containsKey(player.getUniqueId())) {
			instance.monitors.get(player.getUniqueId()).stopMonitor();
		}
		final MonitorThread mon = new MonitorThread(player,target);
		instance.monitors.put(player.getUniqueId(), mon);
		mon.startMonitor();
	}
	public static boolean isMoniting(final Player player) {
		return instance.monitors.containsKey(player.getUniqueId());
	}
	public static void stopMoniting(final Player player) {
		if (instance.monitors.containsKey(player.getUniqueId())) {
			instance.monitors.get(player.getUniqueId()).stopMonitor();
			instance.monitors.remove(player.getUniqueId());
		}
	}
	public static boolean isSilent(final Player player) {
		return instance.silentPlayer.contains(player);
	}
	public static boolean switchSilent(final Player player) {
		if (instance.silentPlayer.contains(player)) {
			instance.silentPlayer.remove(player);
			return false;
		}
		instance.silentPlayer.add(player);
		return true;
	}
}
