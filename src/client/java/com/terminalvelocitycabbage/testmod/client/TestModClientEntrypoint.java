package com.terminalvelocitycabbage.testmod.client;

import com.terminalvelocitycabbage.engine.Entrypoint;
import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.event.HandleEvent;
import com.terminalvelocitycabbage.engine.filesystem.ResourceSource;
import com.terminalvelocitycabbage.engine.filesystem.ResourceType;
import com.terminalvelocitycabbage.engine.filesystem.resources.Resource;
import com.terminalvelocitycabbage.engine.filesystem.sources.ModSource;
import com.terminalvelocitycabbage.engine.mod.ModClientEntrypoint;
import com.terminalvelocitycabbage.engine.registry.Identifier;
import com.terminalvelocitycabbage.templates.events.ServerLifecycleEvent;

@ModClientEntrypoint()
public class TestModClientEntrypoint extends Entrypoint {

    public static final String ID = "testmod";

    public TestModClientEntrypoint() {
        super(ID);
        ClientBase.getInstance().subscribe(this);
    }

    @Override
    public void preInit() {
        super.preInit();

        //Register and init filesystem things
        //Create resource sources for this client
        ResourceSource testModSource = new ModSource(ID, ClientBase.getInstance().getModManager().getMod(this));
        Identifier sourceIdentifier = identifierOf("testMod_jar_resource_source");
        //Define roots for these resources
        testModSource.registerDefaultSourceRoot(ResourceType.MODEL);
        testModSource.registerDefaultSourceRoot(ResourceType.TEXTURE);
        testModSource.registerDefaultSourceRoot(ResourceType.ANIMATION);
        testModSource.registerDefaultSourceRoot(ResourceType.SHADER);
        testModSource.registerDefaultSourceRoot(ResourceType.SOUND);
        testModSource.registerDefaultSourceRoot(ResourceType.DEFAULT_CONFIG);
        //register this source to the filesystem
        ClientBase.getInstance().getFileSystem().registerResourceSource(sourceIdentifier, testModSource);

        //Register Resources
        ClientBase.getInstance().getFileSystem().registerResource(sourceIdentifier, ResourceType.DEFAULT_CONFIG, "testmod.toml");
    }

    @Override
    public void init() {
        Log.info("Mod init");

        //Test
        testFileSystemRegistryStuff();
    }

    public void testFileSystemRegistryStuff() {

        //List resources which are registered
        //getFileSystem().listResources();

        //Test reading the string that is the config file
        Resource resource = ClientBase.getInstance().getFileSystem().getResource(ResourceType.DEFAULT_CONFIG, new Identifier("game", "test.toml"));
        Log.info(resource.asString());

        //Test reading the string that is the mod config file
        Resource modResource = ClientBase.getInstance().getFileSystem().getResource(ResourceType.DEFAULT_CONFIG, new Identifier("testmod", "testmod.toml"));
        Log.info(modResource.asString());
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