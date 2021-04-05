package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.*;
import frc.robot.commands.intake.*;
import frc.robot.commands.shooter.*;

public class ShootOne extends SequentialCommandGroup {

  public ShootOne(Shooter shooter, Intake intake, double power) {
    super(
      new ShootBall(shooter, intake, power),
      new WaitCommand(0.2),
      new InstantCommand(() -> shooter.stopFlywheels(), shooter),
      new InstantCommand(() -> intake.stopIntake(), intake),
      new InstantCommand(() -> intake.stopTower(), intake)
    );
  }
}
