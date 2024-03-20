package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.FieldOrientatedSubsystem;

public class FieldOrientatedCommand extends Command {
    private final FieldOrientatedSubsystem fieldOrientatedSubsystem;
    private boolean fieldOrientated;

    public FieldOrientatedCommand(FieldOrientatedSubsystem fieldOrientatedSubsystem, boolean fieldOrientated) {
        this.fieldOrientatedSubsystem = fieldOrientatedSubsystem;
        this.fieldOrientated = fieldOrientated;
        addRequirements(fieldOrientatedSubsystem);
    }
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        fieldOrientatedSubsystem.flipBooleanState(fieldOrientated);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
