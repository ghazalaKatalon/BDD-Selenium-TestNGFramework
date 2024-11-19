Feature: User Login
  Scenario: Successful login
    Given the user is on the login page
    When the user enters valid username and password
    And clicks the login button
    Then the user should be logged in

 