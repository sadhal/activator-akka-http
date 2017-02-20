
### Creating app in openshift ###

```
oc new-app ticketfly/scala:sbt-0.13~https://github.com/sadhal/contacts-akka-http.git
oc expose service contacts-akka-http --path=/personer
```
