package com.terminalvelocitycabbage.testmod.client;

import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.mod.ModEntrypoint;
import com.terminalvelocitycabbage.engine.mod.ModServerEntrypoint;
import com.terminalvelocitycabbage.engine.server.ServerBase;
import com.terminalvelocitycabbage.templates.events.ServerLifecycleEvent;

import static com.terminalvelocitycabbage.testmod.common.TestMod.ID;

@ModServerEntrypoint()
public class TestModServerEntrypoint extends ModEntrypoint {

    public TestModServerEntrypoint() {
        super(ID);
    }

    @Override
    public void preInit() {
        super.preInit();
        ServerBase.getInstance().getEventDispatcher().listenToEvent(ServerLifecycleEvent.INIT, (event -> onServerInit((ServerLifecycleEvent) event)));
    }

    @Override
    public void init() {
        Log.info("Mod init");
    }

    @Override
    public void destroy() {
        Log.info("Mod Destroy");
    }

    private void onServerInit(ServerLifecycleEvent event) {
        Log.info("Mod heard event " + event.getId() + " from server: " + event.getServer());
    }
}