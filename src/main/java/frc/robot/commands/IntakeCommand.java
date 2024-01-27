package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pickup.IntakeSubsystem;

public class IntakeCommand extends Command {
    private final IntakeSubsystem intakeSubSystem;
    private final double speed;

    public IntakeCommand(IntakeSubsystem intakeSubSystem, double speed) {
        this.intakeSubSystem = intakeSubSystem;
        this.speed = speed;
        addRequirements(intakeSubSystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intakeSubSystem.setPickUpMotorSpeed(speed);
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubSystem.setPickUpMotorSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}

