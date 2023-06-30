package glorydark.dialogue.utils;
import cn.nukkit.Server;

/**
 * @author LT_Name
 */
public class NukkitTypeUtils {

    private static final NukkitType nukkitType;

    public static NukkitType getNukkitType() {
        return nukkitType;
    }

    static {
        switch (Server.getInstance().getCodename().toLowerCase()) {
            case "powernukkitx":
                nukkitType = NukkitType.POWER_NUKKIT_X;
                break;
            case "powernukkit":
                nukkitType = NukkitType.POWER_NUKKIT;
                break;
            case "pm1e":
                nukkitType = NukkitType.PM1E;
                break;
            case "mot":
                nukkitType = NukkitType.MOT;
                break;
            default:
                nukkitType = NukkitType.NUKKITX;
                break;
        }
    }

    public enum NukkitType {

        NUKKITX("NukkitX"),

        POWER_NUKKIT("PowerNukkit"),

        POWER_NUKKIT_X("PowerNukkitX"),

        PM1E("Nukkit PetteriM1 Edition"),

        MOT("Nukkit MOT");
        private final String showName;

        NukkitType(String showName) {
            this.showName = showName;
        }

        public String getShowName() {
            return showName;
        }
    }

}