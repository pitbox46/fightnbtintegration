package github.pitbox46.fightnbtintegration;

import com.mojang.datafixers.util.Pair;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import github.pitbox46.fightnbtintegration.mixins.WeaponTypeReloadListenerMixin;
import github.pitbox46.fightnbtintegration.network.SSyncConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCapabilityPresets;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Config {
    private static final Logger LOGGER = LogManager.getLogger();

    public static File jsonFile;
    public static Map<String, WeaponSchema> JSON_MAP = new HashMap<>();

    public static void init(Path folder) {
        jsonFile = new File(getOrCreateDirectory(folder).toFile(), "epicfightnbt.json");
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

    public static Path getOrCreateDirectory(Path path) {
        try {
            if (Files.exists(path)) {
                return path;
            } else {
                return Files.createDirectory(path);
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        JSON_MAP = flattenMap(new Gson().fromJson(config, new TypeToken<Map<String, Map<String, WeaponSchema>>>(){}.getType()));
    }

    public static void readConfig(File path) {
        try (Reader reader = new FileReader(path)) {
            JSON_MAP = flattenMap(new Gson().fromJson(reader, new TypeToken<Map<String, Map<String, WeaponSchema>>>(){}.getType()));
        } catch (IOException e) {
            e.printStackTrace();
            JSON_MAP = new HashMap<>();
        }
    }

    /**
     * Flattens a map for performance reasons
     * @param map A map of maps
     * @return A new map where keys are the concatenation of the top and bottom key
     */
    protected static Map<String, WeaponSchema> flattenMap(Map<String, Map<String, WeaponSchema>> map) {
        Map<String, WeaponSchema> flatMap = new HashMap<>();
        map.forEach((topKey, bottomMap) -> {
            bottomMap.forEach((bottomKey, schema) -> {
                flatMap.put(topKey + bottomKey, schema);
            });
        });
        return flatMap;
    }

    public static class WeaponSchema {
        public double armor_ignorance = 0;
        public int hit_at_once = 0;
        public double impact = 0;
        public String weapon_type = "sword";
    }

    public static CapabilityItem findWeaponByNBT(ItemStack stack) {
        if(stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            for (String key : tag.getAllKeys()) {
                key = key + tag.getString(key);
                WeaponSchema schema = JSON_MAP.get(key);
                if (schema == null) {
                    continue;
                }
                ResourceLocation weaponType = schema.weapon_type.contains(":") ?
                        new ResourceLocation(schema.weapon_type) :
                        new ResourceLocation("epicfight", schema.weapon_type);
                if (WeaponTypeReloadListenerMixin.getPRESETS().containsKey(weaponType)) {
                    CapabilityItem.Builder toReturn = WeaponTypeReloadListenerMixin.getPRESETS().getOrDefault(weaponType, WeaponCapabilityPresets.SWORD).apply(stack.getItem());
                    if (Double.compare(schema.armor_ignorance, 0.0D) != 0) {
                        toReturn.addStyleAttibutes(CapabilityItem.Styles.ONE_HAND, Pair.of(EpicFightAttributes.ARMOR_NEGATION.get(), EpicFightAttributes.getArmorNegationModifier(schema.armor_ignorance)));
                        toReturn.addStyleAttibutes(CapabilityItem.Styles.TWO_HAND, Pair.of(EpicFightAttributes.ARMOR_NEGATION.get(), EpicFightAttributes.getArmorNegationModifier(schema.armor_ignorance)));
                    }
                    if (Double.compare(schema.impact, 0.0D) != 0) {
                        toReturn.addStyleAttibutes(CapabilityItem.Styles.ONE_HAND, Pair.of(EpicFightAttributes.IMPACT.get(), EpicFightAttributes.getImpactModifier(schema.impact)));
                        toReturn.addStyleAttibutes(CapabilityItem.Styles.TWO_HAND, Pair.of(EpicFightAttributes.IMPACT.get(), EpicFightAttributes.getImpactModifier(schema.impact)));
                    }
                    if (schema.hit_at_once != 0) {
                        toReturn.addStyleAttibutes(CapabilityItem.Styles.ONE_HAND, Pair.of(EpicFightAttributes.MAX_STRIKES.get(), EpicFightAttributes.getMaxStrikesModifier(schema.hit_at_once)));
                        toReturn.addStyleAttibutes(CapabilityItem.Styles.TWO_HAND, Pair.of(EpicFightAttributes.MAX_STRIKES.get(), EpicFightAttributes.getMaxStrikesModifier(schema.hit_at_once)));
                    }
                    return toReturn.build();
                }
            }
        }
        return CapabilityItem.EMPTY;
    }
}
