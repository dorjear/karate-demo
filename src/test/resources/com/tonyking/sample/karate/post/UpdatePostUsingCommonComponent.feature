Feature: Update a post

  Background:
    * def testData =
    """
    {
      "url": "/posts/100",
      "method": "put",
      "description": "Update post successful",
      "requestBodyObject": {
        "id": 1,
        "title": "foo",
        "body": "bar",
        "userId": 1
      },
      "headers": {
        "Accept": "application/json",
        "Content-Type": "application/json",
        "x-messageId": "5436",
        "x-appCorrelationId": "#(java.util.UUID.randomUUID().toString())",
        "x-organisationId": "SGB",
        "x-originatingSystemId": "YF-016",
        "x-consumerType": "Partner"
      },
      "expectedHttpStatus": 200,
      "expectedResponseBody": {
        "id": 100,
        "title": "foo",
        "body": "bar",
        "userId": 1
      }
    }
    """

  Scenario: Create a post
    * call read('classpath:com/tonyking/sample/karate/general/CommonComponent.feature') { data: #(testData) }