package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.repository.SectionRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedSection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public List<Section> saveAllByParsedSectionCollection(Iterable<ParsedSection> collection) {
        List<Section> sections = new ArrayList<>();
        for (ParsedSection parsedSection : collection) {
            Optional<Section> section = findByIdentifier(parsedSection.getSectionId());
            Section modifiedSection = section.
                    orElse(new Section());
            modifiedSection.setName(parsedSection.getTitle());
            modifiedSection.setIdentifier(parsedSection.getSectionId());
            modifiedSection.setUpdated(LocalDateTime.now());
            modifiedSection.setActive(true);
            sections.add(modifiedSection);
        }
       return sectionRepository.saveAll(sections);
    }

    public Optional<Section> findByIdentifier(Integer id) {
        return sectionRepository.findByIdentifier(id);
    }

    public List<Section> findActiveSections() {
        return sectionRepository.findAllByActiveTrue();
    }

    public void modifyExpiredEntities(Integer expirationTime) {
        sectionRepository.modifyExpiredEntities(expirationTime);
    }
}