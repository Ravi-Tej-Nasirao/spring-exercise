package uk.co.lexisnexis.risk.search.company.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchResponse;
import uk.co.lexisnexis.risk.search.company.entity.Company;
import uk.co.lexisnexis.risk.search.company.mapper.CompanySearchMapper;
import uk.co.lexisnexis.risk.search.company.repository.CompanyRepository;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompanySearchDalService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanySearchMapper companySearchMapper;

    @Async
    public void saveCompanies(Optional<CompanySearchResponse> companySearchResponse)  {
        if(companySearchResponse.isPresent()){

            List<String> companyNumbers = companySearchResponse.get().getItems()
                    .stream().map(uk.co.lexisnexis.risk.search.company.dto.Company::getCompanyNumber).collect(Collectors.toList());

            List<Company> dbCompanies = companyRepository.findByCompanyNumberIn(companyNumbers);

            Map<String, Company> companyMap = dbCompanies.stream().collect(Collectors.toMap(Company::getCompanyNumber, Function.identity()));

            companySearchResponse.get().getItems()
                    .forEach( company-> {
                        Company companyDetail = companyMap.get(company.getCompanyNumber());
                        if(Objects.isNull(companyDetail)){
                            companyDetail = Company.builder().build();
                            companyDetail.setCreatedDateTime(Instant.now());
                        }
                        companyDetail.setCompanyDetails(companySearchMapper.mapCompanyDtoToModel(company));
                        companyDetail.setCompanyNumber(company.getCompanyNumber());
                        companyDetail.setCompanyName(company.getTitle());
                        companyDetail.setModifiedDateTime(Instant.now());

                        companyMap.put(company.getCompanyNumber(), companyDetail);

                    });
            companyRepository.saveAll(companyMap.values());
        }
    }

    public List<Company> getCompanyByNumbers(List<String> companyNumbers){
        return companyRepository.findByCompanyNumberIn(companyNumbers);
    }

    public List<Company> getCompanyByName(String companyName){
        return companyRepository.findByCompanyDetailsTitleLike(Arrays.stream(companyName.split(" ")).findFirst().get());
    }
}
