# Project Rename Plan: single-facility → FacilityEpiSim

## Pre-Requisites

- [ ] **Close Eclipse completely** (critical - prevents caching issues)
- [ ] **Create a backup branch**: `git checkout -b backup-before-rename`
- [ ] **Commit any uncommitted changes**

---

## Phase 1: Core Repast Simphony Identity (CRITICAL)

These changes must be synchronized - the runtime matches folder name to context ID.

### Step 1.1: Rename the .rs Folder
```
single-facility.rs/  →  FacilityEpiSim.rs/
```
**Command**: `git mv single-facility.rs FacilityEpiSim.rs`

### Step 1.2: Update context.xml
**File**: `FacilityEpiSim.rs/context.xml`

| Line | Change |
|------|--------|
| 1 | `id="single-facility"` → `id="FacilityEpiSim"` |

### Step 1.3: Update user_path.xml
**File**: `FacilityEpiSim.rs/user_path.xml`

| Line | Change |
|------|--------|
| 1 | `name="single-facility"` → `name="FacilityEpiSim"` |

### Step 1.4: Update scenario.xml
**File**: `FacilityEpiSim.rs/scenario.xml`

| Line | Change |
|------|--------|
| 3 | `context="single-facility"` → `context="FacilityEpiSim"` |
| 4 | `context="single-facility"` → `context="FacilityEpiSim"` |
| 5 | `context="single-facility"` → `context="FacilityEpiSim"` |

---

## Phase 2: Java Class Rename (Recommended)

### Step 2.1: Rename the Builder Class File
```
src/builders/SingleFacilityBuilder.java  →  src/builders/FacilityEpiSimBuilder.java
```
**Command**: `git mv src/builders/SingleFacilityBuilder.java src/builders/FacilityEpiSimBuilder.java`

### Step 2.2: Update Class Declaration
**File**: `src/builders/FacilityEpiSimBuilder.java`

| Change |
|--------|
| `public class SingleFacilityBuilder` → `public class FacilityEpiSimBuilder` |

### Step 2.3: Update Data Loader Reference
**File**: `FacilityEpiSim.rs/repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_2.xml`

| Line | Change |
|------|--------|
| 1 | `builders.SingleFacilityBuilder` → `builders.FacilityEpiSimBuilder` |

### Step 2.4: Update All Java Files That Import/Reference the Builder

| File | Changes Required |
|------|------------------|
| `src/disease/FacilityOutbreak.java` | Update import + 2 usages |
| `src/agents/Person.java` | Update import + 2 usages |
| `src/disease/PersonDisease.java` | Update import + 4 usages |
| `src/agentcontainers/Facility.java` | Update import + 6+ usages |
| `src/agentcontainers/Region.java` | Update import + 2 usages |

**Pattern for each file**:
```java
// Change import
import builders.SingleFacilityBuilder;  →  import builders.FacilityEpiSimBuilder;

// Change all references
SingleFacilityBuilder.isBatchRun  →  FacilityEpiSimBuilder.isBatchRun
SingleFacilityBuilder.xxx  →  FacilityEpiSimBuilder.xxx
```

---

## Phase 3: Eclipse Project Configuration

### Step 3.1: Update .project File
**File**: `.project`

| Line | Change |
|------|--------|
| 3 | `<name>single-facility</name>` → `<name>FacilityEpiSim</name>` |

### Step 3.2: Rename Launch Files
```
launchers/single-facility Model.launch        →  launchers/FacilityEpiSim Model.launch
launchers/Batch single-facility Model.launch  →  launchers/Batch FacilityEpiSim Model.launch
```

### Step 3.3: Update FacilityEpiSim Model.launch
**File**: `launchers/FacilityEpiSim Model.launch`

| Line | Change |
|------|--------|
| 5 | `/single-facility` → `/FacilityEpiSim` |
| 23 | `MODULE_NAME" value="single-facility"` → `MODULE_NAME" value="FacilityEpiSim"` |
| 24 | `single-facility}/single-facility.rs` → `FacilityEpiSim}/FacilityEpiSim.rs` (both occurrences) |
| 25 | `PROJECT_ATTR" value="single-facility"` → `PROJECT_ATTR" value="FacilityEpiSim"` |

### Step 3.4: Update Batch FacilityEpiSim Model.launch
**File**: `launchers/Batch FacilityEpiSim Model.launch`

| Line | Change |
|------|--------|
| 5 | `/single-facility` → `/FacilityEpiSim` |
| 24 | `MODULE_NAME" value="single-facility"` → `MODULE_NAME" value="FacilityEpiSim"` |
| 25 | `single-facility}` → `FacilityEpiSim}` |
| 26 | `PROJECT_ATTR" value="single-facility"` → `PROJECT_ATTR" value="FacilityEpiSim"` |

---

## Phase 4: Build System

### Step 4.1: Update Makefile
**File**: `Makefile`

| Line | Current | New |
|------|---------|-----|
| 1 | `# Makefile for single-facility simulation` | `# Makefile for FacilityEpiSim simulation` |
| 8 | `single-facility.rs` | `FacilityEpiSim.rs` |
| 54 | `single-facility.rs` | `FacilityEpiSim.rs` |
| 120 | `single-facility simulation` | `FacilityEpiSim simulation` |
| 132 | `single-facility Model.launch` | `FacilityEpiSim Model.launch` |
| 146 | `single-facility.rs` | `FacilityEpiSim.rs` |
| 163 | `Single-Facility Disease` | `FacilityEpiSim Disease` |
| 164 | `Single-Facility Disease` | `FacilityEpiSim Disease` |

### Step 4.2: Update Batch Configuration
**File**: `batch/batch_configuration.properties`

| Change |
|--------|
| `single-facility\\single-facility.rs` → `damon-single-facility\\FacilityEpiSim.rs` |
| (Note: Parent folder name remains unless you rename the repo folder too) |

---

## Phase 5: Installer Files

### Step 5.1: Update create_model_archive.xml
**File**: `installer/create_model_archive.xml`

| Change |
|--------|
| `single-facility` → `FacilityEpiSim` (2 occurrences) |

### Step 5.2: Update installation_components.xml
**File**: `installer/installation_components.xml`

| Change |
|--------|
| `<appname>single-facility</appname>` → `<appname>FacilityEpiSim</appname>` |
| `single-facility` → `FacilityEpiSim` (all path references, ~4 occurrences) |

### Step 5.3: Update installation_coordinator.xml
**File**: `installer/installation_coordinator.xml`

| Change |
|--------|
| `single-facility` → `FacilityEpiSim` |

### Step 5.4: Update shortcuts.xml
**File**: `installer/shortcuts.xml`

| Change |
|--------|
| `single-facility` → `FacilityEpiSim` (all occurrences, ~5) |

### Step 5.5: Update Unix_shortcuts.xml
**File**: `installer/Unix_shortcuts.xml`

| Change |
|--------|
| `single-facility` → `FacilityEpiSim` (all occurrences, ~6) |

### Step 5.6: Update start_model.bat
**File**: `installer/start_model.bat`

| Change |
|--------|
| `TITLE single-facility` → `TITLE FacilityEpiSim` |
| `CD "single-facility"` → `CD "FacilityEpiSim"` |
| `single-facility.rs` → `FacilityEpiSim.rs` |

### Step 5.7: Update start_model.command
**File**: `installer/start_model.command`

| Change |
|--------|
| `single-facility` → `FacilityEpiSim` (all occurrences) |

---

## Phase 6: Documentation

### Step 6.1: Update CLAUDE.md
**File**: `CLAUDE.md`

| Line | Change |
|------|--------|
| 21 | `single-facility Model.launch` → `FacilityEpiSim Model.launch` |
| 36 | `single-facility.rs/parameters.xml` → `FacilityEpiSim.rs/parameters.xml` |
| 37 | `single-facility.rs/batch_params.xml` → `FacilityEpiSim.rs/batch_params.xml` |
| 38 | `single-facility Model.launch` → `FacilityEpiSim Model.launch` |
| 44 | `SingleFacilityBuilder` → `FacilityEpiSimBuilder` |

### Step 6.2: Update README.md
- Update git clone URL if repo is renamed
- Update all references to `single-facility.rs`
- Update project description references

### Step 6.3: Update QUICK_START.md
- Update `SingleFacilityBuilder` references → `FacilityEpiSimBuilder`

### Step 6.4: Update TECHNICAL_DOCS.md
- Update "single-facility" references
- Update `SingleFacilityBuilder` references

### Step 6.5: Update conceptual_design.md
- Update "single-facility" references

---

## Phase 7: Cleanup and Verification

### Step 7.1: Delete Auxiliary Files
```bash
# Remove text dump if it exists
rm SingleFacilityCode.txt  # or rename to FacilityEpiSimCode.txt
```

### Step 7.2: Search for Any Missed References
```bash
# Run from project root
grep -r "single-facility" --include="*" .
grep -r "SingleFacility" --include="*.java" .
grep -r "single_facility" --include="*" .
```

### Step 7.3: Re-import into Eclipse
1. Delete project from Eclipse workspace (do NOT delete contents)
2. File → Import → General → Existing Projects into Workspace
3. Select project folder
4. Verify project name shows as "FacilityEpiSim"

### Step 7.4: Verify Runtime
1. Run the model using the new launch configuration
2. Verify Repast runtime finds the context correctly
3. Run a batch simulation to verify batch configuration

---

## Phase 8: Git and GitHub (Optional)

### Step 8.1: Commit All Changes
```bash
git add -A
git commit -m "Rename project from single-facility to FacilityEpiSim"
```

### Step 8.2: Rename GitHub Repository (if desired)
1. Go to GitHub repo → Settings → General
2. Change repository name from `damon-single-facility` to `FacilityEpiSim`
3. Update local remote: `git remote set-url origin https://github.com/EpiForeSITE/FacilityEpiSim.git`
4. Update README.md clone URL

---

## Summary Checklist

| Phase | Items | Status |
|-------|-------|--------|
| Phase 1 | .rs folder + 3 XML files | ☐ |
| Phase 2 | Java class + 6 files | ☐ |
| Phase 3 | .project + 2 launch files | ☐ |
| Phase 4 | Makefile + batch config | ☐ |
| Phase 5 | 7 installer files | ☐ |
| Phase 6 | 5 documentation files | ☐ |
| Phase 7 | Cleanup + verify | ☐ |
| Phase 8 | Git/GitHub (optional) | ☐ |

**Total: ~29 files, 100+ individual changes**
