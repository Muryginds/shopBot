package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.model.Tag;
import org.telegram.galacticMiniatures.bot.repository.ListingRepository;
import org.telegram.galacticMiniatures.bot.repository.SectionRepository;
import org.telegram.galacticMiniatures.bot.repository.TagRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedListing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public void saveAll(Iterable<String> list) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : list) {
            tags.add(new Tag(tagName));
        }
        tagRepository.saveAll(tags);
    }

    public List<Tag> getTagsByCollection(Iterable<String> tagNames) {
        return tagRepository.getTagsByNameIn(tagNames);
    }
}