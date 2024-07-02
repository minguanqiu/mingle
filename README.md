# Mingle

In a project, architecture is very important, especially when multiple people develop.

**Mingle** is a java web architecture, based on spring boot framework, helps build a architecture on
your project

primary goals:

* Simple and easy to use and configuration
* Unified development and maintenance process
* Spring boot powerful feature to use

#### Requirements

mingle works on spring boot 3 and java 17+

#### Concept

In mingle, there are two very important roles, Service and Action, follow introduce below:

##### Service

Alias svc, is a API. Your business logic and process place, provide for receiving and responding to
client messages

![service](docs/images/service.jpg)

##### Action

Is a module or component. Everyone logic can be a Action, provide for Service logic and reuse

![service](docs/images/action.jpg)

## Modules

There are several modules in Mingle. Here is a quick overview:

### mingle-core

Core of mingle module. Is a Service role, based on spring boot web feature.

### mingle-svc-action

Is a Action role, provide request and response design, unfied Action logic development and
maintenance process

### mingle-svc-action-rest

Action implements for restful client feature

### mingle-svc-session

Provide session authentication and authority feature on service, based on spring security

### mingle-svc-data

Provide dao layout, based on spring data jpa

### mingle-svc-redis

Provide redis layout, based on spring data redis



