package glorydark.dialogue;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import glorydark.dialogue.action.requirement.parser.RequirementParserRegistry;
import glorydark.dialogue.commands.DialogueCommands;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.data.DialogueLineData;
import glorydark.dialogue.patch.PatchProcessor;
import glorydark.dialogue.utils.Language;
import glorydark.dialogue.utils.Utils;

import java.io.File;
import java.util.*;

/**
 * @author glorydark
 * @date {2023/6/29} {23:47}
 */
public class DialogueMain extends PluginBase implements Listener {

    protected static HashMap<Player, DialoguePlayTask> playerPlayingTasks = new HashMap<>();
    protected static HashMap<String, DialogueData> dialogues = new HashMap<>();
    protected static LinkedHashMap<Player, ConfigSection> playerDataCaches = new LinkedHashMap<>(); // 缓存

    public static Language language;
    public static int lineMaxLength;
    public static boolean invincibleInDialogue;
    public static String configVersion;
    protected static DialogueMain plugin;
    protected static String path;
    protected static RequirementParserRegistry requirementParserRegistry = new RequirementParserRegistry();

    public static DialogueMain getInstance() {
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

    public static RequirementParserRegistry getRequirementRegistry() {
        return requirementParserRegistry;
    }

    @Override
    public void onEnable() {
        plugin = this;
        path = this.getDataFolder().getPath();
        this.saveResource("default_dialogue_zh_CN.yml");
        this.saveResource("default_dialogue_en_US.yml");
        this.saveResource("config.yml");
        this.saveResource("languages/zh_CN.properties");
        new File(path + "/player_caches/").mkdirs();
        new File(path + "/dialogues/").mkdirs();
        // start loading configurations
        Config config = new Config(path + "/config.yml", Config.YAML);
        configVersion = config.getString("version", "1.1.0");
        lineMaxLength = config.getInt("line_max_length", 64);
        language = new Language(path + "/languages/");
        invincibleInDialogue = config.getBoolean("invincible_in_dialogue", true);
        PatchProcessor.executePatch(); // upgrade configurations
        this.loadAllDialogues();
        this.getServer().getCommandMap().register("", new DialogueCommands("dialogue"));
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("AmbientDialogue Enabled");
    }

    public void loadAllDialogues() {
        this.getServer().getScheduler().cancelTask(this);
        playerPlayingTasks.clear();
        dialogues.clear(); // 先清空，为reload做准备
        File folder = new File(path + "/dialogues/");
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            this.loadDialogue(file);
        }
        this.getLogger().info("§a" + dialogues.size() + " dialogue(s) loaded successfully!");
    }

    public void loadDialogue(String fileName, Config config) {
        List<DialogueLineData> lines = new ArrayList<>();
        for (Map<String, Object> lineDataMap : (List<Map<String, Object>>) config.get("lines", new LinkedHashMap<>())) {
            lines.add(new DialogueLineData((String) lineDataMap.get("text"), (String) lineDataMap.get("speaker_name"), (Integer) lineDataMap.get("exist_ticks"), (Integer) lineDataMap.get("play_ticks")));
        }
        DialogueData data = new DialogueData(
                fileName,
                lines,
                config.getBoolean("player_still", true),
                config.get("open_requirements", new ArrayList<>()),
                config.get("prestart_actions", new ArrayList<>()),
                config.get("tick_actions", new ArrayList<>()),
                config.get("end_actions", new ArrayList<>()));
        dialogues.put(fileName, data);
        this.getLogger().info("§aDialogue Loaded: " + fileName);
    }

    public void loadDialogue(File file) {
        String fileName = Utils.getNameWithoutFormatSuffix(file.getName());
        this.getLogger().info("§eLoading dialogue: " + fileName);
        Config config = new Config(file, Config.YAML);
        loadDialogue(fileName, config);
    }

    @EventHandler
    public void PlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        DialoguePlayTask task = playerPlayingTasks.get(player);
        if (task != null) {
            task.end();
        }
        playerDataCaches.remove(player);
    }

    @EventHandler
    public void PlayerDeathEvent(PlayerDeathEvent event) {
        DialoguePlayTask task = playerPlayingTasks.get(event.getEntity());
        if (task != null) {
            task.end();
        }
    }

    @EventHandler
    public void EntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            DialoguePlayTask task = playerPlayingTasks.get(player);
            if (task != null) {
                if (invincibleInDialogue) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static LinkedHashMap<Player, ConfigSection> getPlayerDataCaches() {
        return playerDataCaches;
    }
}
