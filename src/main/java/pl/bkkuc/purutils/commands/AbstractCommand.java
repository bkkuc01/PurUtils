package pl.bkkuc.purutils.commands;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {

    public AbstractCommand(JavaPlugin javaPlugin, String commandName){
        PluginCommand command = javaPlugin.getCommand(commandName);
        if(command != null) command.setExecutor(this);
    }

    public abstract void execute(CommandSender sender, String[] args);
    public abstract List<String> tab(CommandSender sender, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return tab(sender, args).stream().filter(s -> s.startsWith(args[args.length - 1])).collect(Collectors.toList());
    }
}
