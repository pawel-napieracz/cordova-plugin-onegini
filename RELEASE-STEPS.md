The following steps must be executed to create a release

1. Update the release notes
2. Update the version number in the `plugin.xml` file
3. Commit & push to GitHub
4. Invoke the python task to upload the Documentation to AWS S3: `invoke documentation` (Make sure that you have the following pip libraries installed: invoke 
   and boto3. Also you must have your AWS access key in your env to allow uploading the documentation/)
5. Draft a new GitHub release
9. Update the version number in the `plugin.xml` file for the next development iteration (append with `-SNAPSHOT`)