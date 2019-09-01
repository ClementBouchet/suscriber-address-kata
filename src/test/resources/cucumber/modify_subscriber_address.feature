Feature: Modify the address of a subscriber
  Scenario: Modification of the address of a subscriber living in France and without an effective date
    Given a subscriber with an active address in France
    When the advisor connected to canal modifies the subscriber's address without effective date
    Then the modified subscriber's address is saved on all the contracts of the subscriber
    And a modification movement is created