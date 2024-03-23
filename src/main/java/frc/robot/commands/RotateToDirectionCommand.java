package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.Drive;

public class RotateToDirectionCommand extends Command {
    private final Drive drive;
    private double speed;
    private final double direction;
    private Pose2d initialPose;

    //speed in meters/second
    //distance in meters
    //direction in degrees from -180 to 180 right positive
    public RotateToDirectionCommand(Drive drive, double speed, double direction) {
        this.drive = drive;
        this.speed = speed;
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
        if (direction <= 0) {
            speed = speed * -1;
        }
        drive.runVelocity(new Translation2d(0, 0), speed);
    }

    @Override
    public void end(boolean interrupted) {
//        drive.runVelocity(new ChassisSpeeds(0, 0, 0));
    }

    @Override
    public boolean isFinished() {
        double distanceRot = Math.abs(drive.getPose().getRotation().getRadians() - initialPose.getRotation().getRadians());
        return distanceRot >= Math.abs(direction);
    }
}
