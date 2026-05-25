# Crashlytics Pipeline Glossary

This glossary is the authoritative dictionary for terms used in [`./epics-and-stories.md`](./epics-and-stories.md) and [`../fabric-to-firebase-migration/epics-and-acceptance-criteria.md`](../fabric-to-firebase-migration/epics-and-acceptance-criteria.md). Definitions are platform-neutral wherever possible (Android, iOS, native NDK) and intentionally avoid vendor-specific URLs so the glossary remains valid even as vendor documentation moves.

## ANR

**ANR** (Application Not Responding) is an Android-platform event raised when the main (UI) thread is blocked for longer than a system-defined threshold — typically 5 seconds for foreground input and 10 seconds for broadcast receivers. ANRs are not exceptions in the language sense; they are platform-level health signals reported either by `ActivityManagerService` or by per-process watchdog threads. A Crashlytics pipeline treats ANRs as first-class events so that responsiveness failures appear in the issue list alongside hard crashes. iOS does not have an exact ANR analogue; the closest equivalents are watchdog-terminated processes for excessive launch or resume time.

## Breadcrumb

A **breadcrumb** is a short, structured log entry recorded by the application (or by the Crashlytics SDK on the application's behalf) that captures a user-meaningful event — for example, a button tap, a screen transition, a network request, or a lifecycle state change. Breadcrumbs are appended to an in-memory ring buffer; the most recent N entries are serialized into the crash record when an event is captured. Breadcrumbs are the primary tool an on-call engineer uses to reconstruct what the user was doing in the seconds before a crash. Contrast with [custom key](#custom-key), which is a point-in-time snapshot rather than a chronological event log.

## Crash-Free User

A **crash-free user** is a unique user identifier that recorded at least one session during the measurement window without any fatal-exception event attributable to the application. The **crash-free user percentage** is the count of crash-free users divided by the count of total users in the same window, expressed as a percentage. This metric is the canonical release-quality gate: a 99.5 % crash-free-user rate means one in two hundred users experienced a crash in the window. A related metric is **crash-free sessions**, computed over sessions instead of users.

## Custom Key

A **custom key** is a developer-supplied key/value pair (typically a short string key and a string, int, bool, or float value) attached to the on-device crash-record state and serialized into the crash record at capture time. Custom keys are the primary tool a developer uses to encode application-specific context — feature-flag state, A/B-test variant, account tier, login state — that is otherwise invisible in a generic stack trace. Custom keys are not [breadcrumbs](#breadcrumb): they are snapshots of "what is true right now", not a log of what happened.

## Deobfuscation

**Deobfuscation** is the process of mapping a release-build's obfuscated symbols back to their original source-level names, typically by applying a [mapping file](#mapping-file). Deobfuscation is a sub-step of [symbolication](#symbolication) that handles the obfuscation pass (Android ProGuard/R8 rename `com.example.app.MainActivity.onCreate` to `a.b.c`) and is reversed by looking up obfuscated → original mappings. Without deobfuscation, every release-build Android stack trace is unreadable.

## dSYM

A **dSYM** (debug symbol file, also written `.dSYM`) is an Apple-platform bundle produced by the Xcode toolchain at archive time. It contains the DWARF debug-info that maps hex addresses in the release `.app` binary back to source-level functions, files, and line numbers. dSYM bundles are produced per architecture (typically `arm64` for device builds) and per executable. A Crashlytics pipeline uploads dSYM bundles via [symbol upload](#symbol-upload) and consumes them during [symbolication](#symbolication).

## Fatal Exception

A **fatal exception** is an uncaught exception or signal that causes the application process to terminate involuntarily — for example, an unhandled `RuntimeException` on Android, an `NSException` propagated past the run loop on iOS, or a native signal such as `SIGSEGV`, `SIGABRT`, `SIGILL`, `SIGBUS`, or `SIGFPE`. The Crashlytics SDK installs handlers for each platform's fatal-exception sources during application start (see `CR-EPIC-01`). Fatal exceptions are the canonical "crash" — contrast with [non-fatal exception](#non-fatal-exception), which is caught by the application and reported voluntarily.

## Fingerprint

A **fingerprint** is a deterministic hash computed over a canonicalized representation of a symbolicated stack trace, used to group conceptually identical crashes into a single issue. A good fingerprint algorithm normalizes line-number jitter, ignores top frames that belong to standard runtime libraries, and produces the same hash for the same root cause across releases so trend data is meaningful. Two records with the same fingerprint become events of one issue; two records with different fingerprints become separate issues. Operators can manually merge or split issues to compensate for fingerprint misclassification.

## Mapping File

A **mapping file** (also "ProGuard mapping" or "R8 mapping", typically named `mapping.txt`) is an Android-toolchain artifact that records the obfuscation mappings applied during release builds — for example, "`com.example.app.LoginActivity` → `a.b.c`". A Crashlytics pipeline uploads the mapping file via [symbol upload](#symbol-upload) per release variant, then uses it during [symbolication](#symbolication) to translate obfuscated stack frames back to original names. Mapping files are not bytecode; they are plain-text translation tables.

## NDK Symbol File

An **NDK symbol file** is a packaged set of ELF debug-info artifacts produced by the Android NDK (Native Development Kit) toolchain for native libraries built with `-g`. The packaging is typically a `.zip` of `.so` files with their debug sections preserved, suitable for upload to the Crashlytics pipeline. NDK symbol files are required to resolve native crash addresses (from `SIGSEGV`, `SIGABRT`, and similar signals) into source-level C/C++ function names and line numbers. Without NDK symbol files, native crashes appear as opaque hex addresses.

## Non-Fatal Exception

A **non-fatal exception** is an exception (or other error condition) that the application has caught and handled, but which the application explicitly reports to the Crashlytics pipeline so the condition can be tracked and trended. Non-fatal exceptions do not terminate the process; they are voluntary diagnostic records. Examples include caught-and-recovered networking errors, parsing failures in third-party data, and unexpected-but-handled state. Contrast with [fatal exception](#fatal-exception), which is involuntary and terminates the process.

## Opportunistic Upload

**Opportunistic upload** is the design pattern of deferring crash-record uploads until the on-device context is favorable: the process has restarted (typically the next app launch), connectivity is available, the user has not opted out of metered uploads, and battery state is non-critical. The opposite would be synchronous in-process upload, which is infeasible because the crashing process is exiting and the SDK cannot rely on it staying alive long enough to complete a network call. Opportunistic upload is what guarantees no crash is lost while imposing minimal user-visible cost.

## Regression Alert

A **regression alert** is a notification emitted when an issue previously marked "resolved" (closed) produces a new event in the current release. Regression alerts catch failed fixes — for example, a fix that did not actually land in the release branch, or a refactor that re-introduced the original root cause. Contrast with [velocity alert](#velocity-alert), which fires on event-frequency spikes regardless of resolved/open state. A well-tuned regression alert is high-signal: it almost always indicates a genuine engineering miss.

## Symbolication

**Symbolication** is the umbrella process of translating a release-build crash report's raw stack frames (hex addresses on iOS, obfuscated names on Android, demangled-but-not-original names on NDK) into source-level function names, source files, and line numbers. Symbolication is performed server-side using artifacts that the release pipeline uploads via [symbol upload](#symbol-upload): [mapping files](#mapping-file) for Android, [dSYM](#dsym) bundles for iOS, and [NDK symbol files](#ndk-symbol-file) for native code. The pipeline marks each frame as [deobfuscated](#deobfuscation) (symbol-file matched) or "raw" (no match found) so downstream consumers know the quality of the trace.

## Symbol Upload

**Symbol upload** is the act of pushing release-time [symbolication](#symbolication) artifacts — [mapping files](#mapping-file), [dSYM](#dsym) bundles, [NDK symbol files](#ndk-symbol-file) — from the release build pipeline to the Crashlytics server, indexed by application ID + version + build identifier. Symbol upload is typically wired as a release-pipeline Gradle task (Android: `uploadCrashlyticsMappingFile<Variant>`, `uploadCrashlyticsSymbolFile<Variant>`) or a build phase (iOS: `upload-symbols` script). Symbol upload must succeed for every release variant before crashes from that release start arriving, or the pipeline will produce unreadable stack traces.

## Velocity Alert

A **velocity alert** is a notification emitted when an issue's event count per unit time exceeds a configured threshold — for example, more than 100 events per minute for a single issue, or more than 1 % of all sessions reporting a single issue within an hour. Velocity alerts catch new-or-existing issues that suddenly affect many users at once, regardless of whether the issue was previously known. Contrast with [regression alert](#regression-alert), which is keyed to resolved/closed state. A well-tuned velocity alert is high-signal: it almost always indicates a release-wide regression worth paging on.

## Back to Pipeline

Return to the pipeline document: [`./epics-and-stories.md`](./epics-and-stories.md).
