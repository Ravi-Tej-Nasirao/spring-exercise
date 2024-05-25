package uk.co.lexisnexis.risk.search.company.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import uk.co.lexisnexis.risk.search.company.entity.Company;

import java.util.List;

@Repository
public interface CompanyRepository extends MongoRepository<Company, String> {

    List<Company> findByCompanyDetailsTitleLike(String companyTitle);

    List<Company> findByCompanyNumberIn(List<String> companyNumbers);


}
