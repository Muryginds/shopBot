package org.telegram.galacticMiniatures.parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParsedListingsResult {

    @JsonProperty("results")
    List<ParsedListing> results;

    @JsonProperty("pagination")
    Map<String, Integer> pagination;
}