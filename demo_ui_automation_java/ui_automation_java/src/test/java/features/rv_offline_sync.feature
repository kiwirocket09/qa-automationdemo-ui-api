Feature: RV Module Offline Data Synchronization

  @Regression @Offline
  Scenario Outline: Verify offline reports are successfully synced to the online database
    Given the user is on the "Remote Verification" module
    And the network connection is disconnected
    When the user completes a verification report with title "<ReportTitle>" and detail "<Detail>"
    And the user signs off as "<VerifierName>" with title "<VerifierTitle>"
    And the user saves the report in offline mode
    And the network connection is restored
    Then the offline report "<ReportTitle>" should be visible in the online listing
    And the report data should be consistent with the offline entry

    Examples:
      | ReportTitle       | Detail          | VerifierName | VerifierTitle |
      | RV_Sync_Auto_001  | work evaluation | MarryAnne    | Site Manager  |