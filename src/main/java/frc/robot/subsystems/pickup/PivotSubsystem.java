package frc.robot.subsystems.pickup;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class PivotSubsystem extends SubsystemBase {

        private final MotorController pickUpMotor;
        private double speed;

        public PivotSubsystem() {
            this.pickUpMotor = new Victor(Constants.NotePickup.pivotMotor);
        }

        public void setPickUpMotorSpeed(double speed) {
            this.speed = speed;
        }

        @Override
        public void periodic() {
            pickUpMotor.set(speed);
        }
    }


