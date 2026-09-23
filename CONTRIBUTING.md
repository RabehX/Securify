# Contributing to Securify

Thank you for your interest in contributing to Securify! We welcome bug reports, feature suggestions, documentation improvements, and code contributions.

Securify is free and open-source software licensed under the **GNU General Public License v3.0 (GPLv3)**.

---

## Ways to Contribute

- **Report Bugs**: Submit detailed issues if you experience unexpected crashes, false verdicts, or detection anomalies.
- **Suggest Features**: Open an issue describing ideas for new security checks, threat heuristics, or UX improvements.
- **Improve Documentation**: Fix typos, clarify guides, or expand localization.
- **Submit Pull Requests**: Implement bug fixes, add detection routines, or contribute approved features.

---

## Development Environment Setup

To build and run Securify locally, ensure your workstation has:

- **JDK**: Java 25 (e.g., Eclipse Temurin 25)
- **Android Studio**: Android Studio Ladybug / Meerkat or later
- **Android SDK**:
  - `compileSdk = 37`
  - `targetSdk = 37`
  - `minSdk = 26` (Android 8.0+)
- **Git**: Git 2.40+

### Clone and Build

```bash
git clone https://github.com/RabehX/Securify.git
cd Securify

# Assemble debug APK for FOSS flavor (default)
./gradlew :app:assembleFossDebug

# Assemble debug APK for Play Store flavor
./gradlew :app:assemblePlayDebug

# Run unit test suites across flavors
./gradlew :app:testFossDebugUnitTest :app:testPlayDebugUnitTest
```

---

## Architecture & Code Standards

Securify strictly follows Google's modern Android architecture recommendations:

- **Modularization**:
  - `:app` — Application orchestrator, screens, navigation host, and flavors (`foss` / `play`).
  - `:core:common` — Shared coroutine dispatchers, common utilities, and primitives.
  - `:core:datastore` — Preferences persistence (ProtoBuf + DataStore Core).
  - `:core:designsystem` — Reusable Material 3 Expressive UI components, Liquid Glass, and typography tokens.
  - `:core:network` — Isolated network layer (Retrofit 3, OkHttp 5, API interfaces).
  - `:build-logic` — Composite build with convention plugins.
- **Security Engine**: Powered by **Rei 2.0.0** native multi-domain threat detection (Root, Injection, Framework, Emulator, System Integrity).
- **Flavors**:
  - `foss` (default): Strictly open-source, zero ads, zero telemetry.
  - `play`: Integrates Google Mobile Ads (AdMob), UMP consent, and Firebase Crashlytics.
- **UI Toolkit**: 100% Jetpack Compose with Material 3 Expressive and Liquid Glass. No legacy XML views (except native ad layouts in `app/src/play`).
- **Navigation**: AndroidX Navigation 3 (`androidx.navigation3`).
- **Dependency Injection**: Dagger Hilt.
- **Architectural Guardrails**: Strict modular boundaries — UI modules must never depend on data or network modules.

---

## Code Style & Formatting

We adhere to the [official Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) and Android Compose guidelines.

- Function names: `lowerCamelCase`, except `@Composable` functions which use `UpperCamelCase`.
- Indentation: 4 spaces for Kotlin, 2 spaces for XML/YAML/TOML.
- Format verification: Run `./gradlew spotlessCheck` before submitting a PR.
- Apply auto-formatting: `./gradlew spotlessApply`.

---

## Git & Commit Guidelines

We enforce the **Conventional Commits v1.0.0** standard:

| Prefix | Description | Example |
| :--- | :--- | :--- |
| `feat:` | New user-facing feature | `feat(rei): add injection threat detection mapping` |
| `fix:` | Bug fix | `fix(ui): correct dark mode contrast on AMOLED theme` |
| `docs:` | Documentation changes | `docs: update CONTRIBUTING guide` |
| `refactor:` | Code restructuring without feature changes | `refactor(network): extract api interfaces into core:network` |
| `test:` | Adding or updating tests | `test: add unit tests for SecurifyRepository` |
| `ci:` | GitHub Actions workflow changes | `ci: add pull request validation workflow` |
| `build:` | Dependency updates or Gradle changes | `build(deps): bump hilt to 2.60.1` |
| `chore:` | Miscellaneous maintenance | `chore: update .gitignore` |

---

## Pull Request Process

1. **Check Existing Issues**: Check existing issues or open a discussion before starting major work.
2. **Branch from master**: Create a feature branch with a descriptive name (`git checkout -b feature/my-feature`).
3. **Verify Locally**:
   ```bash
   ./gradlew :app:assembleFossDebug
   ./gradlew :app:testFossDebugUnitTest
   ./gradlew :app:lintFossRelease
   ```
4. **Submit PR**: Open a PR against `master` using the PR template.
5. **UI Changes**: If your PR modifies UI, include before/after screenshots.
