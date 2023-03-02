package com.terminalvelocitycabbage.testmod.server;

import com.terminalvelocitycabbage.engine.Entrypoint;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.event.HandleEvent;
import com.terminalvelocitycabbage.engine.mod.Mod;
import com.terminalvelocitycabbage.engine.server.ServerBase;
import com.terminalvelocitycabbage.templates.events.ServerLifecycleEvent;

@Mod
public class TestModServerEntrypoint extends Entrypoint {

    public static final String ID = "testmod";

    public TestModServerEntrypoint() {
        super(ID);
        ServerBase.getInstance().subscribe(this);
    }

    @Override
    public void init() {
        Log.info("Mod init");
    }

    @Override
    public void destroy() {
        Log.info("Mod Destroy");
    }

    @HandleEvent(eventName = ServerLifecycleEvent.INIT)
    private void onServerInit(ServerLifecycleEvent event) {
        Log.info("Mod heard event " + event.getId() + " from server: " + event.getServer());
    }
}