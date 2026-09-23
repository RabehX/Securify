## Description

<!-- Provide a brief description of the problem solved or the feature introduced. -->

Closes #<!-- Issue number if applicable, e.g. Fixes #12 -->

---

## Type of Change

- [ ] 🐛 Bug fix (non-breaking change fixing an issue)
- [ ] ✨ New feature (non-breaking change adding functionality)
- [ ] ♻️ Refactoring (code restructuring without feature alteration)
- [ ] 🎨 UI / Design System update (Material 3 Expressive changes)
- [ ] 📝 Documentation update (README, Guides, KDoc)
- [ ] 🌐 Localization (translations update or addition)
- [ ] 🔧 Build / CI / Dependencies (Gradle, Version Catalog, Workflows)

---

## Verification & Quality Checklist

Before submitting this pull request, please verify that your changes comply with the project standards:

- [ ] My code adheres to the coding standards documented in [AGENTS.md](AGENTS.md) and [CONTRIBUTING.md](CONTRIBUTING.md).
- [ ] I have executed `./gradlew spotlessCheck` (or `./gradlew spotlessApply`) and formatting passes without error.
- [ ] I have executed `./gradlew testDebugUnitTest` and all existing/new unit tests pass.
- [ ] I have verified modular boundary rules — no illegal dependency leaks between `:core:datastore`, `:core:network`, and `:core:designsystem`.
- [ ] All new user-facing strings are defined in `app/src/main/res/values/strings.xml` and mirrored in `app/src/main/res/values-ar/strings.xml`.
- [ ] My commits conform to the **Conventional Commits v1.0.0** specification.

---

## Visual Changes (UI Modifications Only)

<!-- If your changes affect Compose UI screens or themes, please attach screenshots or screen recordings below. -->

| Before | After |
| :---: | :---: |
| *(Image / Screenshot)* | *(Image / Screenshot)* |
