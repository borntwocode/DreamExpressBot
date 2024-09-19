package uz.pdp.dreamexpressbot.service;

import lombok.*;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.springframework.stereotype.Service;
import uz.pdp.dreamexpressbot.entity.Photo;
import uz.pdp.dreamexpressbot.repo.PhotoRepo;
import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class PhotoService {

    private final TelegramBot telegramBot;
    private final PhotoRepo photoRepo;

    public Photo getPhoto(String photoFilePath) {
        byte[] content = downloadImage(photoFilePath);
        return createPhoto(content);
    }

    public String getFilePath(PhotoSize[] photos) {
        String fileId = getFileId(photos);
        return getFilePath(fileId);
    }

    private String getFileId(PhotoSize[] photos) {
        PhotoSize largestPhoto = photos[photos.length - 1];
        return largestPhoto.fileId();
    }

    private String getFilePath(String fileId) {
        GetFile getFileRequest = new GetFile(fileId);
        GetFileResponse getFileResponse = telegramBot.execute(getFileRequest);
        return getFileResponse.file().filePath();
    }

    @SneakyThrows
    public byte[] downloadImage(String filePath) {
        String fileApi = "https://api.telegram.org/file/bot";
        String url = fileApi + telegramBot.getToken() + "/" + filePath;
        try (InputStream inputStream = new URL(url).openStream()) {
            return inputStream.readAllBytes();
        }
    }

    private Photo createPhoto(byte[] content) {
        Photo photo = new Photo();
        photo.setContent(content);
        return photoRepo.save(photo);
    }

}
