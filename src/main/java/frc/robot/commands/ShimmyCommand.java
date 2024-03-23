package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.pickup.IntakeSubsystem;

public class ShimmyCommand extends Command{
    private final IntakeSubsystem intakeSubsystem;
    public ShimmyCommand(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void execute() {
        new SequentialCommandGroup(
                new ParallelRaceGroup(
                        new TimerCommand(200),
                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                ),
                new TimerCommand(100),
                new ParallelRaceGroup(
                        new TimerCommand(200),
                        new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed)
                )
        );
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.setPickUpMotorSpeed(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}