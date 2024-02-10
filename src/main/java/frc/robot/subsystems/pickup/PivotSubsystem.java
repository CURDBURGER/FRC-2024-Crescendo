package frc.robot.subsystems.pickup;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class PivotSubsystem extends SubsystemBase {
    public enum PivotPosition {
        in, out;
    }

    private final MotorController pivotMotor;
    private double speed;
    private final Encoder pivotEncoder;

    private PivotPosition pivotPosition = PivotPosition.in;

    public PivotSubsystem() {
        this.pivotMotor = new Victor(Constants.NotePickup.pivotMotor);
        this.pivotEncoder = new Encoder(Constants.NotePickup.pivotEncoder1, Constants.NotePickup.pivotEncoder2);
    }

    public void setPivotPosition(PivotPosition pivotPosition) {
        this.pivotPosition = pivotPosition;
    }

    @Override
    public void periodic() {

        var position = pivotEncoder.get();
        if (pivotPosition == PivotPosition.out && position < 245) {
            pivotMotor.set(0.25);
        } else if (pivotPosition == pivotPosition.in && position > 0) {
            pivotMotor.set(-0.25);
        } else {
            pivotMotor.set(0);
        }
        pivotMotor.set(speed);
    }
}


