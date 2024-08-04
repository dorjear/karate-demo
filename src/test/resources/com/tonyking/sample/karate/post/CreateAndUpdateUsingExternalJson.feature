Feature: Create a post and then update it using json file

  Background:
    # Get the current feature file name
    * def featureFileName = karate.feature.fileName
    * print featureFileName
    # Extract the base name without extension
    * def baseName = featureFileName.substring(0, featureFileName.lastIndexOf('.'))
    # Construct the JSON file path. This works but the path is hard code.
    * def jsonFilePath = 'classpath:post/' + baseName + '.json'

#    * def testData = read('classpath:post/testdata.json')
    * def testData = read(baseName+'.json')

    * def createPostData = testData.createPost
    * def updatePostData = testData.updatePost

  Scenario: Create a post and then use the id to update it
    # Generate a UUID for x-appCorrelationId header
    * def correlationId = java.util.UUID.randomUUID().toString()

    # Update the headers with the generated correlation ID
    * def createHeaders = createPostData.headers
    * def updateHeaders = updatePostData.headers
    * def methodToCreate = createPostData.method
    * def methodToUpdate = updatePostData.method
    * def extectedStatus = 201
    * createHeaders['x-appCorrelationId'] = correlationId
    * updateHeaders['x-appCorrelationId'] = correlationId

    # Log the configuration to ensure it's loaded
    * print 'API Base URL:', urlBasePath
    * print 'Expected status:', extectedStatus

    # Step 1: Create a post
    Given url urlBasePath + createPostData.url
    And request createPostData.requestBodyObject
    And headers createHeaders
    When method methodToCreate
    * print 'Expected Status (type):', typeof extectedStatus
    * print 'Expected Status (value):', extectedStatus
#    Then status extectedStatus
    And print 'Post Response:', response
    And def postId = response.id
    * assert responseStatus == createPostData.expectedHttpStatus

    # Calculate the updated ID (created ID minus 1)
    * def updatedId = postId - 1
    * updatePostData.requestBodyObject.id = updatedId

    # Log the calculated updated ID
    * print 'Updated ID:', updatedId

    # Step 2: Update the post using the calculated updated ID
    Given url urlBasePath + updatePostData.url + updatedId
    And request updatePostData.requestBodyObject
    And headers updateHeaders
    When method methodToUpdate
    Then status 200
    And print 'Update Response:', response
    And match response == updatePostData.expectedResponseBody
    And match responseStatus == updatePostData.expectedHttpStatus
