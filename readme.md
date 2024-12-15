# Quiz Project

Quizzy, a JavaFX-based realtime quiz application that allows users to play quizzes in single-player or multiplayer mode.

## Prerequisites

- Java 21 or higher
- Maven 3.6 or higher

## Running the Application

### Manual Setup (without Docker)

1. Build the project (it's already built in the repository, so you can skip this step):
```bash
mvn clean package
```

2. Run the application:
```bash
java --module-path ".\lib\javafx\lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing -jar target/quiz-project-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

### Using Docker

1. Build the Docker image:
```bash
docker build -t quiz-app .
```

2. Run the container:
```bash
docker run -p 5900:5900 -p 8191:8191 quiz-app
```

3. Connect to the application:
   - Install a VNC viewer (like RealVNC Viewer, TightVNC, or VNC Viewer)
   - Connect to `localhost:5900`
   - When prompted, enter the password: `password`
   - The quiz application should now be visible through the VNC viewer

Note: The Docker container runs a VNC server because JavaFX requires a display. The VNC password is set to **"password"** by default - you can change this in the Dockerfile for production use.

Troubleshooting:
- If you can't connect, ensure ports 5900 and 8191 are not in use
- To see container logs: `docker logs <container-id>`
- To stop the container: `docker stop <container-id>`
- To remove the container: `docker rm <container-id>`

## Application Features

- Single-player quiz mode
- Multiplayer quiz mode
- Real-time scoring
- User authentication
- Leaderboard system

## Project Structure

```
quiz-project/
├── src/
│   ├── main/
│   │   ├── java/          # Source code
│   │   └── resources/     # Application resources and FXML
│   └── test/             # Test files
├── pom.xml              # Maven configuration
└── Dockerfile          # Docker configuration
```