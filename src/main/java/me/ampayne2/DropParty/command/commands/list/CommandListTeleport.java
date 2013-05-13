/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2013
 *
 * DropParty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DropParty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DropParty.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.ampayne2.DropParty.command.commands.list;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPTeleportsTable;

public class CommandListTeleport implements DPCommand{

	@Override
	public void execute(CommandSender sender, String[] args) {
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		}else{
			return;
		}
		if(DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null){
			sender.sendMessage(ChatColor.RED + "Drop Party '" + dpid + "' Does Not Exist.");
			return;
		}
		DPTeleportsTable entry = DatabaseManager.getDatabase()
				.select(DPTeleportsTable.class).where().equal("dpid", dpid).execute().findOne();
		if (entry == null){
			sender.sendMessage(ChatColor.AQUA + "No Drop Party Teleports Found.");
			return;
		}else{
			sender.sendMessage(ChatColor.AQUA + "Drop Party " + dpid + " Teleport:");
			sender.sendMessage(ChatColor.AQUA + "  X: " + entry.x);
			sender.sendMessage(ChatColor.AQUA + "  Y: " + entry.y);
			sender.sendMessage(ChatColor.AQUA + "  Z: " + entry.z);
			sender.sendMessage(ChatColor.AQUA + "  Pitch: " + entry.pitch);
			sender.sendMessage(ChatColor.AQUA + "  Yaw: " + entry.yaw);
		}
	}
}
