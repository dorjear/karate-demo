Feature: Verify configuration

  Scenario: Verify configuration is loaded correctly
    * print 'Karate Config:', karate
    * print 'Config Object:', karate.config
    * print 'API Base URL:', urlBasePath
