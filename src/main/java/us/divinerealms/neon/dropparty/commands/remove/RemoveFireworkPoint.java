/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2014 <http://dev.bukkit.org/server-mods/dropparty//>
 *
 * DropParty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DropParty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DropParty.  If not, see <http://www.gnu.org/licenses/>.
 */
package us.divinerealms.neon.dropparty.commands.remove;

import us.divinerealms.neon.dropparty.DropParty;
import us.divinerealms.neon.dropparty.message.DPMessage;
import us.divinerealms.neon.dropparty.modes.PlayerMode;
import us.divinerealms.neon.dropparty.parties.Party;
import us.divinerealms.neon.amplib.command.Command;
import us.divinerealms.neon.amplib.messenger.DefaultMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * A command that removes a firework point of a certain id or sets the sender to firework point removal mode.
 */
public class RemoveFireworkPoint extends Command {

    private final DropParty dropParty;

    public RemoveFireworkPoint(DropParty dropParty) {
        super(dropParty, "fireworkpoint");
        setDescription("Removes a firework point or sets you to firework point removal mode.");
        setCommandUsage("/dp remove fireworkpoint <party> [id]");
        setPermission(new Permission("dropparty.remove.fireworkpoint", PermissionDefault.OP));
        setArgumentRange(1, 2);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            Party party = dropParty.getPartyManager().getParty(partyName);
            if (args.length == 1) {
                dropParty.getPlayerModeController().setPlayerMode((Player) sender, PlayerMode.REMOVING_FIREWORK_POINTS, party);
            } else {
                try {
                    int id = Integer.parseInt(args[1]);
                    if (party.getFireworkPoints().size() > id) {
                        party.removeFireworkPoint(party.getFireworkPoints().get(id));
                        dropParty.getMessenger().sendMessage(sender, DPMessage.REMOVE_FIREWORKPOINT, partyName);
                    } else {
                        dropParty.getMessenger().sendMessage(sender, DPMessage.FIREWORKPOINT_IDDOESNTEXIST, args[1], partyName);
                    }
                } catch (NumberFormatException e) {
                    dropParty.getMessenger().sendMessage(sender, DefaultMessage.ERROR_NUMBERFORMAT);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_DOESNTEXIST, partyName);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        return args.length == 0 ? dropParty.getPartyManager().getPartyList() : new ArrayList<>();
    }

}
