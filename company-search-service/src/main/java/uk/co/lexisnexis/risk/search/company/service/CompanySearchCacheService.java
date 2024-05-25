package uk.co.lexisnexis.risk.search.company.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchResponse;
import uk.co.lexisnexis.risk.search.company.entity.Company;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchCustomException;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CompanySearchCacheService {

    @Autowired
    private CompanySearchService companySearchService;

    @Cacheable(value = "company", keyGenerator = "customKeyGenerator", unless = "#result == null")
    public Optional<CompanySearchResponse> searchCompanies(String apiKey,
                                                           uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest companySearchRequest,
                                                           boolean isActiveCompanyOnly, boolean isActiveOfficersOnly, boolean isLatestDataRequired)
            throws CompanySearchCustomException {
        return companySearchService.searchCompanies(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly, isLatestDataRequired);
    }

}
