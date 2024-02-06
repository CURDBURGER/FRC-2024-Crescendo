// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.ModuleIOSparkMax;
import frc.robot.subsystems.pickup.IntakeSubsystem;

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
    //private final AprilTagSubsystem aprilTagSubsystem = new AprilTagSubsystem();
    private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();

    // Controller
    private final CommandJoystick joystick = new CommandJoystick(0);
    private final CommandXboxController controller = new CommandXboxController(1);

    // Dashboard inputs
    //private final ShuffleboardTab tab = Shuffleboard.getTab("General");

    // private final LoggedDashboardChooser<Command> autoChooser;
//  private final LoggedDashboardNumber flywheelSpeedInput = new LoggedDashboardNumber("Flywheel Speed", 1500.0);

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        drive = new Drive(
                //new GyroIONavX(),
                new ModuleIOSparkMax(0),
                new ModuleIOSparkMax(1),
                new ModuleIOSparkMax(2),
                new ModuleIOSparkMax(3)
        );
//        shooter = new Shooter(new ShooterIOSparkMax());

        // flywheel = new Flywheel(new FlywheelIOSparkMax());
        // drive = new Drive(
        // new GyroIOPigeon2(),
        // new ModuleIOTalonFX(0),
        // new ModuleIOTalonFX(1),
        // new ModuleIOTalonFX(2),
        // new ModuleIOTalonFX(3));
        // flywheel = new Flywheel(new FlywheelIOTalonFX());

        // Set up auto routines
        // NamedCommands.registerCommand(
        //     "Run Flywheel",
        //     Commands.startEnd(
        //             () -> flywheel.runVelocity(flywheelSpeedInput.get()), flywheel::stop, flywheel)
        //         .withTimeout(5.0));
        // autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

        // Set up feedforward characterization
        // autoChooser.addOption(
        //     "Drive FF Characterization",
        //     new FeedForwardCharacterization(
        //         drive, drive::runCharacterizationVolts, drive::getCharacterizationVelocity));
        // autoChooser.addOption(
        //     "Flywheel FF Characterization",
        //     new FeedForwardCharacterization(
        //         flywheel, flywheel::runVolts, flywheel::getCharacterizationVelocity));

        // Configure the button bindings
        configureButtonBindings();
    }

    public void robotEnabled() {
        getManualShoot();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * Joystick} or {@link XboxController}), and then passing it to a {@link
     * JoystickButton}.
     */
    private void configureButtonBindings() {
        // Climber
        controller.start().whileTrue(new ClimberCommand(climberSubsystem, 1.0));
        controller.back().whileTrue(new ClimberCommand(climberSubsystem, -1.0));

        // Auto shoot
        controller.a().whileTrue(getAutoShoot());

        // Manual shoot
        controller.x().whileTrue(getManualShoot());
        joystick.button(11).whileTrue(getManualShoot());

        // Drive
        drive.setDefaultCommand(
                DriveCommands.joystickDrive(
                        drive,
                        () -> {
                            var input = -joystick.getY();
                            //tab.add("forward/back input", input);
                            return input;
                        },
                        () -> {
                            var input = -joystick.getX();
                            //tab.add("side/side input", input);
                            return input;
                        },
                        () -> {
                           // var input = joystick.getTwist();
                            var input = 0;
                            //tab.add("spin input", input);
                            return input;
                        }
                )
        );

        joystick.button(12).onTrue(Commands.runOnce(drive::stopWithX, drive));
        joystick.button(11).onTrue(Commands.runOnce(drive::straightenWheels, drive));
        //joystick.button(8).onTrue(Commands.runOnce(() -> drive.setPose(new Pose2d(drive.getPose().getTranslation(), new Rotation2d())), drive).ignoringDisable(true));

        // hooting


//    shooter.setDefaultCommand(
//            ShooterCommands.triggerShoot(
//                    shooter,
//                    () -> controller.getLeftTriggerAxis(),
//                    () -> controller.getRightTriggerAxis()));


        // controller
        //     .a()
        //     .whileTrue(
        //         Commands.startEnd(
        //             () -> flywheel.runVelocity(flywheelSpeedInput.get()), flywheel::stop, flywheel));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // return autoChooser.get();
        return null;
    }

    private Command getAutoShoot() {
        return new SequentialCommandGroup(
                // spin up
                new ParallelRaceGroup(
                        //new AutomaticAlignCommand(aprilTagSubsystem),
                        //new AutomaticShooterCommand(shooterSubsystem, Constants.Shooter.autoShooterSpeed)
                ),
                new ParallelRaceGroup(
                        //new AutomaticShooterCommand(shooterSubsystem, Constants.Shooter.autoShooterSpeed),
                        new IntakeCommand(intakeSubsystem, -1),
                        new TimerCommand(Constants.Shooter.outtakeTime)
                )
        );
    }

    private Command getManualShoot() {
        return new SequentialCommandGroup(
                // spin up
                new ParallelRaceGroup(
                        new ManualShooterCommand(shooterSubsystem, joystick),
                        new TimerCommand(Constants.Shooter.revTime)
                ),
                new ParallelRaceGroup(
                        new ManualShooterCommand(shooterSubsystem, joystick),
                        new IntakeCommand(intakeSubsystem, -1),
                        new TimerCommand(Constants.Shooter.outtakeTime)
                )
        );
    }
}
