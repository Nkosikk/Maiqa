Feature: Browser scenario execution

  Scenario Outline: Scenario Execute scenario steps
    Given navigate to <url> on desktop viewport
    And click the top navigation Login button
    And enter admin email
    And enter admin password
    And submit login
    When open the authenticated user menu
    And click Admin Panel from the user menu
    And open Courses from the admin sidebar
    Then verify initial course summary before creation
    And click Add Course
    And fill course title
    And fill course description
    And fill course duration
    And select course level
    And fill course price
    And fill thumbnail URL
    And fill meeting URL
    And verify published is checked by default before submit
    And submit the course creation form
    And verify total course count incremented after creation
    And verify the new course appears in the admin course list

    Examples:
      | url     | username   | password      |
      | baseUrl | adminEmail | adminPassword |
