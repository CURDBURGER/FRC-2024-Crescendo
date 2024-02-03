package frc.robot;

public final class Constants {

    public static class Shooter{
        public static int frontLeftMotorChannel = 11;
        public static int backLeftMotorChannel = 12;
        public static int frontRightMotorChannel = 13;
        public static int backRightMotorChannel = 14;
        public static double autoShooterSpeed = .3;
        public static int outtakeTime = 1000;
        public static int revTime = 1000;
    }

    public static class Climber{
        public static int leftClimbMotorChannel = 1;
        public static int rightClimbMotorChannel = 2;
    }


// TODO
public static final class NotePickup {
    public final static int inputMotorChannel = 3; //is a motor not a solenoid
    public final static double inputMotorSpeed = 0.8; //make negative to reverse rotor
    public static int pivotMotor = 4;
    public static int pivotEncoder1 = 0;
    public static int pivotEncoder2 = 1;
}
}
