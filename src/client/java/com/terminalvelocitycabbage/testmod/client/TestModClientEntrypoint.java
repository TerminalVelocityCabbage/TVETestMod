package com.terminalvelocitycabbage.testmod.client;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.filesystem.resources.Resource;
import com.terminalvelocitycabbage.engine.filesystem.resources.ResourceSource;
import com.terminalvelocitycabbage.engine.filesystem.resources.ResourceType;
import com.terminalvelocitycabbage.engine.filesystem.sources.ModSource;
import com.terminalvelocitycabbage.engine.mod.ModClientEntrypoint;
import com.terminalvelocitycabbage.engine.mod.ModEntrypoint;
import com.terminalvelocitycabbage.engine.registry.Identifier;
import com.terminalvelocitycabbage.templates.events.ServerLifecycleEvent;

import static com.terminalvelocitycabbage.testmod.common.TestMod.ID;

@ModClientEntrypoint()
public class TestModClientEntrypoint extends ModEntrypoint {

    public TestModClientEntrypoint() {
        super(ID);
    }

    @Override
    public void preInit() {
        super.preInit();

        //Register Event Listeners
        ClientBase.getInstance().getEventDispatcher().listenToEvent(ServerLifecycleEvent.STARTED, (event) -> onServerInit((ServerLifecycleEvent) event));

        //Register and init filesystem things
        //Create resource sources for this client
        ResourceSource testModSource = new ModSource(ID, getMod());
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
        Log.info("Mod init: " + getNamespace());

        //Test
        //testFileSystemRegistryStuff();
    }

    //Test that this mod has access to resources from both itself and the client
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

    private void onServerInit(ServerLifecycleEvent event) {
        Log.info("Mod heard event " + event.getId() + " from server: " + event.getServer());
    }
}