## Goal
Create a company search application using Spring Boot 3.1.3 or higher.

Expose an endpoint that uses the `TruProxyAPI` to do a company and officer lookup 
via name or registration number.

## Criteria
* The result of the search is returned as JSON
* A request parameter has to be added to decide whether only active companies should be returned
* The officers of each company have to be included in the company details (new field `officers`) 
* Only include officers that are active (`resigned_on` is not present in that case)
* Paging can be ignored
* Please add unit tests and integrations tests, e.g. using WireMock to mock `TruProxyAPI` calls

**Expected Request**

* The name and registration/company number are passed in via body
* The API key is passed in via header `x-api-key`
* If both fields are provided `companyNumber` is used

<pre>
{
    "companyName" : "BBC LIMITED",
    "companyNumber" : "06500244"
}
</pre>

**Expected Response**

* Not all fields that are returned from `TruProxyAPI` are required.
The final JSON should look like this :

<pre>

{
    "total_results": 1,
    "items": [
        {
            "company_number": "06500244",
            "company_type": "ltd",
            "title": "BBC LIMITED",
            "company_status": "active",
            "date_of_creation": "2008-02-11",
            "address": {
                "locality": "Retford",
                "postal_code": "DN22 0AD",
                "premises": "Boswell Cottage Main Street",
                "address_line_1": "North Leverton",
                "country": "England"
            },
            "officers": [
                {
                    "name": "BOXALL, Sarah Victoria",
                    "officer_role": "secretary",
                    "appointed_on": "2008-02-11",
                    "address": {
                        "premises": "5",
                        "locality": "London",
                        "address_line_1": "Cranford Close",
                        "country": "England",
                        "postal_code": "SW20 0DP"
                    }
                }
            ]
        }
    ]
}
</pre>

## Bonus
* Save the companies (by `company_number`) and its officers and addresses in a database 
and return the result from there if the endpoint is called with `companyNumber`.

 
## Example API Requests

**Search for Company:**  
`https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Search?Query={search_term}`

<details>
  <summary>Response Example</summary>

  <pre>
  {
    "page_number": 1,
    "kind": "search#companies",
    "total_results": 20,
    "items": [
        {
            "company_status": "active",
            "address_snippet": "Boswell Cottage Main Street, North Leverton, Retford, England, DN22 0AD",
            "date_of_creation": "2008-02-11",
            "matches": {
                "title": [
                    1,
                    3
                ]
            },
            "description": "06500244 - Incorporated on 11 February 2008",
            "links": {
                "self": "/company/06500244"
            },
            "company_number": "06500244",
            "title": "BBC LIMITED",
            "company_type": "ltd",
            "address": {
                "premises": "Boswell Cottage Main Street",
                "postal_code": "DN22 0AD",
                "country": "England",
                "locality": "Retford",
                "address_line_1": "North Leverton"
            },
            "kind": "searchresults#company",
            "description_identifier": [
                "incorporated-on"
            ]
        }]
  }
  </pre>
</details>

**Get Company Officers:**  
`https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber={number}`
<details>
  <summary>Response Example</summary>

  <pre>
  {
    "etag": "6dd2261e61776d79c2c50685145fac364e75e24e",
    "links": {
        "self": "/company/10241297/officers"
    },
    "kind": "officer-list",
    "items_per_page": 35,
    "items": [
        {
            "address": {
                "premises": "The Leeming Building",
                "postal_code": "LS2 7JF",
                "country": "England",
                "locality": "Leeds",
                "address_line_1": "Vicar Lane"
            },
            "name": "ANTLES, Kerri",
            "appointed_on": "2017-04-01",
            "resigned_on": "2018-02-12",
            "officer_role": "director",
            "links": {
                "officer": {
                    "appointments": "/officers/4R8_9bZ44w0_cRlrxoC-wRwaMiE/appointments"
                }
            },
            "date_of_birth": {
                "month": 6,
                "year": 1969
            },
            "occupation": "Finance And Accounting",
            "country_of_residence": "United States",
            "nationality": "American"
        }]
  }
  </pre>
</details>

## API documentation

**Authentication:**\
Use the API key provided in your request header when calling the endpoints. <br>
Example: curl -s -H 'x-api-key: xxxxxxxxxxxxx' "https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1/Officers?CompanyNumber=10241297"<br>

*API credentials will be provided seperately*

## Do not check the API Key into the repository!

## Flow

![Wireframe](https://raw.githubusercontent.com/TruNarrative/spring-exercise/main/spring_exercise.png)





**Technical Document for Company-Search-Service Microservice Application**

### 1. Introduction
The Company-Search-Service is a microservice application developed using Spring Boot 3, Java 17, MongoDB, Ehcache, Model Mapper, Docker, and Maven. It provides a RESTful API for searching company details based on company name or number, with additional flags to fetch active companies, active officers, and the latest data from the TruProxy API.

### 2. Technologies Used
- **Spring Boot 3**: Provides a robust framework for building microservices with minimal configuration.
- **Java 17**: The latest version of Java for enhanced performance and features.
- **MongoDB**: A NoSQL database used for persisting and retrieving company data.
- **Ehcache**: A distributed in-memory cache for improving application performance by storing frequently accessed data.
- **Model Mapper**: Used for mapping between DTOs (Data Transfer Objects) and entity objects.
- **Docker**: Containerization platform for packaging the application and its dependencies into containers.
- **Maven**: Build automation tool for managing dependencies and building the project.
- **Mockito, WireMock**: Build unit test cases and mock api calls.

### 3. Functionality
- **Search by Company Name or Number**: Users can search for company details using either the company name or number.
- **Flags for Additional Data**: Additional flags can be provided to fetch active companies, active officers, and the latest data from the TruProxy API.
- **Caching with Ehcache**: Data fetched from external sources is cached in Ehcache to improve performance.
- **Persistence with MongoDB**: Fetched data is persisted in MongoDB for future retrieval.
- **RESTful API**: Provides a RESTful API with endpoints supporting both XML and JSON formats.
- **Error Handling**: Proper HTTP status codes and error messages are returned for various scenarios to ensure a good user experience.
   - **200**: Success with company details when either valid company number or name are provided.
   - **404**: Company details not found when either invalid company number or name are provided.
   - **400**: Bad request when company number or company name are not provided or api key not provided.
- **Flexi Response**: Based on header value both json or xml formats are supported for both consume and output.
     
![Company-Search_App-Flow](https://github.com/Ravi-Tej-Nasirao/spring-exercise/assets/136536200/ac3918a0-f8d5-45e8-bde3-6a9f8d8c15a8)




### 4. Good Coding Practices
- **SOLID Principles**: The application is designed following SOLID principles to ensure maintainability, scalability, and testability.
- **AOP for App Health Check**: Aspect-Oriented Programming (AOP) is used for implementing health checks to monitor the application's health and performance.
- **REST Design Principles**: Follows RESTful design principles for designing clean, intuitive, and predictable APIs.

### 5. Advantage of MongoDB over SQL
- **Schema Flexibility**: MongoDB's schema-less nature allows for easy storage and retrieval of unstructured data, making it suitable for storing dynamic and evolving data like company details.
- **Horizontal Scalability**: MongoDB's distributed architecture enables horizontal scalability, allowing the application to handle large volumes of data and traffic efficiently.
- **Document-Oriented**: MongoDB stores data in flexible JSON-like documents, eliminating the need for complex joins and facilitating faster query execution.

### 6. Usage of Ehcache
- **Improved Performance**: Ehcache provides in-memory caching, reducing the need for frequent database queries and improving response times.
- **Reduced Latency**: Cached data is readily available in memory, resulting in reduced latency and improved application performance.
- **Cost-Effective Scalability**: Caching reduces the load on backend systems, enabling cost-effective scalability by reducing the need for additional infrastructure.

### 7. Further considerations those can be considered
- **Vulnerability Check**: Check for OWASP vulnerabilities.
- **Cache**: Redis cache or similar can be considered.
- **Search Engine**: Search Engine like Elastic Search or Solr can be considered.
- **Load Testing**: Using Jmeter or Gatling can be used.
- **Dash Board Creation**: Using Micrometer, Graphana, New Relic or Splunk.

  ### 8. Sample Curl with json as content-type and accepted header

  curl --location 'localhost:8080/api/search/company?activeOnly=true&isActiveCompanyOnly=true&isActiveOfficersOnly=true&isLatestDataRequired=true' \
--header 'Accept: application/vnd.company.app-v1+json' \
--header 'Content-Type: application/json' \
--header 'x-api-key: PwewCEztSW7XlaAKqkg4IaOsPelGynw6SN9WsbNf' \
--data '{
    "companyName": "BBC LIMITED",
    "companyNumber": "06500244"
}'

 ### 9. Sample Curl with xml as content-type and accepted header

 curl --location 'localhost:8080/api/search/company?activeOnly=true&isActiveCompanyOnly=true&isActiveOfficersOnly=true&isLatestDataRequired=true' \
--header 'Accept: application/vnd.company.app-v1+json' \
--header 'Content-Type: application/xml' \
--header 'x-api-key: PwewCEztSW7XlaAKqkg4IaOsPelGynw6SN9WsbNf' \
--data '<CompanySearchRequest>
     <companyName>BBC LIMITED</companyName>
     <companyNumber>06500244</companyNumber>
 </CompanySearchRequest>'
  
### Conclusion
The Company-Search-Service microservice application leverages modern technologies like Spring Boot, MongoDB, Ehcache, and Docker to provide efficient and scalable search functionality for company details. By adhering to good coding practices and leveraging the advantages of NoSQL databases and caching mechanisms, the application ensures optimal performance and reliability for users.


