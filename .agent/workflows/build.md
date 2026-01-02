---
description: Critical project guidelines and automation rules. MUST be followed strictly.
---

# Build & Development Guidelines

**IMPORTANT**: This document serves as the absolute source of truth for development standards in this project. All code and commands MUST adhere to these rules.

## Tool Usage
- **Gradle**: Always use the system-installed `gradle` command. 
  - ❌ DOES NOT USE: `./gradlew`, `gradle wrapper`.
- **Git**: Use standard git commands. Each new feature should be created in a seperate branch which will be merged upon completion. Branch name format: "omera/<feature_name>".

## Automation Rules

### Auto-Run Safe Commands
The following command types are considered safe and should be run automatically without waiting for specific user approval (unless parameters look suspicious):
- **Gradle**: `gradle build`, `gradle test`, `gradle clean`, `gradle bootRun` (background).
- **NPM/Node**: `npm install`, `npx ...` (read-only/generation tasks).
- **Git**: `git status`, `git log`, `git diff`, `git show`.
- **System**: `ls`, `dir`, `cat`, `type`, `Get-Content`.

### Sensitive Commands (Require Approval)
Any command that modifies persistent state, deletes data, or interacts with remote servers (push):
- `git push`, `git commit`
- `rm`, `del`
- System configuration changes

## Code Quality Standards

### Documentation
- **Mandatory**: All classes and public functions MUST have Javadocs explaining their purpose and behavior.

### Testing
- **Coverage**: Every class and function must have corresponding unit tests.
- **Isolation**: Tests must succeed strictly due to the logic being tested (no side-effects).
- **DisplayName**: Always add display name to tests.
- **Assertions**: Use **Google Truth** or **AssertJ** (`assertThat(...)`) for all assertions.
  - ❌ `assertEquals`, `assertTrue` (JUnit)
  - ✅ `assertThat(actual).isEqualTo(expected)`
  - ✅ Use `assertAll` for multiple independent assertions, each should have its own `.as()` message.
  - Single-assertion tests do NOT need `.as()` messages - the test name is sufficient.

### Libraries & Frameworks
- **Lombok**: Use extensively (`@RequiredArgsConstructor`, `@Data`, etc.).
- **Logging**: Use **Log4j2** (`@Log4j2`) for logging.
  - ❌ `@Slf4j`

## Code Style

### Whitespace & Formatting
- **Empty lines after if/else blocks**: Add an empty line after if blocks (or after the corresponding else) before continuing code:
  ```java
  if (condition) {
      doSomething();
  }

  continueCode();
  ```
- **No empty line after class declaration**: Code should start immediately after the class declaration:
  ```java
  public class MyClass {
      public void myMethod() {  // ✅ No empty line before first member
  ```
  
### Documentation Style
- **Enum values**: Do NOT document individual enum values unless truly non-obvious. Self-explanatory names are preferred.
- **Private methods**: No Javadocs needed unless they contain complex/important logic.