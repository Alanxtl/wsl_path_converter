<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# wsl_path_converter Changelog

## [0.3.0]
### Added
- Add configurable WSL mount root for fast path conversion.
- Add settings-page examples for `/mnt`, `/`, and `/wsl` mount roots.
- Add pure Kotlin path conversion tests for custom mount roots.

### Changed
- Update IntelliJ Platform template dependencies and Gradle wrapper.
- Refactor WSL path conversion into a dedicated converter component.
- Keep WSL distribution caching separate from persisted settings.
