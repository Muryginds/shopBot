package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Tag;
import org.telegram.galacticMiniatures.bot.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;

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

    public List<Tag> findTagsByCollection(Iterable<String> tagNames) {
        return tagRepository.findTagsByNameIn(tagNames);
    }
}