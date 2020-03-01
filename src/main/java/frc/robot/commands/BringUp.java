package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class BringUp extends CommandBase {

  Intake intake;

  public BringUp(Intake intakeSystem) {
    intake = intakeSystem;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intake.runTower();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopTower();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return intake.ballAtTowerTop();
  }
}
