package salhackskid.skid;

import me.zero.alpine.bus.EventBus;
import me.zero.alpine.bus.EventManager;
import net.fabricmc.api.ModInitializer;
import salhackskid.skid.module.Module;
import salhackskid.skid.module.ModuleManager;

public class SalHackSkid implements ModInitializer {

    public static final int IntVersion = 1;

    public static EventBus eventBus = new EventManager();

    @Override
    public void onInitialize() {
        System.out.println("Initializing SalHackSkid 1.16 :)");
        for (Module m : ModuleManager.getModules()) {
            m.init();
            m.setToggled(true);
        }
    }
}