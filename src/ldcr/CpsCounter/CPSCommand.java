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
	    sender.sendMessage("§b[CPS] §e/cps <玩家>           查看玩家的CPS值");
	    sender.sendMessage("§b[CPS] §e/cps #mon <玩家>  监视玩家的CPS值");
	} else {
	    String player = args[0];
	    if (args[0].equals("#mon")) {
		if (!(sender instanceof Player)) {
		    sender.sendMessage("§b[CPS] §c错误: 您必须登入游戏才能使用CPS监视模式.");
		    return true;
		}
		if (args.length!=2) {
		    if (CpsCounter.isMoniting((Player) sender)) {
			CpsCounter.stopMoniting((Player) sender);
			sender.sendMessage("§b[CPS] §a监视已停止");
		    } else {
			sender.sendMessage("§b[CPS] §e/cps #mon <玩家>  监视玩家的CPS值");
		    }
		    return true;
		}
		player = args[1];
		final Player p = getPlayer(sender, player);
		if (p==null) return true;
		CpsCounter.startMonitor((Player) sender, p);
		sender.sendMessage("§b[CPS] §a开始监视玩家 "+p.getName());
		return true;
	    }
	    final Player p = getPlayer(sender, player);
	    if (p==null) return true;
	    final Counter c = CpsCounter.getCounter(p);
	    sender.sendMessage("§b[CPS] §a玩家 "+p.getName()+" 当前CPS §e"+c.getCPS()+"§a 最大CPS §d"+c.getMaxCPS()+"§a 上次点击于 §e"+c.getLastClickMs()+"ms §a前.");
	}
	return true;
    }
    private Player getPlayer(final CommandSender sender, final String player) {
	final OfflinePlayer offp = Bukkit.getOfflinePlayer(player);
	if (offp==null) {
	    sender.sendMessage("§b[CPS] §c错误: 玩家 "+player+" 不存在.");
	    return null;
	}
	if (!offp.isOnline()) {
	    sender.sendMessage("§b[CPS] §c错误: 玩家 "+offp.getName()+" 不在线.");
	    return null;
	}
	return offp.getPlayer();
    }

}
