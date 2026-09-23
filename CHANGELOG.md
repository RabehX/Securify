# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [2.0.0] - Unreleased

### Added
- Repository health & community standards (`CONTRIBUTING.md`, `CODE_OF_CONDUCT.md`, `SECURITY.md`, `CHANGELOG.md`, `AGENTS.md`).
- GitHub Issue forms for bug reports and feature requests.
- Pull request template with pre-flight checklist and visual diff tables.
- Automated PR verification workflow (`ci.yml`) covering compilation, linting, and unit tests.
- Code quality tooling integration with Spotless (Ktlint) and Detekt with Compose rules.
- `.editorconfig` with Google NowInAndroid Compose-compatible formatting rules.
- Comprehensive unit test coverage for `SettingsViewModel`, `LegalRepository`, and DataStore preferences.

### Changed
- Refactored UI layer to consolidate reusable components into `:core:designsystem`.
- Bumped target SDK and compile SDK to API 37.
- Updated project presentation and badges in `README.md`.

### Removed
- Removed duplicated UI component definitions from `app/ui/component/*` in favor of `:core:designsystem`.
- Removed legacy documentation configuration (`mkdocs.yml`, `docs/index.md`) and deprecated CI workflow files (`build.yml`, `deploy-docs.yml`).
- Removed unused and dead code components.

---

## [1.4.0] - 2026-05-30

### Added
- **First Open-Source Release**: Full Kotlin & Jetpack Compose Android application codebase published to GitHub.
- Google Play Integrity API provider and backend verification integration.
- REI native root and KernelSU detection integration.
- Automated GitHub Actions release pipeline with SHA-256 APK checksum generation.
- Material 3 Expressive UI with dynamic color, custom seed picker, and pure AMOLED black theme.
- Device & system hardware inspector (ABI, kernel version, fingerprint, patch level).
- Per-app language switcher supporting English and Arabic.
- Logcat diagnostic export via Android Storage Access Framework.
- Native dynamic Markdown parser and viewer for Privacy Policy and Terms of Service.
- Unit test suite for `HomeViewModel` and `IntegrityRepository`.

---

## [1.3.0] - 2024-01-24

### Changed
- Rebuilt application using Kotlin and Jetpack Compose.
- Migrated architecture to MVVM with ProtoBuf DataStore preferences.
- Added Export Logcat diagnostic utility.
- Added Android 15 platform support.
- Introduced Dagger Hilt dependency injection.
- Added device info encryption and masking.

---

## [1.1.0] - 2024-01-24

### Added
- Device info hiding and quick screenshot sharing.
- Arabic language localization.
- Kernel version display.
- Enhanced root detection checks (up to 20 heuristics).

---

## [1.0.0] - 2023-12-31

### Added
- Initial project release.
- Core root detection for Magisk and KernelSU.
- Material Design 3 user interface with dynamic color.

[2.0.0]: https://github.com/RabehX/Securify/compare/v1.4.0...HEAD
[1.4.0]: https://github.com/RabehX/Securify/compare/v1.3.0...v1.4.0
[1.3.0]: https://github.com/RabehX/Securify/compare/v1.1.0...v1.3.0
[1.1.0]: https://github.com/RabehX/Securify/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/RabehX/Securify/releases/tag/v1.0.0
