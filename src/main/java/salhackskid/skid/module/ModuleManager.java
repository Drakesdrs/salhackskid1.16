package salhackskid.skid.module;

import salhackskid.skid.SalHackSkid;
import salhackskid.skid.module.Movement.Speed;

import java.util.*;

public class ModuleManager extends Object{
    private static final List<Module> mods = Arrays.asList(
            new Speed());

    public static List<Module> getModules() {
        return mods;
    }

    public static Module getModule(Class p_class) {
        for (Module l_Mod : mods)
        {
            if (l_Mod.getClass() == p_class)
                return l_Mod;
        }
        return null;
    }

    public static final List<Module> getModuleList(Module.ModuleType p_Type)
    {
        List<Module> list = new ArrayList<>();
        for (Module module : mods)
        {
            if (module.getType().equals(p_Type))
            {
                list.add(module);
            }
        }
        // Organize alphabetically
        list.sort(Comparator.comparing(Module::getDisplayName));

        return list;
    }
}
