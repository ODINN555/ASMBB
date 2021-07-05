package me.ODINN.ASMBB;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ASMBBTabCompleter implements TabCompleter {


    List<String> ASMBBCommands = new ArrayList<String>();


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {


        if(ASMBBCommands.isEmpty()){
            ASMBBCommands.add("delete");
            ASMBBCommands.add("new");
            ASMBBCommands.add("load");
            ASMBBCommands.add("help");
            ASMBBCommands.add("commands");
            ASMBBCommands.add("build");
        }

        List<String> results = new ArrayList<String>();

        if(args.length == 1){
            for(String cmd : ASMBBCommands){
                if(cmd.toLowerCase().startsWith(args[0].toLowerCase()))
                    results.add(cmd);
            }
            return results;
        }



        return null;

    }
}
