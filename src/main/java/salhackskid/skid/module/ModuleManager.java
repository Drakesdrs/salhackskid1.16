package salhackskid.skid.module;

import salhackskid.skid.SalHackSkid;
import salhackskid.skid.managers.Preset;
import salhackskid.skid.managers.PresetsManager;
import salhackskid.skid.module.gui.ColorsModule;
import salhackskid.skid.module.gui.HudModule;
import salhackskid.skid.module.movement.Speed;

import java.lang.reflect.Field;
import java.util.*;

public class ModuleManager extends Object{
    public static ArrayList<Module> mods = new ArrayList<Module>();

    public void init() {
        Add(new Speed());
        Add(new ColorsModule());
        Add(new HudModule());

        final Preset preset = PresetsManager.Get().getActivePreset();

        mods.forEach(mod ->
        {
            preset.initValuesForMod(mod);
        });

        mods.forEach(mod ->
        {
            mod.init();
        });
    }

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

    public void Add(Module mod)
    {
        try
        {
            for (Field field : mod.getClass().getDeclaredFields())
            {
                if (Value.class.isAssignableFrom(field.getType()))
                {
                    if (!field.isAccessible())
                    {
                        field.setAccessible(true);
                    }
                    final Value val = (Value) field.get(mod);
                    val.InitalizeMod(mod);
                    mod.getValueList().add(val);
                }
            }
            mods.add(mod);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
