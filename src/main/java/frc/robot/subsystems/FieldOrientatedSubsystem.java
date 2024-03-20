package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class FieldOrientatedSubsystem extends SubsystemBase {
    private boolean fieldOrientated;

    public FieldOrientatedSubsystem() {
        this.fieldOrientated = false;
    }

    public boolean getBooleanState() {
        return fieldOrientated;
    }
    public void flipBooleanState(boolean fieldOrientated) {
        this.fieldOrientated = !fieldOrientated;
    }
    @Override
    public void periodic() {
        getBooleanState();
    }
}
