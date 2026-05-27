# 📚 Planning Documents Index

This documentation tree holds agile planning artifacts for two programs of work related to mobile crash reporting. These artifacts describe a generic Crashlytics pipeline and a Fabric → Firebase Crashlytics migration; they are independent reference material and do not describe the behavior of the Spring Boot Product API that hosts this repository. Each artifact is authored in plain GitHub-flavored Markdown and is intended to be tool-agnostic — it can be ingested by any modern issue tracker. The two planning documents are accompanied by supporting documents (a glossary for the pipeline and a runbook for the migration) so that terms and operational sequences are unambiguous.

## 📁 Documents

- [Crashlytics Pipeline — Epics & Stories](./crashlytics-pipeline/epics-and-stories.md) — Ten epics (`CR-EPIC-01` … `CR-EPIC-10`) decomposing the crash-reporting pipeline from on-device crash capture through dashboard delivery and issue lifecycle, each with 3–6 user stories keyed `CR-STORY-NNN`.
- [Crashlytics Pipeline — Glossary](./crashlytics-pipeline/glossary.md) — Domain vocabulary referenced from the pipeline stories: ANR, dSYM, mapping file, symbolication, fingerprint, breadcrumb, custom key, velocity alert, regression alert, opportunistic upload, crash-free user.
- [Fabric → Firebase Migration — Epics & Acceptance Criteria](./fabric-to-firebase-migration/epics-and-acceptance-criteria.md) — Eleven epics (`FF-EPIC-01` … `FF-EPIC-11`) covering the Fabric → Firebase Crashlytics migration, each with 3–6 Given/When/Then acceptance criteria keyed `FF-AC-NNN`.
- [Fabric → Firebase Migration — Runbook](./fabric-to-firebase-migration/runbook.md) — Stepwise operational runbook supporting the migration epics: pre-conditions, action, validation, and rollback for each phase.

## 🏷 Identifier Conventions

| Prefix         | Meaning                                              | Format                                                                    |
|----------------|------------------------------------------------------|---------------------------------------------------------------------------|
| `CR-EPIC-NN`   | Crashlytics pipeline epic                            | Two-digit zero-padded (`01` … `10`)                                       |
| `CR-STORY-NNN` | Crashlytics pipeline user story                      | Three-digit zero-padded; allocated sequentially across all pipeline epics |
| `FF-EPIC-NN`   | Fabric → Firebase migration epic                     | Two-digit zero-padded (`01` … `11`)                                       |
| `FF-AC-NNN`    | Fabric → Firebase migration acceptance criterion     | Three-digit zero-padded; allocated sequentially across all migration epics |

Identifiers are **monotonically assigned and never re-used**. Future edits append new IDs rather than re-number existing ones. This stability lets external trackers (Jira, Linear, GitHub Issues, Azure Boards, etc.) reference an artifact by ID without churn.

## 📝 Templates

### User-Story Template (used in `crashlytics-pipeline/epics-and-stories.md`)

> **As a** `<role>`, **I want** `<capability>`, **so that** `<outcome>`.

Roles span multiple stakeholder personas, including `mobile app user`, `mobile app developer`, `release manager`, `on-call engineer`, `SRE`, `support engineer`, `product manager`, and `data analyst`.

### Acceptance-Criteria Template (used in `fabric-to-firebase-migration/epics-and-acceptance-criteria.md`)

Each criterion is a bulleted Given/When/Then triplet:

- **Given** `<pre-condition>`
- **When** `<action>`
- **Then** `<observable outcome>`

Additional `**And**` lines are permitted under any of the three clauses. Each criterion stands alone so it can be evaluated independently.

## ⚠ Scope Note

These planning artifacts are **documentation only**. They introduce no executable code, no mobile-app skeleton, no CI/CD pipeline, no build-tool configuration, and no dependency changes anywhere in the repository.

The host repository is a Spring Boot 3.4.4 / Java 17 backend service (see [`../pom.xml`](../pom.xml) lines 8 and 30) whose runtime behavior — a RESTful Product CRUD API — is entirely unrelated to mobile crash reporting. The Spring Boot service is **not** the mobile application whose crashes would be reported by a Crashlytics pipeline; it is simply the repository that hosts these planning documents. No reader should infer that the Spring Boot service produces, consumes, or processes crash reports.

## 🔙 Back to project

Return to the project root: [`../README.md`](../README.md)
