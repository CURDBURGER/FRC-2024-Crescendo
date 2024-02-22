package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.drive.Drive;

public class DriveToPoseCommand extends Command {
    private final Drive drive;
    private final double speed;
    private final double distance;
    private final double direction;
    private Pose2d initialPose;
    private final GenericEntry ySpeedDebug;

    //speed in meters/second
    //distance in meters
    //direction in degrees from -180 to 180 right positive
    public DriveToPoseCommand(Drive drive, double speed, double distance, double direction) {
        this.drive = drive;
        this.speed = speed;
        this.distance = distance;
        this.direction = Math.toRadians(direction);
        addRequirements(drive);
        ySpeedDebug =  Shuffleboard.getTab("General").add("Y Speed", 0).getEntry();
    }

    @Override
    public void initialize() {
        initialPose = drive.getPose();
    }

    @Override
    public void execute() {
        Rotation2d rotation = drive.getRotation();
        double xSpeed = speed*Math.cos(direction);
        double ySpeed = -speed*Math.sin(direction);
        ySpeedDebug.setDouble(ySpeed);
        drive.runVelocity(
                ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, 0, rotation)
        );
    }

    @Override
    public void end(boolean interrupted) {
        drive.runVelocity(new ChassisSpeeds(0, 0, 0));
    }

    @Override
    public boolean isFinished() {
        double distanceToX = drive.getPose().getX()-initialPose.getX();
        double distanceToY = drive.getPose().getY()-initialPose.getY();
        double distanceToPose = Math.hypot(distanceToX, distanceToY);
        return distanceToPose >= distance;
    }
}
