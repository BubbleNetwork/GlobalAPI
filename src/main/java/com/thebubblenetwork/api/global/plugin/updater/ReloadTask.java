package com.thebubblenetwork.api.global.plugin.updater;

import com.thebubblenetwork.api.global.plugin.Plugman;

import java.io.File;

public abstract class ReloadTask<P> extends Thread implements Runnable{
    private Plugman<P> plugman;
    private File original;
    private P plugin;

    public ReloadTask(Plugman<P> plugman, File original, P plugin) {
        this.plugman = plugman;
        this.original = original;
        this.plugin = plugin;
    }

    public void run() {
        System.out.println("Disabling...");
        try {
            plugman.disable(plugin);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Disabled");
        System.out.println("Unloading...");
        try {
            plugman.unload(plugin);
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Unloaded");
        plugin = null;
        System.out.println("Doing...");
        whenUnloaded();
        System.out.println("Done");
        System.out.println("Loading...");
        try {
            plugin = plugman.load(original);
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        System.out.println("Loaded");
        System.out.println("Enabling");
        try {
            plugman.enable(plugin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Enabled");
    }

    public abstract void whenUnloaded();
}
