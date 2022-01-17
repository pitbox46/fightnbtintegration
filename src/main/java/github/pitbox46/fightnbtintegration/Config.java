package github.pitbox46.fightnbtintegration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import github.pitbox46.fightnbtintegration.network.SSyncConfig;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.capabilities.item.CapabilityItem;
import yesman.epicfight.config.CapabilityConfig;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class Config {
    private static final Logger LOGGER = LogManager.getLogger();

    public static final Map<String, Function<ItemStack, CapabilityItem>> DICTIONARY = new HashMap<>();
    static {
        for(CapabilityConfig.WeaponType type : CapabilityConfig.WeaponType.values()) {
            DICTIONARY.put(type.name().toLowerCase(), (stack) -> type.get(stack.getItem()));
        }
    }

    public static File jsonFile;
    public static Map<String, Map<String, WeaponSchema>> JSON_MAP;

    public static void init(Path folder) {
        jsonFile = new File(FileUtils.getOrCreateDirectory(folder, "serverconfig").toFile(), "epicfightnbt.json");
        try {
            if (jsonFile.createNewFile()) {
                Path defaultConfigPath = FMLPaths.GAMEDIR.get().resolve(FMLConfig.defaultConfigPath()).resolve("epicfightnbt.json");
                InputStreamReader defaults = new InputStreamReader(Files.exists(defaultConfigPath)? Files.newInputStream(defaultConfigPath) :
                        Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/fightnbtintegration/epicfightnbt.json")));
                FileOutputStream writer = new FileOutputStream(jsonFile, false);
                int read;
                while ((read = defaults.read()) != -1) {
                    writer.write(read);
                }
                writer.close();
                defaults.close();
            }
        } catch (IOException error) {
            LOGGER.warn(error.getMessage());
        }

        readConfig(jsonFile);
    }

    public static SSyncConfig configFileToSSyncConfig() {
        try {
            return new SSyncConfig(new String(Files.readAllBytes(jsonFile.toPath())));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void readConfig(String config) {
        JSON_MAP = new Gson().fromJson(config, new TypeToken<Map<String, Map<String, WeaponSchema>>>(){}.getType());
    }

    public static void readConfig(File path) {
        try (Reader reader = new FileReader(path)) {
            JSON_MAP = new Gson().fromJson(reader, new TypeToken<Map<String, Map<String, WeaponSchema>>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            JSON_MAP = new HashMap<>();
        }
    }

    class WeaponSchema {

        public double armor_ignorance = 0;
        public int hit_at_once = 0;
        public double impact = 0;
        public String weapon_type = "sword";
    }

    public static CapabilityItem findWeaponByNBT(ItemStack stack) {
        if(stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            for (String key : tag.keySet()) {
                for (Map.Entry<String, Map<String, WeaponSchema>> condition : JSON_MAP.entrySet()) {
                    if (condition.getKey().equals(key)) {
                        String value = tag.getString(key);
                        for (Map.Entry<String, WeaponSchema> weaponEntry : condition.getValue().entrySet()) {
                            if (weaponEntry.getKey().equals(value)) {
                                WeaponSchema weapon = weaponEntry.getValue();
                                CapabilityItem toReturn = DICTIONARY.get(weapon.weapon_type).apply(stack);
                                toReturn.setConfigFileAttribute(
                                        weapon.armor_ignorance, weapon.impact, weapon.hit_at_once,
                                        weapon.armor_ignorance, weapon.impact, weapon.hit_at_once);
                                return toReturn;
                            }
                        }
                    }
                }
            }
        }
        return CapabilityItem.EMPTY;
    }
}
