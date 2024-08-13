package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.PoseEstimationSubsystem;
import frc.robot.subsystems.drive.Drive;

public class DriveToPoseCommand extends Command {
    private final Drive drive;
    private final PoseEstimationSubsystem poseEstimationSubsystem;
    private final double speed;
    private final double distance;
    private final double direction;
    private Pose2d initialPose;

    //speed in meters/second
    //distance in meters
    //direction in degrees from -180 to 180 right positive
    public DriveToPoseCommand(Drive drive, PoseEstimationSubsystem poseEstimationSubsystem,double speed, double distance, double direction) {
        this.drive = drive;
        this.poseEstimationSubsystem = poseEstimationSubsystem;
        this.speed = speed;
        this.distance = distance;
        this.direction = Math.toRadians(direction);
        addRequirements(drive);
    }

    @Override
    public void initialize() {
        initialPose = poseEstimationSubsystem.getCurrentPose();
    }

    @Override
    public void execute() {
        Rotation2d rotation = poseEstimationSubsystem.getCurrentPose().getRotation();
        double xSpeed = speed*Math.cos(direction);
        double ySpeed = -speed*Math.sin(direction);
        drive.setOrientation(new Translation2d(xSpeed, ySpeed), 0);
    }

    @Override
    public void end(boolean interrupted) {
        drive.runVelocity(new ChassisSpeeds());
    }

    @Override
    public boolean isFinished() {
        double distanceToX = poseEstimationSubsystem.getCurrentPose().getX()-initialPose.getX();
        double distanceToY = poseEstimationSubsystem.getCurrentPose().getY()-initialPose.getY();
        double distanceToPose = Math.hypot(distanceToX, distanceToY);
        return distanceToPose >= distance;
    }
}
