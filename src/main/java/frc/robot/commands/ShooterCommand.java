package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class ShooterCommand extends Command {
    private final ShooterSubsystem shooterSubsystem;
    private final double speed;

    public ShooterCommand(ShooterSubsystem shooterSubsystem, double speed) {
        this.shooterSubsystem = shooterSubsystem;
        this.speed = speed;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooterSubsystem.setShooterSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.setShooterSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
