Feature: Create and Update a post using common components

  Background:
    * def service = karate.feature.fileName
    * def createPostData =
    """
    {
      "url": "/posts",
      "method": "post",
      "description": "Create post successful",
      "requestBodyObject": {
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
      "expectedHttpStatus": 201,
      "expectedResponseBody": {
        "id": 101,
        "title": "foo",
        "body": "bar",
        "userId": 1
      }
    }
    """

    * def updatePostData =
    """
    {
      "url": "/posts/",
      "method": "put",
      "description": "Update post successful",
      "requestBodyObject": {
        "id": 1,
        "title": "foo updated",
        "body": "bar updated",
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
        "title": "foo updated",
        "body": "bar updated",
        "userId": 1
      }
    }
    """

  Scenario: Create and then Update a post
    * def createResponse = call read('classpath:com/tonyking/sample/karate/general/CommonComponent.feature') { data: #(createPostData) }
    * print 'Response is : ', createResponse.result
    * def postId = createResponse.result.id - 1
    * print 'Created Post ID:', postId

    * eval updatePostData.requestBodyObject.id = postId
    * eval updatePostData.url = updatePostData.url + postId

    * def updateResponse = call read('classpath:com/tonyking/sample/karate/general/CommonComponent.feature') { data: #(updatePostData) }
    * print 'Updated Response:', updateResponse
