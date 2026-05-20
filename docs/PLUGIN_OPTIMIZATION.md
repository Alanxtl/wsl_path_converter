# WSL Path Converter Optimization Notes

## High Priority

1. Extract path conversion into a small pure Kotlin component.
   - Current behavior lives inside `CopyPathInWSLProvider`.
   - Extraction would let CI test conversion on Linux, Windows, and macOS without IntelliJ test fixtures.

2. Align README promises with implementation.
   - README says conversion directly calls `wsl.exe wslpath`.
   - Current implementation uses fast `/mnt/<drive>` conversion for normal drive-letter paths and only uses IntelliJ WSL APIs for fallback cases.

3. Decide how to support custom WSL automount roots.
   - Fast conversion assumes `/mnt/c/...`.
   - Users with `/etc/wsl.conf` automount root customizations may receive incorrect paths.

4. Reduce settings-service duplication.
   - `WslPathSettingsService` and `WslCachedDistroService` both persist `WslPathState` into `WslPathSettings.xml`.
   - A cleaner boundary would keep persistence in one service and cache only runtime distribution objects.

## Medium Priority

1. Make non-Windows behavior explicit.
   - The provider currently returns `null` outside Windows.
   - Document whether the action should be hidden, disabled, or simply unavailable on non-Windows systems.

2. Add integration-style tests for selected WSL distribution behavior.
   - Current tests cover fast path conversion and passthrough paths.
   - They do not validate `WslCachedDistroService` interactions or distribution selection.

3. Improve user-facing error handling.
   - If conversion falls back to the original path, the user gets no signal.
   - Consider a notification only for cases where conversion clearly failed.

4. Keep marketplace documentation synchronized with the plugin record.
   - Installation links now point at plugin id `27636`.
   - Future releases should keep README links, badges, and marketplace metadata in sync.

## Lower Priority

1. Add localization keys for settings labels.
   - `messages/MyBundle.properties` exists, but UI labels are hard-coded.

2. Add a small screenshot or GIF to README.
   - This would help users understand where the new menu item appears.

3. Consider a configurable conversion mode.
   - Fast mode: `/mnt/<drive>` string conversion.
   - Accurate mode: IntelliJ WSL API / `wslpath` conversion.
   - Automatic mode: fast for normal paths, accurate when a selected distribution is configured.
