 package frc.robot.subsystems;


 import edu.wpi.first.wpilibj2.command.SubsystemBase;
 import org.photonvision.PhotonCamera;
 import org.photonvision.targeting.MultiTargetPNPResult;

 /**
  *
  */
 public class AprilTagSubsystem extends SubsystemBase {

     PhotonCamera camera;
     MultiTargetPNPResult targets = null;

     public AprilTagSubsystem() {
         camera = new PhotonCamera("photonvision");
     }

     @Override
     public void periodic() {
         // This method will be called once per scheduler run

         var result = camera.getLatestResult();

 // Get a list of currently tracked targets.
         targets = result.getMultiTagResult();


     }

     @Override
     public void simulationPeriodic() {
         // This method will be called once per scheduler run when in simulation

     }

     // Put methods for controlling this subsystem
     // here. Call these from Commands.
     public MultiTargetPNPResult getTargets() {
         return targets;
     }
 }
