package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.ListingWithTag;

import java.util.Optional;

@Repository
@Transactional
public interface ListingWithTagRepository
    extends JpaRepository<ListingWithTag, ListingWithTag.Key> {

 // @Query("SELECT DISTINCT m.id.message.messageId FROM ChatMessageWithTag m WHERE m.id.tag in :tags")
/*  @Query(
          value = "SELECT message_id FROM messages " +
                  "WHERE id IN (SELECT message_id " +
                  "FROM (SELECT message_id, tag_id FROM messages_with_tags " +
                  "WHERE tag_id in :tags) V " +
                  "GROUP BY message_id " +
                  "HAVING COUNT(*) >= :tagsNum)",
          nativeQuery = true)
  List<Integer> getListMessageIdByTagIn(@Param("tags") Iterable<Tag> tags, @Param("tagsNum") Integer tagsNum);*/
}