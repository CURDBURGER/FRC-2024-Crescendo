package frc.robot.commands.autoCommands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.commands.*;
import frc.robot.subsystems.PoseEstimationSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.pickup.IntakeSubsystem;
import frc.robot.subsystems.pickup.PivotSubsystem;

public class TwoPieceCommand extends Command {
    public static Command create(Drive drive, PoseEstimationSubsystem poseEstimationSubsystem, IntakeSubsystem intakeSubsystem, PivotSubsystem pivotSubsystem, ShooterSubsystem shooterSubsystem) {
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        // spin up
                        new AutomaticShooterCommand(shooterSubsystem, Constants.Shooter.autoShooterSpeed),
                        new SequentialCommandGroup(
                                new ParallelRaceGroup(
                                        new TimerCommand(500),
                                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                                ),
                                new TimerCommand(250),
                                new ParallelRaceGroup(
                                        new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
                                        new TimerCommand(Constants.Shooter.outtakeTime)
                                )
                        )
                ),
                new ParallelRaceGroup(
                        new TimerCommand(700),
                        new PivotCommand(pivotSubsystem, true)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, poseEstimationSubsystem, Constants.Auto.normalSpeed, Constants.Auto.xDistanceToNote, 0),
                        new PivotCommand(pivotSubsystem, true)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, poseEstimationSubsystem, Constants.Auto.slowSpeed, .5, 0),
                        new PivotCommand(pivotSubsystem, true),
                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, poseEstimationSubsystem, Constants.Auto.normalSpeed, Constants.Auto.xDistanceToNote + .4, 180),
                        new PivotCommand(pivotSubsystem, false)
                ),
                new ParallelRaceGroup(
                        // spin up
                        new AutomaticShooterCommand(shooterSubsystem, Constants.Shooter.autoShooterSpeed),
                        new SequentialCommandGroup(
                                new ParallelRaceGroup(
                                        new TimerCommand(500),
                                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                                ),
                                new TimerCommand(250),
                                new ParallelRaceGroup(
                                        new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
                                        new TimerCommand(Constants.Shooter.outtakeTime)
                                )
                        )
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, poseEstimationSubsystem, .15, Constants.Auto.xDistanceToNote + 1.3, 0),
                        new PivotCommand(pivotSubsystem, false)
                )
        );
    }

}
