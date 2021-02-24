package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.*;
import frc.robot.commands.shooter.*;
import frc.robot.commands.intake.*;

public class ShootAll extends SequentialCommandGroup {
  
  public ShootAll(Shooter shooter, Intake intake, double power) {
    super(
      new ShootBall(shooter, intake, power),
      new WaitCommand(0.25),
      new ShootBall(shooter, intake, power),
      new WaitCommand(0.25),
      new Index(intake),
      new ShootBall(shooter, intake, power),
      new InstantCommand(() -> shooter.stopFlywheels()),
      new InstantCommand(() -> intake.stopIntake(), intake),
      new InstantCommand(() -> intake.stopTower(), intake)
    );
  }
}
