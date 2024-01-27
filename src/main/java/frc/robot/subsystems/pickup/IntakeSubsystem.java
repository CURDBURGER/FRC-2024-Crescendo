package frc.robot.subsystems.pickup;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class IntakeSubsystem extends SubsystemBase {

        private final MotorController pickUpMotor;
        private double speed;

        public IntakeSubsystem() {
            this.pickUpMotor = new Victor(Constants.NotePickup.inputMotorChannel);
        }

        public void setPickUpMotorSpeed(double speed) {
            this.speed = speed;
        }

        @Override
        public void periodic() {
            pickUpMotor.set(speed);
        }
    }


