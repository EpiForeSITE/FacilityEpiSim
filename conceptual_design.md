# Conceptual Design for Model Verification

This document outlines the conceptual design for verifying that the FacilityEpiSim simulation model is working as intended. It provides a step-by-step approach to ensure the model's correctness and reliability through data analysis and consistency checks.

---

## 1. Define Model Expectations
- **Inputs:** Simulation parameters (e.g., isolation effectiveness, surveillance frequency).
- **Outputs:** Event logs and summary files (admissions.txt, clinicalDetection.txt, daily_population_stats.txt, decolonization.txt, discharged_patients.csv, surveillance.txt, transmissions.txt).
- **Expected Behaviors:**
  - Patient flow (admissions ≈ discharges over time).
  - Disease transmission and detection rates respond to parameter changes.
  - No impossible values (e.g., negative counts, dates out of order).

## 2. Data Collection
- The model outputs detailed event files and summary statistics at the end of each run.
- Each file captures a specific aspect of the simulation (e.g., admissions, transmissions, daily stats).

## 3. Analysis Approach
- **Automated R/Quarto Reports** in `new_analysis/`:
  - Read and summarize each output file.
  - Visualize time series (e.g., daily prevalence, detection rates).
  - Calculate summary statistics (means, medians, totals).
  - Check for internal consistency (e.g., admissions vs. discharges).
  - Compare observed outputs to expected values based on input parameters.

## 4. Verification Steps
- **Internal Consistency Checks:**
  - Admissions ≈ Discharges.
  - Detections ≤ Colonized.
  - Transmissions only when both susceptible and infectious present.
- **Parameter Sensitivity:**
  - Run with different parameter values and confirm outputs change as expected.
- **Edge Case Testing:**
  - Extreme parameter values (e.g., 0% or 100% isolation effectiveness) produce logical results.
- **Documentation:**
  - All findings, plots, and summary tables are included in a Quarto PDF report.

## 5. Review and Iteration
- Review reports for anomalies or unexpected results.
- Iterate on model or analysis scripts as needed.

---

**Next Steps:**
- Implement R/Quarto scripts to automate the above checks and generate a PDF report.
- Begin with simple summaries and plots, then add consistency checks and parameter sensitivity analyses.
