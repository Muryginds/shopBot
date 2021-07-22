package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Section;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface SectionRepository extends JpaRepository<Section, Integer> {
    Optional<Section> getByIdentifier(Integer id);

    @Modifying
    @Query(value = "UPDATE sections SET active = 0 WHERE updated - (SELECT * FROM (SELECT updated FROM sections " +
            "ORDER BY updated DESC LIMIT 1) as upd) < -?1", nativeQuery = true)
    void modifyExpiredEntities(Integer expirationTime);

    List<Section> findAllByActiveTrue();
}