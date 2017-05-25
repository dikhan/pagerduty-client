# PagerDuty Events Client for Java [![Build Status][travis-image]][travis-url]

![][pagerduty-client-logo]

PagerDuty Events Client aims to provide a full-fledged Java client which is easy to use and integrates seamlessly
with PagerDuty Events API v2. Note that the library does not integrate with PagerDuty REST Api - it is only meant
for PagerDuty Events API v2. Please refer to the following link to see the differences between PagerDuty REST API and
Events API:

[What is the difference between PagerDuty APIs?](https://support.pagerduty.com/hc/en-us/articles/214794907-What-is-the-difference-between-PagerDuty-APIs-)

[![License][license-image]][license-url]  |
[![version][maven-version]][maven-url]    |
[![Build Status][travis-image]][travis-url]

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

### Trigger:
This will send a new 'trigger' incident to PagerDuty containing the details specified in the IncidentBuilder.
A helper IncidentBuilder is provided for the sake of simplicity to ease with the creation of trigger incidents. The
trigger event requires two mandatory parameters:
  - **routingKey**: (The GUID of one of your "Generic API" services. This is the "Integration Key" listed on a Generic
    API's service detail page.)    
  - **payload**: The payload class contains mandatory fields that are required to trigger an event. See below
    to see how to construct payload field.

More details can be provided to the incident as previously mentioned by calling the available methods offered by the
IncidentBuilder.
   
#### Payload:
The PagerDuty Events API v2 requires that every incident to contain a payload structure, though payload is only 
supported for trigger incident.  The Payload can be created similar to other trigger using a builder.
Below contains a list of mandatory fields to build a payload instance.  
  - **summary**: A brief text summary of the event, used to generate the summaries/titles of any associated alerts.
  - **source**: The unique location of the affected system, preferably a hostname or FQDN.
  - **severity**: The perceived severity of the status the event is describing with respect to the affected system.
    This can be Severity.CRTICAL, Severity.ERROR, Severity.WARNING, or Severity.INFO.
  
More details can be provided to the payload by calling the available methods offered by the Payload builder.
```
Payload payload = Payload.Builder.newBuilder()
        .setSummary("Summary of this incident")
        .setSource("testing host")
        .setSeverity(Severity.INFO)
        .build();

TriggerIncident incident = TriggerIncident.TriggerIncidentBuilder
        .newBuilder("ROUTING_KEY", payload)
        .setDedupKey("DEDUP_KEY")
        .setTimestamp(OffsetDateTime.now())
        .build();
pagerDutyEventsClient.trigger(incident);
```

### Acknowledge:
This will send a new acknowledge incident to PagerDuty based upon the 'routingKey' and 'dedupKey'
provided. Please note that PagerDuty does not support payload added to the acknowledge event, so by default,
filler context will be used to popular the payload instance.
```
AcknowledgeIncident ack = AcknowledgeIncident.AcknowledgeIncidentBuilder
        .newBuilder("ROUTING_KEY", "DEDUP_KEY")
        .build();
pagerDutyEventsClient.acknowledge(ack);
```

### Resolve:
This will send a new resolve incident to PagerDuty based upon the 'service_key' and 'incident_key'
provided. Payload is also not supported by resolve incident.
```
ResolveIncident resolve = ResolveIncident.ResolveIncidentBuilder
        .newBuilder("ROUTING_KEY", "DEDUP_KEY")
        .build();
pagerDutyEventsClient.resolve(resolve);
```

## Integration:

[PagerDuty Events Client](http://search.maven.org/#search|ga|1|dikhan) can be easily integrated in other projects by
adding the following snippet to the pom:

```
<dependency>
  <groupId>com.github.dikhan</groupId>
  <artifactId>pagerduty-client</artifactId>
  <version>2.0.2</version>
</dependency>
```

The library uses SL4J facade for logging purposes. Thus, making it fully flexible for integration with other
projects whereby a specific logging implementation is already being used (e,g: log4j, logback, etc).

Snapshots of dev versions can be found at [oss.sonatype.org](https://oss.sonatype.org/content/repositories/snapshots/com/github/dikhan/pagerduty-client/)

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


[pagerduty-client-logo]: https://d17oy1vhnax1f7.cloudfront.net/items/0Q3Q3m1W0F230F2l1P1P/PagerDuty_icon_512x512.png?v=f4f9fdf0


[license-url]: https://github.com/dikhan/pagerduty-client/blob/master/LICENSE
[license-image]: https://img.shields.io/badge/license-MIT-blue.svg?style=flat

[travis-url]: https://travis-ci.org/dikhan/pagerduty-client
[travis-image]: https://travis-ci.org/dikhan/pagerduty-client.svg?branch=master

[maven-url]: http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22pagerduty-client%22
[maven-version]: https://img.shields.io/maven-central/v/com.github.dikhan/pagerduty-client.svg?style=flat

