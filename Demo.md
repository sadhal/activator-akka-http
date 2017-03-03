
### Creating app in openshift ###

Choose Add to project, Deploy imagestream.


### Test from cURL ###
```
curl -X GET http://172.30.252.240:8778/personer

curl -X POST http://172.30.252.240:8778/personer -H "Content-Type: application/json" -d '{ "firstName":"aaa","lastName":"bbb","email":"aaa@bbb.se","twitterHandle":"akka"}' -v
```
