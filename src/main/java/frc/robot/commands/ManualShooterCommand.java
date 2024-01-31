package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.ShooterSubsystem;

public class ManualShooterCommand extends Command {
    private final ShooterSubsystem shooterSubsystem;
    private final CommandJoystick joystick;

    public ManualShooterCommand(ShooterSubsystem shooterSubsystem, CommandJoystick joystick) {
        this.shooterSubsystem = shooterSubsystem;
        this.joystick = joystick;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        var speed = (-joystick.getThrottle() + 1) / 2;
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
