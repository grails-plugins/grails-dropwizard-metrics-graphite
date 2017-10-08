package grails.plugin.dropwizard.reporters

import com.codahale.metrics.MetricFilter
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.graphite.Graphite
import com.codahale.metrics.graphite.GraphiteReporter
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext

import java.util.concurrent.TimeUnit

@Slf4j
class GraphiteReporterFactory {

    @Autowired
    MetricRegistry metricRegistry

    @Autowired
    ApplicationContext applicationContext

    @Value('${grails.dropwizard.metrics.reporterFrequency:0}')
    Integer reporterFrequency

    @Value('${grails.dropwizard.metrics.graphite-reporter.graphite-server:localhost}')
    String graphiteServer

    @Value('${grails.dropwizard.metrics.graphite-reporter.graphite-server-port:2003}')
    Integer graphiteServerPort

    @Value('${grails.dropwizard.metrics.graphite-reporter.graphite-metric-prefix:graphite.localhost}')
    String graphiteMetricPrefix

    @Value('${grails.dropwizard.metrics.graphite-reporter.graphite-metric-filter:graphiteMetricFilter}')
    String metricFilter

    /**
     * Instantiates a GraphiteReporter as configured in application.yml, to be registered as a Spring bean named 'dropwizardGraphiteReporter'
     * @return A GraphiteReporter instance
     */
    GraphiteReporter graphiteReporter() {
        MetricFilter filter = applicationContext.getBean(metricFilter) as MetricFilter
        final Graphite graphite = new Graphite(new InetSocketAddress(graphiteServer, graphiteServerPort))
        final GraphiteReporter graphiteReporter = GraphiteReporter.forRegistry(metricRegistry)
                                                                  .convertRatesTo(TimeUnit.SECONDS)
                                                                  .convertDurationsTo(TimeUnit.MILLISECONDS)
                                                                  .prefixedWith(graphiteMetricPrefix)
                                                                  .filter(filter)
                                                                  .build(graphite)
        graphiteReporter.start reporterFrequency, TimeUnit.SECONDS
        log.info 'graphiteReporter started'

        graphiteReporter
    }

    /**
     * Defines a Spring bean named graphiteMetricFilter that is the default metric filter
     * @return A MetricFilter allowing all metrics; i.e. MetricFilter.ALL
     */
    MetricFilter graphiteMetricFilter() {
        MetricFilter.ALL
    }
}
