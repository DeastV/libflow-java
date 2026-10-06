# LibFlow — Object-Oriented Library Management System in Java

[![Language](https://img.shields.io/badge/Language-Java%2017%2B-ED8B00.svg?logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

An object-oriented library management application developed in Java. The system implements a rich domain model managing catalog inventory (Books, DVDs), user behavioral progression, dynamic loan validation rules, automated stock notifications, and binary state persistence.

Developed as part of the **Object-Oriented Programming (Programação com Objetos — PO)** course at **Instituto Superior Técnico (IST), Universidade de Lisboa**.

---

## Architectural Highlights & Design Patterns

The system adheres strictly to clean architecture and software engineering design patterns:

### 1. Strategy Pattern — Loan Rule Engine (`RuleChecker`)
Borrowing eligibility is evaluated through an extensible chain of independent rule strategies, preventing monolithic conditional branching:
* `CheckActiveUser`: Validates that the user is not suspended or penalized by unpaid overdue fines.
* `CheckCategory`: Enforces catalog restrictions (e.g. Reference works cannot be loaned).
* `CheckInventory`: Ensures available copies exist in the active collection.
* `CheckPrice`: Verifies that work value does not exceed borrowing limits for the user's current tier.
* `CheckNumberRequisitions`: Caps concurrent active loans based on user tier.
* `CheckRequestTwice`: Disallows concurrent duplicate loans of the same work by the same user.

### 2. State Pattern — User Behavioral Progression (`UserBehavior`)
User privileges dynamically evolve based on borrowing history and return punctuality:
* **Normal:** Base borrowing limits and standard return deadlines.
* **Cumpridor (Compliant):** Earned after consecutive on-time returns; grants extended loan durations and higher borrowing capacity.
* **Faltoso (Delinquent):** Triggered by overdue returns; imposes strict borrowing limits and requires consecutive timely returns to recover standing.

### 3. Observer Pattern — Availability Notifications (`Notifiable`)
An automated subscription mechanism for depleted inventory:
* Users subscribe to works that currently have zero available copies.
* When a copy is returned, state transitions trigger event notifications (`DISPONIBILIDADE`, `REQUISICAO`) queued directly into the subscriber's notification inbox.

### 4. Persistence via Serialization
* Full application state is saved and restored from binary snapshots using Java Object Serialization (`LibraryManager`), supporting seamless session restoration.

### 5. Command Pattern UI (`bci.app` & `pt.tecnico.uilib`)
* The application interface decouples UI controllers from core domain logic using hierarchical command menus, form prompts, and isolated exception handling.

---

## Project Structure

```
.
├── Makefile                # Build automation
├── LICENSE                 # MIT License
├── .gitignore              # Java build exclusions
├── README.md               # Project documentation
└── src/
    ├── bci/
    │   ├── app/            # Command menus, interaction handlers, and UI exceptions
    │   └── core/           # Domain entities, design patterns, and rule checkers
    └── pt/
        └── tecnico/uilib/  # Terminal dialog, form, and menu driver framework
```

---

## Requirements

* **JDK 17 or higher** (Tested on Java 25 LTS)
* **GNU Make**

On Ubuntu / Debian:
```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk make
```

---

## Compilation and Execution

```bash
# Compile all Java sources
make

# Run the interactive application
make run

# Clean compiled class files
make clean
```

### Optional Data Ingestion
The application supports populating initial catalog and user records from an external formatted text file at launch:
```bash
java -cp bin -Dimport=<path-to-file> bci.app.App
```

---

## Known Limitations

* **Binary Serialization Coupling:** System state persistence relies on native Java Object Serialization, requiring binary class compatibility and preventing external query inspection without deserialization.
* **In-Memory Collection Traversal:** Catalog search and user query operations evaluate through in-memory collection streams rather than persistent indexing structures.

---

## Credits

* **David Vasques** ([@DeastV](https://github.com/DeastV)), **Bruno Fontenele** ([@brunomatos2505](https://github.com/brunomatos2505))
* Collaborative group coursework developed for Programação com Objetos (PO) at Instituto Superior Técnico, Universidade de Lisboa. Terminal dialog framework (`pt/tecnico/uilib`) provided by the teaching staff.
