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
		ActionBarUtils.sendActionBar(player,
				"§4§l玩家 §a"+target.getName()+" "+
						"§6§l | §4§lCPS §c§l"+(counter.getLastClickMs()>1000 ? 0 : counter.getCPS())+"§4 / §1§l"+counter.getMaxCPS()+
						"§6§l | §4§lLCS §c§l"+(counter.getLeftLastClickMs()>1000 ? 0 : counter.getLeftCPS())+"§4 / §1§l"+counter.getLeftMaxCPS()+
						"§6§l | §4§lRCS §c§l"+(counter.getRightLastClickMs()>1000 ? 0 : counter.getRightCPS())+"§4 / §1§l"+counter.getRightMaxCPS());
	}

	public void stopMonitor() {
		task.cancel();
	}

	public void startMonitor() {
		task = Bukkit.getScheduler().runTaskTimer(CpsCounter.getInstance(), this, 10, 10);
	}

}
