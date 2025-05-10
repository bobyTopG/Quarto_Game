# Quarto Game (Java)
<img width="893" alt="Screenshot 2025-05-10 at 2 58 46 PM" src="https://github.com/user-attachments/assets/25206c88-113d-4c91-bb13-2469d65e9de1" />

<img width="893" alt="Screenshot 2025-05-10 at 2 58 04 PM" src="https://github.com/user-attachments/assets/236e8c01-3198-40cc-805a-c62e92ecfb43" />

<img width="893" alt="Screenshot 2025-05-10 at 2 58 24 PM" src="https://github.com/user-attachments/assets/a6e69588-6052-41c2-a9a5-bb9adcddac3a" />


This is a Java-based implementation of **Quarto**, the abstract strategy board game. It supports local multiplayer and AI opponents using Rule-Based logic. The project uses JavaFX for the graphical interface and is designed with modular architecture for easy expansion and testing.

## 🧠 About Quarto

Quarto is a 2-player game played on a 4x4 board. There are 16 unique pieces, each with 4 different attributes:
- Tall or short
- Light or dark
- Round or square
- Solid or hollow

Players take turns choosing a piece for their opponent to place. The goal is to be the first to place a piece that completes a row, column, or diagonal of four pieces that share at least one common attribute.

## 🚀 Features

- JavaFX-based GUI
- Local multiplayer support
- Two AI strategies:
  - **Rule-Based AI** using facts and logical inference
- Real-time win detection
- Clean code structure and modular design
- Debug-friendly with clear logs and structure

## 🧠 AI Strategy Overview

### RuleBasedStrategy
- Implements a lightweight inference engine
- Defines facts (e.g., available pieces, dangerous pieces)
- Applies rules to make logical, efficient decisions

## 🗂️ Project Structure
src/
├── game/       # Core game logic (GameSession, GameRules, Board, Piece, etc.)

├── ui/         # JavaFX GUI (GameView, BoardSpaceView, PieceView)

├── ai/         # AI strategies (MiniMaxStrategy, RuleBasedStrategy)

├── rules/      # Inference system (Fact, Rule, InferenceEngine)

└── utils/      # Utility classes (enums, logging, helpers)
## 🛠️ Getting Started

### Requirements
- Java 17 or higher
- JavaFX SDK
- A Java IDE
