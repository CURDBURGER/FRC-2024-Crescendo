package frc.robot.subsystems.drive;


import edu.wpi.first.math.geometry.Rotation2d;

public interface GyroIO {

    class GyroIOInputs {
        public boolean connected = false;
        public Rotation2d yawPosition = new Rotation2d();
        public Rotation2d rollPosition = new Rotation2d();
        public Rotation2d pitchPosition = new Rotation2d();
    }

    default void updateInputs(GyroIOInputs inputs) {
    }
}