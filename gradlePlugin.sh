rm -rf repo
cd ./plugin
gradle clean jar install uploadArchives
cd ..
