#!/bin/bash
# Run a single Repast Simphony simulation in truly headless mode using batch with 1 run
# Usage: ./run-headless-single.sh

set -e

# Default values
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
SCENARIO_DIR="$PROJECT_ROOT/single-facility.rs"
BATCH_PARAMS="${1:-/tmp/single_run_batch.xml}"
BIN_DIR="$PROJECT_ROOT/bin"
REPAST_HOME="${REPAST_HOME:-/opt/repast-simphony-2.11.0}"

echo "Running single simulation in headless mode..."
echo "Project root: $PROJECT_ROOT"
echo "Scenario: $SCENARIO_DIR"
echo "Batch params: $BATCH_PARAMS"
echo "Repast home: $REPAST_HOME"

# Check if Repast Simphony is installed
if [ ! -d "$REPAST_HOME" ]; then
    echo "Error: Repast Simphony not found at $REPAST_HOME"
    exit 1
fi

# Set up classpath
REPAST_PLUGINS="$REPAST_HOME/eclipse/plugins"
CLASSPATH="$BIN_DIR"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.bin_and_src_2.11.0.jar"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.runtime_2.11.0_extracted/bin"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.runtime_2.11.0_extracted/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.core_2.11.0_extracted/bin"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.core_2.11.0_extracted/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.batch_2.11.0_extracted/bin"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.essentials_2.11.0_extracted/bin"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.essentials_2.11.0_extracted/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.dataLoader_2.11.0_extracted/bin"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/repast.simphony.data_2.11.0_extracted/bin"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/libs.ext_2.11.0_extracted/lib/*"
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/*"

# VM arguments
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
VM_ARGS="$VM_ARGS -Xmx2g -Xms512m"
VM_ARGS="$VM_ARGS -Djava.awt.headless=true"

# Use RepastMain with -batch flag
MAIN_CLASS="repast.simphony.runtime.RepastMain"

echo "Starting simulation..."
java $VM_ARGS -cp "$CLASSPATH" $MAIN_CLASS -batch "$SCENARIO_DIR" "$BATCH_PARAMS"

echo "Simulation completed!"
echo "Check output files in: $PROJECT_ROOT"
