package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.*;
import frc.robot.commands.shooter.*;
import frc.robot.commands.intake.*;

public class ShootAll extends SequentialCommandGroup {
  
  public ShootAll(Shooter shooter, Intake intake) {
    super(
      new ShootBall(shooter, intake),
      new Index(intake),
      new ShootBall(shooter, intake),
      new ShootBall(shooter, intake),
      new InstantCommand(() -> shooter.stopFlywheels()),
      new InstantCommand(() -> intake.stopIntake(), intake),
      new InstantCommand(() -> intake.stopTower(), intake)
    );
  }
}
