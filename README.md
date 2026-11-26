# Student Registration Form – DevOps Lab (Set A, Q1)

## Project layout
```
student-app/
├── pom.xml
├── Dockerfile
├── Jenkinsfile
├── .github/workflows/maven.yml
└── src/
    ├── main/java/com/example/student/App.java
    ├── main/resources/index.html      <- registration form
    └── test/java/com/example/student/AppTest.java
```

## Step 1 & 2 – Form and Maven build
```bash
mvn package
java -jar target/student-app-1.0.0.jar      # open http://localhost:8080
```
Screenshots: form in browser; `BUILD SUCCESS` + `target/student-app-1.0.0.jar`.

## Step 3 – GitHub (add, commit, push)
```bash
git init
git add .
git commit -m "Add student registration app"
git branch -M main
git remote add origin https://github.com/<user>/<repo>.git
git push -u origin main
```
Screenshot: GitHub repo page showing the committed files.

## Step 4 – Jenkins
New Item -> Pipeline -> "Pipeline script from SCM" -> Git -> repo URL -> Script Path `Jenkinsfile` -> Build Now.
Adjust tool names `Maven3` / `JDK17` in the Jenkinsfile to match Manage Jenkins -> Tools.
Screenshot: Stage View / console with `Finished: SUCCESS`.

## Step 5 – GitHub Actions
Already in `.github/workflows/maven.yml`; runs on every push.
Screenshot: Actions tab -> green run.

## Step 6 – Docker
```bash
docker build -t student-app:1.0 .
docker run -d --name student -p 8080:8080 student-app:1.0
docker images | grep student-app
docker ps
```
Open http://localhost:8080; screenshot terminal + browser.
Cleanup: `docker rm -f student`
