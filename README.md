# Infrastructure Engineering PT - Group F - Weather App Backend

[![](https://img.shields.io/github/license/Infrastructure-Engineering-PT-Group-F/backend?style=for-the-badge)](LICENSE.md)
[![](https://img.shields.io/github/actions/workflow/status/Infrastructure-Engineering-PT-Group-F/backend/verify.yml?style=for-the-badge)](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/actions/workflows/verify.yml)
[![](https://api.scorecard.dev/projects/github.com/Infrastructure-Engineering-PT-Group-F/backend/badge?style=for-the-badge)](https://scorecard.dev/viewer/?uri=github.com/Infrastructure-Engineering-PT-Group-F/backend)

The backend of Group F's weather application for the Infrastructure Engineering (PT) course at the Hochschule Burgenland.

It is based on the public reference implementation by [muhlba91](https://github.com/muhlba91/hochschule-burgenland-bswe-ws2024-2at-backend), adapted for this project.

---

## API Specification

The OpenAPI specification can be found in [docs/openapi.yaml](./docs/openapi.yaml).

## Authentication / User Selection

The service does not provide authentication and users are selected by specifying their identifier (UUID) in the request URL.

---

## Configuration

See [src/main/resources/application.yaml](./src/main/resources/application.yaml) for all available and default configuration options.
To run the service successfully, you need to provide the following environment variables:

- `AVWX_API_KEY`: The API key for the [Aviation Weather Rest API](https://avwx.rest/) in the format `Token avwx-api-key`. The value will be used in the `Authorization` header when calling the API.

### Persistence profiles

The active datasource is selected through the `SPRING_PROFILES_ACTIVE` environment variable. There is no datasource configured in the default profile:

| Profile | Datasource | Use case |
|---------|------------|----------|
| `local`       | In-memory H2 | Local development without an external database |
| `test`        | In-memory H2 | Test suite (activated automatically by Gradle) |
| `external-pg` | External PostgreSQL | Deployment and local PostgreSQL testing |

The `external-pg` profile reads its connection parameters from the environment, so no credentials are baked into the image:

- `SPRING_DATASOURCE_URL` (e.g. `jdbc:postgresql://host:5432/weather`)
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

Hibernate runs with `ddl-auto=validate` in every profile, therefore the schema is owned exclusively by the Flyway migrations in [src/main/resources/db/migration](./src/main/resources/db/migration), which are written to apply unchanged on both H2 and PostgreSQL.

---

## Development

The service is implemented in Java using the Spring Boot framework and Gradle as the build tool.

### Code Quality

The code quality is ensured by the following tools:

- [Checkstyle](https://checkstyle.org/) for code style checking
- [PMD](https://pmd.github.io/) for static code analysis
- [JaCoCo](https://www.eclemma.org/jacoco/) for code coverage
- [PiTest](https://pitest.org/) for mutation testing
- [YAMLLint](https://yamllint.readthedocs.io/) for YAML linting
- [Spectral](https://stoplight.io/open-source/spectral/) for OpenAPI linting
- [Helm Lint](https://helm.sh/docs/helm/helm_lint/) for Helm chart linting
- [commitlint](https://commitlint.js.org/) for commit message linting
- [CycloneDX](https://cyclonedx.org/) for software bill of materials generation
- [Grype](https://github.com/anchore/grype) for software bill of materials scanning
- [CodeQL](https://codeql.github.com/) for code scanning
- [OpenSSF Scorecard](https://scorecard.dev/) for supply-chain security rating
- [Renovate](https://www.whitesourcesoftware.com/free-developer-tools/renovate/) for dependency updates

### Testing

To run the tests, run the following command:

```shell
# unit tests and jacoco reporting
./gradlew test jacocoTestReport jacocoTestCoverageVerification

# pitest
./gradlew pitest
```

### Linting

To run the linting checks, run the following command:

```shell
# checkstyle
./gradlew checkstyleMain checkstyleTest

# pmd
./gradlew pmdMain pmdTest

# yamllint
yamllint .

# spectral
spectral lint 'docs/**/*.yaml' --fail-severity info
spectral lint 'docs/**/*.yml' --fail-severity info

# helm lint
helm lint charts/*
```

### Software Bill of Materials

To generate the software bill of materials, run the following command:

```shell
./gradlew cycloneDxBom
grype sbom:build/reports/sbom.json
```

### Running

To run the service against in-memory H2, run the following command:

```shell
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

#### Running against a local PostgreSQL

A [compose.yaml](./compose.yaml) provides a throwaway PostgreSQL for local testing. Start the database and run the app against it with the `external-pg` profile:

```shell
# database only — run the app from the IDE/Gradle
docker compose up postgres

SPRING_PROFILES_ACTIVE=external-pg \
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/weather \
SPRING_DATASOURCE_USERNAME=weather \
SPRING_DATASOURCE_PASSWORD=weather \
AVWX_API_KEY="Token <your-key>" \
  ./gradlew bootRun
```

To run the whole stack (database + application) in containers `./gradlew bootJar` is required first, so the image can pick up the built jar:

```shell
./gradlew bootJar
docker compose --profile app up --build
```

### Building

To build the service, run the following command:

```shell
./gradlew bootJar
```

### Docker

To build the Docker image, run the following command:

```shell
./gradlew bootJar
docker build -t infrastructure-engineering-pt-group-f-backend .
docker run -p 8080:8080 infrastructure-engineering-pt-group-f-backend
```

### Commit Message

Commit messages must adhere to the [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) specification and reference a GitHub issue, e.g. `feat: #42 add feature`. See [CONTRIBUTING.md](./CONTRIBUTING.md) for the full rules.

The rules are enforced in CI by [commitlint](https://commitlint.js.org/) (`commitlint.config.js`). To get the same checks locally before pushing, enable the tracked commit-msg hook once per clone:

```shell
git config core.hooksPath .githooks
```

## GitHub Actions

The GitHub Actions workflows ensure that all code quality checks pass and that the code is deployable:

- **verify.yml** — runs test, lint, SBOM, and build jobs on every push and pull request, including a multi-platform container image build (without publishing). Commit messages are checked by the commitlint workflow; CodeQL, dependency review, and the OpenSSF Scorecard provide code and supply-chain security scanning.
- **release.yml** — triggered on every push to `main`; runs [release-please](https://github.com/googleapis/release-please) to manage the changelog and create GitHub releases. When a release is created it publishes the container image to `ghcr.io/infrastructure-engineering-pt-group-f/backend` and the Helm chart as an OCI artifact to `oci://ghcr.io/infrastructure-engineering-pt-group-f/charts`.

### Container image tags

| Tag | Description |
|-----|-------------|
| `v<semver>` (e.g. `v1.2.3`) | Exact release version managed by release-please |
| `<short-sha>` (e.g. `a1b2c3d`) | Short commit SHA of the triggering commit |
| `latest` | Always points to the most recent release on `main` |

---

## Kubernetes Deployment

On each release the chart is packaged and pushed as an OCI artifact to GHCR `ghcr.io/infrastructure-engineering-pt-group-f/charts`, version-aligned with the application release:

```
oci://ghcr.io/infrastructure-engineering-pt-group-f/charts/weather-app-backend
```

The following example shows an example deployment using the provided [Helm chart](./charts/weather-app-backend/).

```shell
# set the AVWX API key
export AVWX_API_KEY="<your-avwx-api-key>"

# create the helm values file
cat <<EOF > weather-app-backend-values.yaml
---
apiKeys:
  avwx: "Token ${AVWX_API_KEY}"
EOF

# install weather-app-backend from the OCI registry
helm install weather-app-backend \
  oci://ghcr.io/infrastructure-engineering-pt-group-f/charts/weather-app-backend \
  --version <release-version> -f weather-app-backend-values.yaml

# or from a local checkout
helm install weather-app-backend ./charts/weather-app-backend -f weather-app-backend-values.yaml
```

### External PostgreSQL Secret Contract

When `spring.profile` is set to `external-pg`, the backend requires an existing
Kubernetes Secret in the same namespace as the backend Deployment.

Use this tenant-scoped values contract:

~~~yaml
spring:
  profile: external-pg

database:
  existingSecretName: weather-app-backend-db
  urlKey: url
  usernameKey: username
  passwordKey: password
~~~

The referenced Secret must provide all three required keys:

- `url` maps to `SPRING_DATASOURCE_URL`
- `username` maps to `SPRING_DATASOURCE_USERNAME`
- `password` maps to `SPRING_DATASOURCE_PASSWORD`

The chart references these values only through Kubernetes `secretKeyRef`
entries. It does not create a database Secret and no database credential may be
stored in Helm values, Git, logs, screenshots, or documentation.

For the current chart behavior, the `external-pg` profile always expects an
existing Secret through `database.existingSecretName` and the configured key
names. Do not use `database.useExistingSecret` as an opt-out for this profile.

The tenant database Secret must be delivered through the approved external
secret-management process. The end-to-end runtime validation is tracked by
[backend #28](https://github.com/Infrastructure-Engineering-PT-Group-F/backend/issues/28)
and depends on database credential delivery in
[gitops #42](https://github.com/Infrastructure-Engineering-PT-Group-F/gitops/issues/42)
and tenant provisioning in
[gitops #39](https://github.com/Infrastructure-Engineering-PT-Group-F/gitops/issues/39).

The backend image is published publicly, so no image pull secret is required; the chart's `imagePullSecrets` value remains available for private forks.

In [deploy/minikube.sh](./deploy/minikube.sh), you can find an example script to deploy (and access) the service to Minikube.

---

## Notes

- This project is adapted from the public [reference implementation](https://github.com/muhlba91/hochschule-burgenland-bswe-ws2024-2at-backend); unit test generation in the reference was supported by GitHub Copilot (Anthropic Claude 3.5 Sonnet).
- The Java package structure (`io.muehlbachler.bswe`) is kept from the reference implementation; the original license and attribution are preserved in [LICENSE.md](./LICENSE.md).
- The conform commit linter from the reference was removed; commitlint covers commit-message linting. Release automation and image publishing are handled by the release-please workflow.
