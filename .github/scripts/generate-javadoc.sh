#!/bin/bash
set -e

echo "Generating Javadoc for Single-Facility project..."

# Create directories
STUBS_DIR="/tmp/repast-stubs"
LIBS_DIR="/tmp/javadoc-libs"
JAVADOC_OUTPUT="docs/javadoc"

mkdir -p "$STUBS_DIR/src"
mkdir -p "$LIBS_DIR"
mkdir -p "$JAVADOC_OUTPUT"

# Download Apache Commons Math (required dependency)
echo "Downloading Apache Commons Math..."
wget -q https://repo1.maven.org/maven2/org/apache/commons/commons-math3/3.6.1/commons-math3-3.6.1.jar -O "$LIBS_DIR/commons-math3-3.6.1.jar"

# Create Repast Simphony stub classes for Javadoc compilation
echo "Creating Repast Simphony stubs..."

# Create directory structure
mkdir -p "$STUBS_DIR/src/repast/simphony/engine/schedule"
mkdir -p "$STUBS_DIR/src/repast/simphony/engine/environment"
mkdir -p "$STUBS_DIR/src/repast/simphony/random"
mkdir -p "$STUBS_DIR/src/repast/simphony/context"
mkdir -p "$STUBS_DIR/src/repast/simphony/dataLoader"
mkdir -p "$STUBS_DIR/src/repast/simphony/parameter"
mkdir -p "$STUBS_DIR/src/repast/simphony/util"

# Create stub files
cat > "$STUBS_DIR/src/repast/simphony/engine/schedule/ISchedule.java" << 'EOF'
package repast.simphony.engine.schedule;
public interface ISchedule {
    double getTickCount();
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/engine/schedule/ISchedulableAction.java" << 'EOF'
package repast.simphony.engine.schedule;
public interface ISchedulableAction {
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/engine/schedule/ScheduleParameters.java" << 'EOF'
package repast.simphony.engine.schedule;
public class ScheduleParameters {
    public static ScheduleParameters createRepeating(double start, double interval) {
        return null;
    }
    public static ScheduleParameters createOneTime(double time) {
        return null;
    }
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/engine/schedule/ScheduledMethod.java" << 'EOF'
package repast.simphony.engine.schedule;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface ScheduledMethod {
    double start() default 0;
    double interval() default 0;
    double priority() default 0;
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/engine/environment/RunEnvironment.java" << 'EOF'
package repast.simphony.engine.environment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.parameter.Parameters;
public class RunEnvironment {
    public static RunEnvironment getInstance() {
        return null;
    }
    public ISchedule getCurrentSchedule() {
        return null;
    }
    public Parameters getParameters() {
        return null;
    }
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/parameter/Parameters.java" << 'EOF'
package repast.simphony.parameter;
public interface Parameters {
    Object getValue(String paramName);
    Double getDouble(String paramName);
    Integer getInteger(String paramName);
    Boolean getBoolean(String paramName);
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/random/RandomHelper.java" << 'EOF'
package repast.simphony.random;
public class RandomHelper {
    public static double nextDouble() {
        return 0;
    }
    public static int nextInt() {
        return 0;
    }
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/context/Context.java" << 'EOF'
package repast.simphony.context;
public interface Context<T> {
    boolean add(T obj);
    boolean remove(T obj);
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/dataLoader/ContextBuilder.java" << 'EOF'
package repast.simphony.dataLoader;
import repast.simphony.context.Context;
public interface ContextBuilder<T> {
    Context<T> build(Context<T> context);
}
EOF

cat > "$STUBS_DIR/src/repast/simphony/util/ContextUtils.java" << 'EOF'
package repast.simphony.util;
import repast.simphony.context.Context;
public class ContextUtils {
    public static Context<?> getContext(Object obj) {
        return null;
    }
}
EOF

# Compile stub classes
echo "Compiling stubs..."
mkdir -p "$STUBS_DIR/bin"
javac -d "$STUBS_DIR/bin" -sourcepath "$STUBS_DIR/src" $(find "$STUBS_DIR/src" -name "*.java")

# Create JAR from stubs
echo "Creating stubs JAR..."
cd "$STUBS_DIR/bin"
jar cf "$STUBS_DIR/repast-stubs.jar" .
cd -

# Generate Javadoc
echo "Generating Javadoc..."
javadoc \
    -d "$JAVADOC_OUTPUT" \
    -sourcepath src \
    -subpackages agents:agentcontainers:builders:data:disease:processes:utils \
    -classpath "$LIBS_DIR/commons-math3-3.6.1.jar:$STUBS_DIR/repast-stubs.jar" \
    -windowtitle "Single-Facility Disease Transmission Model API" \
    -doctitle "Single-Facility Disease Transmission Model<br/>API Documentation" \
    -header "<b>Single-Facility Model</b>" \
    -bottom "Copyright &#169; 2024 EpiForeSITE. All Rights Reserved." \
    -use \
    -version \
    -author \
    -quiet

echo "Javadoc generation complete!"
echo "Output directory: $JAVADOC_OUTPUT"
