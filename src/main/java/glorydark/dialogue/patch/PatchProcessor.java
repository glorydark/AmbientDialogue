package glorydark.dialogue.patch;

import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.api.DialogueAPI;
import glorydark.dialogue.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author glorydark
 */
public class PatchProcessor {

    public static void executePatch() {
        boolean change = false;
        if (DialogueMain.configVersion.equals("1.1.0")) {
            DialogueMain.getInstance().getLogger().info(TextFormat.YELLOW + "Upgrading configurations: 1.1.0 -> 1.2.0.");
            // v_1_1_0
            File file = new File(DialogueMain.getPath() + "/player_caches/");
            if (file.exists() && file.isDirectory()) {
                for (File listFile : Objects.requireNonNull(file.listFiles())) {
                    Config config = new Config(listFile, Config.YAML);
                    String dialogueId = Utils.getNameWithoutFormatSuffix(listFile.getName());
                    for (String key : config.getRootSection().getKeys()) {
                        Config playerConf = new Config(DialogueMain.getPath() + "/players/" + key + ".yml", Config.YAML);
                        ConfigSection section = playerConf.getSection(DialogueAPI.KEY_PLAYED_TIMES);
                        section.put(dialogueId, config.getInt(key, 0));
                        playerConf.set(DialogueAPI.KEY_PLAYED_TIMES, section);
                        playerConf.save();
                    }
                    listFile.delete();
                }
            }
            try {
                Files.delete(Path.of(file.getPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Config config = new Config(DialogueMain.getPath() + "/languages/zh_CN.properties", Config.PROPERTIES);
            config.set("tip_dialogue_requirement_failed_in_cool_down", "§c该对话暂时无法进入，请耐心等待！");
            change = true;
        }
        if (change) {
            Config config = new Config(DialogueMain.getPath() + "/config.yml", Config.YAML);
            config.set("version", "1.2.0");
            config.save();
        }
    }
}
