package github.pitbox46.fightnbtintegration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import maninhouse.epicfight.capabilities.item.*;
import net.minecraft.item.*;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.loading.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Config {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static final Map<String, Function<ItemStack, CapabilityItem>> DICTIONARY = new HashMap<>();
    static {
        DICTIONARY.put("sword", (stack) -> new SwordCapability(new DummyItem(stack, ToolType.SHOVEL)));
        DICTIONARY.put("pickaxe", (stack) -> new PickaxeCapability(new DummyItem(stack, ToolType.PICKAXE)));
        DICTIONARY.put("axe", (stack) -> new AxeCapability(new DummyItem(stack, ToolType.AXE)));
        DICTIONARY.put("shovel", (stack) -> new ShovelCapability(new DummyItem(stack, ToolType.SHOVEL)));
        DICTIONARY.put("hoe", (stack) -> new HoeCapability(new DummyItem(stack, ToolType.HOE)));
        DICTIONARY.put("bow", (stack) -> new BowCapability(new DummyItem(stack, ToolType.SHOVEL)));
        DICTIONARY.put("crossbow", (stack) -> new CrossbowCapability(new DummyItem(stack, ToolType.SHOVEL)));
    }

    public static File jsonFile;
    public static final Map<String, Map<String, String>> JSON_MAP = new HashMap<>();

    public static void init(Path folder) {
        File file = new File(FileUtils.getOrCreateDirectory(folder, "epicfightnbt").toFile(), "config.json");
        try {
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("__comment", "Values: axe, bow, crossbow, hoe, katana, knuckle, pickaxe, shovel, sword, trident");
                writer.write(GSON.toJson(jsonObject));
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
        jsonFile = file;
        readJson(readFile());
    }

    public static JsonObject readFile() {
        try (Reader reader = new FileReader(jsonFile)) {
            return JSONUtils.fromJson(GSON, reader, JsonObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void readJson(JsonObject jsonObject) {
        if(jsonObject != null) {
            JSON_MAP.clear();
            for(Map.Entry<String, JsonElement> itemType: jsonObject.entrySet()) {
                if(itemType.getKey().equals("__comment")) continue;
                JSON_MAP.put(itemType.getKey(), itemType.getValue().getAsJsonObject().entrySet().stream().collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue().getAsString()), HashMap::putAll));
            }
        }
    }
}
