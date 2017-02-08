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

### Example 
```Apex
    // PushTopic lets you subscribe to channel to track changes on accounts whose billins city is SF.
    PushTopic pushTopic = new OushTopic();
    pushTopic.Name = 'AccountUpdate';
    pushTopic.Query = 'SELECT Id, Name, Phone FROM
        Account WHERE BillingCity=\'San Francisco\'';
    pushTopic.ApiVersion = 37.0;
    
    insert pushTopic;
```
At the minimum, define the PushTopic name, query and API version.  Use the default values for the remaining properties.  By default, the fields in the SELECT statement field list and WHERE clause are the ones that trigger notifications.  Notifications are sent only for the records that match the criteria in the WHERE clause.  To change whihch fields trigger notifications, set pushTopic.NotifyForFields to one on these values:

NotifyForFields Value | Description
------------ | -------------
All | Notifications are generated for all record field changes, provided the evaluated records match the criteria specfied in the WHERE clause.
Referenced (Default) | Changes to fields referenced in the SELECT and WHERE clauses are evaluated.  Notifications are generated for the evaluated records only if they match the criteria specified in the WHERE clause.
Select | Changes to fields referenced in the SELECT clause are evaluated.  Notifications are generated for the evaluated records only if they match the criteria specified in the WHERE clause.
Where | Changes to the fields referenced in the WHERE clause are evaluated.  Notifications are generated for the evaluated records only if they match the criteria specified in the WHERE clause.

To set notification preference explicitly:
```java
pushTopic.NotifyForOperationCreate = true;
pushTopic.NotifyForOperationUpdate = true;
pushTopic.NotifyForOperationUndelete = true;
pushTopic.NotfiyForOperationDelete = true;
```

```json
{
    "clientId": "3e4gr556"
    "data": {
        "event": {
            "createdDate": "2016-09-16T19:45:27.454Z",
            "replayId": 1,
            "type": "created"
      },
     "sobject": {
        "Phone": "(415) 555-1212",
        "Id": "001D000000KneakIAB",
        "Name": "Blackbeard"
      }
  },
  "channel": "/topic/AccountUpdates"
}

```

## Developer Resources
 * [Introducing Streaming API](https://developer.salesforce.com/docs/atlas.en-us.204.0.api_streaming.meta/api_streaming/intro_stream.htm)
 * [Push Topic Object Reference](https://developer.salesforce.com/docs/atlas.en-us.204.0.api.meta/object_ref/pushtopic.htm)
    
![Logo](/Users/vukdukic/Desktop/Screen\Shot\2017-02-07\at\8.50.52\PM.png)
Format: ![Text](url)
