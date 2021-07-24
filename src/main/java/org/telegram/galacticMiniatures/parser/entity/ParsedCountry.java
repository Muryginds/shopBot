package org.telegram.galacticMiniatures.parser.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.telegram.galacticMiniatures.bot.model.AbstractEntity;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedCountry extends AbstractEntity {

    @JsonProperty("country_id")
    Integer countryId;

    @JsonProperty("name")
    String name;

    @JsonProperty("ru_name")
    String ruName;

    @JsonProperty("code")
    Integer code;
}