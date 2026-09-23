# Engineering Guidelines & Agent Context — Securify

This document establishes the architectural principles, development standards, operational workflows, and constraints for human contributors and AI coding agents working on the Securify codebase.

---

## 1. Project Overview

**Securify** is an advanced, privacy-centric open-source Android security verification and hardware attestation utility. The application performs comprehensive on-device security posture evaluations:

- **Rei 2.0.0 Native Security Engine**: Employs reactive, multi-threaded security audits across five distinct threat domains:
  - **Root Detection**: KernelSU, Magisk, APatch, standard `su` binaries, and abnormal mount inspection.
  - **Injection Detection**: Frida hooks, native memory tampering, and suspicious injected libraries.
  - **Framework Detection**: Xposed, LSPosed, EdXposed, and related bytecode manipulation frameworks.
  - **Emulator Detection**: Analysis of QEMU, Genymotion, BlueStacks, and AVD environment artifacts.
  - **System Integrity**: Audits of `/system` mount permissions, SELinux enforcement, and build prop tampering.
- **Google Play Integrity API**: End-to-end cryptographic hardware attestation verification against a configured verification backend.
- **Hardware & System Diagnostics**: Audits kernel release versions (`uname -r`), supported ABI architectures, build fingerprints, and Android security patch levels.
- **Diagnostic Exporting**: Safely captures filtered system logs via the Android Storage Access Framework (SAF) without requiring root permissions.
- **Liquid Glass & Adaptive UI**: Fully responsive Material 3 Expressive UI featuring custom Liquid Glass (`backdrop`), dynamic color palettes, seed theming, and power-efficient AMOLED dark modes.
- **Dual Flavor Architecture**:
  - **`foss`** (Default): 100% free and open-source edition with zero proprietary SDKs, ads, or telemetry.
  - **`play`**: Google Play edition integrating Google Mobile Ads (AdMob), UMP privacy consent, and Firebase Crashlytics.

---

## 2. Technology Stack & Platform Specifications

| Layer / Domain | Technology | Version / Specification |
| :--- | :--- | :--- |
| **Language** | Kotlin | 2.4+ (K2 Compiler backend) |
| **UI Toolkit** | Jetpack Compose | Material 3 Expressive + Liquid Glass |
| **Navigation** | AndroidX Navigation 3 | Type-safe back stack (`androidx.navigation3`) |
| **Dependency Injection** | Dagger Hilt | 2.60+ (`@HiltAndroidApp`, `@HiltViewModel`) |
| **Local Persistence** | AndroidX DataStore | DataStore Core with Protocol Buffers (`kotlinx.serialization.protobuf`) |
| **Networking** | Retrofit 3 + OkHttp 5 | Isolated within `:core:network` |
| **Detection Engine** | Rei 2.0.0 | Native domain-driven security report engine |
| **Build Toolchain** | AGP 9.3+ / Gradle 9.7+ | Gradle Version Catalog (`libs.versions.toml`) + Composite `build-logic` |
| **Security & Obfuscation** | R8 Full Mode + LSParanoid | Bytecode optimization, precise resource shrinking, string encryption |
| **Platform Target** | Android SDK | `minSdk = 26` (8.0+), `compileSdk = 37`, `targetSdk = 37` |

---

## 3. Architecture & Modular Boundaries

The project adheres to Google's official Android Architecture Guidelines and enforces strict modular boundaries via **ProjectGuard**:

```
Securify/
├── build-logic/                  # Centralized convention plugins (composite build)
├── app/                          # Application orchestrator, screens, navigation, and flavors (foss / play)
└── core/
    ├── common/                   # Shared dispatchers, utilities, and common primitives
    ├── datastore/                # User preferences, Protobuf serialization, DataStore repository
    ├── designsystem/             # Design tokens, Material 3 Expressive components, Liquid Glass, Typography
    └── network/                  # Network configuration, Retrofit, OkHttp clients, API interfaces
```

### Modular Boundary Rules (ProjectGuard)
- **`:core:datastore`** must remain completely isolated from UI dependencies (`:core:designsystem`, Compose, etc.).
- **`:core:designsystem`** must remain completely isolated from persistence (`:core:datastore`) or network (`:core:network`) libraries.
- **`:core:network`** must not depend on UI libraries (`:core:designsystem`).
- **Ad & Telemetry Libraries** (`play-services-ads`, `user-messaging-platform`) are strictly restricted to `:app` and must never leak into core libraries or the `foss` flavor.
- **Retrofit** is restricted to `:core:network` and `:app`.
- **MockK** is restricted strictly to `:app` test suites.

---

## 4. Engineering Standards & Coding Conventions

### 4.1 Jetpack Compose & UI Guidelines
- **Strictly Compose Only**: No Android Views or XML layout files are permitted (except native ad views isolated in `app/src/play`).
- **Design System Usage**: UI screens must utilize shared design primitives from `io.github.rabehx.securify.core.designsystem.component.*`.
- **Window Insets & Edge-to-Edge**: Layouts must consume system insets via `WindowInsets.safeDrawing`, `Modifier.imePadding()`, or `Scaffold` content padding.

### 4.2 State Management & Concurrency
- **Unidirectional Data Flow (UDF)**: ViewModels must expose immutable `StateFlow<T>` models (`uiState`).
- **Lifecycle Awareness**: Collect state within composables using `collectAsStateWithLifecycle()`.
- **Coroutines Best Practices**: Inject dispatchers via dependency injection (`@IoDispatcher`, `@DefaultDispatcher`). Never use `GlobalScope`.

### 4.3 Flavor Isolation
- Never reference Play-specific classes (e.g., `AdManager`, `ConsentManager`, Firebase) within `main` source sets. All flavor-specific code must reside strictly within `app/src/play/` or `app/src/foss/`.

### 4.4 Internationalization & Assets
- **Zero Hardcoded Strings**: All user-visible copy must reside in `res/values/strings.xml` and be fully mirrored in the Arabic translation table (`res/values-ar/strings.xml`).

### 4.5 Security & Secrets
- **Zero Committed Secrets**: Keystores (`*.keystore`), signing passwords (`keystore.properties`), service account credentials (`service-account.json`), and Firebase configs (`google-services.json`) must never be committed to Git.

---

## 5. Verification & Quality Commands

All changes must pass local validation before submitting pull requests:

```bash
# Assemble debug APK for FOSS flavor
./gradlew :app:assembleFossDebug --no-daemon

# Assemble debug APK for Play flavor
./gradlew :app:assemblePlayDebug --no-daemon

# Execute unit tests across both flavors
./gradlew :app:testFossDebugUnitTest :app:testPlayDebugUnitTest --no-daemon

# Verify code style and formatting standards
./gradlew spotlessCheck --no-daemon

# Auto-apply official code formatting
./gradlew spotlessApply --no-daemon

# Run comprehensive Android Lint analysis
./gradlew :app:lintFossRelease --no-daemon
```

---

## 6. Commit Message Protocol

All Git commits must strictly conform to the **Conventional Commits v1.0.0** specification:

```text
<type>(<optional scope>): <imperative description>
```

- `feat`: New user-facing functionality (e.g., `feat(rei): add injection threat finding mapping`)
- `fix`: Bug fix (e.g., `fix(integrity): resolve cloud project number resolution`)
- `docs`: Documentation updates (e.g., `docs: update architecture overview`)
- `refactor`: Internal restructuring without behavior alteration (e.g., `refactor(network): isolate retrofit in core:network`)
- `test`: Addition or modification of unit or screenshot tests
- `build`: Build script, Gradle, or dependency catalog updates
- `ci`: GitHub Actions workflow configuration changes
- `chore`: Maintenance tasks and housekeeping
