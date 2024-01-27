package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.function.DoubleSupplier;

public class ClimberSubsystem extends SubsystemBase {
    private DoubleSupplier speed;
    MotorController leftClimberMotor;
    MotorController rightClimberMotor;
    public ClimberSubsystem() {
        this.leftClimberMotor = new Victor(Constants.Climber.leftClimbMotorChannel);
        this.rightClimberMotor = new Victor(Constants.Climber.rightClimbMotorChannel);
    }

    public void setClimerSpeed(DoubleSupplier speed) {
        this.speed = speed;
    }

    @Override
    public void periodic() {
        var speed = this.speed.getAsDouble();
        leftClimberMotor.set(-speed);
        rightClimberMotor.set(-speed);
    }
}
