package uk.co.lexisnexis.risk.search.company.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest;
import uk.co.lexisnexis.risk.search.company.entity.Company;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchCustomException;
import uk.co.lexisnexis.risk.search.company.mapper.CompanySearchMapper;
import uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse;
import uk.co.lexisnexis.risk.search.company.model.Officer;
import uk.co.lexisnexis.risk.search.company.repository.CompanyRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static uk.co.lexisnexis.risk.search.company.constants.CompanySearchConstants.ACTIVE;

/**
 * company search service
 *
 * @author ravin
 * @date 2024/05/25
 */
@Service
@Slf4j
public class CompanySearchService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TruProxyService truProxyService;

    @Autowired
    private CompanySearchDalService companySearchDalService;

    @Autowired
    private CompanySearchMapper companySearchMapper;

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
    public Optional<CompanySearchResponse> searchCompanies(String apiKey,
                                                           uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest companySearchRequest,
                                                           boolean isActiveCompanyOnly, boolean isActiveOfficersOnly, boolean isLatestDataRequired)
            throws CompanySearchCustomException {

        Optional<CompanySearchResponse> companySearchResponse;
        if (isLatestDataRequired) {
            companySearchResponse = getLatestCompanySearchData(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly);
        } else {
            companySearchResponse = getCompanySearchDataFromDatabase(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly);
        }
        return companySearchResponse;
    }

    /**
     * get company search data from database
     *
     * @param apiKey               the api key
     * @param companySearchRequest the company search request
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @return Optional<CompanySearchResponse> get company search data from database
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    private Optional<uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse> getCompanySearchDataFromDatabase(String apiKey, CompanySearchRequest companySearchRequest, boolean isActiveCompanyOnly, boolean isActiveOfficersOnly) throws CompanySearchCustomException {
        Optional<uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse> companySearchResponse;
        List<Company> companies = getCompaniesByCriteria(companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly);
        if (CollectionUtils.isNotEmpty(companies)) {
            return prepareCompanySearchResponse(companies);
        } else {
            return searchCompanies(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly, true);
        }
    }

    /**
     * get latest company search data
     *
     * @param apiKey               the api key
     * @param companySearchRequest the company search request
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @return Optional<CompanySearchResponse> get latest company search data
     * @throws CompanySearchCustomException CompanySearchCustomException
     */
    private Optional<CompanySearchResponse> getLatestCompanySearchData(String apiKey, CompanySearchRequest companySearchRequest, boolean isActiveCompanyOnly, boolean isActiveOfficersOnly) throws CompanySearchCustomException {
        Optional<CompanySearchResponse> companySearchResponse;
        companySearchResponse = truProxyService.searchCompanies(apiKey, companySearchRequest, isActiveCompanyOnly, isActiveOfficersOnly);
        companySearchDalService.saveCompanies(companySearchResponse);
        return companySearchResponse;
    }

    /**
     * get companies by criteria
     *
     * @param companySearchRequest the company search request
     * @return List<Company> get companies by criteria
     */
    private List<Company> getCompaniesByCriteria(CompanySearchRequest companySearchRequest, boolean isActiveCompanyOnly, boolean isActiveOfficersOnly) {
        List<Company> companies;
        if (StringUtils.isNotBlank(companySearchRequest.getCompanyNumber())) {
            companies = companySearchDalService.getCompanyByNumbers(Collections.singletonList(companySearchRequest.getCompanyNumber()));
        } else {
            companies = companySearchDalService.getCompanyByName(companySearchRequest.getCompanyName());
        }
        return filterCompaniesBasedOnSearchCriteria(companies, isActiveCompanyOnly, isActiveOfficersOnly);
    }


    /**
     * filter companies based on search criteria
     *
     * @param companies            the companies
     * @param isActiveCompanyOnly  the is active company only
     * @param isActiveOfficersOnly the is active officers only
     * @return List<Company> filter companies based on search criteria
     */
    private List<Company> filterCompaniesBasedOnSearchCriteria(List<Company> companies, boolean isActiveCompanyOnly, boolean isActiveOfficersOnly) {

        List<Company> filteredCompaniesByActiveFlag = companies
                .stream()
                .filter(
                        company ->
                                isActiveCompanyOnly
                                        ? Objects.nonNull(company.getCompanyDetails()) && StringUtils.isNotBlank(company.getCompanyDetails().getCompanyStatus()) && StringUtils.equalsIgnoreCase(company.getCompanyDetails().getCompanyStatus(), ACTIVE)
                                        : true
                )
                .collect(Collectors.toList());

        if (isActiveOfficersOnly && CollectionUtils.isNotEmpty(filteredCompaniesByActiveFlag)) {
            for (Company company : filteredCompaniesByActiveFlag) {
                if (Objects.nonNull(company.getCompanyDetails()) && CollectionUtils.isNotEmpty(company.getCompanyDetails().getOfficers())) {
                    List<Officer> activeOfficers = company.getCompanyDetails().getOfficers()
                            .stream()
                            .filter(officer -> Objects.isNull(officer.getResignedOn()))
                            .toList();
                    company.getCompanyDetails().setOfficers(activeOfficers);
                }
            }
        }
        return filteredCompaniesByActiveFlag;
    }

        /**
         * prepare company search response
         *
         * @param companies the companies
         * @return Optional<CompanySearchResponse> prepare company search response
         */
        private Optional<uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse>
            prepareCompanySearchResponse(List <Company> companies) {

            uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse companySearchResponse =
                    uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse
                            .builder()
                            .totalResults(companies.size())
                            .items(companies.stream().map(
                                    company -> companySearchMapper.mapCompanyEntityToModel(company)
                            ).collect(Collectors.toList()))
                            .build();
            return Optional.ofNullable(companySearchResponse);
        }

}

