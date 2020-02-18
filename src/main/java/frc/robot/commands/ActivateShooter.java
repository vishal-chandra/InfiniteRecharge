/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;

public class ActivateShooter extends CommandBase {
  
  Shooter shooter;

  public ActivateShooter(Shooter shooterSystem) {
    shooter = shooterSystem;
    addRequirements(shooter);
  }

  @Override
  public void initialize() {
    shooter.startFlywheels();
  }
  
  @Override
  public boolean isFinished() {
    return shooter.checkRPM();
  }
}
