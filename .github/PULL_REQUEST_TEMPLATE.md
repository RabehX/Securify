## Description

<!-- Provide a brief description of the problem solved, feature introduced, or security check added. -->

Closes #<!-- Issue number if applicable, e.g. Fixes #12 -->

---

## Type of Change

- [ ] 🛡️ Security Check / Heuristic (Rei 2.0 domain update)
- [ ] 🐛 Bug fix (non-breaking change fixing an issue or false verdict)
- [ ] ✨ New feature (non-breaking change adding functionality)
- [ ] 🎨 UI / Design System update (Liquid Glass, Material 3 Expressive)
- [ ] 📦 Flavor-specific change (`foss` or `play`)
- [ ] ♻️ Refactoring (code restructuring without feature alteration)
- [ ] 📝 Documentation update (README, Guides, KDoc)
- [ ] 🌐 Localization (Arabic / English translations)
- [ ] 🔧 Build / CI / Dependencies (Gradle, Version Catalog, Workflows)

---

## Verification & Quality Checklist

Before submitting this pull request, please verify that your changes comply with project standards:

- [ ] My code adheres to the architecture guidelines in [AGENTS.md](AGENTS.md) and [CONTRIBUTING.md](CONTRIBUTING.md).
- [ ] I have executed `./gradlew :app:assembleFossDebug` (and `./gradlew :app:assemblePlayDebug` if modifying Play code).
- [ ] I have executed `./gradlew testFossDebugUnitTest` and all unit tests pass without failure.
- [ ] I have verified modular boundary rules (`ProjectGuard`) — no illegal dependency leaks between `:core:datastore`, `:core:network`, and `:core:designsystem`.
- [ ] For Play flavor changes, no proprietary ad or tracking dependencies have leaked into FOSS or core modules.
- [ ] All new user-facing strings are defined in `app/src/main/res/values/strings.xml` and mirrored in `app/src/main/res/values-ar/strings.xml`.
- [ ] My commits conform to the **Conventional Commits v1.0.0** specification.

---

## Visual Changes (UI Modifications Only)

<!-- If your changes affect Compose UI screens or Liquid Glass themes, please attach before/after screenshots below. -->

| Before | After |
| :---: | :---: |
| *(Image / Screenshot)* | *(Image / Screenshot)* |
