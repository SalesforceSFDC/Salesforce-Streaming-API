# Salesforce Streaming API

Streaming API is used to keep external systems in sync with Salesforce and to process busoness logic in an external system in response to data changes in Salesforce.  

## Overview

Salesforce Streaming API:
 * Lets you define events and push notifications to your client app when the events occur.
 * You dont have to keep an active lookout for data changes.
 * You dont have to constantly poll Salesforce and make unnecessary API requests.
 * Interface with Streaming API through PushTopics.
 * Streaming API uses the Bayeux protocol and the CometD messaging library.

## PushTopics

* PushTopic is an sObject that contains the criteria of events that you want to listen to, such as data changes for a particular object.
* Define the criteria as a SOQL query in the PushTopic and specify the record operations to notify on (create, update, delete, and undelete).  
* In addition to event criteria, a PushTopic represents the channel that client apps subscribe to.
* A PushTopic enables us to define the object, fields, and criteriaof interest in receviing theevent notificcations for.

### Supported Topics in PushTopic Queries

PushTopic queries support all custom objects.  PushTopic queries support the following standard objects:
* 1. Account
* 2. Campaign
* 3. Case
* 4. Contact
* 5. Lead
* 6. Opportunity
* 7. Task

Standard objects supported throught a pilot program:
* 8. ContractLineItem
* 9. Entitlement
* 10. LiveChatTranscript
* 11. Quote
* 12. QuoteLineItem
* 13. ServiceContract

``` 
    // PushTopic lets you subscribe to channel to track changes on accounts whose billins city is SF>
    PushTopic pushTopic = new OushTopic();
    pushTopic.Name = 'AccountUpdate';
    pushTopic.Query = 'SELECT Id, Name, Phone FROM
        Account WHERE BillingCity=\'San Francisco\'';
    pushTopic.ApiVersion = 37.0;
    
    insert pushTopic;
```

## Developer Resources
 * [Introducing Streaming API](https://developer.salesforce.com/docs/atlas.en-us.204.0.api_streaming.meta/api_streaming/intro_stream.htm)
 * [Push Topic Object Reference](https://developer.salesforce.com/docs/atlas.en-us.204.0.api.meta/object_ref/pushtopic.htm)
    
