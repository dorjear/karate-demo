@ignore
Feature: Common component for executing requests and validating responses

  Scenario: Execute request and validate response
    * def methodToCall = data.method
    * print 'correlationId is: ', data.headers["x-appCorrelationId"]

    Given url urlBasePath + data.url
    And request data.requestBodyObject
    And headers data.headers
    When method methodToCall
    And print 'Response:', response
    And match response == data.expectedResponseBody
    And match responseStatus == data.expectedHttpStatus

    * def result = response
