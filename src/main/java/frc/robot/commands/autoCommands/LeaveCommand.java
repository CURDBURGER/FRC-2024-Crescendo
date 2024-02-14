package frc.robot.commands.autoCommands;


import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DriveToPoseCommand;
import frc.robot.subsystems.drive.Drive;


public class LeaveCommand {

    public static Command create(Drive drive) {
        return new SequentialCommandGroup(
                new DriveToPoseCommand(drive, 5, 3, 0)
        );
    }

}
