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
public class ParsedOption {

    @JsonProperty("property_values")
    List<ParsedOptionValues> parsedOptionValues;

    @JsonProperty("offerings")
    List<ParsedOptionOfferings> parsedOptionOfferings;
}