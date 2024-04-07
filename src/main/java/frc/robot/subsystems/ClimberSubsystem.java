package frc.robot.subsystems;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase {
    private double leftSpeed;
    private double rightSpeed;
    MotorController leftClimberMotor;
    MotorController rightClimberMotor;
    private final Encoder leftEncoder;
    private final Encoder rightEncoder;
    private static GenericEntry leftEncoderPosition = Shuffleboard.getTab("General").add("Left Encoder Position", 0.0).getEntry();
    private static GenericEntry rightEncoderPosition = Shuffleboard.getTab("General").add("Right Encoder Position", 0.0).getEntry();



    public ClimberSubsystem() {
        this.leftClimberMotor = new Victor(Constants.Climber.leftClimbMotorChannel);
        this.rightClimberMotor = new Victor(Constants.Climber.rightClimbMotorChannel);
        this.leftEncoder = new Encoder(Constants.Climber.leftEncoder1, Constants.Climber.leftEncoder2);
        this.rightEncoder = new Encoder(Constants.Climber.rightEncoder1, Constants.Climber.rightEncoder2);
    }
    public void resetEncoders()
    {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void setClimberSpeed(double leftSpeed, double rightSpeed) {
        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
    }

    //MAKE IT SO YOU CAN control them alone
    @Override
    public void periodic() {
        var leftSpeed = this.leftSpeed;
        var rightSpeed = this.rightSpeed;

        leftEncoderPosition.setDouble(leftEncoder.get());
        rightEncoderPosition.setDouble(rightEncoder.get());

        if (leftEncoder.get() <= 0 && leftSpeed > 0){
            leftClimberMotor.set(0);
        } else {
            leftClimberMotor.set(leftSpeed);
        }
        if (rightEncoder.get() >= 0 && rightSpeed > 0){
            rightClimberMotor.set(0);
        } else {
            rightClimberMotor.set(-rightSpeed);
        }
    }
}
