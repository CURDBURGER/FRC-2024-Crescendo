// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.commands.autoCommands.FourPieceCommand;
import frc.robot.commands.autoCommands.LeaveCommand;
import frc.robot.commands.autoCommands.OnePieceCommand;
import frc.robot.commands.autoCommands.TwoPieceCommand;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.PoseEstimationSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIONavX;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.pickup.IntakeSubsystem;
import frc.robot.subsystems.pickup.PivotSubsystem;

import static edu.wpi.first.wpilibj2.command.Commands.runOnce;
import static frc.robot.Constants.Swerve.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import org.photonvision.PhotonCamera;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // Subsystems
    private Drive drive;
    private final PoseEstimationSubsystem poseEstimationSubsystem;
    private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
    private final ClimberSubsystem climberSubsystem = new ClimberSubsystem();
    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
    private final PivotSubsystem pivotSubsystem = new PivotSubsystem();
    //random vars
    private final PhotonCamera photonCamera = new PhotonCamera("camera");
    //private final SendableChooser<Command> autoChooser;
    private final SendableChooser<AutoChoice> autoChooser = new SendableChooser<>();

    // Controller
    private final CommandXboxController driver = new CommandXboxController(0);
    private final CommandXboxController secondDriver = new CommandXboxController(1);

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
                new ModuleIOTalonFX(BACK_RIGHT),
                poseEstimationSubsystem = new PoseEstimationSubsystem(photonCamera, drive)
        );



        // Configure the button bindings
        configureButtonBindings();

        autoChooser.addOption("Leave", AutoChoice.Leave);
        autoChooser.addOption("One Piece", AutoChoice.OnePiece);
        autoChooser.addOption("Two Piece", AutoChoice.TwoPiece);
        autoChooser.addOption("Four Piece", AutoChoice.FourPiece);
        autoChooser.setDefaultOption("Leave", AutoChoice.Leave);

        // Shuffleboard.getTab("General").add("Auto Choice", autoChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        // autoChooser = AutoBuilder.buildAutoChooser();

        Shuffleboard.getTab("General").add("Auto Choice", autoChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        NamedCommands.registerCommand("Intake", new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed));
        NamedCommands.registerCommand("Pivot In", new PivotCommand(pivotSubsystem, false));
        NamedCommands.registerCommand("Pivot Out", new PivotCommand(pivotSubsystem, true));
        NamedCommands.registerCommand("Shooting", getManualShoot());

    }

    public void robotEnabled() {
        new PivotCommand(pivotSubsystem, false);
        poseEstimationSubsystem.setCurrentPose(new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        drive.straightenWheels();
        drive.resetGyro();
        drive.setFieldState(true);
        climberSubsystem.resetEncoders();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * Joystick} or {@link XboxController}), and then passing it to a {@link
     * JoystickButton}.
     */
    private void configureButtonBindings() {
        //Climber
        driver.leftTrigger().whileTrue(new ClimberCommand(climberSubsystem, -Constants.Climber.fastClimberSpeed));
        driver.leftBumper().whileTrue(new ClimberCommand(climberSubsystem, Constants.Climber.fastClimberSpeed));
        secondDriver.start().whileTrue(new ClimberCommand(climberSubsystem, Constants.Climber.fastClimberSpeed));
        secondDriver.back().whileTrue(new ClimberCommand(climberSubsystem, -Constants.Climber.fastClimberSpeed));
        secondDriver.rightTrigger().whileTrue(new SingleClimberCommand(climberSubsystem, -Constants.Climber.fastClimberSpeed, true));
        secondDriver.rightBumper().whileTrue(new SingleClimberCommand(climberSubsystem, Constants.Climber.fastClimberSpeed, true));
        secondDriver.leftTrigger().whileTrue(new SingleClimberCommand(climberSubsystem, -Constants.Climber.fastClimberSpeed, false));
        secondDriver.leftBumper().whileTrue(new SingleClimberCommand(climberSubsystem, Constants.Climber.fastClimberSpeed, false));

        //Shooting
        driver.rightTrigger().onTrue(getManualShoot());

        //Intake
        driver.rightBumper().whileTrue(new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed));
        driver.rightBumper().whileTrue(new PivotCommand(pivotSubsystem, true));
        driver.rightBumper().whileFalse(new PivotCommand(pivotSubsystem, false));
        driver.a().whileTrue(new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed));
        secondDriver.y().onTrue(runOnce(() -> pivotSubsystem.setOverwrite(true)));
        secondDriver.y().onFalse(runOnce(() -> pivotSubsystem.setOverwrite(false)));

        //Spit
        driver.x().whileTrue(new IntakeCommand(intakeSubsystem, Constants.NotePickup.spitSpeed));
        secondDriver.x().whileTrue(new IntakeCommand(intakeSubsystem, Constants.NotePickup.spitSpeed));

        //Center
        driver.povDown().whileTrue(getCentering());
        secondDriver.a().whileTrue(getCentering());

        //Drive
        driver.povUp().onTrue(runOnce(poseEstimationSubsystem::resetFieldPosition));
        driver.povLeft().onTrue(runOnce(() -> drive.toggleIsFieldOriented()));

        drive.setDefaultCommand(
                DriveCommands.joystickDrive(
                        drive,
                        () -> { // x forward is front, -x is backward
//                            return joystick.getY();
                            return driver.getLeftY();
                        },
                        () -> { // y+ is to the left, y- is to the right
//                            return -joystick.getX();
                            return -driver.getLeftX();
                        },
                        () -> { // z+ is rotating counterclockwise
//                            return -joystick.getTwist();
                            return -driver.getRightX();
                        }
                )
        );
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */

//    private Command getAutoShoot() {
//        return new ParallelRaceGroup(
//                // spin up
//                new ManualShooterCommand(shooterSubsystem, Constants.Shooter.speaker),
//                new SequentialCommandGroup(
//                        new AutomaticAlignCommand(aprilTagSubsystem, drive),
//                        new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed),
//                        new ParallelRaceGroup(
//                                new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed),
//                                new TimerCommand(Constants.Shooter.outtakeTime)
//                        )
//                )
//        );
//    }
    private Command getManualShoot() {
        return new ParallelRaceGroup(
                // spin up
                new ManualShooterCommand(shooterSubsystem, Constants.Shooter.speaker),
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
        );
    }

    public Command getCentering() {
        return new RepeatCommand(
                new SequentialCommandGroup(
                        new ParallelRaceGroup(
                                new TimerCommand(250),
                                new IntakeCommand(intakeSubsystem, Constants.NotePickup.inputMotorSpeed)
                        ),
                        new TimerCommand(100),
                        new ParallelRaceGroup(
                                new TimerCommand(200),
                                new IntakeCommand(intakeSubsystem, -Constants.NotePickup.inputMotorSpeed)
                        )
                )
        );
    }

    public Command getAutonomousCommand() {
        //Getting shuffleboard value
        AutoChoice autoChoice = autoChooser.getSelected();
        Command command;
        switch (autoChoice) {
            case Leave:
                command = LeaveCommand.create(drive);
                break;
            case OnePiece:
                command = OnePieceCommand.create(intakeSubsystem, shooterSubsystem, drive);
                break;
            case TwoPiece:
                command = TwoPieceCommand.create(drive, intakeSubsystem, pivotSubsystem, shooterSubsystem);
                break;
            case FourPiece:
                command = FourPieceCommand.create(drive, intakeSubsystem, pivotSubsystem, shooterSubsystem);
                break;
            default:
                command = new SequentialCommandGroup();
        }
        return new ParallelCommandGroup(command);
    }
}


