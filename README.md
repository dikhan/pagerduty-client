# PagerDuty Events Client for Java [![Build Status][travis-image]][travis-url]

![][pagerduty-client-logo]

PagerDuty Events Client aims to provide a full flexed Java client which is easy to use and integrates seamlessly
with PagerDuty Events API. Note that the library does not integrate with PagerDuty REST Api - it is only meant
for PagerDuty Events API. Please refer to the following link to see the differences between PagerDuty REST API and
Events API:

[What is the difference between PagerDuty APIs?](https://support.pagerduty.com/hc/en-us/articles/214794907-What-is-the-difference-between-PagerDuty-APIs-)

[![License][license-image]][license-url]  |
[![version][maven-version]][maven-url]

## Getting started

PagerDutyEventsClient is really easy to create. The static method exposed with no parameters will create a new client
which internally will default the API calls to PagerDuty Events API (events.pagerduty.com). Please note that as per
PagerDUty Events documentation there is no need to use an ApiAccessKey to make calls to the events API - the service
token is sufficient to trigger/acknowledge/resolve incidents.

An example on how to create the clients is as follows:

```
PagerDutyEventsClient pagerDutyEventsClient = PagerDutyEventsClient.create();
```

The library supports the creation of three different type of incidents. For your reference, below are examples
on how to create each incident type as well as how to use PagerDutyEventsClient to perform the according operation:

- **Trigger**: This will send a new 'trigger' incident to PagerDuty containing the details specified in the IncidentBuilder.
A helper IncidentBuilder is provided for the sake of simplicity to ease with the creation of trigger incidents. The
trigger event requires two mandatory parameters:
  - service_key (The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic
    API's service detail page.)
  - description: Text that will appear in the incident's log associated with this event.
More details can be provided to the incident as previously mentioned by calling the available methods offered by the
IncidentBuilder.
```
Incident incident = Incident.IncidentBuilder
        .trigger("SERVICE_KEY", "INCIDENT DESCRIPTION")
        .client("Creacodetive - PagerDutyClient")
        .clientUrl("http://www.creacodetive.com")
        .details("This is an incident test to test PagerDutyClient")
        .build();
pagerDutyEventsClient.trigger(incident);
```

- **Acknowledge**: This will send a new acknowledge incident to PagerDuty based upon the 'serviceKey' and 'incidentKey'
provided.
```
Incident incident = Incident.IncidentBuilder.acknowledge("SERVICE_KEY", "INCIDENT_KEY");
pagerDutyEventsClient.acknowledge(incident);
```

- **Resolve**: This will send a new resolve incident to PagerDuty based upon the 'service_key' and 'incident_key' provided.
```
Incident incident = Incident.IncidentBuilder.resolve("SERVICE_KEY", "INCIDENT_KEY");
pagerDutyEventsClient.resolve(incident);
```

## Lib integration:

The library uses SL4J facade for logging purposes. Thus, making it fully flexible for integration with other
projects whereby a specific logging implementation is already being used (e,g: log4j, logback, etc).

## Contributing

- Fork it!
- Create your feature branch: git checkout -b my-new-feature
- Commit your changes: git commit -am 'Add some feature'
- Push to the branch: git push origin my-new-feature
- Submit a pull request :D

## Authors

Daniel I. Khan Ramiro - Cisco Systems

See also the list of [contributors](https://github.com/dikhan/pagerduty-client/graphs/contributors) who participated in this project.

## Acknowledgements:

- ApiService calls: http://unirest.io/java.html
- Java objects serialization: https://github.com/FasterXML/jackson
- Testing: http://www.mock-server.com/


[pagerduty-client-logo]: https://cl.ly/2s1P0L2A0734


[license-url]: https://github.com/dikhan/pagerduty-client/master/LICENSE
[license-image]: https://img.shields.io/badge/license-MIT-blue.svg?style=flat

[travis-url]: https://travis-ci.org/cisco/pagerduty-client
[travis-image]: https://img.shields.io/travis/cisco/pagerduty-client.svg?style=flat

[maven-url]: http://search.maven.org/#browse%
[maven-version]: https://img.shields.io/maven-central/v/com.cisco.pagerduty-client/pagerduty-client.svg?style=flat

