Feature: Modify the address of a subscriber
  Scenario: Modification of the address of a subscriber living in France and without an effective date
    Given a subscriber with an active address in France
    And the advisor is connected to "canal"
    When the advisor modifies the subscriber's address without effective date
    Then the modified subscriber's address is saved on all the contracts of the subscriber
    And a modification movement is created