package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.pickup.IntakeSubsystem;

public class AmpShooterCommand extends Command {
    private final ShooterSubsystem shooterSubsystem;
    private final IntakeSubsystem intakeSubsystem;
private double shooterSpeed;
private double intakeSpeed;
    public AmpShooterCommand(ShooterSubsystem shooterSubsystem, IntakeSubsystem intakeSubsystem, double shooterSpeed, double intakeSpeed) {
        this.shooterSubsystem = shooterSubsystem;
        this.intakeSubsystem = intakeSubsystem;
        this.shooterSpeed = shooterSpeed;
        this.intakeSpeed = intakeSpeed;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intakeSubsystem.setPickUpMotorSpeed(intakeSpeed);
        shooterSubsystem.setShooterSpeed(shooterSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.setPickUpMotorSpeed(0);
        shooterSubsystem.setShooterSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
