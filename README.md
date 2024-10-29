# Who Wants to Be a Millionaire | Comp603 Project Part 2

Welcome to "Who Wants to Be a Millionaire"! This is a Java-based trivia game inspired by the popular TV show, allowing players to test their knowledge by answering questions to win increasing amounts of prize money.This project is for **COMP603 PDC Assignment Part 2**.

### Group 40 Members:
- [Setefano Muller | 16924823](https://github.com/Onafanewlevel)
- [Tharuka Rodrigo | 23201288](https://github.com/tharukatr)

## Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Setup and Installation](#setup-and-installation)
- [How to Start Game](#how-to-start-game)
- [How to Play](#how-to-play)

---

## Features

- **Multiple Choice Questions**: Answer questions with four options (A, B, C, or D).
- **Countdown Timer**: Answer each question within the allotted time, or you’ll lose the game and be prompted to start again or quit.
- **Lifelines**:
  - **50:50**: Eliminate two incorrect answers.
  - **Hint**: Get a hint for difficult questions.
- **Database-Driven**: Questions are fetched from a MySQL database for easy management and scalability.
- **Game Results**: Results and Player names are fetched and deiplayed on the Start Screen. You will be able to view previous attempts and how many questions they managed to answer before the game ended.

## Technologies

- **Java**: Core programming language used to develop the game.
- **Swing**: Java GUI toolkit used to build the graphical user interface.
- **MySQL**: Database used to store questions and answers.
- **JDBC**: Java Database Connectivity (JDBC) to connect Java with MySQL with an embeded database.

## Setup and Installation

### Prerequisites

- **Java**: Ensure Java JDK 8 or higher is installed on your system.
- **MySQL**: Install MySQL Server and MySQL Workbench (optional) to manage the database.

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Onafanewlevel/PDCPart2.git
   ```

## How to start the Game
1. **Go into project directory**
    ```bash
    cd PDCPart2
    ```
2. **Build .jar file**
    ```bash
    # ANT
    ant clean jar

    # MAVEN
    maven clean package

    # GRADLE
    gradle clean build
    ```
3. **Run .jar file**

    There is an issue if you run the command in the dist folder, i.e., **java -jar "PDCPart2.jar"** as the .jar file won't be able to read the custom font file.

    Therefore, just run this command in the **project home directory**.
    ```bash
    java -jar "dist/PDCPart2.jar"
    ```

## How to Play
1. Enter your name and click the **Start Game** button
2. You will be given the first question and you will have to select from 4 options. **Only one option is correct.**
3. The game will end either if:
    - you answer incorrectly
    - you answer all questions correctly.
    - the timer runs out.
4. Your results are recorded when the game ends, **however it will not be recorded if you quit or restart the game.**
5. You will be given the option to play again when the game ends
6. The results will be displayed in the **Start Screen**
