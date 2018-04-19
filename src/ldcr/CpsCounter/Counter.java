package ldcr.CpsCounter;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Counter {
    private final ArrayList<Long> counterCPS = new ArrayList<Long>();
    private int maxCPS = 0;
    private final Player player;
    public Counter(final Player p) {
	player = p;
    }
    public void countCPS() {
	counterCPS.add(System.currentTimeMillis());
	removeCPSTimeout();
	if (counterCPS.size() > maxCPS) {
	    maxCPS = counterCPS.size();
	}
	if (counterCPS.size() > 18) {
	    if (!player.hasPermission("cpscounter.cps")) {
		warnOP();
	    }
	}
    }

    public int getCPS() {
	return counterCPS.size();
    }

    public int getMaxCPS() {
	return maxCPS;
    }
    public long getLastClickMs() {
	if (counterCPS.isEmpty()) return -1;
	return System.currentTimeMillis() - counterCPS.get(counterCPS.size()-1);
    }

    private void removeCPSTimeout() {
	while ((!counterCPS.isEmpty()) && ((System.currentTimeMillis() - counterCPS.get(0)) > 1000)) {
	    counterCPS.remove(0);
	}
    }
    long timeout = -1;
    private void warnOP() {
	if (System.currentTimeMillis() > timeout) {
	    timeout = System.currentTimeMillis()+5000;
	    for (final Player player : Bukkit.getOnlinePlayers()) {
		if (player.hasPermission("cpscounter.cps")) {
		    player.sendMessage("§b[CPS] §c玩家 "+this.player.getName()+" CPS异常 ["+getCPS()+" CPS / Max "+getMaxCPS()+" CPS]");
		}
	    }
	}
    }
}
