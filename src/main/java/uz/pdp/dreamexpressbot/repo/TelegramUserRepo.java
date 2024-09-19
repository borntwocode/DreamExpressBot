package uz.pdp.dreamexpressbot.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.dreamexpressbot.entity.TelegramUser;
import java.util.Optional;

public interface TelegramUserRepo extends JpaRepository<TelegramUser, Long> {

    Optional<TelegramUser> findByChatId(Long chatId);

}