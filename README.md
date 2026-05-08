# Jackaroo: A New Game Spin

## Overview
This repository contains a full implementation of the game "Jackaroo: A New Game Spin". It was developed as a multi-milestone project for the CSEN 401 Computer Programming Lab (Spring 2025) at the German University in Cairo. 

The project served as a comprehensive exercise in Object-Oriented Programming (OOP) concepts, evolving from basic class hierarchies to a fully functional game engine and finally a graphical user interface.

## Features
* **Custom Game Engine**: Handles complex board mechanics including track navigation, circular wrapping, trap cells, and Safe Zones.
* **Card-Driven Gameplay**: Features a deck loaded from a CSV file, including 8 standard card types and 2 wild cards (Burner and Saver), each with unique movement and action logic.
* **Single-Player vs. CPU**: Supports 1 human player playing against 3 automated CPU opponents. CPU players automatically validate and execute random actionable moves.
* **JavaFX GUI**: A dynamic, fully visible graphical interface built using JavaFX. It adheres to an MVC (Model-View-Controller) architecture to separate data processing from UI rendering.
* **Robust Error Handling**: Utilizes custom exception hierarchies (e.g., `IllegalMovementException`, `InvalidCardException`) to validate game rules and prevent illegal moves.

## Technologies Used
* **Java**: Core programming language.
* **JavaFX**: Used exclusively for the Graphical User Interface (no Swing).
