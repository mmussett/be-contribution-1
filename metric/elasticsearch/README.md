# Elasticsearch Metric Store

`Elasticsearch` is an open source distributed, search and analytics engine built on top of Apache Lucene.
This implementation publishes entity data to Elasticsearch on RTC completion. 

You can go through [Elasticsearch's](https://www.elastic.co/guide/index.html) exhaustive documentation.

## Pre-requisites

* Install docker, since the bundled integration test's creates a light weight throw away container for all the tests. Existing tests were run again Docker v19.03.13.

* Install Elasticsearch version 7.10.1 and above.

## Known Issue

* Elasticsearch when used with username/password needs latest version of [common-codec](https://mvnrepository.com/artifact/commons-codec/commons-codec) (>= 1.11). For now, you can download this version and replace the bundled older version under <be.home>/lib/ext/tpcl/apache. Upcoming TIBCO BusinessEvents release will include the latest version,

## Getting Started

Assuming you have gone through all the documentation and appropriate [steps](https://github.com/tibco/be-contribution/tree/main/metric) are followed to setup the new metric store.

* Once 'Elasticsearch' is selected as the metric provider, various input fields based on the ones configured in 'metric-store.xml' are available to accept values. 

* You can use any one of the multiple auth/no auth options to connect to the elasticsearch server,
	- No Auth
	- Auth with User Name and Password
	- Auth with Token Service
	- Auth with API keys

* Start Elasticsearch server. Follow any of the options outlined [here](https://www.elastic.co/guide/en/elasticsearch/reference/current/starting-elasticsearch.html).

* Start the BE engine and validate if the configured entities are pushed into elasticsearch. 

* Once the data is published and available in Elastic, it can be viewed via any Elastic compatible visualization/dashboard tools. Most common visualization tool used with Elastic is [Kibana](https://www.elastic.co/kibana), but you can use others as well like [Grafana](https://grafana.com/docs/grafana/latest/datasources/elasticsearch/).




 




