#!/bin/bash
# Install Repast Simphony for headless execution
# This script downloads and sets up Repast Simphony 2.11.0

set -e

REPAST_VERSION="2.11.0"
REPAST_DIR="/opt/repast-simphony-${REPAST_VERSION}"
REPAST_ARCHIVE="repast.simphony.2.11.0.tar.gz"
REPAST_URL="https://github.com/Repast/repast.simphony/releases/download/v${REPAST_VERSION}/${REPAST_ARCHIVE}"

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
sudo tar -xzf "$REPAST_ARCHIVE" -C "$REPAST_DIR" --strip-components=1

# Clean up
rm -f "$REPAST_ARCHIVE"

# Set permissions
sudo chmod -R 755 "$REPAST_DIR"

echo "Repast Simphony installed successfully at $REPAST_DIR"
echo "Add this to your environment:"
echo "export REPAST_HOME=$REPAST_DIR"
