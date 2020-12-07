package salhackskid.skid.module;

import salhackskid.skid.module.Movement.Speed;

import java.util.*;

public class ModuleManager extends Object{
    private static final List<Module> mods = Arrays.asList(
            new Speed());

    public static List<Module> getModules() {
        return mods;
    }
}
