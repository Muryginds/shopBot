package org.telegram.galacticMiniatures.bot.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.galacticMiniatures.bot.repository.ListingWithTagRepository;
import org.telegram.galacticMiniatures.bot.repository.TagRepository;
import org.telegram.galacticMiniatures.bot.repository.UserRepository;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.*;

@Component
@Slf4j
public class SearchCommand extends ServiceCommand {

  private final UserRepository userRepository;
  private final TagRepository tagRepository;
  private final ListingWithTagRepository listingWithTagRepository;

  public SearchCommand(@Value("search") String identifier,
                       @Value("Search") String description, UserRepository userRepository,
                       TagRepository tagRepository,
                       ListingWithTagRepository listingWithTagRepository) {
    super(identifier, description);
    this.userRepository = userRepository;
    this.tagRepository = tagRepository;
    this.listingWithTagRepository = listingWithTagRepository;
  }

  @Override
  public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
    List<String> tagNames = new ArrayList<>();

    for (String string : strings) {
      if (string.startsWith("$")) {
        tagNames.add(string);
      }
    }

   /* boolean messagesFound = false;

    String userName = Utils.getUserName(user);

    var curUser =
            userRepository.getByChatId(String.valueOf(chat.getId()))
                    .orElseGet(() -> userRepository.save(
                            new org.telegram.galacticMiniatures.bot.model.User(chat.getId().toString(), userName)));

    List<SendMessage> messages = new ArrayList<>();
    if (tagNames.size() > 0) {

      Set<ListingTag> listingTags = new HashSet<>();
      for (String tag: tagNames) {
        tagRepository.getByNameAndUser(tag, curUser).ifPresent(listingTags::add);
      }

      if (listingTags.size() == tagNames.size()) {
        List<Integer> messagesId =
          chatMessageWithTagRepository.getListMessageIdByTagIn(listingTags, listingTags.size());
        if (messagesId.size() > 0) {
          for(int number: messagesId) {
            SendMessage message = new SendMessage();
            message.setReplyToMessageId(number);
            message.setChatId(curUser.getChatId());
            message.setText(""+'\u2b08');
            messages.add(message);
          }
          messagesFound = true;
          StringBuilder info = new StringBuilder();
          info.append(curUser.getName()).append(" (").append(curUser.getChatId())
                .append(") made successful search with listingTags: ").append(tagNames)
                .append(" and received ").append(messagesId.size()).append(" message(s)");
          log.info(info.toString());
        }
      }
    }

    if (!messagesFound) {
      messages.add(Utils.prepareSendMessage(chat.getId(), Constants.BOT_SEARCH_NOT_FOUND + tagNames));
      StringBuilder info = new StringBuilder();
      info.append(curUser.getName()).append(" (").append(curUser.getChatId())
            .append(") made bad search with found listingTags: ").append(tagNames)
            .append(", requested listingTags: ").append(Arrays.toString(strings));
      log.warn(info.toString());
    }

    try {
      for (SendMessage mes : messages) {
        absSender.execute(mes);
      }
    } catch (TelegramApiException e) {
      StringBuilder info = new StringBuilder();
      info.append("Command: ").append(getCommandIdentifier()).append(" User: ").append(curUser.getName());
      log.error(info.toString(), e);
    }*/
  }
}