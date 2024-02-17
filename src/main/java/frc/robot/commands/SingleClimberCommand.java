package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;

public class SingleClimberCommand extends Command {
    private final ClimberSubsystem climberSubsystem;
    private final double speed;
    private final boolean isRight;

    public SingleClimberCommand(ClimberSubsystem climberSubsystem, double speed, boolean isRight) {
        this.climberSubsystem = climberSubsystem;
        this.speed = speed;
        this.isRight = isRight;
        addRequirements(climberSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        if (isRight){
            climberSubsystem.setClimberSpeed(0, speed);
        } else{
            climberSubsystem.setClimberSpeed(speed, 0);
        }
    }

    @Override
    public void end(boolean interrupted) {
        climberSubsystem.setClimberSpeed(0, 0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
