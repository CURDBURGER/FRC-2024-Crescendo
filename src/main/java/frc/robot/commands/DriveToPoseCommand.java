package frc.robot.commands;

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
    public DriveToPoseCommand(Drive drive, double speed, double distance, double direction) {
        this.drive = drive;
        this.speed = speed;
        this.distance = distance;
        this.direction = direction;
        addRequirements(drive);
    }

    @Override
    public void execute() {
        Rotation2d rotation = drive.getRotation();
//        double xSpeed =
//        drive.runVelocity(
//                ChassisSpeeds.fromFieldRelativeSpeeds(
//
//            )
//        );
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
