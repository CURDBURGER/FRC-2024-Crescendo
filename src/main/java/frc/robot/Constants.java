package frc.robot;

public final class Constants {

    public static class Shooter {
        public static final int frontLeftMotorChannel = 11;
        public static final int backLeftMotorChannel = 12;
        public static final int frontRightMotorChannel = 13;
        public static final int backRightMotorChannel = 14;
        public static final double autoShooterSpeed = .3;
        public static final int outtakeTime = 1000;
        public static final int revTime = 1000;
    }

    public static class Climber {
        public static final int leftClimbMotorChannel = 1;
        public static final int rightClimbMotorChannel = 2;
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


    public static final double DEAD_ZONE = 0.02;
}
