import org.apache.commons.io.FileUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MyBot extends TelegramLongPollingBot {


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            Message message = update.getMessage();
            if (message.hasText()){
                
            }else if (message.hasVoice()){
                
            } else if (message.hasDocument()) {
                
            } else if (message.hasPhoto()) {
                
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
    }

    @Override
    public String getBotUsername() {
        return "toshkvbot";
    }

    @Override
    public String getBotToken() {
        return "5651697737:AAG-4xJawGFLDsnYnOBWfZ_wMKQ5mAoFI14";
    }

    private void saveFileToFolder(String fileId, String fileName) throws Exception {
        GetFile getFile = new GetFile(fileId);
        File tgFile = execute(getFile);
        String fileUrl = tgFile.getFileUrl(getBotToken());

        URL url = new URL(fileUrl);
        InputStream inputStream = url.openStream();

        FileUtils.copyInputStreamToFile(inputStream, new java.io.File(fileName));

    }
}
