package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class AutomaticShooterCommand extends Command {
    private final ShooterSubsystem shooterSubsystem;
    private double rpm;

    public AutomaticShooterCommand(ShooterSubsystem shooterSubsystem, double rpm) {
        this.shooterSubsystem = shooterSubsystem;
        this.rpm = rpm;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {shooterSubsystem.setShooterSpeed(rpm);}

    @Override
    public void end(boolean interrupted) {shooterSubsystem.setShooterSpeed(0.0);}

    @Override
    public boolean isFinished() {
        return false;
    }
}
