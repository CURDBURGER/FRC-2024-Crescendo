package frc.robot;

import static frc.robot.Constants.Swerve.driveBaseRadius;

import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

import edu.wpi.first.math.util.Units;

public final class Constants {

    public static class Shooter {
        public static final int frontLeftMotorChannel = 11;
        public static final int backLeftMotorChannel = 12;
        public static final int frontRightMotorChannel = 13;
        public static final int backRightMotorChannel = 14;
        public static final double autoShooterSpeed = -1;
        public static final int outtakeTime = 1500;
        public static final int revTime = 1500;
        public static final double backWheelPercent = 1;
        public static double speaker = 1;
    }

    public static class Climber {
        public static final int leftClimbMotorChannel = 2;
        public static final int rightClimbMotorChannel = 3;
        public static final double climberSpeed = .5;
        public static final int fastClimberSpeed = 1;
        public static int leftEncoder1 = 5;
        public static int leftEncoder2 = 6;
        public static int rightEncoder1 = 7;
        public static int rightEncoder2 = 8;
    }

    public static class Swerve {
        public static final int FRONT_LEFT = 0;
        public static final int FRONT_RIGHT = 1;
        public static final int BACK_LEFT = 2;
        public static final int BACK_RIGHT = 3;
        public static final double maxLinearSpeed = Units.feetToMeters(14.5);
        public static final double trackWidthX = Units.inchesToMeters(25.0);
        public static final double trackWidthY = Units.inchesToMeters(25.0);
        public static final double driveBaseRadius = Math.hypot(trackWidthX / 2.0, trackWidthY / 2.0);
        }

    // TODO
    public static final class NotePickup {
        public final static int inputMotorChannel =1; //is a motor not a solenoid
        public final static double inputMotorSpeed = -1; //make negative to reverse rotor
        public final static int pivotMotor = 0;
        public final static int pivotEncoder1 = 1;
        public final static int pivotEncoder2 = 2;
        public final static int outEncoderPosition = -950;
        public final static int inEncoderPosition = 0;
        public static double pivotSpeed = -.65;
        public static int limitSwitch = 4;
        public static int pivotEncoder3 = 3;
        public static double spitSpeed = 1;
        public static int ampPosition = -300;
    }

    public static final class Auto {
        public final static double robotLength = .9144;
        public final static double intakeProtrusionLength = .2032;
        public final static double noteDiameter = .36;
        public final static double noteRadius = noteDiameter/2;
        public final static double xDistanceToNote = 1.85 - robotLength - intakeProtrusionLength - noteRadius;
        public final static double yDistanceToNote = 1.6;
        public final static double directionToNote = Math.atan(yDistanceToNote/(xDistanceToNote-.25)) * (180/Math.PI);
        public static double diagonalDistanceToNote  = Math.hypot((xDistanceToNote-.25), yDistanceToNote);
        public static double normalSpeed = .12;
        public static double fastSpeed = .25;
        public static double diagonalSpeed = .2;
        public static double slowSpeed = .12;
        public static double rotateSpeed = 2;
        public static double clearDistance = .67;
    }

    public static final class Align {
        public static final int redShooterID = 4;
        public static final int blueShooterID = 7;
        public static final double errorMargin = 0.05;
        public static final double shooterRange = .75;
        public static double alignSpeed = 2.6;
    }

    public static final HolonomicPathFollowerConfig pathFollowerConfig = new HolonomicPathFollowerConfig(
                                new PIDConstants(.5, 0, 0), // 2.0 Translation constants 3
                                new PIDConstants(3, 0, 0), // 1.3 Rotation constants 3
                                Swerve.maxLinearSpeed,
                                driveBaseRadius, // Drive base radius (distance from center to furthest module)
                                new ReplanningConfig());


    public static final double linearDeadband = 0.1;
    public static final double rotationalDeadband = .1;

}
