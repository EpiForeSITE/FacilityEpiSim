# Development Container for Single-Facility Project

This directory contains the development container configuration for the Single-Facility Disease Transmission Model project.

## What's Included

The devcontainer provides a complete development environment with:

- **Ubuntu 22.04 LTS** - Stable Linux base
- **Java 11** - Required for Repast Simphony 2.11.0 (architecture-aware: supports AMD64 and ARM64)
- **R** - For data analysis and statistics
  - Pre-installed packages: ggplot2, dplyr, tidyr, readr, knitr, rmarkdown
- **Quarto** - For creating documentation and reports (architecture-aware: supports AMD64 and ARM64)
- **Development Tools** - git, make, vim, nano, and other utilities

### Multi-Architecture Support

The container automatically detects your system architecture (AMD64/x86_64 or ARM64/aarch64) and installs the appropriate versions of Java and Quarto. This means it works seamlessly on:
- Intel/AMD processors (x86_64/amd64)
- Apple Silicon (M1/M2/M3) and other ARM64 systems

## Prerequisites

To use this devcontainer, you need:

1. **Docker** - [Install Docker Desktop](https://www.docker.com/products/docker-desktop)
2. **Visual Studio Code** - [Download VS Code](https://code.visualstudio.com/)
3. **Remote - Containers Extension** - Install from VS Code marketplace

## Getting Started

### Using VS Code

1. Open this project in VS Code
2. When prompted, click "Reopen in Container" (or use Command Palette: "Remote-Containers: Reopen in Container")
3. Wait for the container to build (first time may take 5-10 minutes)
4. Once ready, you'll have a fully configured development environment

### Manual Container Build

If you want to build the container manually:

```bash
# From the project root directory
cd .devcontainer
docker build -f ContainerFile -t single-facility-dev ..
docker run -it -v $(pwd)/..:/workspace single-facility-dev
```

## Verifying the Environment

After the container starts, verify everything is installed:

```bash
# Check Java version (should be 11.x)
java -version

# Check R version
R --version

# Check Quarto version
quarto --version
```

## Working in the Container

### Compiling Java Code

```bash
# Compile the simulation
make compile

# View available make targets
make help
```

### Running R Analysis

```bash
# Start R interactive session
R

# Or run R scripts directly
Rscript your_script.R
```

### Creating Quarto Reports

```bash
# Render a Quarto document
quarto render new_analysis/admissions_analysis.qmd

# Preview Quarto website
quarto preview
```

## Container Features

### VS Code Extensions

The container automatically installs these VS Code extensions:
- Java Extension Pack (for Java development)
- Maven support
- Red Hat Java language support
- R language support
- Quarto extension

### User Configuration

- Default user: `vscode` (non-root for security)
- Workspace mounted at: `/workspace`
- Java home: `/usr/lib/jvm/java-11-openjdk-amd64`

## Troubleshooting

### Container Won't Build

- Ensure Docker is running
- Check your internet connection (downloads packages)
- Try rebuilding: Command Palette → "Remote-Containers: Rebuild Container"

### Java Version Issues

The Makefile is configured for Java 11. If you see version mismatches:

```bash
# Verify JAVA_HOME
echo $JAVA_HOME

# Should output: /usr/lib/jvm/java-11-openjdk-amd64
```

### R Package Issues

If additional R packages are needed:

```bash
# Install from R console
R -e "install.packages('package_name', repos='https://cloud.r-project.org/')"
```

To make packages permanent, add them to the ContainerFile.

## Customization

### Adding More R Packages

Edit `ContainerFile` and add packages to the R installation command:

```dockerfile
RUN R -e "install.packages(c('ggplot2', 'dplyr', 'your_package'), repos='https://cloud.r-project.org/')"
```

### Changing Java Version

If you need a different Java version, modify the ContainerFile:

```dockerfile
RUN apt-get install -y openjdk-17-jdk openjdk-17-jre
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
```

### Adding System Packages

Add packages to the appropriate `apt-get install` section in ContainerFile.

## Notes

- The first build takes longer as it downloads and installs all dependencies
- Subsequent container starts are much faster (seconds)
- Your workspace files are mounted, not copied, so changes persist
- Container changes are ephemeral unless added to ContainerFile

## Support

For issues specific to:
- **Devcontainer setup**: Check VS Code Remote-Containers documentation
- **Project code**: See main project README.md
- **Repast Simphony**: Visit https://repast.github.io/docs/

## License

This devcontainer configuration is part of the Single-Facility project and follows the same license terms.
