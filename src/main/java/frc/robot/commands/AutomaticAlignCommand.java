 package frc.robot.commands;

 import edu.wpi.first.math.geometry.Transform3d;
 import edu.wpi.first.math.kinematics.ChassisSpeeds;
 import edu.wpi.first.networktables.GenericEntry;
 import edu.wpi.first.wpilibj.DriverStation;
 import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
 import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
 import edu.wpi.first.wpilibj2.command.Command;
 import frc.robot.Constants;
 import frc.robot.subsystems.AprilTagSubsystem;
 import frc.robot.subsystems.drive.Drive;
 import org.photonvision.targeting.PhotonTrackedTarget;

 import java.util.List;

 public class AutomaticAlignCommand extends Command {
     private AprilTagSubsystem aprilTagSubsystem;
     private Drive drive;
//     private final GenericEntry canSeeSpeakerTag;
//     private final GenericEntry depthToTag;
//     private final GenericEntry horizontalDistanceToTag;
//     private final GenericEntry yawToTag;
     private final ShuffleboardTab tab = Shuffleboard.getTab("General");
     private int speakerID;
     private boolean doneStrafing = false;
     private boolean doneMovingForward = false;

     public AutomaticAlignCommand(AprilTagSubsystem aprilTagSubsystem, Drive drive) {
         this.aprilTagSubsystem = aprilTagSubsystem;
         this.drive = drive;
//         canSeeSpeakerTag = tab.add("Can See Speaker Tag", false).getEntry();
//         depthToTag = tab.add("Depth To Tag", 0).getEntry();
//         horizontalDistanceToTag = tab.add("Horizontal Distance To Tag", 0).getEntry();
//         yawToTag = tab.add("Yaw To Tag", 0).getEntry();
     }
     //depth to tag  camera is at 0  max possible is 5.75 meters
     //horizontal  0 at center of camera
     //spin with vertical axis of rotation  straight is 180  robot is clockwise positive

     @Override
     public void initialize() {
         if(DriverStation.getAlliance().equals(DriverStation.Alliance.Blue)){
            speakerID = Constants.Align.blueShooterID;
         } else{
             speakerID = Constants.Align.redShooterID;
         }
     }

     @Override
     public void execute() {
         double xVelocity = 0;
         double yVelocity = 0;
         List<PhotonTrackedTarget> targets = aprilTagSubsystem.getTargets();

         if(targets.contains(speakerID)){
//             canSeeSpeakerTag.setBoolean(true);
             Transform3d speakerTag = targets.get(targets.indexOf(speakerID)).getBestCameraToTarget();

//             depthToTag.setDouble(speakerTag.getX());
//             horizontalDistanceToTag.setDouble(speakerTag.getY());
//             yawToTag.setDouble(speakerTag.getRotation().getAngle());

             if (Math.abs(speakerTag.getY()) > Constants.Align.errorMargin) {
                 if (speakerTag.getY() > 0) {
                     yVelocity = Constants.Align.alignSpeed;
                 } else {
                     yVelocity = -Constants.Align.alignSpeed;
                 }
             } else {
                 yVelocity = 0;
                 doneStrafing = true;
             }
             if (speakerTag.getX() > Constants.Align.shooterRange) {
                 xVelocity = Constants.Align.alignSpeed;
             } else {
                 xVelocity = 0;
                 doneMovingForward = true;
             }

//             drive.runVelocity(new ChassisSpeeds(xVelocity, yVelocity, 0));

         } else{
//             canSeeSpeakerTag.setDoubleBoolean(false);
         }
     }

     @Override
     public boolean isFinished() {
         return doneStrafing && doneMovingForward;
     }
 }
