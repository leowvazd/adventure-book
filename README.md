# Adventure Book

Competency Interview - Adventure Book - Pictet

An interactive adventure book application: Spring Boot (Java 21) backend + Angular frontend.

## Project layout

```
src/main/java/...          Spring Boot backend
src/main/resources/books/  Book JSON files, loaded from the classpath at startup
frontend/                  Angular application
data/                      Local H2 database file (saved progress), created on first run
```

## Backend

Requirements: Java 21, Maven.

```bash
mvn spring-boot:run
```

The API starts on **http://localhost:8081**.

- `GET /api/books` — all valid books, summary shape (`id`, `title`, `author`, `difficulty`, `genres`).
- `GET /api/books/{id}` — one valid book, full shape including `sections` (each with its
  `options`, each option with a `gotoId` and an optional `consequence`). 404 if the id doesn't exist.
- `PUT /api/books/{id}/progress` — body `{ currentSectionId, health }`, upserts the save for
  that book. 400 if the section id doesn't exist in the book or health is outside `0..10`.
- `GET /api/books/{id}/progress` — the saved progress, or `200` with a `null` body if nothing is
  saved yet.
- `DELETE /api/books/{id}/progress` — clears the save.

Reaching a dead end during play — an authored `"type": "GAME_OVER"` section, an option pointing
at a section id that doesn't exist, or any non-`END` section with no options — doesn't crash the
game; it's handled client-side (`isGameOver` in `game.ts`) as a "Game Over" screen (Play Again /
Back to Library) instead of a proper "The End". `crystal-caverns.json` and `pirates-jade-sea.json`
both have such a section reachable from real player choices — see
`src/main/resources/books/CHANGELOG.txt` for the intentional edits made to the sample data while
building Objectives 2-4.

## Frontend

Requirements: Node.js, npm.

```bash
cd frontend
npm install
npm start   # ng serve
```

The app starts on **http://localhost:4200** and expects the backend to be running on
`http://localhost:8081`

Routes:
- `/` — home page / library.
- `/play/:id` — game screen for one book.

## Presentation

Here's a short video presentation of how the project works: **[YouTube](https://youtu.be/TW7thZsoWQ8)**
