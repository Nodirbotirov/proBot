import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {

    List<User> users = new ArrayList<>();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            User user = saveUser(chatId);
            if (message.hasText()){
                String text = message.getText();
                if (text.equals("/list")){
                    System.out.println(users);
                }
                if (text.equals("/start")){
                    if (user.getFullName()!=null){
                        try {
                            setLang(chatId, user  );
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }else {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText("Assalom alikum. \n" +
                                "Iltimos ism familiyangizni kiriting\n" +
                                "");
                        sendMessage.setChatId(chatId);
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    user.setStep(BotConstant.ENTER_NAME);
                } else if (user.getStep().equals(BotConstant.ENTER_NAME)) {
                    try {
                        user.setFullName(text);
                        setLang(chatId, user);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }


                } else if (user.getStep().equals(BotConstant.SELECT_LANG)) {
                    user.setStep(BotConstant.WRITE_MSG);
                } else if (user.getStep().equals(BotConstant.WRITE_MSG)) {
                    user.setMsg(text);
                    sendText(chatId, user.getSelectedLang().equals(BotQuery.UZ_SELECT) ?
                            "Adminstrator tez orada siz bilan bog'lanadi":
                            "админитратор скоро свами свяяжится");
                }
            }
//            Document document = message.getDocument();
//            try {
//                saveFileToFolder(document.getFileId(), document.getFileName());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            List<PhotoSize> photo = message.getPhoto();
//            for (PhotoSize photoSize : photo){
//                try {
//                    saveFileToFolder(photoSize.getFileId(), "shift.png");
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            Voice voice = message.getVoice();
//            try {
//                saveFileToFolder(voice.getFileId(), "bm.mp3");
//            } catch (Exception e) {
//                throw new RuntimeException(e);
            
//            }
        }
        else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getFrom().getId().toString();
            String data = update.getCallbackQuery().getData();
            User user = saveUser(chatId);
            if (user.getStep().equals(BotConstant.SELECT_LANG)){
                if (data.equals(BotQuery.UZ_SELECT)){
                    user.setSelectedLang(BotQuery.UZ_SELECT);
                    sendText(chatId, "Xabaringizni qoldiring");
                } else if (data.equals(BotQuery.RU_SELECT)) {
                    user.setSelectedLang(BotQuery.RU_SELECT);
                    sendText(chatId, "оставте сообщиние");
                }user.setStep(BotConstant.WRITE_MSG);
            }
        }
    }

    private User saveUser(String chatId) {
        for (User user : users) {
            if (user.getChatId().equals(chatId)){
                return user;
            }
        }
        User user = new User();
        user.setChatId(chatId);
        users.add(user);
        return null;
    }

    private void sendText(String chatId, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setLang(String chatId, User user) throws TelegramApiException{
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(user.getFullName()+"Iltimos tilni tanlang");
        sendMessage.setChatId(chatId);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> td = new ArrayList<>();

        InlineKeyboardButton inlineKeyboardButtonUz = new InlineKeyboardButton();
        inlineKeyboardButtonUz.setText("\uD83C\uDDFA\uD83C\uDDFF Uz");
        inlineKeyboardButtonUz.setCallbackData("uzbek tilini tanlandi");

        InlineKeyboardButton inlineKeyboardButtonRu = new InlineKeyboardButton();
        inlineKeyboardButtonRu.setText("\uD83C\uDDF7\uD83C\uDDFA Ru");
        inlineKeyboardButtonRu.setCallbackData("rus tilini tanlandi");

        td.add(inlineKeyboardButtonUz);
        td.add(inlineKeyboardButtonRu);

        List<List<InlineKeyboardButton>> tr = new ArrayList<>();
        tr.add(td);

        inlineKeyboardMarkup.setKeyboard(tr);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        execute(sendMessage);
    }

    private void saveFileToFolder(String fileId, String fileName) throws Exception {
        GetFile getFile = new GetFile(fileId);
        File tgFile = execute(getFile);
        String fileUrl = tgFile.getFileUrl(getBotToken());

        URL url = new URL(fileUrl);
        InputStream inputStream = url.openStream();

        FileUtils.copyInputStreamToFile(inputStream, new java.io.File(fileName));

    }

    @Override
    public String getBotUsername() {
        return "toshkvbot";
    }

    @Override
    public String getBotToken() {
        return "5651697737:AAG-4xJawGFLDsnYnOBWfZ_wMKQ5mAoFI14";
    }
}
