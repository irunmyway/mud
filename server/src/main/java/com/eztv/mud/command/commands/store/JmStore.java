package com.eztv.mud.command.commands.store;

import com.eztv.mud.bean.Client;
import com.eztv.mud.bean.Msg;
import com.eztv.mud.command.commands.BaseCommand;

public class JmStore extends BaseCommand {
    public JmStore(Client client, Msg msg, String key) {
        super(client, msg, key);
    }

    @Override
    public void execute() {
        
    }
}
