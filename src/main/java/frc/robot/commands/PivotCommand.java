package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pickup.IntakeSubsystem;
import frc.robot.subsystems.pickup.PivotSubsystem;

public class PivotCommand extends Command {
    private final PivotSubsystem pivotSubsystem;
    private final double speed;

    public PivotCommand(PivotSubsystem pivotSubsystem, double speed) {
        this.pivotSubsystem = pivotSubsystem;
        this.speed = speed;
    }

    @Override
    public void initialize() {
        pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.out);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.in);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
