package uk.co.lexisnexis.risk.search.company.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Component;
import uk.co.lexisnexis.risk.search.company.dto.Company;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchRequest;
import uk.co.lexisnexis.risk.search.company.dto.CompanySearchResponse;

/**
 * company search mapper
 *
 * @author ravin
 * @date 2024/05/25
 */
@Component
public class CompanySearchMapper {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * map company search response model to dto
     *
     * @param companySearchResponseModel the company search response model
     * @return CompanySearchResponse map company search response model to dto
     */
    public CompanySearchResponse mapCompanySearchResponseModelToDto(
            uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse companySearchResponseModel){
        return modelMapper.map(companySearchResponseModel, CompanySearchResponse.class);
    }

    /**
     * map company search response dto to model
     *
     * @param companySearchResponseDto the company search response dto
     * @return CompanySearchResponse map company search response dto to model
     */
    public uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse mapCompanySearchResponseDtoToModel(
            CompanySearchResponse companySearchResponseDto){
        return modelMapper.map(
                companySearchResponseDto,
                uk.co.lexisnexis.risk.search.company.model.CompanySearchResponse.class);
    }

    /**
     * map company search request model to dto
     *
     * @param companySearchRequestModel the company search request model
     * @return CompanySearchRequest map company search request model to dto
     */
    public CompanySearchRequest mapCompanySearchRequestModelToDto(
            uk.co.lexisnexis.risk.search.company.model.CompanySearchRequest companySearchRequestModel){
        return modelMapper.map(companySearchRequestModel, CompanySearchRequest.class);
    }

    /**
     * map company search request dto to model
     *
     * @param companySearchRequestDto the company search request dto
     * @return CompanySearchRequest map company search request dto to model
     */
    public uk.co.lexisnexis.risk.search.company.model.CompanySearchRequest mapCompanySearchRequestDtoToModel(
            CompanySearchRequest companySearchRequestDto){
        return modelMapper.map(
                companySearchRequestDto,
                uk.co.lexisnexis.risk.search.company.model.CompanySearchRequest.class);
    }

    /**
     * map company model to dto
     *
     * @param company the company
     * @return Company map company model to dto
     */
    public Company mapCompanyModelToDto(
            uk.co.lexisnexis.risk.search.company.model.Company company){
        return modelMapper.map(company, Company.class);
    }

    /**
     * map company dto to model
     *
     * @param company the company
     * @return Company map company dto to model
     */
    public uk.co.lexisnexis.risk.search.company.model.Company mapCompanyDtoToModel(
            Company company){
        return modelMapper.map(
                company,
                uk.co.lexisnexis.risk.search.company.model.Company.class);
    }

    /**
     * map company entity to dto
     *
     * @param company the company
     * @return Company map company entity to dto
     */
    public Company mapCompanyEntityToDto(
            uk.co.lexisnexis.risk.search.company.entity.Company company){
        return modelMapper.map(company, Company.class);
    }

    /**
     * map company dto to entity
     *
     * @param company the company
     * @return Company map company dto to entity
     */
    public uk.co.lexisnexis.risk.search.company.entity.Company mapCompanyDtoToEntity(
            Company company){
        return modelMapper.map(
                company,
                uk.co.lexisnexis.risk.search.company.entity.Company.class);
    }

}
