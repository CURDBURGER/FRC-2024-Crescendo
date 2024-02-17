package frc.robot;

public final class Constants {

    public static class Shooter {
        public static final int frontLeftMotorChannel = 11;
        public static final int backLeftMotorChannel = 12;
        public static final int frontRightMotorChannel = 13;
        public static final int backRightMotorChannel = 14;
        public static final double autoShooterSpeed = .5;
        public static final int outtakeTime = 1000;
        public static final int revTime = 1000;
    }

    public static class Climber {
        public static final int leftClimbMotorChannel = 1;
        public static final int rightClimbMotorChannel = 2;
        public static final double climberSpeed = .5;
    }

    public static class WheelModule {
        public static final int FRONT_LEFT = 0;
        public static final int FRONT_RIGHT = 1;
        public static final int BACK_LEFT = 2;
        public static final int BACK_RIGHT = 3;
    }

    // TODO
    public static final class NotePickup {
        public final static int inputMotorChannel = 3; //is a motor not a solenoid
        public final static double inputMotorSpeed = 0.8; //make negative to reverse rotor
        public final static int pivotMotor = 4;
        public final static int pivotEncoder1 = 0;
        public final static int pivotEncoder2 = 1;
    }

    public static final class Auto {
        public final static double robotLength = .9144;
        public final static double intakePretrusionLength = .2032;
        public final static double noteDiameter = .36;
        public final static double noteRadius = noteDiameter/2;
        public final static double xDistanceToNote = 1.98 - robotLength - intakePretrusionLength - noteRadius;
        public final static double yDistanceToNote = 1.45;
        public final static double directionToNote = Math.atan(yDistanceToNote/xDistanceToNote);
        public static double diagonalDistanceToNote  = Math.hypot(xDistanceToNote, yDistanceToNote);
        public static double normalSpeed = 5;
        public static double slowSpeed = 3;
    }

    public static final class Align {
        public static final int redShooterID = 4;
        public static final int blueShooterID = 7;
        public static final double errorMargin = 0.05;
        public static final double shooterRange = .75;
        public static double alignSpeed = 5;
    }


    public static final double DEAD_ZONE = 0.02;
}
