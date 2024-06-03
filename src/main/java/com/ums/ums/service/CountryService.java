package com.ums.ums.service;

import com.ums.ums.entity.Country;
import com.ums.ums.payload.CountryDto;
import com.ums.ums.repository.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private CountryRepository countryRepository;
    private ModelMapper modelMapper;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public List<CountryDto> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        List<CountryDto> dtos = countries.stream().map(c -> covertToDto(c)).collect(Collectors.toList());
        return dtos;
    }

    CountryDto covertToDto(Country country) {
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);
        return countryDto;
    }
}
