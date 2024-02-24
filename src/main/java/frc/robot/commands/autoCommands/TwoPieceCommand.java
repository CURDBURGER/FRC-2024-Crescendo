package frc.robot.commands.autoCommands;


import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;
import frc.robot.commands.*;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.pickup.IntakeSubsystem;
import frc.robot.subsystems.pickup.PivotSubsystem;

public class TwoPieceCommand extends Command {
    public static Command create(Drive drive, IntakeSubsystem intakeSubsystem, PivotSubsystem pivotSubsystem, ShooterSubsystem shooterSubsystem) {
        return new SequentialCommandGroup(
                new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, .2, 0),
                new ParallelRaceGroup(
                        // spin up
                        new AutomaticShooterCommand(shooterSubsystem, Constants.Shooter.autoShooterSpeed),
                        new SequentialCommandGroup(
                                new TimerCommand(Constants.Shooter.revTime),
                                new ParallelRaceGroup(
                                        new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
                                        new TimerCommand(Constants.Shooter.outtakeTime)
                                )
                        )
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.xDistanceToNote, 0),
                        new PivotCommand(pivotSubsystem, true)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.slowSpeed, .5, 0),
                        new PivotCommand(pivotSubsystem, true),
                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.xDistanceToNote + .5, 180),
                        new PivotCommand(pivotSubsystem, false)
                ),
                new ParallelRaceGroup(
                        // spin up
                        new AutomaticShooterCommand(shooterSubsystem, Constants.Shooter.autoShooterSpeed),
                        new SequentialCommandGroup(
                                new TimerCommand(Constants.Shooter.revTime),
                                new ParallelRaceGroup(
                                        new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
                                        new TimerCommand(Constants.Shooter.outtakeTime)
                                )
                        )
                )
        );
    }

}
