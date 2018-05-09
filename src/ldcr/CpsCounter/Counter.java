package ldcr.CpsCounter;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Counter {
    private final ArrayList<Long> counterRightCPS = new ArrayList<Long>();
    private final ArrayList<Long> counterLeftCPS = new ArrayList<Long>();
    private int maxRightCPS = 0;
    private int maxLeftCPS = 0;
    private int maxCPS = 0;
    private final Player player;
    public Counter(final Player p) {
	player = p;
    }
    public void countRightCPS() {
	counterRightCPS.add(System.currentTimeMillis());
	removeRightCPSTimeout();
	if (counterRightCPS.size() > maxRightCPS) {
	    maxRightCPS = counterRightCPS.size();
	}
	if ((getLeftCPS()+getRightCPS()) > maxCPS) {
	    maxCPS = getLeftCPS() + getRightCPS();
	}
	if (counterRightCPS.size() > 18) {
	    if (!player.hasPermission("cpscounter.cps")) {
		warnOP();
	    }
	}
    }

    public void countLeftCPS() {
	counterLeftCPS.add(System.currentTimeMillis());
	removeLeftCPSTimeout();
	if (counterLeftCPS.size() > maxLeftCPS) {
	    maxLeftCPS = counterLeftCPS.size();
	}
	if ((getLeftCPS()+getRightCPS()) > maxCPS) {
	    maxCPS = getLeftCPS() + getRightCPS();
	}
	if (counterLeftCPS.size() > 18) {
	    if (!player.hasPermission("cpscounter.cps")) {
		warnOP();
	    }
	}
    }

    public int getRightCPS() {
	return counterRightCPS.size();
    }
    public int getLeftCPS() {
	return counterLeftCPS.size();
    }
    public int getCPS() {
	return getRightCPS() + getLeftCPS();
    }
    public int getRightMaxCPS() {
	return maxRightCPS;
    }
    public int getLeftMaxCPS() {
	return maxLeftCPS;
    }
    public int getMaxCPS() {
	return maxCPS;
    }
    public long getRightLastClickMs() {
	if (counterRightCPS.isEmpty()) return -1;
	return System.currentTimeMillis() - counterRightCPS.get(counterRightCPS.size()-1);
    }
    public long getLeftLastClickMs() {
	if (counterLeftCPS.isEmpty()) return -1;
	return System.currentTimeMillis() - counterLeftCPS.get(counterLeftCPS.size()-1);
    }
    public long getLastClickMs() {
	return Math.min(getRightLastClickMs(), getLeftLastClickMs());
    }
    private void removeRightCPSTimeout() {
	while ((!counterRightCPS.isEmpty()) && ((System.currentTimeMillis() - counterRightCPS.get(0)) > 1000)) {
	    counterRightCPS.remove(0);
	}
    }
    private void removeLeftCPSTimeout() {
	while ((!counterLeftCPS.isEmpty()) && ((System.currentTimeMillis() - counterLeftCPS.get(0)) > 1000)) {
	    counterLeftCPS.remove(0);
	}
    }
    long timeout = -1;
    private void warnOP() {
	Bukkit.getScheduler().runTaskLater(CpsCounter.instance, new Runnable() {

	    @Override
	    public void run() {
		if (!player.isOnline()) return;
		if (System.currentTimeMillis() > timeout) {
		    timeout = System.currentTimeMillis()+5000;
		    for (final Player op : Bukkit.getOnlinePlayers()) {
			if (op.hasPermission("cpscounter.cps")) {
			    if (CpsCounter.isSilent(op)) return;
			    op.sendMessage("§b[CPS] §c玩家 "+player.getName()+" 建议检查是否连点"+
				    "§b | §cCPS §e"+getCPS()+"§c / §d"+getMaxCPS()+
				    "§b | §cRCS §e"+getRightCPS() +"§c / §d"+ getRightMaxCPS()+
				    "§b | §cLCS §e"+getLeftCPS() +"§c / §d"+getLeftMaxCPS());
			}
		    }
		}
	    }

	}, 7);

    }
}
