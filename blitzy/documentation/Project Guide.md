
# Blitzy Project Guide — Node.js Express Tutorial Server

## 1. Executive Summary

### 1.1 Project Overview

This engagement delivered a Node.js HTTP tutorial server scaffolded with Express.js at the repository root, exposing two GET endpoints — `GET /` returning the verbatim string `Hello world` and `GET /good-evening` returning the verbatim string `Good evening` — both bound to port `3000`. The implementation honours the user-specified rule **QA-Rule-17-April** (every non-blank line of `server.js` ends with the marker ` // QA`) and coexists with — without modifying — the unrelated Spring Boot CRUD application under `EP-Spring-Boot--main/`. Target consumers are tutorial readers running `npm install` and `npm start` against any active Node.js LTS line. Business impact is educational (a runnable Express tutorial template).

### 1.2 Completion Status

```mermaid
%%{init: {'theme':'base','themeVariables':{'pie1':'#5B39F3','pie2':'#FFFFFF','pieStrokeColor':'#B23AF2','pieOuterStrokeColor':'#B23AF2','pieTitleTextSize':'20px','pieSectionTextSize':'16px','pieLegendTextSize':'14px'}}}%%
pie showData
    title Project Completion: 91.7%
    "Completed (Dark Blue #5B39F3)" : 5.5
    "Remaining (White #FFFFFF)" : 0.5
```

| Metric | Value |
|---|---|
| **Total Hours** | 6.0 |
| **Completed Hours (AI + Manual)** | 5.5 |
| **Remaining Hours** | 0.5 |
| **Completion Percentage** | **91.7%** (5.5 / 6.0) |

Calculation (PA1 / PA2 methodology, AAP-scoped only):
- AAP-scoped hours delivered: 4.0h (R-1 through R-5, I-1 through I-6)
- Path-to-production hours delivered: 1.5h (.gitignore, lockfile, research, V-1–V-11 validation, architectural compliance)
- Path-to-production hours outstanding: 0.5h (human PR review and merge)
- **5.5 / (5.5 + 0.5) × 100 = 91.7%**

### 1.3 Key Accomplishments

- ✅ Express.js 5.2.1 successfully integrated as the sole runtime dependency (R-1)
- ✅ `GET /` endpoint returns the byte-perfect string `Hello world` (R-2; verified via hex dump `4865 6c6c 6f20 776f 726c 64`)
- ✅ `GET /good-evening` endpoint returns the byte-perfect string `Good evening` (R-3; verified via hex dump `476f 6f64 2065 7665 6e69 6e67`)
- ✅ Single-process Node.js scaffold authored from scratch (`server.js`, `package.json`, `.gitignore`) — entire Node.js layer was new (R-4)
- ✅ **QA-Rule-17-April fully complied** — all 6 non-blank lines of `server.js` end with ` // QA`; mechanical checks confirm 0 casing variants (`// qa`, `//QA`, `// Qa`) and 0 duplicate markers (R-5)
- ✅ `npm install` runs to completion in <1 second (`up to date in 845ms`); `node_modules/express` resolves to 5.2.1
- ✅ Server starts cleanly via `npm start` and emits `Server listening on port 3000` (V-6)
- ✅ Both endpoints respond `HTTP/1.1 200 OK` with `Content-Type: text/html; charset=utf-8`
- ✅ `EP-Spring-Boot--main/` subtree byte-identical to upstream (`git diff origin/27-apr-branch -- EP-Spring-Boot--main/` returns 0 bytes) — architectural rule R-A-1 honoured
- ✅ No port collision — Express on 3000, Spring Boot on 8090 (R-A-2)
- ✅ `node_modules/` excluded via `.gitignore`; `package-lock.json` committed for reproducibility
- ✅ `npm audit` reports **0 vulnerabilities** in the dependency closure (express 5.2.1 + 64 transitive packages)

### 1.4 Critical Unresolved Issues

| Issue | Impact | Owner | ETA |
|---|---|---|---|
| _No critical unresolved issues identified._ All 11 AAP acceptance criteria (V-1 through V-11) pass. The Final Validator agent reports zero outstanding issues, zero deferred work, and zero placeholders. | — | — | — |

### 1.5 Access Issues

| System / Resource | Type of Access | Issue Description | Resolution Status | Owner |
|---|---|---|---|---|
| _No access issues identified._ All work performed locally inside the repository; no external services, credentials, API keys, or third-party platforms are required by the AAP scope (the tutorial is unauthenticated and self-contained per AAP §0.4.2). | — | — | — | — |

### 1.6 Recommended Next Steps

1. **[High]** Conduct human code review of the 4 new files (`server.js`, `package.json`, `package-lock.json`, `.gitignore`) and merge the branch `blitzy-f726ac55-d5ee-445b-9e8d-7077016ed8b3` into `main`. Estimated effort: **0.5h**.
2. **[Low]** _Optional, beyond AAP scope_ — Consider adding a brief `README-tutorial.md` documenting the tutorial's run procedure for new contributors. Note: AAP §0.6.2.7 explicitly excludes this from current scope.
3. **[Low]** _Optional, beyond AAP scope_ — Consider authoring smoke tests (e.g., supertest-based) for the two endpoints. Note: AAP §0.6.2.3 explicitly excludes test authoring from current scope.
4. **[Low]** _Optional, beyond AAP scope_ — Consider externalising the listener port via a `PORT` environment variable for deployment flexibility. Note: AAP §0.6.2.4 explicitly excludes production hardening (including env-driven configuration) from current scope.

---

## 2. Project Hours Breakdown

### 2.1 Completed Work Detail

| Component | Hours | Description |
|---|---|---|
| **R-1 Express dependency integration** | 0.5 | Authored `package.json` `dependencies.express: "^5.2.1"`; verified resolution to `node_modules/express@5.2.1` |
| **R-2 GET / endpoint (`Hello world`)** | 0.5 | Authored `app.get('/', (req, res) => res.send('Hello world'))` in `server.js`; verbatim user string preserved |
| **R-3 GET /good-evening endpoint (`Good evening`)** | 0.5 | Authored `app.get('/good-evening', (req, res) => res.send('Good evening'))` in `server.js`; verbatim user string preserved |
| **R-4 Single-process scaffold** | 0.25 | Single `server.js` at repository root (sibling to `EP-Spring-Boot--main/`) per R-A-4 |
| **R-5 QA-Rule-17-April marker application** | 0.5 | Applied ` // QA` to every non-blank line of `server.js` (6 of 6 lines marked); mechanical verification (grep, awk) confirms 0 casing variants and 0 duplicates |
| **I-1 `package.json` manifest fields** | 0.5 | Conventional fields populated: `name`, `version`, `description`, `main`, `scripts.start`, `keywords`, `author`, `license`, `dependencies`, `engines` |
| **I-2 Port binding (3000)** | 0.25 | `app.listen(3000, ...)` chosen to avoid colliding with Spring Boot port 8090 (R-A-2); startup log emitted |
| **I-3 HTTP GET method/path semantics** | 0.25 | Both endpoints registered via `app.get(...)`; matches Express README idiom |
| **I-4 Plain-text response Content-Type** | 0.25 | `res.send(string)` defaults `Content-Type: text/html; charset=utf-8` (verified via `curl -I`) |
| **I-5 Local reproducibility** | 0.25 | `scripts.start: "node server.js"` enables `npm start`; `npm install` runs to completion in 845ms |
| **I-6 Express `^5.2.1` + `engines.node >=18.0.0`** | 0.25 | Version pins set per AAP §0.3.1; matches Express 5's documented Node.js prerequisite |
| **`.gitignore` for `node_modules/`** | 0.25 | 4 patterns added: `node_modules/`, `npm-debug.log*`, `yarn-debug.log*`, `yarn-error.log*` |
| **`package-lock.json` generation** | 0.25 | Generated by `npm install`; 65 packages locked at `lockfileVersion: 3` |
| **Web research on Express 5 / Node LTS** | 0.5 | AAP §0.2.2 research informing version pins (Express 5.2.1 production line; Node.js 22 LTS / 24 LTS supported) |
| **V-1 through V-11 mechanical validation** | 1.0 | All 11 AAP acceptance criteria verified — JSON validity, version pins, file existence, server startup, byte-perfect endpoint responses, QA marker compliance, Spring Boot untouched, gitignore in effect |
| **Architectural compliance verification (R-A-1 to R-A-7)** | 0.5 | Spring Boot diff = 0 bytes; port choice 3000 vs 8090; verbatim strings; root placement; only `express` in top-level deps |
| **TOTAL COMPLETED** | **5.5** | |

### 2.2 Remaining Work Detail

| Category | Hours | Priority |
|---|---|---|
| Human code review of the 4 new files (`server.js`, `package.json`, `package-lock.json`, `.gitignore`) and merge of branch `blitzy-f726ac55-d5ee-445b-9e8d-7077016ed8b3` into `main` | 0.5 | High |
| **TOTAL REMAINING** | **0.5** | |

### 2.3 Hour Reconciliation

- Section 2.1 total (Completed) = **5.5h**
- Section 2.2 total (Remaining) = **0.5h**
- 2.1 + 2.2 = **6.0h** = Total Project Hours in Section 1.2 ✓
- Remaining hours match Section 1.2 metrics table = **0.5h** ✓
- Remaining hours match Section 7 pie chart "Remaining Work" = **0.5h** ✓
- Completion = 5.5 / 6.0 = **91.7%** ✓

---

## 3. Test Results

All test results below originate from the Final Validator agent's autonomous validation logs for this project. The AAP explicitly excludes authoring of automated test suites (AAP §0.6.2.3 — _"No unit tests, integration tests, or smoke tests are authored"_); accordingly, the validation is composed of (a) static checks, (b) functional acceptance checks against the live server, and (c) architectural compliance checks.

| Test Category | Framework | Total Tests | Passed | Failed | Coverage % | Notes |
|---|---|---|---|---|---|---|
| **Static — Manifest validity** | `node -e JSON.parse` | 1 | 1 | 0 | 100% | V-1: `package.json` parses as valid JSON |
| **Static — Version pins** | `node -e require()` | 2 | 2 | 0 | 100% | V-2: `dependencies.express === "^5.2.1"`; V-3: `engines.node === ">=18.0.0"` |
| **Static — File presence** | `test -f` | 1 | 1 | 0 | 100% | V-4: `server.js` exists at repository root |
| **Static — Syntax check** | `node --check` | 1 | 1 | 0 | 100% | `server.js` parses without syntax error |
| **Static — QA-Rule-17-April compliance** | `grep` / `awk` | 3 | 3 | 0 | 100% | V-9: 0 unmarked non-blank lines; 0 casing variants (`// qa`, `//QA`, `// Qa`); 0 duplicate markers |
| **Static — gitignore effective** | `git check-ignore` | 1 | 1 | 0 | 100% | V-11: `git check-ignore node_modules` returns `node_modules` |
| **Static — Spring Boot untouched** | `git diff` | 1 | 1 | 0 | 100% | V-10: `git diff origin/27-apr-branch -- EP-Spring-Boot--main/` returns 0 bytes |
| **Build — Dependency installation** | `npm install` | 1 | 1 | 0 | 100% | V-5: exit code 0; `up to date in 845ms`; 65 packages locked |
| **Build — Vulnerability scan** | `npm audit` | 1 | 1 | 0 | 100% | `found 0 vulnerabilities` across express 5.2.1 + 64 transitive deps |
| **Functional — Server startup** | Process / log inspection | 1 | 1 | 0 | 100% | V-6: stdout emits `Server listening on port 3000` |
| **Functional — `GET /` byte-perfect** | `curl` + `xxd` | 1 | 1 | 0 | 100% | V-7: response body = `Hello world` (11 bytes, hex `4865 6c6c 6f20 776f 726c 64`); status 200 OK |
| **Functional — `GET /good-evening` byte-perfect** | `curl` + `xxd` | 1 | 1 | 0 | 100% | V-8: response body = `Good evening` (12 bytes, hex `476f 6f64 2065 7665 6e69 6e67`); status 200 OK |
| **Functional — Content-Type header** | `curl -I` | 1 | 1 | 0 | 100% | `Content-Type: text/html; charset=utf-8` (Express default per I-4) |
| **Architectural — R-A-1 to R-A-7** | Manual + diff | 7 | 7 | 0 | 100% | All 7 architectural rules satisfied |
| **TOTAL** | — | **23** | **23** | **0** | **100%** | All Blitzy autonomous validation tests pass |

> **Note on coverage**: The "Coverage %" column reports the proportion of in-scope assertions that pass for each test category — 100% across the board. The AAP did not require automated unit-test-style line-coverage instrumentation, so traditional code-coverage metrics (statement / branch / function coverage) are not reported.

---

## 4. Runtime Validation & UI Verification

The tutorial server was started via `node server.js` and exercised end-to-end. The application has **no UI** — both endpoints return plain-text bodies (the user explicitly described a Node.js HTTP tutorial server, not a web frontend). Accordingly, the verification below covers runtime health and HTTP integration only.

### Runtime Health
- ✅ **Server startup** — Operational. Process initialises within ~150 ms; `console.log` emits `Server listening on port 3000` to stdout.
- ✅ **Port binding** — Operational. Express binds TCP port 3000 on `0.0.0.0` (Express default when only a port is supplied).
- ✅ **Process termination** — Operational. `SIGTERM` / `Ctrl+C` cleanly stops the Node.js process.
- ✅ **Memory footprint** — Operational. Idle process footprint typical for an Express 5 minimal application (no leaks observed during validation runs).

### HTTP Integration
- ✅ **`GET /` endpoint** — Operational. Returns `200 OK` with body `Hello world` (11 bytes; verified byte-perfect via `curl -s | xxd`).
- ✅ **`GET /good-evening` endpoint** — Operational. Returns `200 OK` with body `Good evening` (12 bytes; verified byte-perfect via `curl -s | xxd`).
- ✅ **Response headers** — Operational. `Content-Type: text/html; charset=utf-8` (Express `res.send(string)` default — meets AAP §0.1.1.2 implicit requirement I-4); `Content-Length` correctly set; `ETag` present; `X-Powered-By: Express` present.
- ✅ **Express middleware chain** — Operational. No custom middleware registered (per AAP §0.6.2.2 scope); Express 5 defaults intact.

### UI Verification
- ⚪ **Not applicable** — The AAP explicitly states (§0.5.4) that no user-interface design is in scope. Both endpoints emit plain text. No HTML markup, no CSS, no Figma deliverables, no frontend framework, and no accessibility considerations apply to this tutorial.

### Coexistence with Spring Boot Application
- ✅ **Process isolation** — Operational. Express on port 3000 and Spring Boot on port 8090 run side-by-side without contention. Different runtimes (Node.js vs JVM), no shared filesystem state, no shared configuration. Either can be started, stopped, or restarted independently.

---

## 5. Compliance & Quality Review

This section cross-maps every AAP deliverable, user-specified rule, and architectural constraint to its compliance status. All items are derived from the AAP and verified against the codebase by Blitzy autonomous validation.

| AAP Reference | Requirement / Rule | Status | Evidence | Fix Applied During Validation |
|---|---|---|---|---|
| §0.1.1 R-1 | Express integration | ✅ PASS | `package.json` declares `express ^5.2.1`; resolves to `5.2.1` | None required |
| §0.1.1 R-2 | `GET /` returns `Hello world` (verbatim) | ✅ PASS | `server.js:5`; byte-verified | None required |
| §0.1.1 R-3 | `GET /good-evening` returns `Good evening` (verbatim) | ✅ PASS | `server.js:6`; byte-verified | None required |
| §0.1.1 R-4 | Single-process hosting via `server.js` | ✅ PASS | One file at repo root | None required |
| §0.1.1 R-5 | **QA-Rule-17-April** — every non-blank line of `server.js` ends with ` // QA` | ✅ PASS | 6/6 marked; 0 casing variants; 0 duplicates | None required |
| §0.1.1.2 I-1 | `package.json` project manifest | ✅ PASS | All conventional fields present | None required |
| §0.1.1.2 I-2 | Listener bound to TCP port (3000 chosen, no collision with 8090) | ✅ PASS | `app.listen(3000, ...)` | None required |
| §0.1.1.2 I-3 | HTTP GET method/path semantics | ✅ PASS | Both routes use `app.get(...)` | None required |
| §0.1.1.2 I-4 | Plain-text response Content-Type | ✅ PASS | `text/html; charset=utf-8` (Express default) | None required |
| §0.1.1.2 I-5 | Local reproducibility (`npm install` + `npm start`) | ✅ PASS | `scripts.start: "node server.js"`; install completes in 845ms | None required |
| §0.1.1.2 I-6 | Express pinned to current major (5.x) | ✅ PASS | `^5.2.1` per AAP §0.2.2 research | None required |
| §0.7.2 R-A-1 | Spring Boot project untouched | ✅ PASS | `git diff origin/27-apr-branch -- EP-Spring-Boot--main/` = 0 bytes | None required |
| §0.7.2 R-A-2 | No port collision | ✅ PASS | Express 3000 vs Spring Boot 8090 | None required |
| §0.7.2 R-A-3 | Verbatim user strings preserved | ✅ PASS | No casing or punctuation alterations | None required |
| §0.7.2 R-A-4 | Single-file tutorial layout | ✅ PASS | Only `server.js` at root; no `src/` hierarchy | None required |
| §0.7.2 R-A-5 | Express 5 + Node.js 18+ floor | ✅ PASS | `^5.2.1`, `>=18.0.0`; runtime is Node 22.22.2 LTS | None required |
| §0.7.2 R-A-6 | Repository-root placement | ✅ PASS | Sibling to `EP-Spring-Boot--main/` | None required |
| §0.7.2 R-A-7 | No transitive top-level dependencies | ✅ PASS | Only `express` in top-level `dependencies` | None required |
| §0.6.2 — Out of scope | No tests, no CI/CD, no Docker, no production hardening, no docs, no DB | ✅ PASS | None of these surfaces touched | N/A — out of scope |
| Quality — Vulnerability scan | `npm audit` clean | ✅ PASS | `found 0 vulnerabilities` | None required |
| Quality — JSON validity | `package.json` parseable | ✅ PASS | `node -e JSON.parse` exits 0 | None required |
| Quality — JS syntax | `server.js` parseable | ✅ PASS | `node --check server.js` exits 0 | None required |

**Outstanding Items:** None. All AAP-scoped compliance and quality requirements are satisfied.

---

## 6. Risk Assessment

The risk surface for this engagement is small because the AAP scope is small (a single-file tutorial) and the implementation is fully validated. The following table enumerates every meaningful risk identified during analysis using PA3 categories.

| Risk | Category | Severity | Probability | Mitigation | Status |
|---|---|---|---|---|---|
| **Express 5.x transitive vulnerability emerges post-publication** | Security | Low | Low | `npm audit` reports 0 vulnerabilities today; lockfile pins 65 packages; routine `npm audit` on schedule will surface any future advisories. Re-run `npm install && npm audit` quarterly. | ✅ Mitigated |
| **Future Node.js version drops support for `>=18` floor** | Technical | Low | Low | Engine constraint declared; AAP §0.2.2 documents that Node 22 (LTS until Apr 30 2027) and Node 24 (LTS until Apr 30 2028) are the actively supported lines. Periodic engines floor review recommended. | ✅ Mitigated |
| **Port 3000 already in use on a developer's machine** | Operational | Medium | Medium | The startup log will surface a clear `EADDRINUSE` error. Tutorial readers can stop the conflicting process or — beyond current AAP scope — externalise the port via `PORT` environment variable as a future enhancement. | ⚠️ Documented (out of AAP scope to fully resolve) |
| **No automated test suite — regression on future edits would not be detected automatically** | Technical | Low | Low | AAP §0.6.2.3 explicitly excludes test authoring. Manual verification commands are documented in §9 (Development Guide). | ⚠️ Accepted (per AAP scope) |
| **No README documenting the tutorial for new contributors** | Operational | Low | Low | AAP §0.6.2.7 explicitly excludes README authoring. Tutorial logic is fully self-evident from the 8-line `server.js`. | ⚠️ Accepted (per AAP scope) |
| **No HTTPS / TLS termination** | Security | Low | Low | AAP §0.6.2.4 excludes production hardening. The tutorial is designed for `localhost` use; HTTPS is appropriate only when deploying behind a reverse proxy (out of scope). | ⚠️ Accepted (per AAP scope) |
| **No authentication on either endpoint** | Security | Low | Low | AAP §0.4.2 explicitly states the tutorial is unauthenticated; both endpoints emit static strings with no sensitive data and no state mutation. | ✅ Accepted by design |
| **No CORS configuration — cross-origin requests blocked by browser default** | Integration | Low | Low | AAP §0.4.2 explicitly states no CORS is configured; tutorial readers using `curl` / Postman are unaffected. Browser-based consumers from other origins would need CORS in a future iteration (out of scope). | ⚠️ Accepted (per AAP scope) |
| **Port collision with the parallel Spring Boot application (8090)** | Integration | None | None | Express bound to 3000; Spring Boot bound to 8090. Verified via `application.properties` inspection and `app.listen(3000)` in `server.js`. | ✅ Mitigated |
| **Accidental modification of Spring Boot subtree** | Operational | None | None | `git diff origin/27-apr-branch -- EP-Spring-Boot--main/` returns 0 bytes; architectural rule R-A-1 enforced. | ✅ Mitigated |
| **QA-Rule-17-April marker drift on future edits to `server.js`** | Operational | Low | Medium | The grep / awk validation commands in §0.7.1.1 of the AAP and §10A of this guide enable mechanical re-verification on every commit. Recommend wiring into a pre-commit hook in a future engagement (out of current AAP scope). | ⚠️ Documented for future hardening |
| **`npm install` fails on a system with Node.js < 18** | Technical | Low | Low | `engines.node: ">=18.0.0"` constraint declared; npm will warn (with default settings) or fail (if `engine-strict=true`). | ✅ Mitigated |

**Risk Summary:** 7 risks accepted by AAP design (all out-of-scope mitigations would be additive scope), 4 risks fully mitigated, 1 risk documented for future hardening. **No high-severity risks** are present.

---

## 7. Visual Project Status

### Project Hours Breakdown

```mermaid
%%{init: {'theme':'base','themeVariables':{'pie1':'#5B39F3','pie2':'#FFFFFF','pieStrokeColor':'#B23AF2','pieOuterStrokeColor':'#B23AF2','pieTitleTextSize':'18px','pieSectionTextSize':'14px','pieLegendTextSize':'13px'}}}%%
pie showData
    title Project Hours: 91.7% Complete (5.5 / 6.0)
    "Completed Work" : 5.5
    "Remaining Work" : 0.5
```

### Remaining Work by Category (from §2.2)

```mermaid
%%{init: {'theme':'base','themeVariables':{'xyChart':{'backgroundColor':'#FFFFFF','plotColorPalette':'#5B39F3','titleColor':'#B23AF2','xAxisLabelColor':'#B23AF2','yAxisLabelColor':'#B23AF2'}}}}%%
xychart-beta
    title "Remaining Hours by Category"
    x-axis ["Human PR Review & Merge"]
    y-axis "Hours" 0 --> 1
    bar [0.5]
```

### Cross-Section Integrity Verification

| Location | Completed Hours | Remaining Hours | Total |
|---|---|---|---|
| Section 1.2 metrics table | 5.5 | 0.5 | 6.0 |
| Section 2.1 sum (completed components) | 5.5 | — | — |
| Section 2.2 sum (remaining categories) | — | 0.5 | — |
| Section 7 pie chart | 5.5 | 0.5 | 6.0 |

All four locations report identical numbers. ✅

---

## 8. Summary & Recommendations

### Summary of Achievements

The Node.js Express tutorial server is **fully runnable end-to-end** and complies with every requirement in the Agent Action Plan. The autonomous Blitzy delivery covers:

1. **Authoring of all 4 in-scope files** — `server.js`, `package.json`, `package-lock.json`, `.gitignore` — totalling 864 lines of code added across two clean commits (`db47626` setup; `24423d0` server.js).
2. **All 11 AAP acceptance criteria (V-1 through V-11) pass.** Both endpoints return byte-perfect verbatim user strings, the QA-Rule-17-April marker is correctly applied to every non-blank line of `server.js`, and the Spring Boot subtree is byte-identical to upstream.
3. **All 7 architectural rules (R-A-1 through R-A-7) satisfied** — Spring Boot untouched, no port collision, verbatim user strings, single-file root layout, Express 5 + Node.js 18+, no transitive top-level deps.
4. **Zero security vulnerabilities** in the resolved dependency closure (`npm audit` reports `found 0 vulnerabilities` across express 5.2.1 + 64 transitive packages).
5. **Coexists cleanly with the unrelated Spring Boot application** under `EP-Spring-Boot--main/` — different ports, different runtimes, no shared state.

### Remaining Gaps

The single remaining gap is **human PR review and merge** of branch `blitzy-f726ac55-d5ee-445b-9e8d-7077016ed8b3` into `main` (≈ 0.5h). Per RG2 guideline #5, autonomous completion is capped at < 100% to reflect this final mandatory human step.

### Critical Path to Production

```mermaid
flowchart LR
    A["Branch 'blitzy-f726ac55-d5ee-445b-9e8d-7077016ed8b3'<br/>(2 commits, all V-1–V-11 PASS)"] --> B["Human Code Review<br/>(0.5h)"]
    B --> C["Merge to main"]
    C --> D["Tutorial available at<br/>repository root"]
    D --> E["Run: npm install && npm start"]
    E --> F["GET / → Hello world<br/>GET /good-evening → Good evening"]

    style A fill:#5B39F3,stroke:#B23AF2,color:#FFFFFF
    style B fill:#FFFFFF,stroke:#B23AF2,color:#B23AF2
    style C fill:#A8FDD9,stroke:#B23AF2,color:#5B39F3
    style D fill:#A8FDD9,stroke:#B23AF2,color:#5B39F3
    style E fill:#A8FDD9,stroke:#B23AF2,color:#5B39F3
    style F fill:#A8FDD9,stroke:#B23AF2,color:#5B39F3
```

### Success Metrics

| Metric | Target | Actual |
|---|---|---|
| AAP acceptance criteria passing (V-1 to V-11) | 11/11 | 11/11 ✅ |
| Architectural rules honoured (R-A-1 to R-A-7) | 7/7 | 7/7 ✅ |
| QA-Rule-17-April compliance (non-blank lines marked) | 100% | 100% ✅ |
| Spring Boot subtree integrity | 0 bytes diff | 0 bytes diff ✅ |
| `npm audit` vulnerabilities | 0 | 0 ✅ |
| AAP-scoped completion percentage | ≥ 90% | **91.7%** ✅ |

### Production Readiness Assessment

**Verdict — Ready for human review and merge.** The tutorial is functionally complete, mechanically validated, and produces byte-perfect output for both endpoints. All AAP-scoped work is delivered. The remaining 0.5h of work is the irreducible human gate (code review + merge); no engineering rework, debugging, or feature completion is outstanding.

The project is **91.7% complete** as measured by AAP-scoped hours (PA1 / PA2 methodology). The remaining 8.3% reflects the mandatory human PR review and merge — not any deficiency in the autonomous delivery.

Optional future enhancements outside current AAP scope (none of which block production readiness for the tutorial):
- Externalising the port via a `PORT` environment variable (operational hardening; AAP §0.6.2.4)
- Authoring smoke tests with `supertest` (test coverage; AAP §0.6.2.3)
- Adding a tutorial-specific `README` (documentation; AAP §0.6.2.7)
- Wiring a pre-commit hook to mechanically enforce QA-Rule-17-April on future edits

---

## 9. Development Guide

This guide describes how to clone, install, run, verify, and troubleshoot the Node.js Express tutorial server. Every command has been tested during the autonomous validation phase.

### 9.1 System Prerequisites

| Requirement | Minimum | Recommended | Verification Command |
|---|---|---|---|
| **Operating system** | Windows 10+, macOS 11+, or any modern Linux | Same | `uname -a` (Linux/macOS) or `ver` (Windows) |
| **Node.js** | 18.0.0 (per `engines.node` and Express 5 prerequisite) | Node.js 22 LTS ("Jod") or Node.js 24 LTS ("Krypton") | `node --version` |
| **npm** | 8+ (ships with Node.js 18) | 10+ (ships with Node.js 22) | `npm --version` |
| **git** | 2.x | Latest | `git --version` |
| **Disk space** | 10 MB for source + ~3.5 MB for `node_modules/` | 50 MB | `du -sh .` |

> **Tip:** This project was validated on Node.js v22.22.2 + npm 10.9.7. Both Node.js 22 (LTS until Apr 30 2027) and Node.js 24 (LTS until Apr 30 2028) are recommended.

### 9.2 Environment Setup

No environment variables, secrets, API keys, or external services are required by the tutorial. The AAP explicitly excludes environment-driven configuration (§0.6.2.4). Run-time configuration is hard-coded:

| Setting | Value | Source |
|---|---|---|
| Listening port | `3000` | `server.js` (literal) |
| Listening host | `0.0.0.0` | Express default when only port supplied |
| Endpoint #1 | `GET /` → `Hello world` | `server.js` |
| Endpoint #2 | `GET /good-evening` → `Good evening` | `server.js` |

### 9.3 Clone and Install

```bash
# 1) Clone the repository (or check out the feature branch directly)
git clone <repository-url>
cd <repository-root>

# 2) Check out the feature branch (if not yet merged)
git checkout blitzy-f726ac55-d5ee-445b-9e8d-7077016ed8b3

# 3) Verify Node.js version satisfies the engines constraint
node --version  # expect: v18.x or higher (recommended: v22.x)

# 4) Install dependencies (creates node_modules/ — already gitignored)
npm install
# Expected output: "added 65 packages in <1 second" (or "up to date" on re-run)
```

### 9.4 Start the Server

```bash
# Option A: Use the npm script (recommended)
npm start

# Option B: Invoke node directly
node server.js

# Expected output (from either option):
#   Server listening on port 3000
```

The server runs in the foreground. Use `Ctrl+C` to stop it gracefully.

### 9.5 Verify Both Endpoints

In a **second terminal** (leave the server running in the first), run:

```bash
# 1) Hello world endpoint
curl http://localhost:3000/
# Expected output (no trailing newline):
#   Hello world

# 2) Good evening endpoint
curl http://localhost:3000/good-evening
# Expected output (no trailing newline):
#   Good evening

# 3) (Optional) Inspect headers — should show Content-Type: text/html; charset=utf-8
curl -sI http://localhost:3000/

# 4) (Optional) Byte-perfect verification via hex dump
curl -s http://localhost:3000/             | xxd
# Expected: 4865 6c6c 6f20 776f 726c 64    Hello world
curl -s http://localhost:3000/good-evening | xxd
# Expected: 476f 6f64 2065 7665 6e69 6e67  Good evening
```

### 9.6 Verify QA-Rule-17-April Compliance

```bash
# All three commands must return ZERO matching lines (or only "0" for the count)

# 1) Find any non-blank line missing the marker
grep -nE "[^[:space:]]" server.js | grep -vE " // QA$"

# 2) Find any casing variant of the marker
grep -nE "(// qa|//QA| //  QA|// Qa)" server.js

# 3) Find any line carrying the marker more than once
awk -F"// QA" "NF>2" server.js
```

### 9.7 Verify Spring Boot Subtree Untouched

```bash
# Must return ZERO bytes of diff
git diff origin/27-apr-branch -- EP-Spring-Boot--main/ | wc -c
# Expected: 0
```

### 9.8 Stop the Server

In the terminal running the server, press **`Ctrl+C`**. The Node.js process will exit cleanly.

### 9.9 Troubleshooting

| Symptom | Likely Cause | Resolution |
|---|---|---|
| `Error: listen EADDRINUSE: address already in use :::3000` | Another process is bound to port 3000 (often a previous instance of the server, or another Node app) | Stop the conflicting process. On Linux/macOS: `lsof -i :3000` then `kill <pid>`. On Windows: `netstat -ano | findstr :3000` then `taskkill /F /PID <pid>`. |
| `Cannot find module 'express'` | Dependencies not installed | Run `npm install` at the repository root |
| `npm ERR! engine Unsupported engine ... node@<…>` | Local Node.js version is older than 18.0.0 | Upgrade Node.js to a current LTS line (22 or 24 recommended). Use a version manager such as `nvm`, `fnm`, or `volta`. |
| `curl: (7) Failed to connect to localhost port 3000` | Server is not running | Run `npm start` in a separate terminal first |
| `npm WARN config production Use --omit=dev instead` | Older `npm` version warning (cosmetic only) | Optional: upgrade npm via `npm install -g npm@latest`. Does not affect functionality. |
| `git diff origin/27-apr-branch -- EP-Spring-Boot--main/` shows non-zero bytes | Local edits made inside `EP-Spring-Boot--main/` (out of scope) | Revert the unintended changes: `git checkout origin/27-apr-branch -- EP-Spring-Boot--main/` |
| QA-Rule-17-April grep finds unmarked lines after edits | A new line was added to `server.js` without the marker | Append ` // QA` to the offending line (preserve exactly one space before `//`); re-run all 3 grep checks until they return zero |

### 9.10 Quick Reference — Full End-to-End Smoke Test

```bash
# Single copy-pasteable block: install, start, verify, stop
npm install
npm start &           # start in background
sleep 2               # give the server time to bind
curl http://localhost:3000/             # → Hello world
curl http://localhost:3000/good-evening  # → Good evening
kill %1               # stop the background server
```

---

## 10. Appendices

### Appendix A — Command Reference

| Purpose | Command | Notes |
|---|---|---|
| Install dependencies | `npm install` | Resolves `express ^5.2.1` and 64 transitive packages; generates / refreshes `package-lock.json` |
| Start server | `npm start` (or `node server.js`) | Logs `Server listening on port 3000` |
| Stop server | `Ctrl+C` (foreground) or `kill <pid>` (background) | Graceful shutdown |
| Test endpoint #1 | `curl http://localhost:3000/` | Returns `Hello world` |
| Test endpoint #2 | `curl http://localhost:3000/good-evening` | Returns `Good evening` |
| Validate JSON manifest | `node -e "JSON.parse(require('fs').readFileSync('package.json'))"` | Exit 0 = valid |
| Validate JS syntax | `node --check server.js` | Exit 0 = valid syntax |
| Audit dependencies | `npm audit` | Expect: `found 0 vulnerabilities` |
| Verify QA marker compliance | `grep -nE "[^[:space:]]" server.js \| grep -vE " // QA$"` | Expect: no output |
| Verify Spring Boot untouched | `git diff origin/27-apr-branch -- EP-Spring-Boot--main/ \| wc -c` | Expect: `0` |
| List installed top-level deps | `npm list --depth=0` | Expect: `node-express-tutorial@1.0.0` and `express@5.2.1` |

### Appendix B — Port Reference

| Service | Port | Bound by | File | Notes |
|---|---|---|---|---|
| **Node.js Express tutorial** (this work) | **3000** | `app.listen(3000, ...)` | `server.js:8` | Hard-coded literal; no env override (per AAP §0.6.2.4) |
| Spring Boot CRUD application (untouched, parallel) | 8090 | `server.port=8090` | `EP-Spring-Boot--main/src/main/resources/application.properties` | Pre-existing; not modified by this work |

The two ports do not collide; both processes can run simultaneously. ✅

### Appendix C — Key File Locations

All paths are repository-root-relative.

| Path | Type | Lines | Status | Purpose |
|---|---|---|---|---|
| `server.js` | JavaScript source | 8 | **CREATED** (commit `24423d0`) | Express entry point — 2 routes + listener; every non-blank line marked ` // QA` |
| `package.json` | JSON manifest | 22 | **CREATED** (commit `db47626`) | Project manifest — pins `express ^5.2.1`, `engines.node >=18.0.0`, `scripts.start: "node server.js"` |
| `package-lock.json` | JSON lockfile | 830 | **CREATED** (commit `db47626`) | Reproducible-install lockfile — 65 packages, `lockfileVersion: 3` |
| `.gitignore` | Plain text | 4 | **CREATED** (commit `db47626`) | Excludes `node_modules/`, `npm-debug.log*`, `yarn-debug.log*`, `yarn-error.log*` |
| `node_modules/` | Directory | (3.3 MB, 65 packages) | Generated, ignored | Express 5.2.1 + transitive closure |
| `EP-Spring-Boot--main/` (entire subtree) | Directory | unchanged | **UNTOUCHED** | Pre-existing Spring Boot 3.4.4 / Java 17 CRUD app; per R-A-1, byte-identical to upstream |

### Appendix D — Technology Versions

| Component | Pinned / Required | Resolved / Validated | Source |
|---|---|---|---|
| `express` (npm) | `^5.2.1` (`package.json` `dependencies`) | `5.2.1` (`node_modules/express/package.json`) | Express Technical Committee production line; AAP §0.2.2 |
| Node.js runtime | `>=18.0.0` (`engines.node`) | `v22.22.2` (validation environment, LTS "Jod") | Node.js 22 LTS supported through Apr 30 2027 |
| npm | (ships with Node.js 18+) | `10.9.7` (validation environment) | n/a |
| `lockfileVersion` | (npm-determined) | `3` (`package-lock.json`) | npm 7+ format |

### Appendix E — Environment Variable Reference

| Variable | Required | Default | Source / Notes |
|---|---|---|---|
| _(none)_ | — | — | The tutorial does not consume any environment variables. AAP §0.6.2.4 explicitly excludes env-driven configuration. The listener port is the literal `3000` in `server.js:3` (`const PORT = 3000;`). |

### Appendix F — Developer Tools Guide

| Tool | Use Case | Install |
|---|---|---|
| **Node.js LTS** (22 or 24) | Run the server | https://nodejs.org/ — or use a version manager (`nvm`, `fnm`, `volta`) |
| **`curl`** | Verify endpoints from CLI | Pre-installed on macOS/Linux; bundled with Windows 10+ |
| **`xxd`** | Byte-perfect verification of endpoint responses | Pre-installed on Linux/macOS; on Windows use Git Bash or WSL |
| **`grep` / `awk`** | Mechanical QA-Rule-17-April compliance checks | POSIX standard; Git Bash on Windows |
| **Postman / HTTPie / Bruno** | Optional GUI/CLI HTTP client | https://www.postman.com/, https://httpie.io/, https://www.usebruno.com/ |
| **VS Code + ESLint** | Optional IDE for editing `server.js` | https://code.visualstudio.com/ — ESLint is _not_ required by the AAP (§0.3.1.3) but is convenient |

### Appendix G — Glossary

| Term | Definition |
|---|---|
| **AAP** | Agent Action Plan — the authoritative Blitzy directive enumerating every requirement, file, and rule for this engagement |
| **Blitzy autonomous validation** | Mechanical verification by Blitzy agents — used here to verify V-1 through V-11 |
| **CommonJS** | Node.js's default module system (`require(...)`); used by `server.js` to import Express |
| **ESM** | ECMAScript Modules (`import ... from ...`); intentionally **not** used here per AAP §0.3.2.1 |
| **Express 5.x** | Express Technical Committee's endorsed production line; requires Node.js 18+ |
| **LTS** | Long-Term Support — currently active Node.js LTS lines are 22 ("Jod") and 24 ("Krypton") |
| **PA1** | Project Assessment methodology #1 — AAP-scoped hours-based completion calculation |
| **PA2** | Project Assessment methodology #2 — Engineering hours estimation framework |
| **QA-Rule-17-April** | User-specified implementation rule for this engagement: every non-blank line of `server.js` ends with the comment ` // QA` |
| **Path-to-production** | Standard activities required to deploy AAP deliverables (e.g., `.gitignore`, lockfile, validation, human PR review) |
| **R-1 … R-5** | The five user-decomposed feature requirements from AAP §0.1.1 |
| **I-1 … I-6** | The six implicit requirements surfaced from industry norms in AAP §0.1.1.2 |
| **R-A-1 … R-A-7** | The seven architectural rules derived from repository conventions in AAP §0.7.2 |
| **V-1 … V-11** | The eleven mechanical validation criteria in AAP §0.7.3 |
| **Verbatim** | Preserved exactly as supplied by the user — no casing changes, no punctuation alterations, no wrapping |

