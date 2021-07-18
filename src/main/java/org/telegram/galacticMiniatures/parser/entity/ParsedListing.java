package org.telegram.galacticMiniatures.parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParsedListing {

    @JsonProperty("listing_id")
    Integer id;

    @JsonProperty("title")
    String title;

    @JsonProperty("price")
    Double price;

    @JsonProperty("tags")
    List<String> tags;

    @JsonProperty("sku")
    List<String> sku;
}