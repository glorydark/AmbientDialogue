package glorydark.dialogue;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import glorydark.dialogue.commands.DialogueCommands;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.data.DialogueLineData;
import glorydark.dialogue.utils.Language;

import java.io.File;
import java.util.*;

/**
 * @author glorydark
 * @date {2023/6/29} {23:47}
 */
public class DialogueMain extends PluginBase {

    private static DialogueMain plugin;

    private static String path;

    public static HashMap<Player, DialoguePlayTask> playerPlayingTasks = new HashMap<>();

    public static HashMap<String, DialogueData> dialogues = new HashMap<>();

    public static Language language;

    @Override
    public void onEnable() {
        plugin = this;
        path = this.getDataFolder().getPath();
        this.saveResource("config.yml");
        this.saveResource("languages/zh_cn.properties");
        new File(path+"/dialogues/").mkdirs();
        language = new Language(new Config(path+"/config.yml", Config.YAML).getString("lang"), path+"/languages/", path+"/players/");
        this.loadAllDialogues();
        this.getServer().getCommandMap().register("", new DialogueCommands("dialogue"));
        this.getLogger().info("AmbientDialogue Enabled");
    }

    public void loadAllDialogues(){
        dialogues.clear(); // 先清空，为reload做准备
        File folder = new File(path+"/dialogues/");
        for(File file : Objects.requireNonNull(folder.listFiles())){
            String fileName = file.getName();
            this.getLogger().info("§eLoading dialogue: "+fileName);
            Config config = new Config(file, Config.YAML);
            List<DialogueLineData> lines = new ArrayList<>();
            for(Map<String, Object> lineDataMap : (List<Map<String, Object>>) config.get("lines", new LinkedHashMap<>())){
                lines.add(new DialogueLineData((String) lineDataMap.get("text"), (String) lineDataMap.get("speaker_name"), (Integer) lineDataMap.get("exist_ticks"), (Integer) lineDataMap.get("play_ticks")));
            }
            DialogueData data = new DialogueData(fileName, lines, new ArrayList<>(config.getStringList("commands")), new ArrayList<>(config.getStringList("messages")));
            dialogues.put(fileName, data);
            this.getLogger().info("§aDialogue Loaded: "+fileName);
        }
        this.getLogger().info("§a"+dialogues.size()+"dialogue(s) loaded successfully!");
    }

    public static DialogueMain getPlugin() {
        return plugin;
    }

    public static String getPath() {
        return path;
    }

    public static HashMap<Player, DialoguePlayTask> getPlayerPlayingTasks() {
        return playerPlayingTasks;
    }

    public static HashMap<String, DialogueData> getDialogues() {
        return dialogues;
    }

    public static Language getLanguage() {
        return language;
    }
}
