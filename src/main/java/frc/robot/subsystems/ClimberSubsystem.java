package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase {
    private double leftSpeed;
    private double rightSpeed;
    MotorController leftClimberMotor;
    MotorController rightClimberMotor;

    public ClimberSubsystem() {
        this.leftClimberMotor = new Victor(Constants.Climber.leftClimbMotorChannel);
        this.rightClimberMotor = new Victor(Constants.Climber.rightClimbMotorChannel);
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
        leftClimberMotor.set(leftSpeed);
        rightClimberMotor.set(-rightSpeed);

    }
}
