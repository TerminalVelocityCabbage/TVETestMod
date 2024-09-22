package com.terminalvelocitycabbage.testmod.client;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.filesystem.resources.Resource;
import com.terminalvelocitycabbage.engine.filesystem.resources.ResourceCategory;
import com.terminalvelocitycabbage.engine.filesystem.resources.ResourceSource;
import com.terminalvelocitycabbage.engine.filesystem.sources.ModSource;
import com.terminalvelocitycabbage.engine.mod.ModClientEntrypoint;
import com.terminalvelocitycabbage.engine.mod.ModEntrypoint;
import com.terminalvelocitycabbage.engine.registry.Identifier;
import com.terminalvelocitycabbage.engine.translation.Language;
import com.terminalvelocitycabbage.engine.translation.Localizer;
import com.terminalvelocitycabbage.game.client.GameClient;
import com.terminalvelocitycabbage.game.client.registry.GameLocalizedTexts;
import com.terminalvelocitycabbage.game.common.events.ModLocalizedTextRegistryEvent;
import com.terminalvelocitycabbage.templates.events.ResourceRegistrationEvent;
import com.terminalvelocitycabbage.templates.events.ResourceSourceRegistrationEvent;
import com.terminalvelocitycabbage.templates.events.ServerLifecycleEvent;

import static com.terminalvelocitycabbage.testmod.common.TestMod.ID;

@ModClientEntrypoint()
public class TestModClientEntrypoint extends ModEntrypoint {

    Identifier modLocalizedText;
    Identifier testModResourceSource;

    public TestModClientEntrypoint() {
        super(ID);
    }

    @Override
    public void registerEventListeners() {
        //Register Event Listeners
        getEventDispatcher().listenToEvent(ServerLifecycleEvent.STARTED, (event) -> onServerInit((ServerLifecycleEvent) event));
        getEventDispatcher().listenToEvent(ModLocalizedTextRegistryEvent.EVENT, (event) -> registerLocalizedTexts(((ModLocalizedTextRegistryEvent) event).getLocalizer()));
        getEventDispatcher().listenToEvent(ResourceSourceRegistrationEvent.EVENT, (event -> registerResourceSources((ResourceSourceRegistrationEvent) event)));

        //Register Resources
        getEventDispatcher().listenToEvent(ResourceRegistrationEvent.getEventNameFromCategory(ResourceCategory.DEFAULT_CONFIG), event -> ((ResourceRegistrationEvent) event).registerResource(testModResourceSource, ResourceCategory.DEFAULT_CONFIG, "testmod.toml"));
        getEventDispatcher().listenToEvent(ResourceRegistrationEvent.getEventNameFromCategory(ResourceCategory.LOCALIZATION), event -> ((ResourceRegistrationEvent) event).registerResource(testModResourceSource, ResourceCategory.LOCALIZATION, "en-us.toml"));
    }

    private void registerResourceSources(ResourceSourceRegistrationEvent event) {
        //Register and init filesystem things
        //Create resource sources for this client
        ResourceSource testModSource = new ModSource(ID, getMod());
        Identifier sourceIdentifier = identifierOf("testMod_jar_resource_source");
        //Define roots for these resources
        testModSource.registerDefaultSources();
        //register this source to the filesystem
        testModResourceSource = event.getRegistry().register(sourceIdentifier, testModSource).getIdentifier();
    }

    private void registerLocalizedTexts(Localizer localizer) {
        modLocalizedText = localizer.registerTranslatableText(ID, "modstuff.modtest");
    }

    @Override
    public void init() {
        Log.info("Mod init: " + getNamespace());

        //Test
        //testFileSystemRegistryStuff();
        var localizer = ((GameClient) ClientBase.getInstance()).getLocalizer();
        Log.info(localizer.localize(GameLocalizedTexts.HELLO));
        Log.info(localizer.localize(GameLocalizedTexts.GOODBYE));
        Log.info(localizer.localize(GameLocalizedTexts.ANOTHER_TRANSLATION));
        Log.info(localizer.localize(GameLocalizedTexts.TEST, "one", "two"));
        Log.info(localizer.localize(modLocalizedText));
        localizer.changeLanguage(Language.SPANISH_SPAIN);
        Log.info(localizer.localize(GameLocalizedTexts.HELLO));
        Log.info(localizer.localize(GameLocalizedTexts.GOODBYE));
        Log.info(localizer.localize(GameLocalizedTexts.ANOTHER_TRANSLATION));
        Log.info(localizer.localize(GameLocalizedTexts.TEST, "uno", "dos"));
        Log.info(localizer.localize(modLocalizedText));
    }

    //Test that this mod has access to resources from both itself and the client
    public void testFileSystemRegistryStuff() {

        //List resources which are registered
        //getFileSystem().listResources();

        //Test reading the string that is the config file
        Resource resource = ClientBase.getInstance().getFileSystem().getResource(ResourceCategory.DEFAULT_CONFIG, new Identifier("game", "test.toml"));
        Log.info(resource.asString());

        //Test reading the string that is the mod config file
        Resource modResource = ClientBase.getInstance().getFileSystem().getResource(ResourceCategory.DEFAULT_CONFIG, new Identifier("testmod", "testmod.toml"));
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