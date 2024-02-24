package frc.robot.commands.autoCommands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.commands.*;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.pickup.IntakeSubsystem;
import frc.robot.subsystems.pickup.PivotSubsystem;


public class FourPieceCommand {

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
                        new DriveToPoseCommand(drive, Constants.Auto.slowSpeed, Constants.Auto.noteRadius, 0),
                        new PivotCommand(pivotSubsystem, true),
                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.xDistanceToNote + Constants.Auto.noteRadius, 180),
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
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.diagonalDistanceToNote, Constants.Auto.directionToNote),
                        new PivotCommand(pivotSubsystem, true)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.slowSpeed, Constants.Auto.noteRadius+.3, 0),
                        new PivotCommand(pivotSubsystem, true),
                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                ),
                new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.noteRadius+.3, 180),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.diagonalDistanceToNote, -180 + Constants.Auto.directionToNote),
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
                ),
                new DriveToPoseCommand(drive, Constants.Auto.slowSpeed, .01, 0),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.diagonalDistanceToNote, -Constants.Auto.directionToNote),
                        new PivotCommand(pivotSubsystem, true)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.slowSpeed, Constants.Auto.noteRadius+.3, 0),
                        new PivotCommand(pivotSubsystem, true),
                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                ),
                new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.noteRadius+.3, 180),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, Constants.Auto.normalSpeed, Constants.Auto.diagonalDistanceToNote, 180 - Constants.Auto.directionToNote),
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
