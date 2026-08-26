# Adventure Book

Competency Interview - Adventure Book - Pictet

An interactive adventure book application: Spring Boot (Java 21) backend + Angular frontend.

## Status

- **Objective 1 — done:** home page listing all books, with client-side search (title/author) and difficulty filter.
- Objectives 2-5: not started yet.

## Project layout

```
src/main/java/...          Spring Boot backend
src/main/resources/books/  Book JSON files, loaded from the classpath at startup
frontend/                  Angular application
```

## Backend

Requirements: Java 21, Maven.

```bash
mvn spring-boot:run
```

The API starts on **http://localhost:8081**.

- `GET /api/books` — returns all valid books (`id`, `title`, `author`, `difficulty`).

Books are loaded once at startup from `src/main/resources/books/*.json`. A book file that
fails to parse (e.g. empty/malformed) is skipped and logged as a warning rather than
crashing the application — `dragon-quest.json` in the provided sample data is empty and
is skipped this way.

## Frontend

Requirements: Node.js, npm.

```bash
cd frontend
npm install
npm start   # ng serve
```

The app starts on **http://localhost:4200** and expects the backend to be running on
`http://localhost:8081` (CORS is enabled for `http://localhost:4200` on the backend).

## Design notes

- Search and difficulty filtering are done **client-side** in Angular: the backend
  exposes a single simple `GET /api/books`, and the whole catalog is fetched once and
  filtered in-memory using signals. This keeps the API surface minimal and the UI
  instantly responsive, which is appropriate given the catalog is small; a larger
  catalog would justify moving filtering server-side via query parameters instead.
- The backend only parses `title`/`author`/`difficulty` from each book file so far
  (`sections` is ignored) — the section/option/consequence model will be added when the
  game itself is implemented (Objective 2).
