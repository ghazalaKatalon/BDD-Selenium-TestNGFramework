Feature: Login

  Scenario Outline: Login
    Given the user is on the login <page>
    When the user enters valid username and password
    And clicks the login button
    Then the user should be logged in
    
        Examples: 
        |page|
        |https://www.saucedemo.com/v1/|
