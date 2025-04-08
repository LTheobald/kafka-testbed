# Kafka Testbed

Testing Kafka using a number of technologies I use at work. These include:

* Azure DevOps for CI/CD
* AWS for hosting
* Kafka & a schema registry
* Containerised Spring Boot applications

## Single Spring Boot Application

To save myself time, I'm using a single Spring Boot service as the code base for multiple 'micro-services'. Using Spring Profiles, only one service is enabled in the deployment. This saves me time from having to create multiple microservices where 95% of the code is identical.