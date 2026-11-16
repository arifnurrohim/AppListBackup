# AppList Backup

A lightweight Android app that creates a backup list of the apps installed on your device. Pick the apps you want, save the list, and open it later to see what was installed.

## Features

- Lists user and system apps with name, package, version, icon and install date.
- Select individual apps, select all, or select none before saving.
- Saves the app list as an XML backup file.
- Opens saved backups in the built-in viewer with sort and filter options.
- Shares or sends a backup file by email.
- Catppuccin themes with selectable accent colours.
- No network permission and no account required.

## App info

| | |
|---|---|
| Package | `app.arifnurrohim.applistbackup` |
| Version | 2.0.3 (versionCode 20003) |
| Min SDK | 21 |
| Target SDK | 34 |
| Main activity | `app.arifnurrohim.applistbackup.DashboardActivity` |

## Install

Download the latest APK from the [Releases](../../releases/latest) page. The release APK is signed with the project release key.

## Build

Requirements:

- JDK 21
- Android SDK 34
- Build tools 35.0.0

```sh
cd src
sh gradlew assembleDebug
sh gradlew assembleRelease
```

Outputs:

- `src/app/build/outputs/apk/debug/app-debug.apk`
- `src/app/build/outputs/apk/release/app-release.apk` (signed when `KEYSTORE` is set)

### AndroidIDE

The project builds with AndroidIDE (AGP 8.10.1, Gradle 9.2.1). AndroidIDE sets `android.aapt2FromMavenOverride` to its ARM64 AAPT2 automatically; the x86_64 AAPT2 from Maven cannot run on the device.

`buildToolsVersion "35.0.0"` is pinned because AGP 8.10.1 cannot parse AndroidIDE's `37.0` build-tools revision.

### Release signing

`src/app/build.gradle` reads signing values from environment variables:

| Variable | Description |
|---|---|
| `KEYSTORE` | Path to the keystore file |
| `STORE_PASS` | Keystore password |
| `KEY_ALIAS` | Key alias (defaults to `apps`) |
| `KEY_PASS` | Key password (defaults to `STORE_PASS`) |

The release workflow expects these repository secrets:

- `KEYSTORE_BASE64`
- `KEYSTORE_PASSWORD`
- `KEY_ALIAS`
- `KEY_PASSWORD` (optional)

## Repository layout

```
.
├── .github/workflows/     # Build and release CI
├── base.apk               # Original APK extracted from the device
├── CHANGELOG.md
├── README.md
└── src/                   # Gradle project
    ├── build.gradle
    ├── settings.gradle
    ├── gradlew / gradlew.bat
    └── app/
        └── src/main/
            ├── AndroidManifest.xml
            ├── java/
            └── res/
```

## Source notes

The `src/` project was reconstructed from `base.apk` with [jadx](https://github.com/skylot/jadx). It is not the original upstream project, and some Kotlin code appears as equivalent Java.

Build fixes applied to make the reconstructed project compile:

- Rebuilt the corrupted nine-patch PNGs (`btn_bg_pressed`, `btn_bg_selected`).
- Replaced raw numeric style values with symbolic names.
- Added `res/values/ids.xml` for IDs that only existed in `public.xml`.
- Removed unreferenced decompiled AdMob/Gson code.
- Replaced the broken decompiled Base64 implementation with `android.util.Base64`.
- Fixed decompilation artifacts in `DomAppItemParser` and `BackupActivity`.
- Removed the stale jadx `R.java` files so AGP can generate them.

## Changelog

See [CHANGELOG.md](CHANGELOG.md).

## Credits

- [Catppuccin](https://github.com/catppuccin/catppuccin) colour palette.
- [Google Material Design Icons](https://github.com/google/material-design-icons).

## License

No license file is included in this repository yet. Add an appropriate license before redistributing.
