package com.terminalvelocitycabbage.testmod.client;

import com.terminalvelocitycabbage.engine.Entrypoint;
import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.event.HandleEvent;
import com.terminalvelocitycabbage.engine.mod.ModClientEntrypoint;
import com.terminalvelocitycabbage.templates.events.ServerLifecycleEvent;

@ModClientEntrypoint()
public class TestModClientEntrypoint extends Entrypoint {

    public static final String ID = "testmod";

    public TestModClientEntrypoint() {
        super(ID);
        ClientBase.getInstance().subscribe(this);
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