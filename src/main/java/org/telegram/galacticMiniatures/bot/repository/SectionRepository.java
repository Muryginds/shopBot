package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Section;

import java.util.List;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE sections SET active = false WHERE EXTRACT(EPOCH FROM updated - " +
            "(SELECT * FROM (SELECT MAX(updated) FROM sections) as upd)) " +
            "< -?1", nativeQuery = true)
    void modifyExpiredEntities(Integer expirationTime);

    List<Section> findByIdentifierIn(List<Integer> identifiers);

    List<Section> findAllByActiveTrue();
}