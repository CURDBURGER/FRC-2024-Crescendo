package frc.robot.subsystems.drive;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.PathPlannerLogging;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

import static frc.robot.Constants.Swerve.*;


// import frc.robot.util.LocalADStarAK;


public class Drive extends SubsystemBase {

    private static final double MAX_LINEAR_SPEED = Constants.Swerve.MAX_LINEAR_SPEED;
    private static final double TRACK_WIDTH_X = Constants.Swerve.TRACK_WIDTH_X;
    private static final double TRACK_WIDTH_Y = Constants.Swerve.TRACK_WIDTH_Y;
    private static final double DRIVE_BASE_RADIUS = Constants.Swerve.DRIVE_BASE_RADIUS;
    private static final double MAX_ANGULAR_SPEED = MAX_LINEAR_SPEED / DRIVE_BASE_RADIUS;

    private final GyroIO gyroIO;
    private final GyroIO.GyroIOInputs gyroInputs = new GyroIO.GyroIOInputs();
    private GenericEntry gyroYawDebug;
    private GenericEntry gyroRollDebug;
    private GenericEntry gyroPitchDebug;
    private GenericEntry poseXDebug;
    private GenericEntry poseYDebug;
    private GenericEntry poseRotDebug;
    private final Module[] modules = new Module[4]; // FL, FR, BL, BR


    private SwerveDriveKinematics kinematics = new SwerveDriveKinematics(getModuleTranslations());
    private Pose2d pose = new Pose2d();
    private Rotation2d lastGyroRotation = new Rotation2d();
    private StructArrayPublisher<SwerveModuleState> publisher;
    private boolean isFieldOriented = true;
    private ChassisSpeeds robotRelativeSpeeds;

    public Drive(
            GyroIO gyroIO,
            ModuleIO flModuleIO,
            ModuleIO frModuleIO,
            ModuleIO blModuleIO,
            ModuleIO brModuleIO) {
        this.gyroIO = gyroIO;
AutoBuilder.configureHolonomic()
AutoBuilder.configureHolonomic(
            this::getPose, // Robot pose supplier
            this::setPose, // Method to reset odometry (will be called if your auto has a starting pose)
            this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
            this::runVelocity, // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
            new HolonomicPathFollowerConfig( // HolonomicPathFollowerConfig, this should likely live in your Constants class
                    new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                    new PIDConstants(5.0, 0.0, 0.0), // Rotation PID constants
                    4.5, // Max module speed, in m/s
                            0.4, // Drive base radius in meters. Distance from robot center to furthest module.
                            new ReplanningConfig() // Default path replanning config. See the API for the options here
            ),
                    () -> {
        // Boolean supplier that controls when the path will be mirrored for the red alliance
        // This will flip the path being followed to the red side of the field.
        // THE ORIGIN WILL REMAIN ON THE BLUE SIDE

        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            return alliance.get() == DriverStation.Alliance.Red;
        }
        return false;
    },
            this // Reference to this subsystem to set requirements
            );

        modules[FRONT_LEFT] = new Module(flModuleIO, 0, "FL");
        modules[FRONT_RIGHT] = new Module(frModuleIO, 1, "FR");
        modules[BACK_LEFT] = new Module(blModuleIO, 2, "BL");
        modules[BACK_RIGHT] = new Module(brModuleIO, 3, "BR");

        publisher = NetworkTableInstance
                .getDefault()
                .getStructArrayTopic("MyStates", SwerveModuleState.struct)
                .publish();
        gyroYawDebug = Shuffleboard.getTab("General").add("Gyro Yaw", 0).getEntry();
        gyroRollDebug = Shuffleboard.getTab("General").add("Gyro Roll", 0).getEntry();
        gyroPitchDebug = Shuffleboard.getTab("General").add("Gyro Pitch", 0).getEntry();
        poseXDebug = Shuffleboard.getTab("General").add("Pose X", 0).getEntry();
        poseYDebug = Shuffleboard.getTab("General").add("Pose Y", 0).getEntry();
        poseRotDebug = Shuffleboard.getTab("General").add("Pose Rotation", 0).getEntry();
        

    }

    public void periodic() {
        gyroIO.updateInputs(gyroInputs);
        for (var module : modules) {
            module.periodic();
        }

        // Stop moving when disabled
        if (DriverStation.isDisabled()) {
            for (var module : modules) {
                module.stop();
            }
        }

        // Update odometry
        SwerveModulePosition[] wheelDeltas = new SwerveModulePosition[4];
        for (int i = 0; i < 4; i++) {
            wheelDeltas[i] = modules[i].getPositionDelta();
        }

        // The twist represents the motion of the robot since the last
        // loop cycle in x, y, and theta based only on the modules,
        // without the gyro. The gyro is always disconnected in simulation.
        var twist = kinematics.toTwist2d(wheelDeltas);
        gyroYawDebug.setDouble(gyroInputs.yawPosition.getDegrees());
        gyroRollDebug.setDouble(gyroInputs.rollPosition.getDegrees());
        gyroPitchDebug.setDouble(gyroInputs.pitchPosition.getDegrees());
//        if (gyroInputs.connected) {
//            // If the gyro is connected, replace the theta component of the twist
//            // with the change in angle since the last loop cycle.
//            twist = new Twist2d(
//                    twist.dx,
//                    twist.dy,
//                    gyroInputs.yawPosition.minus(lastGyroRotation).getRadians()
//            );
//            lastGyroRotation = gyroInputs.yawPosition;
//        }
        // Apply the twist (change since last loop cycle) to the current pose
        pose = pose.exp(twist);

        poseXDebug.setDouble(pose.getX());
        poseYDebug.setDouble(pose.getY());
        poseRotDebug.setDouble(pose.getRotation().getDegrees());
//

    }

    /**
     * Runs the drive at the desired velocity.
     *
//     * @param speeds Speeds in meters/sec
     */
//    public void runVelocity(ChassisSpeeds speeds) {
//
//        // Calculate module setpoints
//        ChassisSpeeds discreteSpeeds = ChassisSpeeds.discretize(speeds, 0.02);
//        SwerveModuleState[] setpointStates = kinematics.toSwerveModuleStates(discreteSpeeds);
//        SwerveDriveKinematics.desaturateWheelSpeeds(setpointStates, MAX_LINEAR_SPEED);
//
//        // Send setpoints to modules
//        SwerveModuleState[] optimizedSetpointStates = new SwerveModuleState[4];
//        for (int i = 0; i < 4; i++) {
//            // The module returns the optimized state, useful for logging
//            optimizedSetpointStates[i] = modules[i].runSetpoint(setpointStates[i]);
//        }
//
//        optimizedSetpointStates = setpointStates;
//
//        publisher.set(optimizedSetpointStates);
//
//
//        // Log setpoint states
////        Logger.recordOutput("SwerveStates/Setpoints", setpointStates);
////        Logger.recordOutput("SwerveStates/SetpointsOptimized", optimizedSetpointStates);
//
//    }
        public void setOrientation(Translation2d linearVelocity, double omega){
            Rotation2d rotation;
            if (isFieldOriented) {
                rotation = getRotation();
            } else {
                rotation = new Rotation2d(0);
            }
             robotRelativeSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                    linearVelocity.getX() * MAX_LINEAR_SPEED,
                    linearVelocity.getY() * MAX_LINEAR_SPEED,
                    omega * MAX_ANGULAR_SPEED,
                    rotation
            );
            runVelocity(robotRelativeSpeeds);
        }

        public void runVelocity(ChassisSpeeds chassisSpeeds) {

        // Calculate module setpoints
        ChassisSpeeds discreteSpeeds = ChassisSpeeds.discretize(chassisSpeeds, 0.02);
        SwerveModuleState[] setpointStates = kinematics.toSwerveModuleStates(discreteSpeeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(setpointStates, MAX_LINEAR_SPEED);

        // Send setpoints to modules
        SwerveModuleState[] optimizedSetpointStates = new SwerveModuleState[4];
        for (int i = 0; i < 4; i++) {
            // The module returns the optimized state, useful for logging
            optimizedSetpointStates[i] = modules[i].runSetpoint(setpointStates[i]);
        }

        optimizedSetpointStates = setpointStates;

        publisher.set(optimizedSetpointStates);


        // Log setpoint states
//        Logger.recordOutput("SwerveStates/Setpoints", setpointStates);
//        Logger.recordOutput("SwerveStates/SetpointsOptimized", optimizedSetpointStates);

    }

    public ChassisSpeeds getRobotRelativeSpeeds()
    {
        return robotRelativeSpeeds;
    }

    /**
     * Stops the drive.
     */
    public void stop() {
        runVelocity(new Translation2d(), 0);
    }

    public void toggleIsFieldOriented() {
        isFieldOriented = !isFieldOriented;
    }

    /**
     * Stops the drive and turns the modules to an X arrangement to resist movement. The modules will
     * return to their normal orientations the next time a nonzero velocity is requested.
     */
    public void stopWithX() {
        Rotation2d[] headings = new Rotation2d[4];
        for (int i = 0; i < 4; i++) {
            headings[i] = getModuleTranslations()[i].getAngle();
        }
        kinematics.resetHeadings(headings);
        stop();
    }

    public void straightenWheels() {
        Rotation2d[] headings = new Rotation2d[4];
        for (int i = 0; i < 4; i++) {
            headings[i] = new Rotation2d(0);
        }
        kinematics.resetHeadings(headings);
        stop();
    }

    /**
     * Runs forwards at the commanded voltage.
     */
    public void runCharacterizationVolts(double volts) {
        for (int i = 0; i < 4; i++) {
            modules[i].runCharacterization(volts);
        }
    }

    /**
     * Returns the average drive velocity in radians/sec.
     */
    public double getCharacterizationVelocity() {
        double driveVelocityAverage = 0.0;
        for (var module : modules) {
            driveVelocityAverage += module.getCharacterizationVelocity();
        }
        return driveVelocityAverage / 4.0;
    }

    /**
     * Returns the module states (turn angles and drive velocities) for all of the modules.
     */
//    @AutoLogOutput(key = "SwerveStates/Measured")
    private SwerveModuleState[] getModuleStates() {
        SwerveModuleState[] states = new SwerveModuleState[4];
        for (int i = 0; i < 4; i++) {
            states[i] = modules[i].getState();
        }
        return states;
    }

    /**
     * Returns the current odometry pose.
     */
//    @AutoLogOutput(key = "Odometry/Robot")
    public Pose2d getPose() {
        return pose;
    }

    /**
     * Returns the current odometry rotation.
     */
    public Rotation2d getRotation() {
        return pose.getRotation();
    }

    /**
     * Resets the current odometry pose.
     */
    public void setPose(Pose2d pose) {
        this.pose = pose;
    }

    public void resetGyro() {
        gyroIO.resetGyro();
    }

    /**
     * Returns the maximum linear speed in meters per sec.
     */
    public double getMaxLinearSpeedMetersPerSec() {
        return MAX_LINEAR_SPEED;
    }

    /**
     * Returns the maximum angular speed in radians per sec.
     */
    public double getMaxAngularSpeedRadPerSec() {
        return MAX_ANGULAR_SPEED;
    }

    /**
     * Returns an array of module translations.
     */
    public static Translation2d[] getModuleTranslations() {
        return new Translation2d[]{
                new Translation2d(TRACK_WIDTH_X / 2.0, TRACK_WIDTH_Y / 2.0), // FL
                new Translation2d(TRACK_WIDTH_X / 2.0, -TRACK_WIDTH_Y / 2.0), // FR
                new Translation2d(-TRACK_WIDTH_X / 2.0, TRACK_WIDTH_Y / 2.0), // BL
                new Translation2d(-TRACK_WIDTH_X / 2.0, -TRACK_WIDTH_Y / 2.0) // BR
        };
    }
}