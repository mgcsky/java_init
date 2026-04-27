# Demo Spring Boot Docker Setup

Practical Docker setup for local development and CI-ready image building.

## Prerequisites

- Docker Desktop (or Docker Engine + Compose plugin)
- At least 4GB RAM available for Docker

## Quick start

1. Create local environment file:

   ```powershell
   copy .env.example .env
   ```

2. Build and start services:

   ```powershell
   docker compose up -d --build
   ```

3. Follow app logs:

   ```powershell
   docker compose logs -f app
   ```

4. Stop and clean up:

   ```powershell
   docker compose down
   ```

App runs at `http://localhost:8081` and health endpoint is `http://localhost:8081/actuator/health`.

## Services

- `app`: Spring Boot application (Java 21, non-root container user)
- `mysql`: MySQL 8.4 with persistent volume `mysql-data`

## Environment variables

Use `.env.example` as template. Keep secrets in `.env` only (do not commit).

Main variables:

- `APP_PORT`
- `SPRING_PROFILES_ACTIVE`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `MYSQL_PORT`
- `MYSQL_ROOT_PASSWORD`
- `MYSQL_DATABASE`
- `MYSQL_USER`
- `MYSQL_PASSWORD`

## Troubleshooting

- Port conflict:
  - Change `APP_PORT` or `MYSQL_PORT` in `.env`
  - Restart with `docker compose up -d --build`
- Database not ready yet:
  - MySQL has a healthcheck, wait 10-30 seconds then recheck logs
  - Run `docker compose logs -f mysql app`
- Wrong database credentials:
  - Verify `.env` values and ensure `SPRING_DATASOURCE_*` matches MySQL settings
  - Recreate containers with `docker compose down -v && docker compose up -d --build`

## CI/CD readiness (minimal)

- Build image in CI with BuildKit cache (faster rebuilds)
- Tag image with:
  - immutable tag: commit SHA
  - optional stable tag: semantic version or release tag
- Push to container registry (GHCR, Docker Hub, ECR, etc.)
- Add image scanning before deploy (Trivy or Grype)

Example tagging strategy:

- `demo-app:<git-sha>`
- `demo-app:<semver>`
