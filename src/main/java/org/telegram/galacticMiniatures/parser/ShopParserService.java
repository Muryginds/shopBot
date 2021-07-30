package org.telegram.galacticMiniatures.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.Listing;
import org.telegram.galacticMiniatures.bot.model.Section;
import org.telegram.galacticMiniatures.bot.service.*;
import org.telegram.galacticMiniatures.parser.entity.*;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ShopParserService {

    private static final String SITE_URL = "https://openapi.etsy.com/v2/";
    private static final String FILE_COUNTRIES = "src/main/resources/countries.json";

    private final SectionService sectionService;
    private final ListingService listingService;
    private final ListingWithImageService listingWithImageService;
    private final ListingWithOptionService listingWithOptionService;
    private final CountryService countryService;
    //private final TagService tagService;
    //private final ListingWithTagsService listingWithTagsService;

    //private Map<Integer, List<ParsedListing>> listings;
    private List<Listing> listingList;
    private List<Section> sectionList;

    @Value("${etsy.shopId}")
    private String shopId;
    @Value("${etsy.apiKey}")
    private String apiKey;
    @Value("${schedule.entityExpirationTime}")
    private Integer expirationTime;

    public void parseInfo() {
        parseSections();
        parseListings();
        parseListingImages();
        parseListingOptions();
        makeExpiredEntitiesNotActive();

        // parseListingsWithTags();
        // oneTimeCountyParser();
    }

    private void oneTimeCountyParser() {
        ObjectMapper mapper = new ObjectMapper();
        List<ParsedCountry> resultList = null;
        File file = Paths.get(FILE_COUNTRIES).toFile();

        try {
            resultList = mapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Cant read file Countries. " + e.getMessage());
        }
        if (resultList != null) {
            countryService.saveAllParsedCountries(resultList);
            log.info("Task: COUNTRIES saved to DB");
        }

        log.info("Task: Country parser finished");
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
            resultList = results.getResults().stream().filter(p -> p.getCount() > 0).collect(Collectors.toList());
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
            for (Section section : sectionList) {
                List<ParsedListing> resultList = new ArrayList<>();
                Integer nextPage = 1;
                while (nextPage != null) {
                    sb = new StringBuilder(SITE_URL);
                    sb.append("shops/")
                            .append(shopId)
                            .append("/sections/")
                            .append(section.getIdentifier())
                            .append("/listings/active?api_key=")
                            .append(apiKey)
                            .append("&limit=100")
                            .append("&page=")
                            .append(nextPage);
                    url = new URL(sb.toString());
                    results = mapper.readValue(url, ParsedListingsResult.class);
                    resultList.addAll(results.getResults());
                    nextPage = results.getPagination().get("next_page");
                }
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

        if (images.size() > 0) {
            listingWithImageService.saveAllByParsedImageMap(images);
            log.info("Scheduled task: LISTING IMAGES saved to DB");
        }

        log.info("Scheduled task: LISTING IMAGES parser finished");
    }
    private void parseListingOptions() {
        ObjectMapper mapper = new ObjectMapper();
        Map<Listing, List<ParsedOption>> options = new HashMap<>();
        try {
            StringBuilder sb;
            URL url;
            ParsedOptionsResult results;
            List<ParsedOption> resultList;
            for (Listing listing : listingList) {
                sb = new StringBuilder(SITE_URL);
                sb.append("listings/")
                        .append(listing.getIdentifier())
                        .append("/inventory?api_key=")
                        .append(apiKey);
                url = new URL(sb.toString());
                results = mapper.readValue(url, ParsedOptionsResult.class);
                resultList = results.getResults().values().stream()
                        .flatMap(Collection::stream)
                        //.filter(l -> l.getParsedOptionValues().size() > 0)
                        .collect(Collectors.toList());
                if (resultList.size() > 0) {
                    options.put(listing, resultList);
                }
            }
        } catch (IOException e) {
            log.error("Scheduled task: LISTING OPTIONS failed: " + e.getMessage());
        }

        if (options.size() > 0) {
            listingWithOptionService.saveAllByParsedOptionMap(options);
            log.info("Scheduled task: LISTING OPTIONS saved to DB");
        }

        log.info("Scheduled task: LISTING OPTIONS parser finished");
    }

    private void makeExpiredEntitiesNotActive() {
        listingWithOptionService.modifyExpiredEntities(expirationTime);
        listingWithImageService.modifyExpiredEntities(expirationTime);
        listingService.modifyExpiredEntities(expirationTime);
        sectionService.modifyExpiredEntities(expirationTime);
    }
}