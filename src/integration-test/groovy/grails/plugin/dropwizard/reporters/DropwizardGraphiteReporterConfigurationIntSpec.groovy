package grails.plugin.dropwizard.reporters

import grails.core.GrailsApplication
import grails.test.mixin.integration.Integration
import spock.lang.Specification

@Integration
class DropwizardGraphiteReporterConfigurationIntSpec extends Specification {

    static final String CONSOLE_REPORTER_CONFIG = 'grails.dropwizard.metrics.console-reporter'
    static final String CSV_REPORTER_CONFIG = 'grails.dropwizard.metrics.csv-reporter'
    static final String GRAPHITE_REPORTER_CONFIG = 'grails.dropwizard.metrics.graphite-reporter'
    static final String SLF4_REPORTER_CONFIG = 'grails.dropwizard.metrics.slf4j-reporter'

    GrailsApplication grailsApplication

    void "verify Slf4jReporter not available by default"() {
        expect:
            grailsApplication.config[SLF4_REPORTER_CONFIG]
            !grailsApplication.mainContext.containsBean('dropwizardSlf4jReporter')
    }

    void "verify ConsoleReporter not available by default"() {
        expect:
            grailsApplication.config[CONSOLE_REPORTER_CONFIG]
            !grailsApplication.mainContext.containsBean('dropwizardConsoleReporter')
    }

    void "verify CsvReporter not available by default"() {
        expect:
            grailsApplication.config[CSV_REPORTER_CONFIG]
            !grailsApplication.mainContext.containsBean('dropwizardCsvReporter')
    }

    void "verify GraphiteReporter available by default"() {
        expect:
            grailsApplication.config[GRAPHITE_REPORTER_CONFIG]
            grailsApplication.mainContext.containsBean('dropwizardGraphiteReporter')
    }
}
