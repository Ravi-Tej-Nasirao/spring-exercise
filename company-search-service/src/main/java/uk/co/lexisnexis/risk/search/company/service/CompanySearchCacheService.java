package uk.co.lexisnexis.risk.search.company.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchCustomException;
import uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse;

import java.util.Optional;

/**
 * company search cache service
 *
 * @author ravin
 * @date 2024/05/28
 */
@Component
@Slf4j
public class CompanySearchCacheService {

    @Autowired
    private CompanySearchService companySearchService;

    /**
     * search companies
     *
     * @param apiKey               the api key
     * @param companySearchRequest the company search request
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @param isLatestDataRequired the is latest data required
     * @return Optional<CompanySearchResponse> search companies
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    @Cacheable(value = "company", keyGenerator = "customKeyGenerator", unless = "#result == null")
    public Optional<CompanySearchResponse> searchCompanies(String apiKey,
                                                           uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest companySearchRequest,
                                                           boolean isActiveCompanyOnly, boolean isActiveOfficersOnly, boolean isLatestDataRequired)
            throws CompanySearchCustomException {
        return companySearchService.searchCompanies(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly, isLatestDataRequired);
    }

}
