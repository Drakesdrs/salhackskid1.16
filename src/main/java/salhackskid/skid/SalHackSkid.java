package salhackskid.skid;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import salhackskid.skid.gui.managers.HudManager;
import salhackskid.skid.module.Module;
import salhackskid.skid.module.ModuleManager;
import salhackskid.skid.gui.managers.CommandManager;

public class SalHackSkid implements ModInitializer {

    public static final int IntVersion = 1;

    public static EventBus eventBus = new EventManager();

    static MinecraftClient mc = MinecraftClient.getInstance();

    @Override
    public void onInitialize() {
        System.out.println("Initializing SalHackSkid 1.16 :)");
        for (Module m : ModuleManager.getModules()) {
            m.init();
            m.setToggled(true);
        }
    }

    public static HudManager getHudManager() {
        return new HudManager();
    }

    public static CommandManager getCommandManager() {
        return new CommandManager();
    }

    public static void SendMessage(String string)
    {
        if (mc.inGameHud != null || mc.player == null)
            mc.inGameHud.getChatHud().addMessage(Text.of(string));
    }
}
