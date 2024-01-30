package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import frc.robot.subsystems.ShooterSubsystem;

public class ManualShooterCommand extends Command {
    private final ShooterSubsystem shooterSubsystem;
    private final double speed;
    private final CommandJoystick joystick = new CommandJoystick(0);


    public ManualShooterCommand(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        speed = (-joystick.getThrottle() + 1) / 2;
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
