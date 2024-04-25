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
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.*;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIONavX;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import static frc.robot.Constants.Swerve.*;

import com.pathplanner.lib.auto.AutoBuilder;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // Subsystems
    private final Drive drive;
    //random vars
    private final SendableChooser<Command> autoChooser;
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
                new ModuleIOTalonFX(BACK_RIGHT)
        );
        // Configure the button bindings
        configureButtonBindings();

        // autoChooser.addOption("Leave", AutoChoice.Leave);
        // autoChooser.addOption("One Piece", AutoChoice.OnePiece);
        // autoChooser.addOption("Two Piece", AutoChoice.TwoPiece);
        // autoChooser.addOption("Four Piece", AutoChoice.FourPiece);
        // autoChooser.setDefaultOption("Leave", AutoChoice.Leave);

        // Shuffleboard.getTab("General").add("Auto Choice", autoChooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        autoChooser = AutoBuilder.buildAutoChooser();

        Shuffleboard.getTab("General").add("Auto Choice", autoChooser).withWidget(BuiltInWidgets.kComboBoxChooser);


    }

    public void robotEnabled() {
        drive.setPose(new Pose2d(new Translation2d(0, 0), new Rotation2d(0)));
        drive.straightenWheels();
        drive.resetGyro();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * Joystick} or {@link XboxController}), and then passing it to a {@link
     * JoystickButton}.
     */
    private void configureButtonBindings() {

        //Drive
        driver.povUp().onTrue(Commands.runOnce(() -> drive.setPose(new Pose2d(drive.getPose().getTranslation(), new Rotation2d())), drive));
        driver.povLeft().onTrue(Commands.runOnce(() -> drive.toggleIsFieldOriented()));

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


}


