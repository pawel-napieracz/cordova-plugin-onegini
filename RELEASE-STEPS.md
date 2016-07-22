The following steps must be executed to create a release

1. update the release notes
2. update the version number in the `plugin.xml` file
3. commit & push to Gitlab
4. Invoke the python task to upload the Documentation to AWS S3: `invoke documentation` (Make sure that you have the following pip libraries installed: invoke 
   and boto3. Also you must have your AWS access key in your env to allow uploading the documentation/)
5. create a new tag and push
6. apply changes to the public repository https://github.com/Onegini/onegini-cordova-plugin
7. commit & push to gitHub
8. create a new tag and push
9. Update the Gitlab repo version number in the `plugin.xml` file for the next development iteration (including `SNAPSHOT`)