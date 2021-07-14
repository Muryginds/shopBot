package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Section;

import java.util.Optional;

@Repository
@Transactional
public interface SectionRepository extends JpaRepository<Section, Integer> {
    Optional<Section> getByIdentifier(Integer id);
}