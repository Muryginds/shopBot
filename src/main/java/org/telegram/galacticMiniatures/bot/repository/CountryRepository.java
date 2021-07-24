package org.telegram.galacticMiniatures.bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.galacticMiniatures.bot.model.Country;

import java.util.Optional;

@Repository
@Transactional
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByCountryId(Integer id);
}