Feature: Create a post

  Background:
    * def TitleGenerator = Java.type('com.tonyking.sample.karate.post.TitleGenerator')
    * def currentTime = new Date().getTime()
    * def generatedTitle = TitleGenerator.generateTitle(currentTime)

    * print "Generated title: " , generatedTitle
    * def testData =
    """
    {
      "url": "/posts",
      "method": "post",
      "description": "Create post successful",
      "requestBodyObject": {
        "title": "#(generatedTitle)",
        "body": "bar",
        "userId": 1
      },
      "headers": {
        "Accept": "application/json",
        "Content-Type": "application/json",
        "x-messageId": "5436",
        "x-appCorrelationId": "",
        "x-organisationId": "SGB",
        "x-originatingSystemId": "YF-016",
        "x-consumerType": "Partner"
      },
      "expectedHttpStatus": 201,
      "expectedResponseBody": {
        "id": 101,
        "title": "#(generatedTitle)",
        "body": "bar",
        "userId": 1
      }
    }
    """

  Scenario: Create a post
    * call read('classpath:com/tonyking/sample/karate/general/CommonComponent.feature') { data: #(testData) }