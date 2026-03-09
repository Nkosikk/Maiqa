# Test Automation Framework Baseline

## Purpose
- This repository is a clean starter framework.
- It intentionally contains only minimal automation scaffolding.
- Add scenario-specific implementation only when a request is received.

## Baseline Files (Current)
- `pom.xml`: dependencies and Surefire include list.
- `testng.xml`: TestNG suite entry.
- `src/test/java/runners/TestNGRunner.java`: Cucumber + TestNG runner.
- `src/test/java/features/smoke/Smoke.feature`: baseline smoke scenario.
- `src/test/java/StepDefinations/Base.java`: shared WebDriver lifecycle.
- `src/test/java/StepDefinations/stepsDef.java`: sample step bindings.
- `src/test/java/Helpers/BrowserSetup.java`: browser bootstrap and options.
- `src/test/java/Utils/ReusableFunctions.java`: generic Selenium helpers.
- `src/test/java/API/Common/*`: API shared constants/config primitives.
- `src/Configurations.properties`: token placeholders only (no secrets).

## Prerequisites
- Java 11
- Maven 3.8+
- Chrome/Firefox/Edge installed locally for UI runs

## Run The Baseline
- Default:
```bash
mvn test
```
- With runtime overrides:
```bash
mvn test -DbaseUrl=https://example.com -Dbrowser=chrome -Dheadless=true
```

## Runtime Controls
- `baseUrl`: target URL opened at session start. Default: `https://example.com`.
- `browser`: `chrome`, `firefox`, or `edge`. Default: `chrome`.
- `headless`: `true`/`false`. Default: `true`.

## File Relationship Rules

### UI Chain
- `*.feature` -> step definition method (`StepDefinations/*`) -> optional page/helper class -> `BrowserSetup` -> `TestNGRunner`.

### API Chain
- API test class (`API/Tests/*`) -> request builder (`API/Common/RequestBuilders/*`) -> payload builder (`API/Common/PayloadBuilders/*`) -> shared config/constants (`API/Common/*`) -> runtime values (`Configurations.properties`).

## Scenario Intake Normalization
- Any incoming request format (ticket text, plain text, pseudo-steps, or Gherkin) MUST be normalized into a runnable feature file.
- Final scenario shape MUST be `Feature` + `Scenario` or `Scenario Outline` + `Examples` when data variation exists.
- Variable input values MUST use placeholders to support reuse and environment portability.
- Step phrases MUST describe intent and be reusable across scenarios; avoid one-off wording when behavior is identical.

## Step Definition Contract
- Each step phrase MUST map to exactly one annotated method.
- Step definition classes MUST orchestrate flow only and MUST NOT contain raw locator logic.
- If behavior already exists, reuse the existing step phrase/method instead of adding semantic duplicates.
- Assertions and browser interactions SHOULD be delegated to page/helper classes.

## Page Object Contract
- UI locators and UI operations MUST live in page/helper classes, not in step definitions.
- New page classes MUST be organized by functional area under `src/test/java/Pages/`.
- Page methods MUST encapsulate waits and action stability concerns.
- Step definitions SHOULD call high-level page methods rather than low-level Selenium commands.

## Test Data And Environment Contract
- Secrets (credentials, tokens, API keys) MUST NOT be hardcoded in Java or feature files.
- Runtime values MUST be injected through properties, environment variables, or JVM system properties.
- `src/Configurations.properties` MUST remain a template in version control.
- Scenario data SHOULD remain environment-agnostic so the same script can run across environments with only config changes.

## New UI Scenario Workflow
1. Add a feature file under `src/test/java/features/<area>/`.
2. Add matching step annotations/methods in `StepDefinations/stepsDef.java` or a new class in `StepDefinations/`.
3. If UI interactions grow, create `src/test/java/Pages/<Area>/<PageName>.java` and keep locators/actions there.
4. Keep `TestNGRunner` glue aligned with your step-definition package.
5. Run `mvn test` before opening PR.

## New API Scenario Workflow
1. Create a test class in `src/test/java/API/Tests/`.
2. Create `RequestBuilders` and `PayloadBuilders` folders when first needed:
    - `src/test/java/API/Common/RequestBuilders/`
    - `src/test/java/API/Common/PayloadBuilders/`
3. Put endpoint calls in request builders and JSON object creation in payload builders.
4. Add JSON schemas to `src/test/java/API/Common/Schemas/` only if schema validation is required.
5. Add new API test class include in `pom.xml` if you want it in default suite execution.

## Guardrails
- Never commit real credentials, tokens, or environment-specific secrets.
- Keep tests environment-agnostic; inject env differences via properties.
- Keep step text and step-definition annotations exactly synchronized.
- Prefer reusable helpers/page classes over duplicating Selenium code in steps.

## Definition Of Done For A New Script
- Scenario compiles and runs via Maven.
- Added files follow framework folder conventions.
- No hardcoded secrets or account data.
- Assertions are deterministic and tied to the scenario outcome.
