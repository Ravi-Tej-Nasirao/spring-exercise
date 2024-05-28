package uk.co.lexisnexis.risk.search.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchResponse;
import uk.co.lexisnexis.risk.search.company.entity.Company;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchCustomException;
import uk.co.lexisnexis.risk.search.company.mapper.CompanySearchMapper;
import uk.co.lexisnexis.risk.search.company.repository.CompanyRepository;
import uk.co.lexisnexis.risk.search.company.service.CompanySearchDalService;
import uk.co.lexisnexis.risk.search.company.service.CompanySearchService;
import uk.co.lexisnexis.risk.search.company.service.TruProxyService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanySearchServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private TruProxyService truProxyService;

    @Mock
    private CompanySearchDalService companySearchDalService;

    @Mock
    private CompanySearchMapper companySearchMapper;

    @InjectMocks
    private CompanySearchService companySearchService;

    private CompanySearchRequest companySearchRequest;

    @BeforeEach
    public void setUp() {
        companySearchRequest = new CompanySearchRequest();
        companySearchRequest.setCompanyName("Test Company");
    }

    @Test
    public void testSearchCompanies_withLatestData() throws CompanySearchCustomException {
        uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse response = new uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse();
        when(truProxyService.searchCompanies(anyString(), any(CompanySearchRequest.class), anyBoolean(), anyBoolean()))
                .thenReturn(Optional.of(response));
        doNothing().when(companySearchDalService).saveCompanies(any(Optional.class));

        Optional<uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse> result = companySearchService.searchCompanies("apiKey", companySearchRequest, true, true, true);

        assertTrue(result.isPresent());
        assertEquals(response, result.get());
        verify(truProxyService).searchCompanies(anyString(), any(CompanySearchRequest.class), anyBoolean(), anyBoolean());
        verify(companySearchDalService).saveCompanies(any(Optional.class));
    }

    @Test
    public void testSearchCompanies_withoutLatestData() throws CompanySearchCustomException {
        Company company = new Company();
        when(companySearchDalService.getCompanyByName(anyString()))
                .thenReturn(Collections.singletonList(company));
        when(companySearchMapper.mapCompanyModelToDto(any()))
                .thenReturn(new uk.co.lexisnexis.risk.search.company.dto.Company());
        
        Optional<uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse> result = companySearchService.searchCompanies("apiKey", companySearchRequest, true, true, false);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getTotalResults());
        verify(companySearchDalService).getCompanyByName(anyString());
        verify(companySearchMapper).mapCompanyModelToDto(any());
    }

    @Test
    public void testSearchCompanies_noDataFound() throws CompanySearchCustomException {
        when(companySearchDalService.getCompanyByName(anyString())).thenReturn(Collections.emptyList());
        when(truProxyService.searchCompanies(anyString(), any(CompanySearchRequest.class), anyBoolean(), anyBoolean()))
                .thenReturn(Optional.empty());

        Optional<CompanySearchResponse> result = companySearchService.searchCompanies("apiKey", companySearchRequest, true, true, false);

        assertFalse(result.isPresent());
        verify(companySearchDalService).getCompanyByName(anyString());
        verify(truProxyService).searchCompanies(anyString(), any(CompanySearchRequest.class), anyBoolean(), anyBoolean());
    }

}