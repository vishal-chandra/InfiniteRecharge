/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class FeedBall extends CommandBase {
  
  Intake intake;
  boolean lastBoolean;
  int switchFlipCount = 0;

  public FeedBall(Intake intakeSystem) {
    intake = intakeSystem;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    lastBoolean = intake.ballAtTowerTop();
    intake.runTower();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(intake.ballAtTowerTop() != lastBoolean) {
      switchFlipCount++;
      lastBoolean = intake.ballAtTowerTop();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopTower();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if(switchFlipCount == 2) {
      intake.ballCount--;
      switchFlipCount = 0;
      return true;
    }
    else return false;
  }
}
