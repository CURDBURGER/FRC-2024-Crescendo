// package frc.robot.subsystems;
//
//
// import edu.wpi.first.wpilibj2.command.SubsystemBase;
// import org.photonvision.PhotonCamera;
// import org.photonvision.targeting.MultiTargetPNPResult;
// import org.photonvision.targeting.PhotonTrackedTarget;
//
// import java.util.List;
//
// /**
//  *
//  */
// public class AprilTagSubsystem extends SubsystemBase {
//
//     PhotonCamera camera;
//     MultiTargetPNPResult multiTargets = null;
//     List<PhotonTrackedTarget> targets = null;
//
//     public AprilTagSubsystem() {
//         camera = new PhotonCamera("Photon_Camera");
//     }
//
//     @Override
//     public void periodic() {
//         // This method will be called once per scheduler run
//
//         var result = camera.getLatestResult();
//
// // Get a list of currently tracked targets.
//         multiTargets = result.getMultiTagResult();
//         if(result.hasTargets()){
//             targets = result.getTargets();
//         }
//     }
//
//     @Override
//     public void simulationPeriodic() {
//         // This method will be called once per scheduler run when in simulation
//
//     }
//
//     // Put methods for controlling this subsystem
//     // here. Call these from Commands.
//     public MultiTargetPNPResult getMultiTargetPNPResults() {
//         return multiTargets;
//     }
//
//     public List<PhotonTrackedTarget> getTargets(){return targets;}
// }
