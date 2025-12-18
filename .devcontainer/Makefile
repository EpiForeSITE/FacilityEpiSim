# Makefile for running Repast Simphony simulations in devcontainer
# This Makefile provides targets for running single and batch simulations

# Variables
PROJECT_ROOT = ..
SCENARIO_DIR = $(PROJECT_ROOT)/single-facility.rs
BATCH_PARAMS = $(PROJECT_ROOT)/batch/batch_params.xml
BIN_DIR = $(PROJECT_ROOT)/bin
REPAST_HOME ?= /opt/repast-simphony-2.11.0

# Scripts
INSTALL_SCRIPT = ./install-repast.sh
RUN_SINGLE_SCRIPT = ./run-single.sh
RUN_BATCH_SCRIPT = ./run-batch.sh

.PHONY: help
help:
	@echo "Repast Simphony Simulation Targets"
	@echo "==================================="
	@echo ""
	@echo "Setup:"
	@echo "  install-repast    - Download and install Repast Simphony 2.11.0"
	@echo "  compile           - Compile Java source files"
	@echo ""
	@echo "Single Run:"
	@echo "  run-single        - Run a single simulation (produces detailed output files)"
	@echo "  run-single-custom - Run single simulation with custom scenario"
	@echo "                      Usage: make run-single-custom SCENARIO=/path/to/scenario"
	@echo ""
	@echo "Batch Run:"
	@echo "  run-batch         - Run batch simulation (produces aggregated outputs)"
	@echo "  run-batch-custom  - Run batch with custom parameters"
	@echo "                      Usage: make run-batch-custom BATCH_PARAMS=/path/to/params.xml"
	@echo ""
	@echo "Utilities:"
	@echo "  clean-outputs     - Clean simulation output files"
	@echo "  check-setup       - Verify Repast Simphony installation"
	@echo ""
	@echo "Environment Variables:"
	@echo "  REPAST_HOME       - Repast Simphony installation directory (default: /opt/repast-simphony-2.11.0)"
	@echo "  SCENARIO_DIR      - Scenario directory for single runs (default: ../single-facility.rs)"
	@echo "  BATCH_PARAMS      - Batch parameters file (default: ../batch/batch_params.xml)"

.PHONY: install-repast
install-repast:
	@echo "Installing Repast Simphony..."
	@$(INSTALL_SCRIPT)

.PHONY: check-setup
check-setup:
	@echo "Checking Repast Simphony installation..."
	@if [ -d "$(REPAST_HOME)" ]; then \
		echo "✓ Repast Simphony found at $(REPAST_HOME)"; \
	else \
		echo "✗ Repast Simphony not found at $(REPAST_HOME)"; \
		echo "  Run 'make install-repast' to install"; \
		exit 1; \
	fi
	@if [ -d "$(BIN_DIR)" ]; then \
		echo "✓ Compiled classes found at $(BIN_DIR)"; \
	else \
		echo "✗ Compiled classes not found at $(BIN_DIR)"; \
		echo "  Run 'make compile' to compile"; \
	fi
	@if [ -d "$(SCENARIO_DIR)" ]; then \
		echo "✓ Scenario directory found at $(SCENARIO_DIR)"; \
	else \
		echo "✗ Scenario directory not found at $(SCENARIO_DIR)"; \
	fi
	@echo "✓ Setup verification complete"

.PHONY: compile
compile:
	@echo "Compiling Java source files..."
	@mkdir -p $(BIN_DIR)
	@if [ -d "$(REPAST_HOME)" ]; then \
		cd $(PROJECT_ROOT) && \
		find src -name "*.java" -print0 | xargs -0 javac \
			-cp "$(BIN_DIR):$(REPAST_HOME)/eclipse/plugins/repast.simphony.bin_and_src_2.11.0/repast.simphony.bin_and_src.jar:$(REPAST_HOME)/eclipse/plugins/repast.simphony.runtime_2.11.0/lib/*:$(REPAST_HOME)/eclipse/plugins/repast.simphony.core_2.11.0/lib/*" \
			-d $(BIN_DIR); \
		echo "✓ Compilation complete"; \
	else \
		echo "Error: Repast Simphony not found. Run 'make install-repast' first"; \
		exit 1; \
	fi

.PHONY: run-single
run-single: check-setup
	@echo "Running single simulation with scenario: $(SCENARIO_DIR)"
	@$(RUN_SINGLE_SCRIPT) $(SCENARIO_DIR)

.PHONY: run-single-custom
run-single-custom:
	@if [ -z "$(SCENARIO)" ]; then \
		echo "Error: SCENARIO variable not set"; \
		echo "Usage: make run-single-custom SCENARIO=/path/to/scenario"; \
		exit 1; \
	fi
	@echo "Running single simulation with custom scenario: $(SCENARIO)"
	@$(RUN_SINGLE_SCRIPT) $(SCENARIO)

.PHONY: run-batch
run-batch: check-setup
	@echo "Running batch simulation with parameters: $(BATCH_PARAMS)"
	@$(RUN_BATCH_SCRIPT) $(PROJECT_ROOT) $(BATCH_PARAMS)

.PHONY: run-batch-custom
run-batch-custom:
	@if [ -z "$(PARAMS)" ]; then \
		echo "Error: PARAMS variable not set"; \
		echo "Usage: make run-batch-custom PARAMS=/path/to/batch_params.xml"; \
		exit 1; \
	fi
	@echo "Running batch simulation with custom parameters: $(PARAMS)"
	@$(RUN_BATCH_SCRIPT) $(PROJECT_ROOT) $(PARAMS)

.PHONY: clean-outputs
clean-outputs:
	@echo "Cleaning simulation output files..."
	@cd $(PROJECT_ROOT) && rm -f *.txt *.csv output/*.txt output/*.csv 2>/dev/null || true
	@echo "✓ Output files cleaned"

.PHONY: quick-test
quick-test: compile run-single
	@echo "Quick test complete!"
	@echo "Check output files in $(PROJECT_ROOT)"
