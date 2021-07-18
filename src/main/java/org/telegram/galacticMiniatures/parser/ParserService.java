package org.telegram.galacticMiniatures.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.parser.entity.*;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParserService {

    private static final String SITE_URL = "https://openapi.etsy.com/v2/";

    private final SectionService sectionService;
    private final ListingService listingService;
    private final ListingWithImageService listingWithImageService;
    //private final TagService tagService;
    //private final ListingWithTagsService listingWithTagsService;

    private List<Section> sectionList;
    private List<Listing> listingList;
    //private Map<Integer, List<ParsedListing>> listings;

    @Value("${etsy.shopId}")
    private String shopId;
    @Value("${etsy.apiKey}")
    private String apiKey;

    @Scheduled(fixedRateString = "${schedule.sectionParsePeriod}")
    private void parseInfo() {
        parseSections();
        parseListings();
       // parseListingsWithTags();
        parseListingImages();
    }

    private void parseSections() {
        ObjectMapper mapper = new ObjectMapper();
        List<ParsedSection> resultList = null;
        StringBuilder sb = new StringBuilder(SITE_URL);
        sb.append("shops/")
                .append(shopId)
                .append("/sections?api_key=")
                .append(apiKey);
        try {
            URL url = new URL(sb.toString());
            ParsedSectionsResult results = mapper.readValue(url, ParsedSectionsResult.class);
            resultList = results.getResults();
        } catch (IOException e) {
            log.error("Scheduled task: SECTIONS failed: " + e.getMessage());
        }

        if (resultList != null) {
            sectionList = sectionService.saveAllByParsedSectionCollection(resultList);
            log.info("Scheduled task: SECTIONS saved to DB");
        }

        log.info("Scheduled task: SECTIONS parser finished");
    }

    private void parseListings() {
        ObjectMapper mapper = new ObjectMapper();
        //Set<String> tags = new HashSet<>();
        Map<Section, List<ParsedListing>> listings = new HashMap<>();
        try {
            StringBuilder sb;
            URL url;
            ParsedListingsResult results;
            List<ParsedListing> resultList;
            for (Section section : sectionList) {
                sb = new StringBuilder(SITE_URL);
                sb.append("shops/")
                    .append(shopId)
                    .append("/sections/")
                    .append(section.getIdentifier())
                    .append("/listings?api_key=")
                    .append(apiKey);
                url = new URL(sb.toString());
                results = mapper.readValue(url, ParsedListingsResult.class);
                resultList = results.getResults().stream()
                        .filter(p ->
                                    p.getPrice() != null &&
                                    p.getTitle() != null)
                        .collect(Collectors.toList());

                listings.put(section, resultList);
/*                Set<String> tagNames = resultList.stream()
                        .filter(p -> p.getTags() != null)
                        .flatMap(p -> p.getTags().stream())
                        .collect(Collectors.toSet());
                tags.addAll(tagNames);*/
            }
        } catch (IOException e) {
            log.error("Scheduled task: LISTINGS failed: " + e.getMessage());
        }

        if (listings.size() > 0) {
            listingList = listingService.saveAllByParsedListingMap(listings);
            log.info("Scheduled task: LISTINGS saved to DB");
//            tagService.saveAll(tags);
//            log.info("Scheduled task: TAGS saved to DB");
        }

        log.info("Scheduled task: LISTINGS parser finished");
    }

//    private void parseListingsWithTags() {
//        List<ParsedListing> parsedListings = listings.values().stream()
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());
//        listingWithTagsService.saveAllByParsedListingCollection(parsedListings);
//        log.info("Scheduled task: LISTINGS WITH TAGS saved to DB");
//        log.info("Scheduled task: LISTINGS WITH TAGS parser finished");
//    }

    private void parseListingImages() {
        ObjectMapper mapper = new ObjectMapper();
        Map<Listing, List<ParsedImage>> images = new HashMap<>();
        try {
            StringBuilder sb;
            URL url;
            ParsedImagesResult results;
            List<ParsedImage> resultList;
            for (Listing listing : listingList) {
                sb = new StringBuilder(SITE_URL);
                sb.append("listings/")
                        .append(listing.getIdentifier())
                        .append("/images?api_key=")
                        .append(apiKey);
                url = new URL(sb.toString());
                results = mapper.readValue(url, ParsedImagesResult.class);
                resultList = results.getResults().stream()
                        .filter(p -> p.getImageUrl() != null).collect(Collectors.toList());

                images.put(listing, resultList);
            }
        } catch (IOException e) {
            log.error("Scheduled task: LISTING IMAGES failed: " + e.getMessage());
        }

        if (listingList.size() > 0) {
            listingWithImageService.saveAllByParsedImageMap(images);
            log.info("Scheduled task: LISTING IMAGES saved to DB");
        }

        log.info("Scheduled task: LISTING IMAGES parser finished");
    }
}