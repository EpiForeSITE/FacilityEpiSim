# GitHub Actions Workflows

This directory contains GitHub Actions workflows for automating various tasks in the Single-Facility project.

## Available Workflows

### 1. Run Simulation (`run-simulation.yml`)

**Trigger:** Manual (workflow_dispatch)

**Purpose:** Runs a single Repast Simphony simulation with configurable parameters.

**How to use:**
1. Go to the "Actions" tab in the GitHub repository
2. Select "Run Simulation" from the workflow list
3. Click "Run workflow"
4. (Optional) Configure parameters:
   - **Simulation duration**: Number of days after burn-in (default: 60)
   - **Burn-in period**: Number of days for burn-in (default: 30)
5. Click "Run workflow" to start

**What it does:**
- Installs Java 11 and Repast Simphony 2.11.0
- Compiles the simulation code
- Updates parameters based on your inputs
- Runs the simulation headlessly
- Creates a summary of results
- Uploads all output files as artifacts

**Output artifacts:**
The workflow creates an artifact named `simulation-results-<run_number>` containing:
- `admissions.txt` - Patient admission events
- `transmissions.txt` - Disease transmission events
- `clinicalDetection.txt` - Clinical detection events
- `surveillance.txt` - Surveillance testing results
- `decolonization.txt` - Decolonization events
- `discharged_patients.csv` - Discharged patient records
- `daily_population_stats.txt` - Daily statistics
- `daily_prevalence.txt` - Prevalence data
- `detection_verification.txt` - Detection metrics
- `simulation_results.txt` - Summary results
- `simulation_summary.md` - Human-readable summary

**Artifact retention:** 30 days

### 2. Render and Publish Docs (`render_and_publish_docs.yml`)

**Trigger:** Push to main/master branch

**Purpose:** Renders documentation using Quarto and publishes to GitHub Pages.

### 3. Draft PDF (`draft_pdf.yml`)

**Trigger:** (See workflow file for details)

**Purpose:** Generates PDF drafts of documentation.

## Notes

- All workflows run on `ubuntu-latest` runners
- The Run Simulation workflow uses the `SimpleHeadlessRunner` Java class for headless execution
- Original parameter files are automatically restored after the simulation run
- Simulation outputs are retained for 30 days and can be downloaded from the Actions tab
