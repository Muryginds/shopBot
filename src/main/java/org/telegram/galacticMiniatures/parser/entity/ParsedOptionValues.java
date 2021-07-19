package org.telegram.galacticMiniatures.parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParsedOptionValues {

    @JsonProperty("property_name")
    String propertyName;

    @JsonProperty("values")
    List<String> values;
}