FROM vbehar/sbt-openshift

ENV MYAPP_HOME /opt/myapp

WORKDIR ${MYAPP_HOME}

# SBT required files for downloading dependencies
COPY build.sbt ${MYAPP_HOME}/
COPY project/build.properties project/plugins.sbt ${MYAPP_HOME}/project/
RUN [ "sbt", "update" ]

# Source code, for building the fat JAR file
COPY src ${MYAPP_HOME}/src
RUN [ "sbt", "assembly" ]

CMD [ "java", "-jar", "${MYAPP_HOME}/target/scala-2.11/myapp.jar" ]
