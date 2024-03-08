package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pickup.PivotSubsystem;

public class AmpCommand extends Command {
    private final PivotSubsystem pivotSubsystem;
    private boolean setAmp;


    public AmpCommand(PivotSubsystem pivotSubsystem, boolean setAmp) {
        this.pivotSubsystem = pivotSubsystem;
        this.setAmp = setAmp;
    }

    @Override
    public void initialize() {
        pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.in);
    }
    @Override
    public void execute() {
       if(setAmp){
            pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.amp);
        } else{
            pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.in);
        }
    }

    @Override
    public void end(boolean interrupted) {
        pivotSubsystem.setPivotPosition(PivotSubsystem.PivotPosition.in);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
