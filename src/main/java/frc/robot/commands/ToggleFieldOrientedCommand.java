package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.Drive;

public class ToggleFieldOrientedCommand extends Command {
    private final Drive drive;

    public ToggleFieldOrientedCommand(Drive drive) {
        this.drive = drive;
        addRequirements(drive);
    }
    @Override
    public void execute() {
        drive.toggleFieldOriented();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
