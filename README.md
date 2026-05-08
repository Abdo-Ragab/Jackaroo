# Jackaroo: A New Game Spin

## Overview
[cite_start]This repository contains a full implementation of the game "Jackaroo: A New Game Spin"[cite: 3]. [cite_start]It was developed as a multi-milestone project for the CSEN 401 Computer Programming Lab (Spring 2025) at the German University in Cairo[cite: 1, 2, 3]. 

[cite_start]The project served as a comprehensive exercise in Object-Oriented Programming (OOP) concepts [cite: 6, 518][cite_start], evolving from basic class hierarchies to a fully functional game engine and finally a graphical user interface[cite: 519, 726].

## Features
* [cite_start]**Custom Game Engine**: Handles complex board mechanics including track navigation, circular wrapping, trap cells, and Safe Zones[cite: 116, 124, 348, 582].
* [cite_start]**Card-Driven Gameplay**: Features a deck loaded from a CSV file, including 8 standard card types and 2 wild cards (Burner and Saver), each with unique movement and action logic[cite: 153, 250, 276].
* [cite_start]**Single-Player vs. CPU**: Supports 1 human player playing against 3 automated CPU opponents[cite: 325, 391, 393]. [cite_start]CPU players automatically validate and execute random actionable moves[cite: 672, 767].
* [cite_start]**JavaFX GUI**: A dynamic, fully visible graphical interface built using JavaFX[cite: 743]. [cite_start]It adheres to an MVC (Model-View-Controller) architecture to separate data processing from UI rendering[cite: 736, 737, 738, 741].
* [cite_start]**Robust Error Handling**: Utilizes custom exception hierarchies (e.g., `IllegalMovementException`, `InvalidCardException`) to validate game rules and prevent illegal moves[cite: 415, 425, 465].

## Technologies Used
* [cite_start]**Java**: Core programming language[cite: 23].
* [cite_start]**JavaFX**: Used exclusively for the Graphical User Interface (no Swing)[cite: 743].
