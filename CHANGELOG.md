# Changelog

All notable changes to this project are documented here.

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
