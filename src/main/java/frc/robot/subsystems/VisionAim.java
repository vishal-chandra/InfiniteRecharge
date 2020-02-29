/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class VisionAim extends CommandBase {
  private final Drive m_drive;
  private final Vision m_vision;
  double xAngle;
  double validTarget;
  public boolean isVisionAimOn = false; //blocks values from controller when true

  /**
   * Creates a new VisionAim.
   */
  public VisionAim(Vision vision, Drive drive) {
    m_vision = vision;
    m_drive = drive;
    addRequirements(vision, drive);
  }

  /**
   * Called when the command is initially scheduled.
   * Begins turning robot
   */
  @Override
  public void initialize() {
    double validTarget = m_vision.getValidTarget();
    this.validTarget = validTarget;
    
    double xAngle = m_vision.getXAngle();
    this.xAngle = xAngle;

    //only begins turning if there's a valid target and robot is not already aligned
    if (validTarget == 1) {
      if (xAngle >= 3.0 || xAngle <= -3.0) {
      isVisionAimOn = true;
      
        //Turns right or left depending on angle
        if (xAngle > 0) //turn right
          m_drive.curvatureDrive(0.0, 0.5);
        else if (xAngle < 0) //turn left
          m_drive.curvatureDrive(0.0, -0.5);
        }
        
      else {
        updateStatus(3);
        cancel();
      }
    }
    else {
      updateStatus(1);
      cancel();
    }
  }

  /** 
   * Called every time the scheduler runs while the command is scheduled.
   */
  @Override
  public void execute() {
  }

  /** 
   * Called once the command ends or is interrupted.
   * */ 
  @Override
  public void end(final boolean interrupted) {
    isVisionAimOn = false;
  }

  /**
   * Returns true when the command should end.
   * Continuously tests if the robot is aligned with the target
   * Ends command and stops motors if robot is aligned
   */
  @Override
  public boolean isFinished() {
    double xAngle = m_vision.getXAngle();
    this.xAngle = xAngle;

    if (xAngle <= 3.0 && xAngle >= -3.0) {
      m_drive.curvatureDrive(0.0, 0.0); // stops motors

      updateStatus(2);

      return true; // stops command
    }
    else 
      return false;
  }

  public void updateStatus(int x) {
    if (x == 1)
      SmartDashboard.putString("Status", "No Valid Target");
    else if (x == 2)
      SmartDashboard.putString("Status", "Aiming Complete");
    else if (x == 3)
      SmartDashboard.putString("Status", "Already Aligned");
  }
}
