package glorydark.dialogue.utils;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Language {

    public Map<String, Map<String, Object>> lang = new HashMap<>();

    public String defaultLang;

    public Language(String langPath) {
        this.defaultLang = Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry();
        File files = new File(langPath);
        for (File file : Objects.requireNonNull(files.listFiles())) {
            if (file.getName().endsWith(".properties")) {
                lang.put(file.getName().replace(".properties", ""), new Config(file, Config.PROPERTIES).getAll());
            }
        }
    }

    public String translateString(String key, Object... params) {
        String originText = (String) lang.getOrDefault(defaultLang, new HashMap<>()).getOrDefault(key, "Key not found!");
        for (int i = 1; i <= params.length; i++) {
            originText = originText.replaceAll("%" + i + "%", params[i - 1].toString());
        }
        return originText;
    }

    public String translateString(Player player, String key, Object... params) {
        String originText = (String) lang.getOrDefault(getPlayerLanguage(player), new HashMap<>()).getOrDefault(key, "Key not found!");
        for (int i = 1; i <= params.length; i++) {
            originText = originText.replaceAll("%" + i + "%", params[i - 1].toString());
        }
        return originText;
    }

    public String getPlayerLanguage(Player player) {
        String langName;
        if (player == null) {
            langName = defaultLang;
        } else {
            langName = player.getLoginChainData().getLanguageCode();
        }
        return lang.containsKey(langName) ? langName : defaultLang;
    }
}
