
# Overview

How to extend the social email notification?
 
This is a sample to add new email notification when connected users post an activity

# Description 

The Email Notification feature help you avoid missing anything in your organization. These emails help you to keep track of activities and events in your Social Intranet.

There are 2 types of email notification:  notification email for each event and digest email that collects all notifications during a certain period and is sent once per day or per week.

By default eXo Platform supports almost kind of events like: New User, Connection Request, Space Invitation, Space Join Request, Mention, Comment on Activity, Like on Activity, Post on my Stream, Post in my Spaces.

In some cases, you might want to add more notification or implement customized notification based on dedicated event. In this tutorial, you will see how to add a new notification ‘Someone in My Connections posts a message’ by implementing your own eXo Social Notification plugin

# How to build 

	mvn clean install

1. Copy custom-notification-extension.war file to the ${PLF_TOMCAT}/webapps directory.

2. Copy the custom-notification-config-xxx.jar file to the ${PLF_TOMCAT}/lib directory. 

Start tomcat
