# Engineering Guidelines & Agent Context — Securify

This document establishes the architectural principles, development standards, operational workflows, and constraints for human contributors and AI coding agents working on the Securify codebase.

---

## 1. Project Overview

**Securify** is an advanced, privacy-centric open-source Android security verification utility. The application performs comprehensive on-device security posture evaluations:

- **Root & Tampering Detection**: Employs deep heuristics and native REI routines to detect privilege escalation frameworks (KernelSU, Magisk, APatch, standard `su` binaries) and abnormal mounts.
- **Google Play Integrity API**: Performs end-to-end hardware attestation verification against a configured cryptographic verification backend, presenting basic, device, and strong integrity verdicts.
- **Hardware & System Diagnostics**: Audits kernel release versions (`uname -r`), supported ABI architectures, build fingerprints, and Android security patch levels.
- **Diagnostic Exporting**: Safely captures filtered system logs via the Android Storage Access Framework (SAF) without requiring root permissions.
- **Adaptive Architecture**: Fully responsive Material 3 Expressive UI supporting phones, foldables, and tablets, with dynamic color palettes, seed theming, and power-efficient AMOLED dark modes.

---

## 2. Technology Stack & Platform Specifications

| Layer / Domain | Technology | Version / Specification |
| :--- | :--- | :--- |
| **Language** | Kotlin | 2.4+ (K2 Compiler backend) |
| **UI Toolkit** | Jetpack Compose | Material 3 Expressive (BOM 2026.05+) |
| **Navigation** | AndroidX Navigation 3 | Type-safe back stack (`androidx.navigation3:navigation3-ui:1.1.2`) |
| **Dependency Injection** | Dagger Hilt | 2.59+ (`@HiltAndroidApp`, `@HiltViewModel`) |
| **Local Persistence** | AndroidX DataStore | DataStore Core with Protocol Buffers (`kotlinx.serialization.protobuf`) |
| **Networking** | Retrofit 3 + OkHttp 5 | Cryptographic nonce exchange and remote Markdown policy loading |
| **Build Toolchain** | AGP 9.3+ / Gradle 9.7+ | Gradle Version Catalog (`libs.versions.toml`) + Composite `build-logic` |
| **Security & Obfuscation** | R8 Full Mode + LSParanoid | Bytecode optimization, precise resource shrinking, string encryption |
| **Platform Target** | Android SDK | `minSdk = 26` (8.0+), `compileSdk = 37`, `targetSdk = 37` |

---

## 3. Architecture & Modular Boundaries

The project adheres to Google's official Android Architecture Guidelines and enforces strict modular boundaries via **ProjectGuard**:

```
Securify/
├── build-logic/                  # Centralized convention plugins (composite build)
├── app/                          # Application orchestrator, feature screens, and navigation
└── core/
    ├── datastore/                # User preferences, Protobuf serialization, DataStore repository
    └── designsystem/             # Design tokens, Material 3 Expressive components, Typography
```

### Modular Boundary Rules (ProjectGuard)
- **`:core:datastore`** must remain completely isolated from UI dependencies (`:core:designsystem`, Compose, etc.).
- **`:core:designsystem`** must remain completely isolated from persistence or network libraries.
- **`:app`** acts as the integrator, depending on `:core:datastore` and `:core:designsystem`.
- **MockK & Retrofit** are restricted by rule to their respective declared modules and cannot be leaked into core libraries.

---

## 4. Engineering Standards & Coding Conventions

### 4.1 Jetpack Compose & UI Guidelines
- **Strictly Compose Only**: No Android Views, ViewBinding, or XML layout files are permitted.
- **Design System Usage**: All UI screens must utilize shared design primitives from `io.github.rabehx.securify.core.designsystem.component.*`. Avoid raw, ad-hoc button or card implementations in feature screens.
- **Window Insets & Edge-to-Edge**: All layouts must consume system insets via `WindowInsets.safeDrawing`, `Modifier.imePadding()`, or `Scaffold` content padding.

### 4.2 State Management & Concurrency
- **Unidirectional Data Flow (UDF)**: ViewModels must expose immutable `StateFlow<T>` models (`uiState`).
- **Lifecycle Awareness**: Collect state within composables using `collectAsStateWithLifecycle()`.
- **Coroutines Best Practices**: Inject dispatchers via dependency injection (`CoroutineDispatcher`). Never use `GlobalScope`.

### 4.3 Internationalization & Assets
- **Zero Hardcoded Strings**: All user-visible copy must reside in `res/values/strings.xml` and be fully mirrored in the Arabic translation table (`res/values-ar/strings.xml`).
- **Vector Assets**: All icons must use optimized vector drawables (`xml`) or standardized Tabler icon symbols.

### 4.4 Security & Secrets
- **Zero Committed Secrets**: Keystores, signing passwords, and sensitive API keys must never be committed to Git.
- **Strict Intent Security**: Internal activities and services must declare `android:exported="false"`. External intents must use explicit class bindings.

---

## 5. Verification & Quality Commands

All changes must pass local validation before submitting pull requests:

```bash
# Compile and build debug APK
./gradlew assembleDebug --no-daemon

# Execute local unit test suites
./gradlew testDebugUnitTest --no-daemon

# Verify code style and formatting standards
./gradlew spotlessCheck --no-daemon

# Auto-apply official code formatting
./gradlew spotlessApply --no-daemon

# Run comprehensive Android Lint analysis
./gradlew lintDebug --no-daemon
```

---

## 6. Commit Message Protocol

All Git commits must strictly conform to the **Conventional Commits v1.0.0** specification:

```text
<type>(<optional scope>): <imperative description>
```

- `feat`: New user-facing functionality (e.g., `feat(integrity): add strong integrity verdict mapping`)
- `fix`: Bug fix (e.g., `fix(theme): prevent AMOLED black clipping on dialog corners`)
- `docs`: Documentation updates (e.g., `docs(readme): update API verification instructions`)
- `refactor`: Internal restructuring without behavior alteration (e.g., `refactor(designsystem): standardize TopAppBar actions`)
- `test`: Addition or modification of unit/integration tests
- `build`: Build script, Gradle, or dependency catalog updates
- `ci`: GitHub Actions workflow configuration changes
- `chore`: Maintenance tasks and housekeeping
