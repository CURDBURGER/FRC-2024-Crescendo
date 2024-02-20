package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.drive.Drive;

public class DriveToPoseCommand extends Command {
    private final Drive drive;
    private final double speed;
    private final double distance;
    private final double direction;
    private Pose2d initialPose;

    //speed in meters/second
    //distance in meters
    //direction in degrees from -180 to 180 right positive
    public DriveToPoseCommand(Drive drive, double speed, double distance, double direction) {
        this.drive = drive;
        this.speed = speed;
        this.distance = distance;
        this.direction = Math.toRadians(direction);
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        initialPose = drive.getPose();
    }

    @Override
    public void execute() {
        Rotation2d rotation = drive.getRotation();
        double xSpeed = -speed*Math.sin(direction);
        double ySpeed = speed*Math.cos(direction);
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
        double distanceToPose = Math.sqrt(distanceToX * distanceToX + distanceToY * distanceToY);
        return distanceToPose >= distance;
    }
}
