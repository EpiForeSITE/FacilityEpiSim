#!/bin/bash
# Run a single Repast Simphony simulation (headless)
# Usage: ./run-single.sh [scenario_dir]

set -e

# Default values
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SCENARIO_DIR="${1:-$PROJECT_ROOT/single-facility.rs}"
BIN_DIR="$PROJECT_ROOT/bin"
REPAST_HOME="${REPAST_HOME:-/opt/repast-simphony-2.11.0}"

# Check if Repast Simphony is installed
if [ ! -d "$REPAST_HOME" ]; then
    echo "Error: Repast Simphony not found at $REPAST_HOME"
    echo "Please set REPAST_HOME environment variable or run install-repast.sh"
    exit 1
fi

# Check if scenario directory exists
if [ ! -d "$SCENARIO_DIR" ]; then
    echo "Error: Scenario directory not found: $SCENARIO_DIR"
    exit 1
fi

echo "Running single simulation..."
echo "Project root: $PROJECT_ROOT"
echo "Scenario: $SCENARIO_DIR"
echo "Repast home: $REPAST_HOME"

# Set up classpath
REPAST_PLUGINS="$REPAST_HOME/eclipse/plugins"
CLASSPATH="$BIN_DIR"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.bin_and_src_2.11.0/repast.simphony.bin_and_src.jar"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.runtime_2.11.0/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.core_2.11.0/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.batch_2.11.0/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.data_2.11.0/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.essentials_2.11.0/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/libs.ext_2.11.0/lib/*"

# VM arguments from launch configuration
VM_ARGS="-XX:+IgnoreUnrecognizedVMOptions"
VM_ARGS="$VM_ARGS --add-opens=java.base/java.lang.reflect=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-modules=ALL-SYSTEM"
VM_ARGS="$VM_ARGS --add-exports=java.base/jdk.internal.ref=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-exports=java.base/java.lang=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-exports=java.xml/com.sun.org.apache.xpath.internal=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-exports=java.xml/com.sun.org.apache.xpath.internal.objects=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-exports=java.desktop/sun.awt=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-exports=java.desktop/sun.java2d=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-opens=java.base/java.lang=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-opens=java.base/java.util=ALL-UNNAMED"

# Memory settings
VM_ARGS="$VM_ARGS -Xmx2g -Xms512m"

# Headless mode
VM_ARGS="$VM_ARGS -Djava.awt.headless=true"

# Main class
MAIN_CLASS="repast.simphony.runtime.RepastMain"

# Run the simulation
echo "Starting simulation (this may take a while)..."
java $VM_ARGS -cp "$CLASSPATH" $MAIN_CLASS "$SCENARIO_DIR"

echo "Simulation completed!"
echo "Check output files in: $PROJECT_ROOT"
