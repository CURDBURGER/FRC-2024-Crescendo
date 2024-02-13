package frc.robot.subsystems.drive;


import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

import static frc.robot.Constants.WheelModule.*;

/**
 * Module IO implementation for SparkMax drive motor controller, SparkMax turn motor controller (NEO
 * or NEO 550), and analog absolute encoder connected to the RIO
 *
 * <p>NOTE: This implementation should be used as a starting point and adapted to different hardware
 * configurations (e.g. If using a CANcoder, copy from "ModuleIOTalonFX")
 *
 * <p>To calibrate the absolute encoder offsets, point the modules straight (such that forward
 * motion on the drive motor will propel the robot forward) and copy the reported values from the
 * absolute encoders using AdvantageScope. These values are logged under
 * "/Drive/ModuleX/TurnAbsolutePositionRad"
 */
public class ModuleIOSparkMax implements ModuleIO {
    // Gear ratios for SDS MK4i L2, adjust as necessary
    private static final double DRIVE_GEAR_RATIO = (50.0 / 14.0) * (17.0 / 27.0) * (45.0 / 15.0);
    private static final double TURN_GEAR_RATIO = 150.0 / 7.0;     // Check if rotation is correct

    private final CANSparkMax driveSparkMax;
    private final CANSparkMax turnSparkMax;

    private final RelativeEncoder driveEncoder;
    private final RelativeEncoder turnRelativeEncoder;
    private final CANcoder turnAbsoluteEncoder;

    private final boolean isTurnMotorInverted = false;
    private final Rotation2d absoluteEncoderOffset;

    public ModuleIOSparkMax(int index) {
        switch (index) {
            case FRONT_LEFT: // FL
                driveSparkMax = new CANSparkMax(6, MotorType.kBrushless);
                turnSparkMax = new CANSparkMax(5, MotorType.kBrushless);
                turnAbsoluteEncoder = new CANcoder(1, "rio");
                absoluteEncoderOffset = new Rotation2d((-0.781982421875 * 0) * 2.0 * Math.PI); // MUST BE CALIBRATED
                driveSparkMax.setInverted(true);
                break;
            case FRONT_RIGHT: // FR
                driveSparkMax = new CANSparkMax(8, MotorType.kBrushless);
                turnSparkMax = new CANSparkMax(7, MotorType.kBrushless);
                turnAbsoluteEncoder = new CANcoder(2, "rio");
                absoluteEncoderOffset = new Rotation2d((-0.382568359375 * 0) * 2.0 * Math.PI); // MUST BE CALIBRATED
                driveSparkMax.setInverted(true);
                break;
            case BACK_LEFT: // BL
                driveSparkMax = new CANSparkMax(1, MotorType.kBrushless);
                turnSparkMax = new CANSparkMax(2, MotorType.kBrushless);
                turnAbsoluteEncoder = new CANcoder(3, "rio");
                absoluteEncoderOffset = new Rotation2d((-0.77197265625 * 0) * 2.0 * Math.PI); // MUST BE CALIBRATED
                driveSparkMax.setInverted(true);
                break;
            case BACK_RIGHT: // BR
                driveSparkMax = new CANSparkMax(3, MotorType.kBrushless);
                turnSparkMax = new CANSparkMax(4, MotorType.kBrushless);
                turnAbsoluteEncoder = new CANcoder(4, "rio");
                absoluteEncoderOffset = new Rotation2d((-0.059326171875 + 0.05) * 2.0 * Math.PI); // MUST BE CALIBRATED
                driveSparkMax.setInverted(false);
                break;
            default:
                throw new RuntimeException("Invalid module index");
        }

        // turnSparkMax.setInverted(true);

        driveSparkMax.restoreFactoryDefaults();
        turnSparkMax.restoreFactoryDefaults();

        driveSparkMax.setCANTimeout(250);
        turnSparkMax.setCANTimeout(250);

        driveEncoder = driveSparkMax.getEncoder();
        turnRelativeEncoder = turnSparkMax.getEncoder();

        turnSparkMax.setInverted(isTurnMotorInverted);
        driveSparkMax.setSmartCurrentLimit(40);
        turnSparkMax.setSmartCurrentLimit(20);
        driveSparkMax.enableVoltageCompensation(12.0);
        turnSparkMax.enableVoltageCompensation(12.0);

        driveEncoder.setPosition(0.0);
        driveEncoder.setMeasurementPeriod(10);
        driveEncoder.setAverageDepth(2);

        turnRelativeEncoder.setPosition(turnAbsoluteEncoder.getPosition().getValue() * 2.0 * Math.PI);
        turnRelativeEncoder.setMeasurementPeriod(10);
        turnRelativeEncoder.setAverageDepth(2);
        // turnRelativeEncoder.setInverted(true);

        driveSparkMax.setCANTimeout(0);
        turnSparkMax.setCANTimeout(0);

        driveSparkMax.burnFlash();
        turnSparkMax.burnFlash();
    }

    @Override
    public void updateInputs(ModuleIOInputs inputs) {
        inputs.drivePositionRad = Units.rotationsToRadians(driveEncoder.getPosition()) / DRIVE_GEAR_RATIO;
        inputs.driveVelocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(driveEncoder.getVelocity()) / DRIVE_GEAR_RATIO;
        inputs.driveAppliedVolts = driveSparkMax.getAppliedOutput() * driveSparkMax.getBusVoltage();
        inputs.driveCurrentAmps = new double[]{driveSparkMax.getOutputCurrent()};

        inputs.turnAbsolutePosition = new Rotation2d(turnAbsoluteEncoder.getPosition().getValue() * 2.0 * Math.PI).minus(absoluteEncoderOffset);
        inputs.turnPosition = new Rotation2d(Rotation2d.fromRotations(turnRelativeEncoder.getPosition() / TURN_GEAR_RATIO).getRadians() % Math.PI);
        //inputs.turnPosition = Rotation2d.fromRotations(turnRelativeEncoder.getPosition() / TURN_GEAR_RATIO);
        inputs.turnVelocityRadPerSec = Units.rotationsPerMinuteToRadiansPerSecond(turnRelativeEncoder.getVelocity()) / TURN_GEAR_RATIO;
        inputs.turnAppliedVolts = turnSparkMax.getAppliedOutput() * turnSparkMax.getBusVoltage();
        inputs.turnCurrentAmps = new double[]{turnSparkMax.getOutputCurrent()};
    }

    @Override
    public void setDriveVoltage(double volts) {
        driveSparkMax.setVoltage(volts);
    }

    @Override
    public void setTurnVoltage(double volts) {
        turnSparkMax.setVoltage(volts);
    }

    @Override
    public void setDriveBrakeMode(boolean enable) {
        driveSparkMax.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
    }

    @Override
    public void setTurnBrakeMode(boolean enable) {
        turnSparkMax.setIdleMode(enable ? IdleMode.kBrake : IdleMode.kCoast);
    }
}