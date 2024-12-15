# Use Ubuntu base image
FROM ubuntu:22.04

# Avoid interactive prompts
ENV DEBIAN_FRONTEND=noninteractive

# Install Eclipse Temurin Java 23
RUN apt-get update && \
    apt-get install -y wget apt-transport-https && \
    mkdir -p /etc/apt/keyrings && \
    wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | tee /etc/apt/keyrings/adoptium.asc && \
    echo "deb [signed-by=/etc/apt/keyrings/adoptium.asc] https://packages.adoptium.net/artifactory/deb $(awk -F= '/^VERSION_CODENAME/{print$2}' /etc/os-release) main" | tee /etc/apt/sources.list.d/adoptium.list && \
    apt-get update && \
    apt-get install -y temurin-23-jdk \
    wget \
    unzip \
    xvfb \
    x11vnc \
    xterm \
    openbox \
    libgtk-3-0 \
    libgl1-mesa-glx \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables
ENV DISPLAY=:99
ENV RESOLUTION=1920x1080x24

# Create app directory and lib structure
WORKDIR /app
RUN mkdir -p /app/lib/javafx/lib

# Download and extract JavaFX SDK
RUN wget https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_linux-x64_bin-sdk.zip \
    && unzip openjfx-21.0.1_linux-x64_bin-sdk.zip \
    && cp -r javafx-sdk-21.0.1/lib/* /app/lib/javafx/lib/ \
    && rm -rf javafx-sdk-21.0.1 openjfx-21.0.1_linux-x64_bin-sdk.zip

# Download MySQL connector
RUN wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.27/mysql-connector-java-8.0.27.jar -P /app/lib/

# Copy application JAR
COPY out/artifacts/quiz_project_jar/quiz-project.jar /app/

# Set up VNC
RUN mkdir ~/.vnc && \
    x11vnc -storepasswd "password" ~/.vnc/passwd && \
    chmod 600 ~/.vnc/passwd

# Expose ports
EXPOSE 5900
EXPOSE 8191

# Create startup script
RUN echo '#!/bin/bash\n\
Xvfb $DISPLAY -screen 0 $RESOLUTION &\n\
sleep 2\n\
openbox &\n\
x11vnc -forever -usepw -display $DISPLAY &\n\
java --module-path "/app/lib/javafx/lib" \
--add-modules javafx.controls,javafx.fxml,javafx.web,javafx.swing \
-jar /app/quiz-project.jar\n\
' > /app/startup.sh && chmod +x /app/startup.sh

CMD ["/app/startup.sh"]