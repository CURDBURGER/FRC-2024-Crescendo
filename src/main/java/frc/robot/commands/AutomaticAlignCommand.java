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

             var x = fieldToCamera.getX(); //depth to tag  camera is at 0  max possible is 5.75 meters
             var y = fieldToCamera.getY(); //horizontal  0 at center of camera
             var z = fieldToCamera.getZ();
             var rotation = fieldToCamera.getRotation().getZ(); //spin with vertical axis of rotation  straight is 180  robot is clockwise positive

             tab.add("AprilTag X", x);
             tab.add("AprilTag Y", y);
             tab.add("AprilTag Z", z);
             tab.add("AprilTag Rotation", rotation);
         }


     }

 }
