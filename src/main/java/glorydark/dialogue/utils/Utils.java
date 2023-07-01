package glorydark.dialogue.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;

/**
 * @author glorydark
 * @date {2023/6/30} {0:03}
 */
public class Utils {

    private static final String blankOfALine = "                                                                ";

    /*引用代码: 由于Java是基于Unicode编码的，因此，一个汉字的长度为1，而不是2。
     * 但有时需要以字节单位获得字符串的长度。例如，“123abc长城”按字节长度计算是10，而按Unicode计算长度是8。
     * 为了获得10，需要从头扫描根据字符的Ascii来获得具体的长度。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255。
     * 因此，可以编写如下的方法来获得以字节为单位的字符串长度。*/
    public static Integer getStringCharCount(String s) {
        int length = 0;
        for (int i = 0; i < s.length(); i++) {
            int ascii = Character.codePointAt(s, i);
            if (ascii >= 0 && ascii <= 255)
                length++;
            else
                length += 2;
        }
        return length;
    }

    public static String getLineBlank(){
        return blankOfALine;
    }

    public static void parseAndExecuteCommand(Player player, String command){
        if (command.startsWith("console#")) {
            Server.getInstance().dispatchCommand(Server.getInstance().getConsoleSender(), command.replace("%player%", player.getName()));
        } else if (command.startsWith("op#")) {
            if (player.isOp()) {
                Server.getInstance().dispatchCommand(player, command.replace("%player%", player.getName()));
            } else {
                Server.getInstance().addOp(player.getName());
                try {
                    Server.getInstance().dispatchCommand(player, command.replace("%player%", player.getName()));
                }catch (Exception e){
                    Server.getInstance().removeOp(player.getName()); // 防止撤销不掉
                }
                Server.getInstance().removeOp(player.getName());
            }
        } else {
            Server.getInstance().dispatchCommand(player, command.replace("%player%", player.getName()));
        }
    }
}
