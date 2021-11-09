package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.repository.SectionRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedSection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    @Transactional
    public List<Section> saveAllByParsedSectionCollection(List<ParsedSection> list) {
        List<Section> updatedSectionsList = new ArrayList<>();
        List<Integer> identifiers = list.stream()
                .map(ParsedSection::getSectionId)
                .collect(Collectors.toList());
        Map<Integer, Section> map = findByIdentifierList(identifiers).stream()
                .collect(Collectors.toMap(Section::getIdentifier, s -> s));
        for (ParsedSection parsedSection : list) {
            Section updatedSection = map.getOrDefault(parsedSection.getSectionId(), new Section());
            updatedSection.setName(parsedSection.getTitle());
            updatedSection.setIdentifier(parsedSection.getSectionId());
            updatedSection.setUpdated(LocalDateTime.now());
            updatedSection.setActive(true);
            updatedSectionsList.add(updatedSection);
        }
       return sectionRepository.saveAll(updatedSectionsList);
    }

    public List<Section> findByIdentifierList(List<Integer> identifiers) {
        return sectionRepository.findByIdentifierIn(identifiers);
    }

    public List<Section> findActiveSections() {
        return sectionRepository.findAllByActiveTrue();
    }

    public void modifyExpiredEntities(Integer expirationTime) {
        sectionRepository.modifyExpiredEntities(expirationTime);
    }
}