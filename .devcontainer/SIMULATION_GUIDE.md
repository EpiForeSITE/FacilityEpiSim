# Running Repast Simphony Simulations

This guide explains how to run single and batch simulations using the provided scripts and Makefile.

## Quick Start

1. **Install Repast Simphony** (first time only):
   ```bash
   cd .devcontainer
   make install-repast
   ```

2. **Compile the code**:
   ```bash
   make compile
   ```

3. **Run a single simulation**:
   ```bash
   make run-single
   ```

4. **Run a batch simulation**:
   ```bash
   make run-batch
   ```

## Available Scripts

### install-repast.sh

Downloads and installs Repast Simphony 2.11.0 to `/opt/repast-simphony-2.11.0`.

**Usage:**
```bash
./install-repast.sh
```

This only needs to be run once. The script will check if Repast Simphony is already installed and skip if it exists.

### run-single.sh

Runs a single simulation with detailed output files.

**Usage:**
```bash
./run-single.sh [scenario_directory]
```

**Default:** `../single-facility.rs`

**Example:**
```bash
./run-single.sh ../single-facility.rs
```

**Outputs:** Creates detailed event files in the project root:
- `admissions.txt`
- `transmissions.txt`
- `clinicalDetection.txt`
- `surveillance.txt`
- `decolonization.txt`
- `daily_population_stats.txt`
- `discharged_patients.csv`

### run-batch.sh

Runs multiple simulations with parameter sweeps, producing aggregated outputs.

**Usage:**
```bash
./run-batch.sh [model_directory] [batch_parameters_file]
```

**Defaults:**
- Model directory: `..` (project root)
- Batch parameters: `../batch/batch_params.xml`

**Example:**
```bash
./run-batch.sh .. ../batch/batch_params.xml
```

**Outputs:** Creates aggregated CSV files in `output/` directory with one row per simulation run.

## Makefile Targets

### Setup Targets

- `make install-repast` - Install Repast Simphony 2.11.0
- `make check-setup` - Verify Repast installation and environment
- `make compile` - Compile Java source files

### Simulation Targets

- `make run-single` - Run single simulation with default scenario
- `make run-single-custom SCENARIO=/path` - Run with custom scenario
- `make run-batch` - Run batch simulation with default parameters
- `make run-batch-custom PARAMS=/path` - Run with custom batch parameters

### Utility Targets

- `make clean-outputs` - Remove simulation output files
- `make quick-test` - Compile and run a quick test simulation
- `make help` - Show all available targets

## Configuration

### Environment Variables

- `REPAST_HOME` - Repast Simphony installation directory (default: `/opt/repast-simphony-2.11.0`)
- `JAVA_HOME` - Java installation directory (automatically set in devcontainer)

### Customizing Parameters

#### Single Run Parameters

Edit `single-facility.rs/parameters.xml` to change simulation parameters:
- Disease parameters (beta, importation rate, decolonization time)
- Detection parameters (surveillance adherence, detection times)
- Intervention parameters (isolation effectiveness)
- Simulation duration (burn-in period, measurement period)

#### Batch Run Parameters

Edit `batch/batch_params.xml` to define parameter sweeps:
- Set `runs` attribute for number of replications
- Use `<parameter type="list">` for discrete parameter values
- Use `<parameter type="number">` for parameter ranges with start, end, and step

**Example:**
```xml
<sweep runs="10">
  <parameter name="isolationEffectiveness" type="list" value_type="double" values="0.3 0.5 0.7"/>
  <parameter name="probSurveillanceDetection" type="number" number_type="double" start="0.8" end="1.0" step="0.1"/>
</sweep>
```

This creates 10 runs × 3 isolation values × 3 detection values = 90 total simulations.

## Troubleshooting

### Repast Simphony Not Found

**Error:** `Repast Simphony not found at /opt/repast-simphony-2.11.0`

**Solution:**
```bash
cd .devcontainer
make install-repast
```

### Compilation Errors

**Error:** Cannot find classes or dependencies

**Solution:**
1. Ensure Repast Simphony is installed
2. Run `make check-setup` to verify environment
3. Try `make compile` again

### No Output Files

**Error:** Simulation runs but no output files appear

**Check:**
1. Look in project root for single run outputs
2. Look in `output/` directory for batch run outputs
3. Check that `isBatchRun` parameter is set correctly:
   - `false` for single runs (detailed outputs)
   - `true` for batch runs (aggregated outputs)

### Memory Issues

**Error:** Out of memory errors during simulation

**Solution:** Edit the scripts to increase memory:
```bash
# In run-single.sh or run-batch.sh, change:
VM_ARGS="$VM_ARGS -Xmx2g -Xms512m"
# To:
VM_ARGS="$VM_ARGS -Xmx4g -Xms1g"
```

## Performance Tips

1. **Batch Runs:** Use multiple CPU cores by adjusting the batch configuration
2. **Large Sweeps:** Break into smaller parameter ranges and run separately
3. **Memory:** Allocate more heap space for larger populations or longer simulations
4. **Headless:** All scripts run in headless mode (no GUI) for better performance

## Output Analysis

After running simulations, use the R scripts and Quarto reports in `new_analysis/` and `docs/` directories:

```bash
# Render a specific analysis
cd new_analysis
quarto render admissions_analysis.qmd

# Generate all documentation
cd ..
make website
```

## Additional Resources

- [Repast Simphony Documentation](https://repast.github.io/docs/)
- [Batch Run Guide](https://repast.github.io/docs/RepastBatchRunsGettingStarted.pdf)
- Project README: `../README.md`
- Devcontainer Guide: `./README.md`
