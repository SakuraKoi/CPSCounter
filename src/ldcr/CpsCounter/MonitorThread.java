package ldcr.CpsCounter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import ldcr.Utils.Bukkit.ActionBarUtils;

public class MonitorThread implements Runnable {
    private final Player player;
    private final Player target;
    private final Counter counter;
    private BukkitTask task;
    public MonitorThread(final Player player, final Player target) {
	this.player = player;
	this.target = target;
	counter = CpsCounter.getCounter(target);
    }

    @Override
    public void run() {
	if (!player.isOnline()) {
	    stopMonitor();
	    return;
	}
	if (!target.isOnline()) {
	    stopMonitor();
	    return;
	}
	ActionBarUtils.sendActionBar(player, "§b玩家 §a"+target.getName()+"§b 当前CPS §e"+counter.getCPS()+"§b 最大CPS §d"+counter.getMaxCPS()+"§b 上次点击于 §e"+counter.getLastClickMs()+"ms§b 前");
    }

    public void stopMonitor() {
	task.cancel();
    }

    public void startMonitor() {
	task = Bukkit.getScheduler().runTaskTimer(CpsCounter.instance, this, 10, 10);
    }

}
