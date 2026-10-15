# Project Instructions

## Project Overview

Java SDK for the CentralNic Reseller (fka RRPProxy) backend API, successor to the deprecated
Hexonet Java SDK, published to Maven Central. Node is present only for the release tooling,
never for product code.

- **Language / runtime:** Java, built with Maven (`maven.compiler.release` 17 in `pom.xml`)
- **Coordinates:** `io.github.centralnicgroup-opensource:java-sdk`, package
  `com.centralnicreseller.apiconnector`
- **Default branch:** `master`

## Architecture

- **The code targets the Java 17 API, whatever JDK runs the build.** `pom.xml` sets
  `maven.compiler.release=17`, so never use an API added after 17 — the devcontainer runs
  JDK 25.
- **Every build plugin is declared inside the `dev` profile only.** CI and the release both
  build with `-Pdev`; a plain `mvn` run uses Maven's default plugin versions.
- **`getVersion()` in `APIClient.java` and the `pom.xml` version are written by the
  release** — do not edit them by hand.
- **`target/apidocs` and `target/site` are committed release output** (un-ignored in
  `.gitignore`, listed as release assets) — regenerate, never hand-edit.

## Coding Standards

- **Style:** Checkstyle Sun conventions via super-linter (`VALIDATE_JAVA`), config
  `.github/linters/sun_checks.xml`.
- CI lints only changed files under `src/main/**/*.java` — test code and untouched files are
  not checked, so do not read a clean run as a clean codebase.

## Testing

- **Framework:** JUnit 4 (`junit:junit` 4.13.2), tests in
  `src/test/java/com/centralnicreseller/apiconnector/*Test.java`.
- **No real API calls in unit tests.** `APIClientTest` serves canned `[RESPONSE]` bodies
  from a local `com.sun.net.httpserver.HttpServer` on `127.0.0.1` (`TestServer`) and points
  the client at it with `setURL(...)`.
- `src/test/java/com/centralnicreseller/apiconnector/App.java` is the demo, not a test — it
  calls the live OT&E API with `CNR_TEST_USER` / `CNR_TEST_PASSWORD`.

### Running tests

```sh
mvn -B clean site package -Pdev   # what the shared java-sdk-test.yml runs
pnpm run test-demo                # demo App against OT&E — needs credentials
```

## Build, CI & Policies

- **CI** delegates to
  [rtldev-middleware-shareable-workflows](https://github.com/centralnicgroup-opensource/rtldev-middleware-shareable-workflows)
  (`java-sdk-test.yml` on pull requests, `java-sdk-release.yml` on push to `master`). Do not
  reimplement them in this repository's workflow stubs.
- **CI JDK matrix** comes from the `RTLDEV_MW_CI_JAVA_MATRIX` organisation variable, not from
  this repository.
- **Releases:** semantic-release (`.releaserc.json`) deploys to Maven Central through
  `maven-settings.xml` with `-Pdev`, writes the changelog to `HISTORY.md` and commits it back
  to `master`.
- **Lockfiles:** `pnpm-lock.yaml` is committed deliberately; `package-lock.json` is
  git-ignored — pnpm only.
- **Supply chain:** `minimumReleaseAge: 1440` in `pnpm-workspace.yaml` is enforced
  workspace-wide — wait out a red build rather than lowering it.
- **Devcontainer:** the frame is in `.devcontainer/`; the shared behaviour comes from the
  `devbase` devcontainer Feature (`devbase:2`), JDK 25 and Maven from the `java` Feature.
  Never fork devbase's scripts here.
- **Runtime version policy:** Java target from `maven.compiler.release` in `pom.xml`; Node
  `engines` and `devEngines.packageManager` (pnpm `^11.0.0`) in `package.json` cover the
  release tooling.

## Git Conventions

- **Commit messages:** Conventional Commits with **mandatory scope** —
  `<type>(<scope>): <summary>`. Never append a `Co-Authored-By:` trailer.
- **Commit type selection:** `fix`/`feat` are reserved for source changes — they trigger a
  release. Everything else uses a non-releasing type: `ci` (workflows, devcontainer),
  `build` (build tooling), `chore`, `docs`, `test`, `refactor`.
- **Breaking changes:** add `BREAKING CHANGE: <summary>` to the commit body after a blank
  line, and document the consumer upgrade path in the same change.
- **Branch creation:** before creating a branch, and again after any PR merges,
  run `git checkout master && git pull --ff-only` first — it brings in the
  merged commits and their release tags. Never branch from a stale local
  `master`, from `origin/master` (it becomes the new branch's upstream) or from
  another feature branch.
- **Branch naming:** prefix with the Jira issue ID — `RSRMID-1234-short-description`.
- **Pull requests:** include the Jira issue link in the description; after opening, add the
  PR URL as a comment on the Jira issue.
- **Merging:** rebase-merge (`gh pr merge --rebase`). Squash merges are disabled at the
  repository level.

## Atlassian / JIRA

Work is tracked in **Jira Cloud**, project `RSRMID` — not GitHub Issues. Load the
`jira-conventions` skill before creating, editing or transitioning an issue: it carries the
required fields, this repository's title prefix and component, and the worklog rule for Done and Cancelled.

Two of its rules apply whether or not the skill is loaded:

- Write every Jira issue key as a link to the issue, never bare:
  `[RSRMID-1234](https://centralnic.atlassian.net/browse/RSRMID-1234)`.
  Branch names and commit messages keep the bare key.
- Once an issue's last pull request has merged and its worklog is in, close it
  without asking if nothing is left — the skill lists what counts.

## Do NOT

- Add dependencies without explicit request
- Add `Co-Authored-By:` trailers to commit messages
- Fork the `devbase` devcontainer Feature's scripts into this repository
- Hand-edit `HISTORY.md`, the `getVersion()` string, the `pom.xml` version, `target/apidocs`
  or `target/site` — the release owns them
- Commit `.env` or display its contents — it is git-ignored for local environment variables
  such as credentials
