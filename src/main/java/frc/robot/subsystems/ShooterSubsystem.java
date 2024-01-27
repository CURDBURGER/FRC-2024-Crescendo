package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.util.function.DoubleSupplier;

public class ShooterSubsystem extends SubsystemBase {
    private DoubleSupplier speed;
    CANSparkMax frontLeftMotor;
    CANSparkMax backLeftMotor;
    CANSparkMax frontRightMotor;
    CANSparkMax backRightMotor;

    public ShooterSubsystem() {
        this.frontLeftMotor = new CANSparkMax(Constants.Shooter.frontLeftMotorChannel, CANSparkMax.MotorType.kBrushed);
        this.backLeftMotor = new CANSparkMax(Constants.Shooter.backLeftMotorChannel, CANSparkMax.MotorType.kBrushed);
        this.frontRightMotor = new CANSparkMax(Constants.Shooter.frontRightMotorChannel, CANSparkMax.MotorType.kBrushed);
        this.backRightMotor = new CANSparkMax(Constants.Shooter.backRightMotorChannel, CANSparkMax.MotorType.kBrushed);
    }

    public void setShooterSpeed(DoubleSupplier speed) {
        this.speed = speed;
    }

    @Override
    public void periodic() {
        var speed = this.speed.getAsDouble();
        frontLeftMotor.set(-speed);
        backLeftMotor.set(-speed);
        frontRightMotor.set(speed);
        backRightMotor.set(speed);
    }
}