package frc.robot;

public final class Constants {

    public static class Shooter{
        public static int frontLeftMotorChannel = 13;
        public static int backLeftMotorChannel = 14;
        public static int frontRightMotorChannel = 15;
        public static int backRightMotorChannel = 16;
        public static int encoderChannel1 = 0;
        public static int encoderChannel2 = 1;
        public static double autoShooterSpeed = .3;
        public static long outtakeTime = 1000;
    }

    public static class Climber{
        public static int leftClimbMotorChannel = 17;
        public static int rightClimbMotorChannel = 18;
    }


// TODO
public static final class NotePickup {
    public final static int inputMotorChannel = 9; //is a motor not a solenoid
    public final static double inputMotorSpeed = 0.8; //make negative to reverse rotor
    public static int pivotMotor = 1;
    public static int pivotEncoder1 = 0;
    public static int pivotEncoder2 = 1;
}
}
