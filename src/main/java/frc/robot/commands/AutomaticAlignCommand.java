package frc.robot.commands;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AprilTagSubsystem;

public class AutomaticAlignCommand extends Command {
    private AprilTagSubsystem aprilTagSubsystem;
    private final ShuffleboardTab tab = Shuffleboard.getTab("General");

    public AutomaticAlignCommand(AprilTagSubsystem aprilTagSubsystem) {
        this.aprilTagSubsystem = aprilTagSubsystem;
    }

    @Override
    public void execute() {
        var result = aprilTagSubsystem.getTargets();
        if (result.estimatedPose.isPresent) {
            Transform3d fieldToCamera = result.estimatedPose.best;

            var x = fieldToCamera.getX();
            var y = fieldToCamera.getY();
            var z = fieldToCamera.getZ();
            var rotation = fieldToCamera.getRotation().getZ();

            tab.add("x pos", x);
            tab.add("y pos", y);
            tab.add("z pos", z);
            tab.add("rotation", rotation);

        }

    }

}
