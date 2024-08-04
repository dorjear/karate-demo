Feature: Create a post and then update it using organic way

  Scenario: Create a post and then use the id to update it

    # Generate a UUID for x-appCorrelationId header
    * def correlationId = java.util.UUID.randomUUID().toString()

    # Log the generated UUID
    * print 'Generated Correlation ID:', correlationId

    # Log the configuration to ensure it's loaded
    * print 'API Base URL:', urlBasePath

    # Step 1: Create a post
    * def postRequestBody =
    """
    {
      "title": "foo",
      "body": "bar",
      "userId": 1
    }
    """
    Given url urlBasePath + '/posts'
    And request postRequestBody
    And header Content-Type = 'application/json; charset=UTF-8'
    And header x-appCorrelationId = correlationId
    When method post
    Then status 201
    And print 'Post Response:', response
    And def postId = response.id

    # Calculate the updated ID (created ID minus 1)
    * def updatedId = postId - 1

    # Log the calculated updated ID
    * print 'Updated ID:', updatedId

    # Step 2: Update the post using the calculated updated ID
    * def updateRequestBody =
    """
    {
      "id": "#(updatedId)",
      "title": "foo",
      "body": "bar",
      "userId": 1
    }
    """
    Given url urlBasePath + '/posts/' + updatedId
    And request updateRequestBody
    And header Content-Type = 'application/json; charset=UTF-8'
    And header x-appCorrelationId = correlationId
    When method put
    Then status 200
    And print 'Update Response:', response
    And match response == updateRequestBody
