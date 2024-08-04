Feature: Create and Update a post using common components and read json from a file

  Background:
    * def service = karate.feature.fileName
    * def createPostData = read('CreatePost.json')
    * def updatePostData = read('UpdatePost.json')


  Scenario: Create and then Update a post
    * def createResponse = call read('classpath:com/tonyking/sample/karate/general/CommonComponent.feature') { data: #(createPostData) }
    * print 'Response is : ', createResponse.result
    * def postId = createResponse.result.id - 1
    * print 'Created Post ID:', postId

    * eval updatePostData.requestBodyObject.id = postId
    * eval updatePostData.url = updatePostData.url + postId

    * def updateResponse = call read('classpath:com/tonyking/sample/karate/general/CommonComponent.feature') { data: #(updatePostData) }
    * print 'Updated Response:', updateResponse
