package net.Demonly.dworld.config;

import com.google.common.io.ByteStreams;
import net.Demonly.dworld.DWorld;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class Configuration
{
    private File file;
    private FileConfiguration config;
    private DWorld plugin;

    public Configuration(File file, DWorld plugin)
    {
        this.file = file;
        this.load();
        this.plugin = plugin;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    private void load()
    {
        try {
            if (!file.exists()) {
                if (!plugin.getDataFolder().exists()) {
                    plugin.getDataFolder().mkdirs();
                }

                try {
                    InputStream in = plugin.getResource(file.getName());
                    OutputStream out = new FileOutputStream(file);
                    ByteStreams.copy(in, out);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            config = YamlConfiguration.loadConfiguration(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void save()
    {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
