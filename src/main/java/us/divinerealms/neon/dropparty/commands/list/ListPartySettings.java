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
package us.divinerealms.neon.dropparty.commands.list;

import us.divinerealms.neon.dropparty.DropParty;
import us.divinerealms.neon.dropparty.message.DPMessage;
import us.divinerealms.neon.dropparty.parties.Party;
import us.divinerealms.neon.amplib.command.Command;
import us.divinerealms.neon.amplib.messenger.PageList;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * A command that lists the party settings of a drop party.
 */
public class ListPartySettings extends Command {

    private final DropParty dropParty;

    public ListPartySettings(DropParty dropParty) {
        super(dropParty, "partysettings");
        setDescription("Lists the settings of a drop party.");
        setCommandUsage("/dp list partysettings <party> [page]");
        setPermission(new Permission("dropparty.list.partysettings", PermissionDefault.TRUE));
        setArgumentRange(1, 2);
        setPlayerOnly(false);
        this.dropParty = dropParty;
    }

    @Override
    public void execute(String command, CommandSender sender, String[] args) {
        String partyName = args[0];
        if (dropParty.getPartyManager().hasParty(partyName)) {
            Party party = dropParty.getPartyManager().getParty(partyName);
            int pageNumber = 1;
            if (args.length == 2) {
                pageNumber = PageList.getPageNumber(args[1]);
            }
            party.getSettingsList().sendPage(pageNumber, sender);
        } else {
            dropParty.getMessenger().sendMessage(sender, DPMessage.PARTY_DOESNTEXIST, partyName);
        }
    }

    @Override
    public List<String> getTabCompleteList(String[] args) {
        return args.length == 0 ? dropParty.getPartyManager().getPartyList() : new ArrayList<>();
    }

}
