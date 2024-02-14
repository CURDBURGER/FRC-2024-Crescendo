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

public class TwoPieceCommand extends Command{
    public static Command create(Drive drive, IntakeSubsystem intakeSubsystem, PivotSubsystem pivotSubsystem, ShooterSubsystem shooterSubsystem) {
        return new SequentialCommandGroup(
                new ParallelRaceGroup(
                        new AutomaticShooterCommand(shooterSubsystem, .5),
                        new TimerCommand(250)
                ),
                new ParallelRaceGroup(
                        new AutomaticShooterCommand(shooterSubsystem, .5),
                        new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
                        new TimerCommand(250)
                ),
                new ParallelCommandGroup(
                        new DriveToPoseCommand(drive, 5, 2, 0),
                        new PivotCommand(pivotSubsystem, true)
                ),
                new ParallelRaceGroup(
                        new DriveToPoseCommand(drive, 3, 10, 0),
                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed),
                        new TimerCommand(1000)
                ),
                new ParallelCommandGroup(
                        new DriveToPoseCommand(drive, 5, 2, 180),
                        new PivotCommand(pivotSubsystem, false)
                ),
                new ParallelRaceGroup(
                        new AutomaticShooterCommand(shooterSubsystem, .5),
                        new TimerCommand(250)
                ),
                new ParallelRaceGroup(
                        new AutomaticShooterCommand(shooterSubsystem, .5),
                        new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
                        new TimerCommand(250)
                )
        );
    }

}
