package frc.robot.subsystems;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;
//import edu.wpi.first.math.geometry.Transform3d;
import java.util.List;

/**
 *
 */
public class AprilTagSubsystem extends SubsystemBase {

    PhotonCamera camera
    public AprilTagSubsystem() {
        camera = new PhotonCamera("photonvision");

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run

        var result = camera.getLatestResult();

        boolean hasTargets = result.hasTargets();

// Get a list of currently tracked targets.
        List<PhotonTrackedTarget> targets = result.getTargets();

        for(PhotonTrackedTarget target : targets){
            int targetID = target.getFiducialId();
            double poseAmbiguity = target.getPoseAmbiguity();

            // Transform Doesnt actually exist?
//            Transform3d bestCameraToTarget = target.getBestCameraToTarget();
//            Transform3d alternateCameraToTarget = target.getAlternateCameraToTarget();
        }




    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run when in simulation

    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}

