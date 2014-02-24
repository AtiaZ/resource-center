**************************
HOW TO BUILD:
**************************

REQUIRES : Maven 3.0.4

Add this to your maven settings.xml

		<profile>
      <id>exo-repo</id>
			<repositories>
        <repository>
          <id>exo-public</id>
          <name>Repository for eXo PLF builds</name>
          <url>http://repository.exoplatform.org/public</url>
          <layout>default</layout>
          <snapshotPolicy>always</snapshotPolicy>
        </repository>
      </repositories>
    </profile>
    
and 
		<activeProfiles>
		  <activeProfile>exo-repo</activeProfile>
		<activeProfiles>    

then run below command:
"mvn clean install" to build 

This will download all the dependencies needed


