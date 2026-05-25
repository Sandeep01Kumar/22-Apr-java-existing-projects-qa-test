# Fabric → Firebase Crashlytics Migration Runbook

This runbook supports the epics in [`./epics-and-acceptance-criteria.md`](./epics-and-acceptance-criteria.md). Each step title maps to the corresponding `FF-EPIC-NN` title (the auto-generated step anchors use the `step-NN` prefix while epic anchors use the `ff-epic-NN` prefix, so callers that need to deep-link to a specific step should use the `step-NN--…` anchor — see this document's `Step` headings). Steps are designed to be executable sequentially, with rollback paths defined for each. The runbook is **tool-agnostic** — wherever a specific CI provider, issue tracker, or notification platform would normally appear, the text uses neutral phrasing such as "your CI provider" or "your alerting platform". All identifiers in this document (`com.example.app`, `MY_FIREBASE_PROJECT_ID`, `MAPPING_FILE.txt`, `app-release.symbols.zip`, `FAKE_API_TOKEN_DO_NOT_USE`) are **obvious stand-ins** — never substitute real values into this file.

## Step 01 — Inventory & Discovery of Current Fabric Footprint

**Pre-conditions**

- Read access to every Fabric-using application repository.
- A scratch branch named for the migration (for example `migration/fabric-to-firebase`) is created and checked out.
- A scratch report file `MIGRATION_INVENTORY.md` is initialized at the repository root with section headings `## Build Files`, `## Initialization Sites`, and `## Secrets & API Keys`.

**Action**

1. Run a recursive grep across the repository for the legacy coordinates and SDK names:
   ```bash
   grep -RIn -E "io.fabric|com.crashlytics|Crashlytics.framework" .
   ```
2. Append each match to `MIGRATION_INVENTORY.md` under the appropriate section with `<path>:<line>` format.
3. Locate every initialization call:
   ```bash
   grep -RIn -E "Fabric\.with\(" .
   ```
4. Locate every `<meta-data>` element keyed `io.fabric.ApiKey` in Android manifests and every `Fabric` dictionary in iOS `Info.plist` files, recording placeholder names (NEVER real key values) in `MIGRATION_INVENTORY.md`.
5. Commit `MIGRATION_INVENTORY.md` to the migration branch.

**Validation**

- The inventory report exists and is committed — see [`FF-AC-001`](./epics-and-acceptance-criteria.md#ff-ac-001--repo-audit-complete).
- Every initialization site is listed with file path and line number — see [`FF-AC-002`](./epics-and-acceptance-criteria.md#ff-ac-002--initialization-sites-catalogued).
- Every Fabric API key reference is listed without disclosing real values — see [`FF-AC-003`](./epics-and-acceptance-criteria.md#ff-ac-003--secrets--api-keys-catalogued).

**Rollback**

- This step is read-only — delete the migration branch to revert. No application or infrastructure state is changed.

## Step 02 — Link Fabric Apps to Firebase Projects

**Pre-conditions**

- A Firebase organization and billing account exist for the migrating team.
- The inventory from Step 01 lists every Fabric application (Android by package, iOS by bundle ID such as `com.example.app`).
- A team member with Firebase project-creation permissions is available.

**Action**

1. In the Firebase console, create one Firebase project per environment (`MY_FIREBASE_PROJECT_ID`-dev, -staging, -prod or equivalent stand-ins).
2. For each Fabric application, run the Firebase console's "Link Fabric apps to Firebase" wizard, selecting the matching Firebase project.
3. Download the per-environment `google-services.json` and `GoogleService-Info.plist` placeholders and store them in your secure secret manager. NEVER commit the downloaded files to the host Spring Boot repository (`../../pom.xml` Maven POM is unrelated).
4. Record the Firebase project IDs (stand-ins only) in this runbook's appendix or the inventory report.

**Validation**

- A Firebase project exists per environment — see [`FF-AC-004`](./epics-and-acceptance-criteria.md#ff-ac-004--firebase-projects-provisioned-per-environment).
- The "Link Fabric apps to Firebase" wizard reports success — see [`FF-AC-005`](./epics-and-acceptance-criteria.md#ff-ac-005--fabric-apps-linked-to-firebase-projects).
- Configuration files retrieved and stored in the secret manager — see [`FF-AC-006`](./epics-and-acceptance-criteria.md#ff-ac-006--configuration-artifacts-retrieved).
- Firebase Crashlytics dashboard accessible with appropriate IAM role — see [`FF-AC-007`](./epics-and-acceptance-criteria.md#ff-ac-007--firebase-crashlytics-dashboard-accessible).

**Rollback**

- Linking can be undone in the Firebase console by removing the Fabric-app linkage from the Firebase project. Doing so does NOT delete the Firebase project itself. Crash uploads from Fabric continue unaffected because no SDK changes have been made yet.

## Step 03 — Build Tooling Migration (Android Gradle Plugin)

**Pre-conditions**

- Step 01 inventory complete; the Android project structure (modules, Gradle file paths) is known.
- Step 02 Firebase projects exist for each environment.
- The Android repository builds successfully in its current Fabric configuration (a clean baseline).

**Action**

1. In the root `build.gradle`, replace the Fabric Gradle tooling classpath with the Firebase one. Example illustrative snippet (≤ 3 lines):
   ```groovy
   classpath 'com.google.gms:google-services:<version>'
   classpath 'com.google.firebase:firebase-crashlytics-gradle:<version>'
   ```
2. In every Android application module's `build.gradle`, swap the plugin declaration. Example illustrative snippet (≤ 3 lines):
   ```groovy
   apply plugin: 'com.google.gms.google-services'
   apply plugin: 'com.google.firebase.crashlytics'
   ```
3. Delete any `apply plugin: 'io.fabric'` (or `id 'io.fabric'` in the plugins block) declarations from every module.
4. Run `./gradlew tasks --all` and confirm that Firebase Crashlytics tasks (e.g., `uploadCrashlyticsMappingFile<Variant>`) are listed.

**Validation**

- The root `build.gradle` references the Firebase Crashlytics Gradle plugin classpath — see [`FF-AC-008`](./epics-and-acceptance-criteria.md#ff-ac-008--project-level-plugin-classpath-updated).
- The app module's plugin declaration points to `com.google.firebase.crashlytics` — see [`FF-AC-009`](./epics-and-acceptance-criteria.md#ff-ac-009--app-level-plugin-declaration-swapped).
- Zero `io.fabric` references remain across all modules — see [`FF-AC-010`](./epics-and-acceptance-criteria.md#ff-ac-010--fabric-plugin-fully-removed-from-all-modules).
- `./gradlew assembleDebug assembleRelease` succeeds — see [`FF-AC-011`](./epics-and-acceptance-criteria.md#ff-ac-011--android-build-succeeds-with-new-plugin).

**Rollback**

- Revert the `build.gradle` changes via `git restore`.
- Re-add `apply plugin: 'io.fabric'` and the legacy Fabric classpath if the rollback decision is taken before SDK swap (Step 05).
- Confirm the build succeeds again with the legacy plugin.

## Step 04 — Build Tooling Migration (iOS CocoaPods / SPM)

**Pre-conditions**

- Step 01 inventory lists every iOS target consuming the Fabric `Podfile` or framework binary.
- The iOS workspace builds successfully against the legacy Fabric `Podfile` configuration.
- A choice has been made between CocoaPods or Swift Package Manager for Firebase; the choice is recorded below.

**Action**

1. Open the iOS `Podfile`. Remove the legacy lines. Example illustrative snippet (≤ 3 lines):
   ```ruby
   pod 'Fabric'
   pod 'Crashlytics'
   ```
2. Add the Firebase Crashlytics pod. Example illustrative snippet (≤ 3 lines):
   ```ruby
   pod 'FirebaseCrashlytics'
   ```
3. Run `pod install --repo-update` and commit the updated `Podfile.lock`.
4. (Optional SPM path) Add the Firebase iOS SDK package to the Xcode project via File → Add Packages, select the `FirebaseCrashlytics` product, and commit the updated `Package.resolved`.
5. Record the chosen approach (CocoaPods vs. SPM) in this runbook's step header or appendix.

**Validation**

- The `Podfile` no longer contains the substrings `Fabric` or `Crashlytics` (except inside `FirebaseCrashlytics`) — see [`FF-AC-012`](./epics-and-acceptance-criteria.md#ff-ac-012--podfile-replaced-with-firebase-crashlytics-pod).
- `pod install` succeeds and updates `Podfile.lock` — see [`FF-AC-013`](./epics-and-acceptance-criteria.md#ff-ac-013--pod-install-succeeds).
- If SPM was chosen, `Package.resolved` is updated and committed — see [`FF-AC-014`](./epics-and-acceptance-criteria.md#ff-ac-014--optional-spm-path-documented).
- `xcodebuild ... build` succeeds — see [`FF-AC-015`](./epics-and-acceptance-criteria.md#ff-ac-015--ios-build-succeeds-with-firebase-pod).

**Rollback**

- Revert the `Podfile` changes via `git restore`.
- Re-run `pod install` to restore the Fabric pods.
- For SPM, remove the Firebase iOS SDK package from the Xcode project and re-add the Fabric framework as needed.

## Step 05 — SDK Dependency Swap

**Pre-conditions**

- Step 03 (Android Gradle plugin) and Step 04 (iOS pod / SPM) complete.
- A clean build succeeds on both platforms with the new plugins / pods.

**Action**

1. In each Android module's `build.gradle`, delete the legacy SDK line. Example illustrative snippet (≤ 3 lines):
   ```groovy
   implementation 'com.crashlytics.sdk.android:crashlytics:<legacy-version>'
   ```
2. Add the Firebase BoM and the Firebase Crashlytics SDK. Example illustrative snippet (≤ 3 lines):
   ```groovy
   implementation platform('com.google.firebase:firebase-bom:<bom-version>')
   implementation 'com.google.firebase:firebase-crashlytics'
   ```
3. Run `./gradlew :app:dependencies` and confirm the legacy `com.crashlytics.sdk.android:crashlytics` is gone and `com.google.firebase:firebase-crashlytics` is present with the BoM-resolved version.
4. For iOS, confirm the linked frameworks of every target include `FirebaseCrashlytics` and exclude the legacy `Crashlytics.framework`.
5. Audit transitive dependencies (`./gradlew :app:dependencies` Android; framework inspection iOS) for any remaining `com.crashlytics.sdk.android:answers`, `com.crashlytics.sdk.android:beta`, or `Fabric.framework` references; remove any found.

**Validation**

- Legacy Crashlytics SDK gone from Android — see [`FF-AC-016`](./epics-and-acceptance-criteria.md#ff-ac-016--legacy-crashlytics-sdk-removed-android).
- Firebase BoM declared — see [`FF-AC-017`](./epics-and-acceptance-criteria.md#ff-ac-017--firebase-bom-declared-android).
- Firebase Crashlytics SDK present — see [`FF-AC-018`](./epics-and-acceptance-criteria.md#ff-ac-018--firebase-crashlytics-sdk-added-android).
- iOS targets link `FirebaseCrashlytics` — see [`FF-AC-019`](./epics-and-acceptance-criteria.md#ff-ac-019--firebase-crashlytics-framework-linked-ios).
- Transitive Fabric Answers / Beta dependencies removed — see [`FF-AC-020`](./epics-and-acceptance-criteria.md#ff-ac-020--transitive-fabric-dependencies-cleaned).

**Rollback**

- Revert the SDK-line changes via `git restore`.
- Re-add the legacy `com.crashlytics.sdk.android:crashlytics:<legacy-version>` Android dependency.
- For iOS, re-add the legacy Fabric pod / framework via the rollback of Step 04.
- Re-run `./gradlew :app:dependencies` and `pod install` to confirm the rollback.

## Step 06 — Initialization Code Migration

**Pre-conditions**

- Step 05 SDK swap complete; the Firebase Crashlytics SDK is linked on both platforms.
- The application currently still calls `Fabric.with(this, new Crashlytics())` (per the inventory in `MIGRATION_INVENTORY.md` from Step 01).

**Action**

1. Delete every Fabric initialization line. Example illustrative snippet (≤ 3 lines):
   ```java
   Fabric.with(this, new Crashlytics());
   ```
2. Remove the corresponding `import io.fabric.sdk.android.Fabric;` and `import com.crashlytics.android.Crashlytics;` import lines.
3. Confirm Firebase auto-initialization. In most modern Firebase setups, `FirebaseApp.initializeApp(...)` is invoked automatically by the `google-services` plugin (Android) or the `GoogleService-Info.plist` linker step (iOS); explicit initialization is rarely required. Example illustrative snippet (≤ 3 lines):
   ```kotlin
   FirebaseApp.initializeApp(this)
   ```
4. Migrate Crashlytics customizations. Example illustrative snippet (≤ 3 lines):
   ```kotlin
   FirebaseCrashlytics.getInstance().setUserId("<userId>")
   ```
5. Build, install, and launch a smoke-test debug build to confirm Firebase initialization succeeds.

**Validation**

- All Fabric initialization calls removed — see [`FF-AC-021`](./epics-and-acceptance-criteria.md#ff-ac-021--fabric-initialization-calls-removed).
- Firebase auto-initialization confirmed in logs and Firebase console — see [`FF-AC-022`](./epics-and-acceptance-criteria.md#ff-ac-022--firebase-auto-initialization-verified).
- Custom keys and user identifiers migrated — see [`FF-AC-023`](./epics-and-acceptance-criteria.md#ff-ac-023--custom-keys--user-identifiers-migrated).
- Non-fatal exception reporting migrated — see [`FF-AC-024`](./epics-and-acceptance-criteria.md#ff-ac-024--non-fatal-exception-reporting-migrated).

**Rollback**

- Revert all initialization and import-line changes via `git restore`.
- Confirm the Fabric initialization is restored and the application builds again with the (rolled-back) Step 05 SDK configuration.

## Step 07 — Symbol Upload Pipeline Migration

**Pre-conditions**

- Step 06 initialization complete and Firebase is auto-initializing on app launch.
- The Android release pipeline can produce a ProGuard/R8 `MAPPING_FILE.txt` artifact.
- The iOS release archive produces dSYM bundles.
- The Firebase service-account JSON (or per-app upload credential) is stored in your secure secret manager and is referenced by a stand-in name such as `FAKE_API_TOKEN_DO_NOT_USE`.

**Action**

1. Wire the Android mapping upload task into the release pipeline. Example illustrative snippet (≤ 3 lines):
   ```bash
   ./gradlew :app:assembleRelease :app:uploadCrashlyticsMappingFileRelease
   ```
2. If the project includes NDK modules, enable native symbol upload. Example illustrative snippet (≤ 3 lines):
   ```groovy
   firebaseCrashlytics { nativeSymbolUploadEnabled = true }
   ```
   Then invoke `./gradlew :app:uploadCrashlyticsSymbolFileRelease` in the release pipeline.
3. For iOS, wire the Crashlytics run-script build phase. Example illustrative snippet (≤ 3 lines):
   ```bash
   "${PODS_ROOT}/FirebaseCrashlytics/upload-symbols" -gsp "$GoogleServiceInfoPath" -p ios "$DWARF_DSYM_FOLDER_PATH"
   ```
4. Run a release build on a CI agent or a local machine and observe the symbol-upload artifacts appearing in the Firebase Crashlytics "Mapping files" / "dSYMs" management views for `MY_FIREBASE_PROJECT_ID`.
5. Trigger a controlled test crash on a release build of `com.example.app` and verify the resulting issue in Firebase Crashlytics has demangled stack frames.

**Validation**

- Android mapping upload task wired and successful — see [`FF-AC-025`](./epics-and-acceptance-criteria.md#ff-ac-025--android-mapping-file-upload-task-wired).
- NDK symbol upload (if applicable) wired and successful — see [`FF-AC-026`](./epics-and-acceptance-criteria.md#ff-ac-026--ndk-symbol-upload-task-wired-if-applicable).
- iOS dSYM upload configured — see [`FF-AC-027`](./epics-and-acceptance-criteria.md#ff-ac-027--ios-dsym-upload-configured).
- Symbol artifacts visible in Firebase console — see [`FF-AC-028`](./epics-and-acceptance-criteria.md#ff-ac-028--symbol-artifacts-visible-in-firebase-console).
- Deobfuscated stack traces verified — see [`FF-AC-029`](./epics-and-acceptance-criteria.md#ff-ac-029--deobfuscated-stack-traces-verified).

**Rollback**

- Remove the Firebase symbol-upload task invocations from the release pipeline.
- Re-enable the legacy Fabric upload task (`crashlyticsUploadDistribution<Variant>` or equivalent) until parity is approved.
- Document the rollback in `MIGRATION_INVENTORY.md`.

## Step 08 — Parity Validation Between Fabric and Firebase

**Pre-conditions**

- Step 07 symbol upload pipeline complete on both platforms.
- Both Fabric and Firebase Crashlytics are receiving live crash data in parallel.
- Stakeholders (engineering lead, release manager, product manager) have agreed on numerical parity thresholds and the validation window length (typically 7–14 days).

**Action**

1. Open a shared parity report file `PARITY_REPORT.md` in the migration branch with section headings `## Thresholds`, `## Daily Crash-Free Users`, `## Daily ANR Rate`, `## Top-N Overlap`, `## Gap Analysis`, and `## Sign-Off`.
2. Daily for the validation window, copy the Fabric and Firebase dashboard values into `PARITY_REPORT.md`.
3. At window end, compute deltas (crash-free users delta, ANR rate delta, top-N overlap %) and compare to thresholds.
4. Investigate any out-of-threshold delta; document the root cause (typically a [symbolication](../crashlytics-pipeline/glossary.md), [fingerprint](../crashlytics-pipeline/glossary.md), or sampling difference) and a mitigation or accepted-risk decision.
5. Hold a parity sign-off meeting and annotate the report with PARITY APPROVED or PARITY REJECTED.

**Validation**

- Parity thresholds defined and approved — see [`FF-AC-030`](./epics-and-acceptance-criteria.md#ff-ac-030--parity-thresholds-defined-and-approved).
- Crash-free users parity within threshold — see [`FF-AC-031`](./epics-and-acceptance-criteria.md#ff-ac-031--crash-free-users-parity-within-threshold).
- ANR parity within threshold — see [`FF-AC-032`](./epics-and-acceptance-criteria.md#ff-ac-032--anr-rate-parity-within-threshold).
- Top-N overlap meets threshold — see [`FF-AC-033`](./epics-and-acceptance-criteria.md#ff-ac-033--top-n-issue-overlap-meets-threshold).
- Gap analysis documented — see [`FF-AC-034`](./epics-and-acceptance-criteria.md#ff-ac-034--gap-analysis-documented).
- Parity sign-off recorded — see [`FF-AC-035`](./epics-and-acceptance-criteria.md#ff-ac-035--parity-sign-off-recorded).

**Rollback**

- If parity is REJECTED, do NOT proceed to Steps 09–11.
- Either (a) extend the validation window, address gaps, and re-evaluate, or (b) revert the SDK swap (Steps 03–07) by rolling back each step in reverse order, keeping Fabric as the primary crash-reporting system until the rejected gaps can be eliminated.

## Step 09 — CI/CD Pipeline Updates

**Pre-conditions**

- Step 08 parity APPROVED.
- Access to your CI provider's configuration files and secret store.

**Action**

1. In your CI provider's build configuration, remove the legacy Fabric upload task invocations (`crashlyticsUploadDistribution<Variant>`, `crashlyticsUploadDeobs<Variant>`).
2. Add the Firebase symbol-upload tasks (`uploadCrashlyticsMappingFile<Variant>`, `uploadCrashlyticsSymbolFile<Variant>`) after the assemble/bundle step.
3. Remove the legacy `FABRIC_API_KEY`-style secret from the CI secret store; add the new Firebase upload credential as a CI secret referenced by a stand-in name (`FIREBASE_SERVICE_ACCOUNT_JSON` or similar). Real values live in the secret store, not in the repository.
4. Configure the build pipeline to mark the build as failed (non-zero exit) if symbol upload fails — no "best effort" semantics.
5. Run a release build through the updated pipeline and confirm symbol artifacts appear in the Firebase Crashlytics dashboard.

**Validation**

- Fabric upload tasks removed from CI — see [`FF-AC-036`](./epics-and-acceptance-criteria.md#ff-ac-036--fabric-upload-tasks-removed-from-ci).
- Firebase upload tasks invoked in CI — see [`FF-AC-037`](./epics-and-acceptance-criteria.md#ff-ac-037--firebase-symbol-upload-tasks-invoked-in-ci).
- Firebase credentials injected securely — see [`FF-AC-038`](./epics-and-acceptance-criteria.md#ff-ac-038--firebase-credentials-injected-securely).
- Pipeline fails fast on symbol upload failure — see [`FF-AC-039`](./epics-and-acceptance-criteria.md#ff-ac-039--pipeline-fails-fast-on-symbol-upload-failure).

**Rollback**

- Restore the previous CI configuration via `git restore` on the CI-config files.
- Re-instate the legacy `FABRIC_API_KEY` secret if it has not already been removed from the secret store.
- Confirm the previous release-build pipeline runs successfully.

## Step 10 — Alert & Dashboard Re-routing

**Pre-conditions**

- Step 08 parity APPROVED.
- Step 09 CI pipeline updated and producing reliable symbol-upload artifacts.
- A list of existing Fabric Beta / Answers / Crashlytics alerting integrations (Slack, PagerDuty, email, BigQuery export) and dashboards (Datadog, Grafana, internal wikis).

**Action**

1. In the Firebase Crashlytics dashboard for `MY_FIREBASE_PROJECT_ID`, enable velocity alerts and regression alerts with thresholds that match the legacy Fabric configuration.
2. Configure the Firebase → Slack integration (or the equivalent for your alerting platform). Send a synthetic alert to verify routing.
3. Configure the Firebase → PagerDuty integration (or the equivalent on-call escalation platform). Verify that an on-call engineer receives a test page.
4. Update operational dashboards to embed Firebase Crashlytics widgets (or link to Firebase issue URLs). Audit dashboards for any remaining Fabric links and update them.
5. (Optional) Enable Firebase Crashlytics BigQuery export and migrate any Fabric-Answers analytics queries to the new dataset.

**Validation**

- Velocity and regression alerts configured — see [`FF-AC-040`](./epics-and-acceptance-criteria.md#ff-ac-040--velocity-and-regression-alerts-configured).
- Slack and PagerDuty routing verified — see [`FF-AC-041`](./epics-and-acceptance-criteria.md#ff-ac-041--slack-and-pagerduty-routing-verified).
- Operational dashboards updated — see [`FF-AC-042`](./epics-and-acceptance-criteria.md#ff-ac-042--operational-dashboards-updated).
- Optional BigQuery export configured if applicable — see [`FF-AC-043`](./epics-and-acceptance-criteria.md#ff-ac-043--optional-bigquery-export-configured).

**Rollback**

- Disable the Firebase Crashlytics alerts and re-enable the legacy Fabric alerts.
- Restore the previous dashboard configurations via dashboard-config version control or manual edit.
- Document the rollback in `MIGRATION_INVENTORY.md` and re-open the parity validation window if necessary.

## Step 11 — Decommissioning & Team Enablement

**Pre-conditions**

- Steps 08, 09, and 10 complete and stable for at least 30 days (rollback window).
- All on-call engineers have access to the Firebase Crashlytics dashboard.
- The Fabric organization administrator is available.

**Action**

1. Delete the Fabric API key `<meta-data>` element from every `AndroidManifest.xml`. Example illustrative snippet (≤ 3 lines):
   ```xml
   <meta-data android:name="io.fabric.ApiKey" android:value="FAKE_API_TOKEN_DO_NOT_USE" />
   ```
   (The above is the element being DELETED — do not commit any version that contains it.)
2. Delete the `Fabric` dictionary from every `Info.plist`.
3. Run a final repository-wide search for `io.fabric`, `com.crashlytics`, `Fabric.framework`, and `Fabric/` and update or remove any remaining documentation references.
4. Archive the Fabric organization workspace (or revoke access to it) following the Fabric organization administrator's procedure. Record the archival in this runbook below.
5. Hold a team-enablement session covering the Firebase Crashlytics dashboard, alert escalation paths, symbol-upload troubleshooting, and the [crashlytics-pipeline glossary](../crashlytics-pipeline/glossary.md). Capture attendance.
6. Update on-call rotation runbooks to reference Firebase Crashlytics issue URLs instead of Fabric issue URLs.

**Validation**

- Fabric API keys removed from Android — see [`FF-AC-044`](./epics-and-acceptance-criteria.md#ff-ac-044--fabric-api-keys-removed-android).
- Fabric API keys removed from iOS — see [`FF-AC-045`](./epics-and-acceptance-criteria.md#ff-ac-045--fabric-api-keys-removed-ios).
- Residual references removed from documentation — see [`FF-AC-046`](./epics-and-acceptance-criteria.md#ff-ac-046--residual-references-removed-from-documentation).
- Fabric workspace archived — see [`FF-AC-047`](./epics-and-acceptance-criteria.md#ff-ac-047--fabric-workspace-archived).
- Team enablement session complete — see [`FF-AC-048`](./epics-and-acceptance-criteria.md#ff-ac-048--team-enablement-session-complete).

**Rollback**

- If issues surface within the 30-day post-decommissioning observation period, contact the Fabric organization administrator about un-archiving the workspace (some sunset milestones may make this irreversible).
- If un-archival is not possible, the rollback path is to keep Firebase Crashlytics as the sole crash-reporting system and address the surfaced issues directly in Firebase (no return to Fabric).
- Re-add the deleted API-key elements ONLY if the un-archival path is available and the team explicitly elects to re-enable Fabric.

## Cross-References

- [`./epics-and-acceptance-criteria.md`](./epics-and-acceptance-criteria.md) — Migration epics and Given/When/Then acceptance criteria. Each FF-AC-NNN referenced from the Validation blocks above resolves to its definition here.
- [`../crashlytics-pipeline/glossary.md`](../crashlytics-pipeline/glossary.md) — Domain glossary for ANR, dSYM, mapping file, symbolication, fingerprint, velocity alert, regression alert, and other vocabulary used in this runbook.
- [`../README.md`](../README.md) — Planning documents index (`docs/README.md`).
- [`../../README.md`](../../README.md) — Project root README (`README.md`).
