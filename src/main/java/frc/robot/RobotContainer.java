// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.commands.autoCommands.AmpAutoCommand;
import frc.robot.commands.autoCommands.FourPieceCommand;
import frc.robot.commands.autoCommands.LeaveCommand;
import frc.robot.commands.autoCommands.TwoPieceCommand;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIONavX;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.pickup.IntakeSubsystem;
import frc.robot.subsystems.pickup.PivotSubsystem;
import frc.robot.subsystems.AprilTagSubsystem;

import static frc.robot.Constants.WheelModule.*;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // Subsystems
    private final Drive drive;
    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
    private final AprilTagSubsystem aprilTagSubsystem = new AprilTagSubsystem();
    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    private final PivotSubsystem pivotSubsystem = new PivotSubsystem();

    //random vars
    private boolean isFieldOriented = false;
    private final SendableChooser<AutoChoice> autoChooser = new SendableChooser<>();
    private GenericEntry debugFieldOriented = Shuffleboard.getTab("General").add("Is Field Oriented", isFieldOriented).getEntry();

    // Controller
    private final CommandJoystick joystick = new CommandJoystick(0);
    private final CommandXboxController controller = new CommandXboxController(1);

    // Dashboard inputs

    // private final LoggedDashboardChooser<Command> autoChooser;
//  private final LoggedDashboardNumber flywheelSpeedInput = new LoggedDashboardNumber("Flywheel Speed", 1500.0);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        drive = new Drive(
                new GyroIONavX(),
                new ModuleIOTalonFX(FRONT_LEFT),
                new ModuleIOTalonFX(FRONT_RIGHT),
                new ModuleIOTalonFX(BACK_LEFT),
                new ModuleIOTalonFX(BACK_RIGHT)
        );
        // Configure the button bindings
        configureButtonBindings();

        autoChooser.addOption("Leave", AutoChoice.Leave);
        autoChooser.addOption("Two Piece", AutoChoice.TwoPiece);
        autoChooser.addOption("Four Piece", AutoChoice.FourPiece);
        autoChooser.addOption("Amp", AutoChoice.Amp);
        autoChooser.setDefaultOption("Leave", AutoChoice.Leave);

        Shuffleboard.getTab("General").add("Auto Choice", autoChooser).withWidget(BuiltInWidgets.kComboBoxChooser);
    }

    public void robotEnabled() {
        new PivotCommand(pivotSubsystem, false);
        drive.setPose(new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        drive.straightenWheels();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * Joystick} or {@link XboxController}), and then passing it to a {@link
     * JoystickButton}.
     */
    private void configureButtonBindings() {
        // Climber
        joystick.button(12).whileTrue(new ClimberCommand(climberSubsystem, Constants.Climber.climberSpeed));
        joystick.button(11).whileTrue(new ClimberCommand(climberSubsystem, -Constants.Climber.climberSpeed));
        controller.start().whileTrue(new ClimberCommand(climberSubsystem, Constants.Climber.climberSpeed));
        controller.back().whileTrue(new ClimberCommand(climberSubsystem, -Constants.Climber.climberSpeed));
        controller.rightBumper().whileTrue(new SingleClimberCommand(climberSubsystem, Constants.Climber.climberSpeed, true));
        controller.rightTrigger().whileTrue(new SingleClimberCommand(climberSubsystem, -Constants.Climber.climberSpeed, true));
        controller.leftBumper().whileTrue(new SingleClimberCommand(climberSubsystem, Constants.Climber.climberSpeed, false));
        controller.leftTrigger().whileTrue(new SingleClimberCommand(climberSubsystem, -Constants.Climber.climberSpeed, false));

        // Auto shoot
//        joystick.trigger().whileTrue(getAutoShoot());
        controller.a().whileTrue(getAutoShoot());

        // Manual shoot
        joystick.button(2).onTrue(getManualShoot());
        joystick.trigger().onTrue(getManualShoot());
        controller.x().onTrue(getManualShoot());

        //Intake
        joystick.button(3).whileTrue(new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed));
        joystick.button(3).whileTrue(new PivotCommand(pivotSubsystem, true));
        joystick.button(3).whileFalse(new PivotCommand(pivotSubsystem, false));
        controller.y().whileTrue(new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed));
        controller.b().whileTrue(new PivotCommand(pivotSubsystem, true));
        controller.b().whileFalse(new PivotCommand(pivotSubsystem, false));

        //Amp
        joystick.button(4).whileTrue(new ClimberCommand(climberSubsystem, Constants.Amp.ampClimberSpeed));
        joystick.button(6).whileTrue(new ClimberCommand(climberSubsystem, -Constants.Amp.ampClimberSpeed));
        //Spit
        joystick.button(8).whileTrue(new IntakeCommand(intakeSubsystem, Constants.NotePickup.spitSpeed));

        // Drive
        joystick.button(10).onTrue(toggleFieldOriented());
        drive.setDefaultCommand(
                DriveCommands.joystickDrive(
                        drive,
                        () -> { // x forward is front, -x is backward
                            return joystick.getY();
                        },
                        () -> { // y+ is to the left, y- is to the right
                            return -joystick.getX();
                        },
                        () -> { // z+ is rotating counterclockwise
                            return -joystick.getTwist();
                        },
//                        joystick.button(10).getAsBoolean()
                        true
                )
        );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */

    private Command getAutoShoot() {
        return new ParallelRaceGroup(
                // spin up
                new ManualShooterCommand(shooterSubsystem, Constants.Shooter.speaker),
                new SequentialCommandGroup(
                       new AutomaticAlignCommand(aprilTagSubsystem, drive),
                        new ParallelRaceGroup(
                                new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
                                new TimerCommand(Constants.Shooter.outtakeTime)
                        )
                )
        );
    }

    private Command getManualShoot() {
        return new ParallelRaceGroup(
                // spin up
                new ManualShooterCommand(shooterSubsystem, Constants.Shooter.speaker),
                new SequentialCommandGroup(
                        new TimerCommand(Constants.Shooter.revTime),
                        new ParallelRaceGroup(
                                new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
                                new TimerCommand(Constants.Shooter.outtakeTime)
                        )
                )
        );
    }

    public Command toggleFieldOriented() {
//        this.isFieldOriented = !this.isFieldOriented;
//        debugFieldOriented.setBoolean(isFieldOriented);
        return new ParallelRaceGroup();
    }

    public Command getAutonomousCommand() {
        //Getting shuffleboard value
        AutoChoice autoChoice = autoChooser.getSelected();

        Command command;
        switch (autoChoice) {
            case Leave:
                command = LeaveCommand.create(drive);
                break;
            case TwoPiece:
                command = TwoPieceCommand.create(drive, intakeSubsystem, pivotSubsystem, shooterSubsystem);
                break;
            case FourPiece:
                command = FourPieceCommand.create(drive, intakeSubsystem, pivotSubsystem, shooterSubsystem);
                break;
            case Amp:
                command = AmpAutoCommand.create(drive, climberSubsystem, pivotSubsystem, intakeSubsystem);
                break;
            default:
                command = new SequentialCommandGroup();
        }
        return new ParallelCommandGroup(/*new LightCommand(lightsSubsystem),*/ command);
    }
}


