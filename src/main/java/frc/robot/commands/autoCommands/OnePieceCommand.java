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

public class OnePieceCommand extends Command {
    public static Command create(IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem, Drive drive) {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
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
                        )
                ),
                new DriveToPoseCommand(drive, .15, 2, 70)
        );
    }

}
