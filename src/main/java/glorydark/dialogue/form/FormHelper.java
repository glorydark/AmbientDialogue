package glorydark.dialogue.form;

import cn.lanink.gamecore.form.windows.AdvancedFormWindowCustom;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowModal;
import cn.lanink.gamecore.form.windows.AdvancedFormWindowSimple;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.response.FormResponseCustom;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.data.DialogueLineData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author glorydark
 * @date {2023/7/1} {11:04}
 */
public class FormHelper {

    // 选择对话
    public static void showDialogueSelect(Player showing) {
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(DialogueMain.getLanguage().translateString(showing, "form_dialogue_select_title"), DialogueMain.getLanguage().translateString(showing, "form_dialogue_select_content"));
        simple.addButton(new ElementButton(DialogueMain.getLanguage().translateString(showing, "form_button_return")));
        List<Map.Entry<String, DialogueData>> entryList = new ArrayList<>(DialogueMain.getDialogues().entrySet());
        for (Map.Entry<String, DialogueData> entry : entryList) {
            simple.addButton(new ElementButton(entry.getKey()));
        }
        simple.onClicked((elementButton, player) -> {
            DialogueData data = DialogueMain.getDialogues().get(elementButton.getText());
            if (data != null) {
                showDialogueEditTypeSelect(showing, data);
            }
        });
        simple.showToPlayer(showing);
    }

    // 选择对话编辑类型
    public static void showDialogueEditTypeSelect(Player showing, DialogueData data) {
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(DialogueMain.getLanguage().translateString(showing, "form_dialogue_edit_type_select_title"), DialogueMain.getLanguage().translateString(showing, "form_dialogue_edit_type_select_content"));
        simple.addButton(new ElementButton(DialogueMain.getLanguage().translateString(showing, "form_dialogue_edit_type_create_line")));
        simple.addButton(new ElementButton(DialogueMain.getLanguage().translateString(showing, "form_dialogue_edit_type_edit_line")));
        simple.addButton(new ElementButton(DialogueMain.getLanguage().translateString(showing, "form_dialogue_edit_type_remove_line")));
        simple.addButton(new ElementButton(DialogueMain.getLanguage().translateString(showing, "form_dialogue_edit_type_basic_configuration")));
        simple.addButton(new ElementButton(DialogueMain.getLanguage().translateString(showing, "form_button_return")));
        simple.onClicked((elementButton, player) -> {
            switch (simple.getResponse().getClickedButtonId()) {
                case 0:
                    showCreateDialogueLine(showing, data);
                    break;
                case 1:
                    showDialogueLineSelect(showing, data, FormType.EditLine);
                    break;
                case 2:
                    showDialogueLineSelect(showing, data, FormType.RemoveLine);
                    break;
                case 3:
                    showBasicConfigurationEdit(showing, data);
                    break;
                case 4:
                    showDialogueSelect(showing);
                    break;
            }
        });
        simple.showToPlayer(showing);
    }

    // 选择需要编辑的对话
    public static void showDialogueLineSelect(Player showing, DialogueData data, FormType type) {
        AdvancedFormWindowSimple simple = new AdvancedFormWindowSimple(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_select_title"), DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_select_content"));
        simple.addButton(new ElementButton(DialogueMain.getLanguage().translateString(showing, "form_button_return")));
        for (DialogueLineData lineData : data.getDialogueLineData()) {
            simple.addButton(new ElementButton(lineData.getText()));
        }
        simple.onClicked((elementButton, player) -> {
            int id = simple.getResponse().getClickedButtonId();
            if (id == 0) {
                showDialogueEditTypeSelect(showing, data);
                return;
            }
            switch (type) {
                case EditLine:
                    showEditDialogueLine(showing, data, id - 1, data.getDialogueLineData().get(id - 1));
                    break;
                case RemoveLine:
                    data.getDialogueLineData().remove(id - 1);
                    showReturn(showing, data, (player1, data1) -> showDialogueEditTypeSelect(player1, data), true);
                    break;
                case CreateLine:
                    showCreateDialogueLine(showing, data);
                    break;
            }
        });
        simple.onClosed(player -> showDialogueEditTypeSelect(showing, data));
        simple.showToPlayer(showing);
    }

    public static void showCreateDialogueLine(Player showing, DialogueData data) {
        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_create_title"));
        custom.addElement(new ElementInput(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_speaker_name_description")));
        custom.addElement(new ElementInput(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_line_description")));
        custom.addElement(new ElementInput(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_exist_ticks_description")));
        custom.addElement(new ElementInput(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_play_ticks_description")));
        custom.onResponded((formResponseCustom, player) -> {
            FormResponseCustom responses = custom.getResponse();
            String speakerName = responses.getInputResponse(0);
            String text = responses.getInputResponse(1);
            String existTicks = responses.getInputResponse(2);
            String playTicks = responses.getInputResponse(3);
            if (speakerName.equals("") || text.equals("") || existTicks.equals("") || playTicks.equals("")) {
                showReturn(player, data, (player1, data1) -> showDialogueLineSelect(player, data, FormType.EditLine), false);
                return;
            }
            data.getDialogueLineData().add(new DialogueLineData(speakerName, text, Integer.parseInt(existTicks), Integer.parseInt(playTicks)));
            data.saveAll();
            showReturn(player, data, (player1, data1) -> showDialogueEditTypeSelect(player, data), true);
        });
        custom.showToPlayer(showing);
    }

    public static void showEditDialogueLine(Player showing, DialogueData data, int index, DialogueLineData lineData) {
        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_title"));
        custom.addElement(new ElementInput(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_speaker_name_description"), "", lineData.getSpeakerName()));
        custom.addElement(new ElementInput(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_line_description"), "", lineData.getText()));
        custom.addElement(new ElementInput(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_exist_ticks_description"), "", String.valueOf(lineData.getExistDuration())));
        custom.addElement(new ElementInput(DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_play_ticks_description"), "", String.valueOf(lineData.getPlayDuration())));
        custom.onResponded((formResponseCustom, player) -> {
            FormResponseCustom responses = custom.getResponse();
            if (data.getDialogueLineData().indexOf(lineData) == index) { // 防止同时更改，导致出错
                lineData.setSpeakerName(responses.getInputResponse(0));
                lineData.setText(responses.getInputResponse(1));
                lineData.setExistDuration(Integer.parseInt(responses.getInputResponse(2)));
                lineData.setPlayDuration(Integer.parseInt(responses.getInputResponse(3)));
            }
            data.saveAll();
            showReturn(player, data, (player1, data1) -> showDialogueLineSelect(player, data, FormType.EditLine), true);
        });
        custom.showToPlayer(showing);
    }

    // 返回界面
    public static void showReturn(Player showing, DialogueData data, BiConsumer<Player, DialogueData> consumer, boolean success) {
        AdvancedFormWindowModal modal = new AdvancedFormWindowModal(DialogueMain.getLanguage().translateString(showing, "form_return_title")
                , success ? DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_success") : DialogueMain.getLanguage().translateString(showing, "form_dialogue_line_edit_failed")
                , DialogueMain.getLanguage().translateString(showing, "form_return_button1")
                , DialogueMain.getLanguage().translateString(showing, "form_return_button2"));
        modal.onClickedTrue(player -> consumer.accept(player, data));
        modal.showToPlayer(showing);
    }

    // 基础配置界面
    public static void showBasicConfigurationEdit(Player showing, DialogueData data) {
        AdvancedFormWindowCustom custom = new AdvancedFormWindowCustom(DialogueMain.getLanguage().translateString(showing, "form_dialogue_edit_basic_title"));
        custom.addElement(new ElementToggle(DialogueMain.getLanguage().translateString(showing, "form_dialogue_edit_basic_player_still"), data.isPlayerStill()));
        custom.onResponded((formResponseCustom, player) -> {
            data.setPlayerStill(formResponseCustom.getToggleResponse(0));
            data.saveAll();
            showReturn(player, data, FormHelper::showDialogueEditTypeSelect, true);
        });
        custom.showToPlayer(showing);
    }
}
