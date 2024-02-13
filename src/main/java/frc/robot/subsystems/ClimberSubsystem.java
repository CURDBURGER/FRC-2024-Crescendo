package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimberSubsystem extends SubsystemBase {
    private double speed;
    MotorController leftClimberMotor;
    MotorController rightClimberMotor;

    public ClimberSubsystem() {
        this.leftClimberMotor = new Victor(Constants.Climber.leftClimbMotorChannel);
        this.rightClimberMotor = new Victor(Constants.Climber.rightClimbMotorChannel);
    }

    public void setClimberSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void periodic() {
        var speed = this.speed;
        leftClimberMotor.set(-speed);
        rightClimberMotor.set(-speed);

    }
}
