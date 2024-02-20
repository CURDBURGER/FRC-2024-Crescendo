package frc.robot.subsystems.pickup;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
//import com.ctre.phoenix;

public class PivotSubsystem extends SubsystemBase {
    public enum PivotPosition {
        in, out;
    }

    private final PWMVictorSPX pivotMotor;
    private final Encoder pivotEncoder;
    private final DigitalInput limitSwitch;
    private static ShuffleboardTab tab = Shuffleboard.getTab("General");
    private static GenericEntry encoderPosition = tab.add("Encoder Position", 0.0).getEntry();
    private static GenericEntry limitSwitchOn = tab.add("Limit Switch", false).getEntry();



    private PivotPosition pivotPosition = PivotPosition.in;

    public PivotSubsystem() {
        this.pivotMotor = new PWMVictorSPX(Constants.NotePickup.pivotMotor);
        this.pivotEncoder = new Encoder(Constants.NotePickup.pivotEncoder1, Constants.NotePickup.pivotEncoder2, Constants.NotePickup.pivotEncoder3);
        this.limitSwitch = new DigitalInput(Constants.NotePickup.lmiitSwitch);
    }

    public void setPivotPosition(PivotPosition pivotPosition) {
        this.pivotPosition = pivotPosition;
    }

    @Override
    public void periodic() {
        encoderPosition.setDouble(pivotEncoder.get());
        if (limitSwitch.get() == false) {
            pivotEncoder.reset();
        }
        limitSwitchOn.setBoolean(limitSwitch.get());

        var position = pivotEncoder.get();
        if (pivotPosition == PivotPosition.out && position > Constants.NotePickup.outEncoderPosition) {
            pivotMotor.set(Constants.NotePickup.pivotSpeed);
        } else if (pivotPosition == pivotPosition.in && position < Constants.NotePickup.inEncoderPosition  && limitSwitch.get() == true) {
            pivotMotor.set(-Constants.NotePickup.pivotSpeed);
        } else {
            pivotMotor.set(0);
        }
//        if (pivotPosition == PivotPosition.out) {
//            pivotMotor.set(Constants.NotePickup.pivotSpeed);
//        } else if (pivotPosition == pivotPosition.in && limitSwitch.get() == true) {
//            pivotMotor.set(-Constants.NotePickup.pivotSpeed);
//        } else {
//            pivotMotor.set(0);
//        }
    }
}


