package salhackskid.skid;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import salhackskid.skid.gui.managers.DirectoryManager;
import salhackskid.skid.gui.managers.HudManager;
import salhackskid.skid.managers.PresetsManager;
import salhackskid.skid.module.Module;
import salhackskid.skid.module.ModuleManager;
import salhackskid.skid.gui.managers.CommandManager;

public class SalHackSkid implements ModInitializer {

    public static final int IntVersion = 1;

    public static EventBus eventBus = new EventManager();

    static MinecraftClient mc = MinecraftClient.getInstance();

    private static ModuleManager m_ModuleManager = new ModuleManager();
    private static HudManager m_HudManager = new HudManager();
    private static DirectoryManager m_DirectoryManager = new DirectoryManager();
    private static CommandManager m_CommandManager = new CommandManager();
    private static PresetsManager m_PresetsManager = new PresetsManager();

    @Override
    public void onInitialize() {
        Init();
    }



    public static void Init()
    {
        System.out.println("initalizing salhack object (all static fields)");
        m_DirectoryManager.Init();

        /// load before mods
        m_PresetsManager.LoadPresets(); // must be before module init
        m_ModuleManager.init();
        m_HudManager.Init();
        m_CommandManager.InitalizeCommands();

    }

    public static HudManager getHudManager() {
        return new HudManager();
    }

    public static CommandManager getCommandManager() {
        return new CommandManager();
    }

    public static DirectoryManager getDirectoryManager() {
        return new DirectoryManager();
    }

    public static PresetsManager getPresetsManager() {
        return new PresetsManager();
    }

    public static void SendMessage(String string)
    {
        if (mc.inGameHud != null || mc.player == null)
            mc.inGameHud.getChatHud().addMessage(Text.of(string));
    }

}
