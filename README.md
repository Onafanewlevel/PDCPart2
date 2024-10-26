# Who Wants to Be a Millionaire

Welcome to "Who Wants to Be a Millionaire"! This is a Java-based trivia game inspired by the popular TV show, allowing players to test their knowledge by answering questions to win increasing amounts of prize money.

## Table of Contents
- [Features](#features)
- [Technologies](#technologies)
- [Setup and Installation](#setup-and-installation)
- [How to Play](#how-to-play)
- [Database Setup](#database-setup)
- [Project Structure](#project-structure)
- [Future Improvements](#future-improvements)
- [Contributing](#contributing)
- [License](#license)

---

## Features

- **Multiple Choice Questions**: Answer questions with four options (A, B, C, or D).
- **Dynamic Prize Levels**: Earn prize money based on the question level you answer correctly.
- **Countdown Timer**: Answer each question within the allotted time, or you’ll lose the chance to earn money for that question.
- **Lifelines**:
  - **50:50**: Eliminate two incorrect answers.
  - **Hint**: Get a hint for difficult questions.
- **Database-Driven**: Questions are fetched from a MySQL database for easy management and scalability.

## Technologies

- **Java**: Core programming language used to develop the game.
- **Swing**: Java GUI toolkit used to build the graphical user interface.
- **MySQL**: Database used to store questions and answers.
- **JDBC**: Java Database Connectivity (JDBC) to connect Java with MySQL.

## Setup and Installation

### Prerequisites

- **Java**: Ensure Java JDK 8 or higher is installed on your system.
- **MySQL**: Install MySQL Server and MySQL Workbench (optional) to manage the database.

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/WhoWantsToBeAMillionaire.git
   cd WhoWantsToBeAMillionaire
