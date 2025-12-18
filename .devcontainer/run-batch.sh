#!/bin/bash
# Run a batch of Repast Simphony simulations (headless)
# Usage: ./run-batch.sh [model_dir] [batch_params]

set -e

# Default values
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
MODEL_DIR="${1:-$PROJECT_ROOT}"
BATCH_PARAMS="${2:-$PROJECT_ROOT/batch/batch_params.xml}"
BIN_DIR="$PROJECT_ROOT/bin"
REPAST_HOME="${REPAST_HOME:-/opt/repast-simphony-2.11.0}"

# Check if Repast Simphony is installed
if [ ! -d "$REPAST_HOME" ]; then
    echo "Error: Repast Simphony not found at $REPAST_HOME"
    echo "Please set REPAST_HOME environment variable or run install-repast.sh"
    exit 1
fi

# Check if batch parameters file exists
if [ ! -f "$BATCH_PARAMS" ]; then
    echo "Error: Batch parameters file not found: $BATCH_PARAMS"
    exit 1
fi

echo "Running batch simulation..."
echo "Project root: $PROJECT_ROOT"
echo "Model directory: $MODEL_DIR"
echo "Batch parameters: $BATCH_PARAMS"
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
CLASSPATH="$CLASSPATH:$REPAST_PLUGINS/groovy_all_2.11.0/lib/*"

# VM arguments from launch configuration
VM_ARGS="-XX:+IgnoreUnrecognizedVMOptions"
VM_ARGS="$VM_ARGS --add-opens=java.base/java.lang.reflect=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-modules=ALL-SYSTEM"
VM_ARGS="$VM_ARGS --add-exports=java.base/jdk.internal.ref=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-exports=java.base/java.lang=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-exports=java.xml/com.sun.org.apache.xpath.internal=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-exports=java.xml/com.sun.org.apache.xpath.internal.objects=ALL-UNNAMED"
VM_ARGS="$VM_ARGS --add-opens=java.base/java.lang=ALL-UNNAMED"

# Memory settings
VM_ARGS="$VM_ARGS -Xmx2g -Xms512m"

# Headless mode
VM_ARGS="$VM_ARGS -Djava.awt.headless=true"

# Main class for batch runs
MAIN_CLASS="repast.simphony.batch.standalone.StandAloneMain"

# Program arguments
PROG_ARGS="-model_dir \"$MODEL_DIR\""

# Run the batch simulation
echo "Starting batch simulation (this may take a while)..."
java $VM_ARGS -cp "$CLASSPATH" $MAIN_CLASS $PROG_ARGS

echo "Batch simulation completed!"
echo "Check output files in: $MODEL_DIR/output"
