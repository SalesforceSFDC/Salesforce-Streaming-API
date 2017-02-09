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

SOQL query:
```SQL
SELECT <comma-separated list of fields> FROM <Salesforce object> WHERE <filter criteria>
```

SOQL query example:
```SQL
SELECT Id, Name, Phone FROM Account WHERE BillingCity='San Francisco' OR BillingCity='New York'
```

The following requirements apply to PushTopic queries:
 * The SELECT statement's field list must include Id.
 * Only one object per query is allowed.
 * The object must be valid for the specified API version.

As of API version 37.0, Salesforce stores events that match PushTopic queries, even if no one is subscribed to the PushTopic. The events are stored for 24 hours, and you can retrieve them at any time during that window. As of API version 37.0, each event notification contains a field called replayId.  

Similar to replaying a video, Streaming API replays the event notifications that were sent by using the replayId field. The value of the replayId field is a number that identifies the event in the stream. The replay ID is unique for the org and channel. When you replay an event, you’re retrieving a stored event from a location in the stored stream. You can either retrieve a stream of events starting after the event specified by a replay ID, or you can retrieve all stored events. Here’s a summary of the replay options we can specify when subscribing to a channel.

The sequence of events when using Streaming API is as follows:
 * Create a PushTopic based on a SOQL query. This defines the channel.
 * Clients subscribe to the channel.
 * A record is created, updated, deleted, or undeleted (an event occurs). The changes to that record are evaluated.
 * If the record changes match the criteria of the PushTopic query, a notification is generated by the server and received by the subscribed clients.

### Push Technology
Push technology, also called the publish/subscribe model, transfers information that is initiated from a server to the client. This type of communication is the opposite of pull technology in which a request for information is made from a client to the server.

### Bayeux Protocol, CometD, and Long Polling
Streaming API uses the Bayeux protocol and CometD for long polling.

### Streaming API Terms
Learn about terms used for Streaming API.

### How the Client Connects
Streaming API uses the HTTP/1.1 request-response model and the Bayeux protocol (CometD implementation). A Bayeux client connects to Streaming API in multiple stages.

### Message Reliability
As of API version 37.0, Streaming API provides reliable message delivery by enabling you to replay past events. In API version 36.0 and earlier, clients might not receive all messages in some situations.

### Message Durability
Salesforce stores events for 24 hours, so you can retrieve stored events during that retention window. The Streaming API event framework decouples event producers from event consumers. A subscriber can retrieve events at any time and isn’t restricted to listening to events at the time they’re sent.

## Developer Resources
 * [Introducing Streaming API](https://developer.salesforce.com/docs/atlas.en-us.204.0.api_streaming.meta/api_streaming/intro_stream.htm)
 * [Push Topic Object Reference](https://developer.salesforce.com/docs/atlas.en-us.204.0.api.meta/object_ref/pushtopic.htm)
    
