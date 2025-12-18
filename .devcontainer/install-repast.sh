#!/bin/bash
# Install Repast Simphony for headless execution
# This script downloads and sets up Repast Simphony 2.11.0

set -e

REPAST_VERSION="2.11.0"
REPAST_DIR="/opt/repast-simphony-${REPAST_VERSION}"
REPAST_ARCHIVE="repast.simphony.updatesite.${REPAST_VERSION}.zip"
REPAST_URL="https://github.com/Repast/repast.simphony/releases/download/v.${REPAST_VERSION}/${REPAST_ARCHIVE}"

echo "Installing Repast Simphony ${REPAST_VERSION}..."

# Check if already installed
if [ -d "$REPAST_DIR" ]; then
    echo "Repast Simphony is already installed at $REPAST_DIR"
    exit 0
fi

# Create installation directory
sudo mkdir -p "$REPAST_DIR"
cd /tmp

# Download Repast Simphony
echo "Downloading Repast Simphony from $REPAST_URL..."
wget -q "$REPAST_URL" || {
    echo "Error: Failed to download Repast Simphony"
    echo "Please download manually from: https://github.com/Repast/repast.simphony/releases"
    exit 1
}

# Extract
echo "Extracting Repast Simphony..."
sudo unzip -q "$REPAST_ARCHIVE" -d "$REPAST_DIR"

# The updatesite zip contains plugins in repast.simphony.updatesite subdirectory
# Create eclipse/plugins directory structure
sudo mkdir -p "$REPAST_DIR/eclipse/plugins"
sudo mkdir -p "$REPAST_DIR/eclipse/features"
if [ -d "$REPAST_DIR/repast.simphony.updatesite/plugins" ]; then
    sudo mv "$REPAST_DIR/repast.simphony.updatesite/plugins"/* "$REPAST_DIR/eclipse/plugins/" 2>/dev/null || true
fi
if [ -d "$REPAST_DIR/repast.simphony.updatesite/features" ]; then
    sudo mv "$REPAST_DIR/repast.simphony.updatesite/features"/* "$REPAST_DIR/eclipse/features/" 2>/dev/null || true
fi
sudo rm -rf "$REPAST_DIR/repast.simphony.updatesite"

# Clean up
rm -f "$REPAST_ARCHIVE"

# Set permissions
sudo chmod -R 755 "$REPAST_DIR"

# Extract nested JARs from OSGI bundles for easier access
echo "Extracting nested libraries from OSGI bundles..."
cd "$REPAST_DIR/eclipse/plugins"
for jar in repast.simphony.runtime_2.11.0.jar repast.simphony.batch_2.11.0.jar repast.simphony.core_2.11.0.jar repast.simphony.essentials_2.11.0.jar repast.simphony.dataLoader_2.11.0.jar repast.simphony.data_2.11.0.jar repast.simphony.scenario_2.11.0.jar libs.ext_2.11.0.jar; do
    if [ -f "$jar" ]; then
        sudo unzip -q -o "$jar" -d "${jar%.jar}_extracted" 2>/dev/null || true
    fi
done

echo "Repast Simphony installed successfully at $REPAST_DIR"
echo "Add this to your environment:"
echo "export REPAST_HOME=$REPAST_DIR"
