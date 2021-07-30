package org.telegram.galacticMiniatures.bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public void saveAllParsedCountries(List<ParsedCountry> list) {
        List<Country> countries = new ArrayList<>();
        for (ParsedCountry country : list) {
            Optional<Country> optionalCountry = countryRepository.findByCountryId(country.getCountryId());
            Country modifiedCountry = optionalCountry.orElse(new Country(country.getCountryId()));
            modifiedCountry.setCode(country.getCode());
            modifiedCountry.setName(country.getName());
            modifiedCountry.setRuName(country.getRuName());
            countries.add(modifiedCountry);
        }
        countryRepository.saveAll(countries);
    }
}