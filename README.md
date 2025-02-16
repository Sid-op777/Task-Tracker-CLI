# Task Tracker CLI Tool

## Overview
Task Tracker is a command-line tool for managing tasks efficiently. This project is built using Java and Maven, and it allows users to track their tasks seamlessly.

## Installation

### Prerequisites
Ensure you have the following installed on your system:
- [Java JDK 11+](https://www.oracle.com/java/technologies/javase-downloads.html)
- [Apache Maven](https://maven.apache.org/download.cgi)

### Building the Project
1. Clone the repository:
   ```sh
   git clone <repository-url>
   cd TaskTracker
   ```
2. Build the project using Maven:
   ```sh
   mvn clean package
   ```
   This will generate a `.jar` file inside the `target/` directory.

## Running the CLI Tool
To run the CLI tool, execute the following command from the project directory:
```sh
java -jar target/TaskTracker-1.0-SNAPSHOT.jar <arguments>
```

## Creating a Shortcut Script
To simplify execution, you can create a script.

### Windows (Batch File)
1. Create a file named `tcli.bat` in the project root.
2. Add the following content:
   ```bat
   @echo off
   cd /d "%~dp0"
   java -jar "target/TaskTracker-1.0-SNAPSHOT.jar" %*
   ```
3. Now, you can run the tool with:
   ```sh
   tcli <arguments>
   ```

### Linux/macOS (Shell Script)
1. Create a file named `tcli` in the project root.
2. Add the following content:
   ```sh
   #!/bin/bash
   cd "$(dirname "$0")"
   java -jar "target/TaskTracker-1.0-SNAPSHOT.jar" "$@"
   ```
3. Make the script executable:
   ```sh
   chmod +x tcli
   ```
4. Now, you can run the tool with:
   ```sh
   ./tcli <arguments>
   ```

### Making It Globally Accessible
To run the tool from any directory:
1. Add the project directory to the system `PATH`:
    - On **Windows**:
        - Open *System Properties* → *Advanced* → *Environment Variables*.
        - Under *System variables*, find `Path` and edit it.
        - Add the absolute path of the project root.
    - On **Linux/macOS**:
      ```sh
      echo 'export PATH="$HOME/path-to-project:$PATH"' >> ~/.bashrc
      source ~/.bashrc
      ```
2. Now, you can run the tool from anywhere using:
   ```sh
   tcli <arguments>
   ```
Note: I didn't test the Linux/macOS script works or not so figure that out on your own🤠
