package com.stardevllc.pluginlibdownloader;

import com.stardevllc.helper.FileHelper;
import com.stardevllc.helper.URLClassLoaderAccess;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public class LibDownloader {
    
    protected Path folder;

    public LibDownloader(Path folder) {
        this.folder = folder;
        
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(this.folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Path download(String url, String fileName) {
        return FileHelper.downloadFile(url, folder, fileName, true);
    }
    
    public void loadAsPlugin(Path pluginFile) {
        loadAsPlugin(pluginFile, null);
    }
    
    public void loadAsPlugin(Path pluginFile, JavaPlugin parentPlugin) {
        if (!Files.exists(pluginFile)) {
            throw new IllegalArgumentException("File " + pluginFile.getFileName() + " does not exist");
        }

        try {
            Bukkit.getPluginManager().loadPlugin(pluginFile.toFile());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } 
        
        if (parentPlugin != null) {
            URLClassLoaderAccess loader = URLClassLoaderAccess.create((URLClassLoader) parentPlugin.getClass().getClassLoader());
            try {
                loader.addURL(pluginFile.toUri().toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void downloadAsPlugin(String url, String fileName) {
        loadAsPlugin(download(url, fileName), null);
    }
    
    public void downloadAsPlugin(String url, String fileName, JavaPlugin parentPlugin) {
        loadAsPlugin(download(url, fileName), parentPlugin);
    }
}
