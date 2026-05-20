# AGENTS.md

This repository contains a JetBrains IDE plugin named **WSL Path Converter**. It adds a `WSL Path` entry to the IDE copy-path menu and converts Windows file paths into WSL-style paths.

## Environment

- Use JDK 21. On this machine, the known local JDK is `D:\envs\java\jdk-21.0.11`.
- Before running Gradle in a fresh PowerShell session:

```powershell
$env:JAVA_HOME='D:\envs\java\jdk-21.0.11'
$env:Path="$env:JAVA_HOME\bin;$env:Path"
```

- Use the Gradle wrapper. Do not rely on a globally installed Gradle.

## Important Files

- `src/main/resources/META-INF/plugin.xml`: plugin id, action registration, extension registration, settings registration.
- `src/main/kotlin/com/github/alanxtl/wslpathconverter/CopyPathInWSLProvider.kt`: copy-path provider and Windows-to-WSL path conversion logic.
- `src/main/kotlin/com/github/alanxtl/wslpathconverter/WslPathConfigurable.kt`: settings UI and cached WSL distribution service.
- `src/main/kotlin/com/github/alanxtl/wslpathconverter/WslPathSettingsService.kt`: persisted plugin settings.
- `gradle.properties`: plugin metadata, IntelliJ Platform target, Gradle version, IDE compatibility range.
- `gradle/libs.versions.toml`: dependency and Gradle plugin versions.
- `.github/workflows/*.yml`: CI, release, and optional UI-test workflows.

## Commands

Run these from the repository root:

```powershell
.\gradlew.bat test --console=plain
.\gradlew.bat buildPlugin --console=plain
.\gradlew.bat verifyPlugin --console=plain
```

For local manual testing inside an IDE sandbox:

```powershell
.\gradlew.bat runIde --console=plain
```

## Development Rules

- Keep changes focused. This plugin is intentionally small.
- Prefer IntelliJ Platform APIs over direct process calls when an official API exists.
- Keep path conversion behavior covered by tests before changing production logic.
- Do not edit generated or cache directories: `.gradle/`, `.kotlin/`, `.intellijPlatform/`, `.qodana/`, `build/`.
- Do not commit local IDE metadata unless it is already intentionally tracked.
- Preserve Windows-specific behavior. Many tests and runtime paths depend on Windows semantics.

## Testing Notes

- `CopyPathInWSLProviderTest` currently uses IntelliJ's platform test framework.
- Some path conversion assertions are guarded by `SystemInfo.isWindows`; Linux CI can miss Windows-specific behavior.
- If path conversion logic changes, prefer extracting pure conversion logic so it can be tested on every OS.

## Release Notes

- Plugin description is generated from the `<!-- Plugin description -->` block in `README.md`.
- `CHANGELOG.md` is consumed by the Gradle changelog plugin during release tasks.
- Marketplace publishing requires `PUBLISH_TOKEN`, `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, and `PRIVATE_KEY_PASSWORD`.
