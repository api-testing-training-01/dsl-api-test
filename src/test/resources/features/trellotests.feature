Feature: Boards

  @deleteBoardPostCond
  Scenario: Creating a new board
    When I send a POST request to "https://api.trello.com/1/boards" with body
    """
    {
    "name": "API BOARD",
    "desc": "Testing"
    }
    """
    Then Response status code should be 200
      And Response body should match with "src/test/resources/schemas/schemaPost.json" json schema
      And Response should contain the following data
        | name          | API BOARD |
        | desc          | Testing   |

 @deleteBoardPostCond
  Scenario: Getting a board
    Given I send a POST request to "https://api.trello.com/1/boards"
     And I store response as "PostResponse"
    Then Response status code should be 200
    When I send a GET request to "https://api.trello.com/1/boards/{PostResponse.id}"
    Then Response status code should be 200
     And Response body should match with "src/test/resources/schemas/schemaGet.json" json schema

 @deleteBoardPostCond
  Scenario: Updating a board
   Given I send a POST request to "https://api.trello.com/1/boards"
   And I store response as "PostResponse"
   Then Response status code should be 200
    When I send a PUT request to "https://api.trello.com/1/boards/{PostResponse.id}" with body
    """
    {
    "name": "API BOARD EDIT",
    "desc": "Testing edit"
    }
    """
    Then Response status code should be 200
    And Response should contain the following data
     | name          | API BOARD EDIT|
     | desc          | Testing edit|

  Scenario: Deleting a board
    Given I send a POST request to "https://api.trello.com/1/boards"
    And I store response as "PostResponse"
    Then Response status code should be 200
    When I send a DELETE request to "https://api.trello.com/1/boards/{PostResponse.id}"
    Then Response status code should be 200
    And Response body should match with "src/test/resources/schemas/schemaDelete.json" json schema