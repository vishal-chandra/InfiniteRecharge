package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.*;
import frc.robot.commands.intake.*;
import frc.robot.commands.shooter.*;

public class ShootOne extends SequentialCommandGroup {

  public ShootOne(Shooter shooter, Intake intake) {
    super(
      new ShootBall(shooter, intake),
      new InstantCommand(() -> shooter.stopFlywheels(), intake),
      new InstantCommand(() -> intake.stopIntake(), intake),
      new InstantCommand(() -> intake.stopTower(), intake)
    );
  }
}
