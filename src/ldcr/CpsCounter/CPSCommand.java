package ldcr.CpsCounter;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CPSCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
	if (args.length==0) {
	    sender.sendMessage("§b§lCPS §7>> §e/cps <玩家>           查看玩家的CPS值");
	    sender.sendMessage("§b§lCPS §7>> §e/cps #mon <玩家>  监视玩家的CPS值");
	} else {
	    String player = args[0];
	    if (args[0].equals("#mon")) {
		if (!(sender instanceof Player)) {
		    sender.sendMessage("§b§lCPS §7>> §c错误: 您必须登入游戏才能使用CPS监视模式.");
		    return true;
		}
		if (!sender.hasPermission("cpscounter.cps")) {
		    sender.sendMessage("§b§lCPS §7>> §c你没有权限执行此命令");
		    return true;
		}
		if (args.length!=2) {
		    if (CpsCounter.isMoniting((Player) sender)) {
			CpsCounter.stopMoniting((Player) sender);
			sender.sendMessage("§b§lCPS §7>> §a监视已停止");
		    } else {
			sender.sendMessage("§b§lCPS §7>> §e/cps #mon <玩家>  监视玩家的CPS值");
		    }
		    return true;
		}
		player = args[1];
		final Player p = getPlayer(sender, player);
		if (p==null) return true;
		CpsCounter.startMonitor((Player) sender, p);
		sender.sendMessage("§b§lCPS §7>> §a开始监视玩家 "+p.getName());
		return true;
	    }
	    final Player p = getPlayer(sender, player);
	    if (p==null) return true;
	    final Counter counter = CpsCounter.getCounter(p);
	    sender.sendMessage("§b§lCPS §7>> §b玩家 §a"+p.getName()+
		    "§6 | §bCPS §e"+(counter.getLastClickMs()>1000 ? 0 : counter.getCPS())+"§b / §d"+counter.getMaxCPS()+
		    "§6 | §bLCS §e"+(counter.getLeftLastClickMs()>1000 ? 0 : counter.getLeftCPS())+"§b / §d"+counter.getLeftMaxCPS()+
		    "§6 | §bRCS §e"+(counter.getRightLastClickMs()>1000 ? 0 : counter.getRightCPS())+"§b / §d"+counter.getRightMaxCPS());
	}
	return true;
    }
    private Player getPlayer(final CommandSender sender, final String player) {
	final OfflinePlayer offp = Bukkit.getOfflinePlayer(player);
	if (offp==null) {
	    sender.sendMessage("§b§lCPS §7>> §c错误: 玩家 "+player+" 不存在.");
	    return null;
	}
	if (!offp.isOnline()) {
	    sender.sendMessage("§b§lCPS §7>> §c错误: 玩家 "+offp.getName()+" 不在线.");
	    return null;
	}
	return offp.getPlayer();
    }

}
