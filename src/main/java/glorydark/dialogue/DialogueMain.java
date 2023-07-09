package glorydark.dialogue;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
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
public class DialogueMain extends PluginBase implements Listener {

    private static DialogueMain plugin;

    private static String path;

    public static HashMap<Player, DialoguePlayTask> playerPlayingTasks = new HashMap<>();

    public static HashMap<String, DialogueData> dialogues = new HashMap<>();

    public static Language language;

    public static int lineMaxLength;

    @Override
    public void onEnable() {
        plugin = this;
        path = this.getDataFolder().getPath();
        this.saveResource("config.yml");
        this.saveResource("languages/zh_cn.properties");
        new File(path+"/dialogues/").mkdirs();
        Config config = new Config(path+"/config.yml", Config.YAML);
        lineMaxLength = config.getInt("line_max_length", 64);
        language = new Language(config.getString("lang"), path+"/languages/", path+"/player_lang_cache.yml");
        this.loadAllDialogues();
        this.getServer().getCommandMap().register("", new DialogueCommands("dialogue"));
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("AmbientDialogue Enabled");
    }

    public void loadAllDialogues(){
        dialogues.clear(); // 先清空，为reload做准备
        File folder = new File(path+"/dialogues/");
        for(File file : Objects.requireNonNull(folder.listFiles())){
            this.loadDialogue(file);
        }
        this.getLogger().info("§a"+dialogues.size()+"dialogue(s) loaded successfully!");
    }

    public void loadDialogue(File file){
        String fileName = file.getName();
        this.getLogger().info("§eLoading dialogue: "+fileName);
        Config config = new Config(file, Config.YAML);
        List<DialogueLineData> lines = new ArrayList<>();
        for(Map<String, Object> lineDataMap : (List<Map<String, Object>>) config.get("lines", new LinkedHashMap<>())){
            lines.add(new DialogueLineData((String) lineDataMap.get("text"), (String) lineDataMap.get("speaker_name"), (Integer) lineDataMap.get("exist_ticks"), (Integer) lineDataMap.get("play_ticks")));
        }
        DialogueData data = new DialogueData(fileName, lines, new ArrayList<>(config.getStringList("commands")), new ArrayList<>(config.getStringList("messages")), config.getBoolean("player_still", true));
        dialogues.put(fileName, data);
        this.getLogger().info("§aDialogue Loaded: "+fileName);
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

    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent event){
        DialoguePlayTask task = playerPlayingTasks.get(event.getPlayer());
        if(task == null){
            return;
        }
        if(task.dialogueData.isPlayerStill()) {
            if ((event.getFrom().getFloorX() != event.getTo().getFloorX()) || (event.getFrom().getFloorZ() != event.getTo().getFloorZ())) {
                event.setCancelled();
            }
        }
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event){
        DialoguePlayTask task = playerPlayingTasks.get(event.getPlayer());
        if(task != null){
            task.cancel();
            playerPlayingTasks.remove(event.getPlayer());
        }
    }

}
