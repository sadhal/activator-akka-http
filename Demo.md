
### Creating app in openshift ###

```
oc new-app ticketfly/scala:sbt-0.13~https://github.com/sadhal/activator-akka-http.git
oc expose service activator-akka-http --path=/users
```
