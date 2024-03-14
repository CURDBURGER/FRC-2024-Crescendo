package frc.robot.commands.autoCommands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.commands.*;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.pickup.IntakeSubsystem;
import frc.robot.subsystems.pickup.PivotSubsystem;


public class AmpAutoCommand {

    public static Command create(Drive drive, ClimberSubsystem climberSubsystem, PivotSubsystem pivotSubsystem, IntakeSubsystem intakeSubsystem) {
        return new SequentialCommandGroup(
                new RotateToCommand(drive, Constants.Auto.rotateSpeed, -90),
                new ParallelCommandGroup(
                        new ClimberCommand(climberSubsystem, Constants.Amp.ampClimberSpeed),
                        new TimerCommand(Constants.Amp.ampClimberTime)
                ),
                new ParallelCommandGroup(
                        new ClimberCommand(climberSubsystem, -Constants.Amp.ampClimberSpeed),
                        new TimerCommand(Constants.Amp.ampClimberTime)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.xDistanceToNote, 0),
                        new PivotCommand(pivotSubsystem, true)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.slowSpeed, .5, 90),
                        new PivotCommand(pivotSubsystem, true),
                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                )
        );
    }

}
