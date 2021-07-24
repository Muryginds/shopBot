package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.galacticMiniatures.bot.model.*;
import org.telegram.galacticMiniatures.bot.repository.CountryRepository;
import org.telegram.galacticMiniatures.parser.entity.ParsedCountry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public Optional<Country> findByCountryId (Integer id) {
        return countryRepository.findByCountryId(id);
    }

    public void saveAllParsedCountries(List<ParsedCountry> list) {
        List<Country> countries = new ArrayList<>();
        for (ParsedCountry country : list) {
            countries.add(new Country(country.getCountryId(),
                                        country.getName(),
                                        country.getRuName(),
                                        country.getCode()));
        }
        countryRepository.saveAll(countries);
    }

}