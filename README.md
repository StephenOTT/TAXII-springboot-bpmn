# TAXII-springboot
SpringBoot implementation of TAXII server

This provides a full spec compliant TAXII implementation based on SpringBoot and a workflow engine.

This library is used as the primary building block for TAXII server implementations.  
Most implementations implement customized security configurations and workflow configurations.  
These configurations typically do not require changes to core code, rather are BPMN deployments within the workflow 
engine (Camunda), and Spring Security configurations.

This TAXII server is designed to be used as a configurable microservice that can scale and configure for simple and complex processing requirements.

# BPMN Usage examples

Base processes that loaded into the engine to handle typical processing.

Additional processes can be enacted as needed for the specific implementation.

![POST collection objects](./docs/bpmn/add_collection_objects.png)

![harvester example](./docs/bpmn/harvester_example.png)

![create user](./docs/bpmn/create_user.png)

![delete user](./docs/bpmn/delete_user.png)

![email ioc](./docs/bpmn/event_process_email_ioc.png)

![create tenant](./docs/bpmn/create_tenant.png)


## Example Sub-Implementations

1. Processing Redacted Content

1. Processing Content requiring translation

1. Identification of problematic content requiring manual review before processing into taxii collections

1. Version control of Taxii Data: handling scenarios of Duplicate ID + Same Modified Dates, Duplicate ID + Different modified Dates, etc.

1. Notification of specific user access

1. Complex Harvesting and conversions: Harvesting from non-STIX source into STIX and processing into local or remote TAXII DB or TAXII HTTP source.

1. Life-cycle old STIX data requiring review or removal

1. Layering TAXII servers to manage processing load: Intake Server + Processing Server + Storage Server: All implementations of the same core springboot Taxii server, but each with their own deployments.

1. Process submitted data into third-party systems that are non-TAXII servers (servers, files, csv, file systems, RPC, HTTP, etc).


# Sample Json

Here are some examples of JSON and Mongo JSON generation.

```json
{
  "type": "tenant",
  "tenant": {
    "tenant_id": "1234567890",
    "tenant_slug": "tenant123",
    "title": "Some Title",
    "description": "some description",
    "versions": [
      "taxii-2.0"
    ],
    "max_content_length": 10485760
  },
  "_id": "a5d05629-c845-4458-a32e-2872ba3be23e",
  "created_at": ISODate("2019-01-07T03:56:04.794Z"),
  "modified_at": ISODate("2019-01-07T03:56:04.794Z")
}
```

```json
{
  "type": "tenant",
  "tenant": {
    "tenant_id": "1234567890",
    "tenant_slug": "tenant123",
    "title": "Some Title",
    "description": "some description",
    "versions": [
      "taxii-2.0"
    ],
    "max_content_length": 10485760
  },
  "_id": "a5d05629-c845-4458-a32e-2872ba3be23e",
  "created_at": "2019-01-07T03:56:04.794Z",
  "modified_at": "2019-01-07T03:56:04.794Z"
}
```

```json
{
  "type": "user",
  "tenant_id": "a5d05629-c845-4458-a32e-2872ba3be23e",
  "username": "steve",
  "_id": "f128f3b5-64b5-44fc-89b2-3a103c912bf8",
  "created_at": "2019-01-07T03:56:14.466Z",
  "modified_at": "2019-01-07T03:56:14.433Z"
}
```

```json
{
  "type": "user",
  "tenant_id": "a5d05629-c845-4458-a32e-2872ba3be23e",
  "username": "steve",
  "_id": "f128f3b5-64b5-44fc-89b2-3a103c912bf8",
  "created_at": ISODate("2019-01-07T03:56:14.466Z"),
  "modified_at": ISODate("2019-01-07T03:56:14.433Z")
}
```


# Docker Image

...


# Building a Customized Fat-Jar

...

# Security Configurations

## HTTPS

## Admin User

## Workflow Engine Admin

## Basic Auth


# STIX Redaction processing

The STIX library provides redaction support.  
Redacted STIX data could result in JSON that is non-compliant to the spec, such as missing required fields.  
The typical processing pattern for possibly redacted content is to attempt processing of each object, and in places 
where processing of the individual object 'fails', the object will be marked for manual processing and review.

Example:

![processing of redacted data](./docs/bpmn/add_collection_objects.png)

# Sharable Workflows: Actions based on STIX Data

This microservice can be used to create a processing engine for STIX Data that is consumed from a TAXII or any other STIX (or even non-STIX) data source.  
The goal of this capability is to provide "Sharable Workflows" of processes that handle actioning based on STIX data such as Cyber Observables.

Consider a Email Indicator of Compromise such as:

![email IOC](./docs/bpmn/event_process_email_ioc.png)

This process can be triggered based on logic within the TAXII engine or be a message received from another TAXII server.  

A sharable workflow can be used as a reusable template that is executable within the workflow engine.

The workflow engine can execute the bpmn and each implementor can choose their execution language for each task with the 
External Task Model, or using the script engine support within the workflow engine.

1. External Task Model: setup where tasks that require action, generate "work", which is picked up by external services 
(in any language) through REST, and the external service actions the chunk of work and returns the result(s).

1. Scripting Engine Support: setup where tasks are executed within the workflow engine using Java or a scripting engine.  
Any scripting engine that can integrate with JVM can be used, such as:
   1. <strong>Python</strong>
   1. Groovy
   1. Ruby
   1. JavaScript
   
   
# Adding Custom REST Endpoints

...


# Endpoints

The default endpoints for Taxii + administrative endpoints that are beyond the core Taxii spec.

:exclamation: endpoints are subject to change without notice at this time. 

## Discovery

### `/taxii`

## Tenants (API-Roots)

...

### GET `/taxii/tanant/:tenant_slug`

...

## Users

...

### GET `/taxii/tanant/:tenant_slug/users`

...

### GET `/taxii/tanant/:tenant_slug/users/user_id`

...

## Collections

...


### GET `/taxii/tanant/:tenant_slug/collections`

...


### GET `/taxii/tanant/:tenant_slug/collections/collection_id`

...


### GET `/taxii/tanant/:tenant_slug/collections/collection_id/objects`

...


### POST `/taxii/tanant/:tenant_slug/collections/collection_id/objects`

...


## Status

...


### GET `/taxii/tanant/:tenant_slug/status/:statusId`

...


### PUT /taxii/tanant/:tenant_slug/status/:statusId/operations

An adminstrative endpoint.  Subject to change without notice.  Not part of the Taxii Spec.

...

Query Params:

All three params are required.

1. `operator`: `add`, `subtract`
1. `property`: 
    1. `up_success_count_down_failure_count`
    1. `up_success_count_down_pending_count`
    1. `up_pending_count_down_failure_count`
    1. `up_failure_count_down_pending_count`
    1. `failure_count`
    1. `pending_count`
    1. `successes`
    1. `failures`
    1. `pendings`
    1. `failure_count`
    1. `pending_count`
    1. `failures`
    1. `pending`
    
1. `value`: count properties require Number >= 1.  Internally converted to Long / int64.  non-count properties are converted to String and will enforce UUID formats.
