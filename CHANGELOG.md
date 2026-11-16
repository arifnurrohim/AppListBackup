# Changelog

All notable changes to this project are documented here.

## [2.0.4] - 2026-11-17

### Added

- Search by app name or package name in the backup app list and backup file viewer.
- "System apps" checkbox to filter system apps in both screens.
- Long-press an app to copy its package name in both screens.
- Backup XML files now record whether each app is a system app.
- Added Creator, email, GitHub repository, Catppuccin, Material Icons, and Material Symbols links to the About page.
- Removed the About section from Settings.
- Updated the dashboard About card and footer text.

### Changed

- Renamed the release signing key alias to `AppListBackup`.
- Bumped version to 2.0.4 (versionCode 20004).

## [2.0.3] - 2026-11-17

### Changed

- Removed the original-app credit from the About and Settings screens.
- Updated the Help link to <https://arifnurrohim.my.id/applistbackup/>.
- Bumped version to 2.0.3 (versionCode 20003).

### Fixed

- Rebuilt corrupted nine-patch button assets so resource compilation succeeds.
- Converted raw numeric style values to symbolic resource names.
- Added missing title/subtitle IDs used by the title bar styles.
- Removed unreferenced decompiled AdMob/Gson classes.
- Replaced the broken decompiled Base64 implementation with `android.util.Base64`.
- Fixed decompilation artifacts in the XML parser and backup loader.
- Removed duplicate decompiled `R.java` files.
- Pinned build tools to 35.0.0 so release lint works with AndroidIDE.

### Build

- Added GitHub Actions workflows for debug builds and tagged releases.
- Added release signing support through environment variables and repository secrets.

## [2.0.2]

- Initial reconstructed source from the installed APK.
