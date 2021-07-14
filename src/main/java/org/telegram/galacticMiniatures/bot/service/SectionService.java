package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.repository.SectionRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedSection;

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
            Optional<Section> section = getByIdentifier(parsedSection.getSectionId());
            sections.add(section.orElse(new Section(parsedSection.getTitle(), parsedSection.getSectionId())));
        }
       return sectionRepository.saveAll(sections);
    }

    public Optional<Section> getByIdentifier(Integer id) {
        return sectionRepository.getByIdentifier(id);
    }

    public List<Section> getSections() {
        return sectionRepository.findAll();
    }
}