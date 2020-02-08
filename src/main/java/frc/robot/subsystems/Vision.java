package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  
  NetworkTableEntry tx, ty, ta;
  
  /**
   * Creates a new Vision.
   */
  public Vision() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");
  }

  /**
   * Retreive values from limelight and display on SmartDashboard.
   */
  public void getValues() {
    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);

    //distance to target
    double dy = (8.0833 - 1.9167) / Math.tan(y); //height of target (needs to be adjusted) - height of limelight / tan(angle)

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightDy", dy);
    SmartDashboard.putNumber("LimelightArea", area);
  }

  /**
   * Generates driving and steering commands based on tracking data from limelight.
   * @return steering adjustment value (+ right command, - left command)
   */
  public double makeAdjustment() {
    //NEED TO TEST ROBOT TO FIGURE THIS VALUE OUT:
    double Kp = -0.1; //proportional control constant 
    double min_command = 0.05; // minimum amount of power needed to actually make a movement

    double x = tx.getDouble(0.0); //angle of adjustment

    double heading_error = -x;
    double steering_adjust = 0.0;

    //if angle of adjustment is less than 1.0, the robot needs to move at least min_command
    if (x > 1.0)
      steering_adjust = Kp * heading_error - min_command;
    else if (x < 1.0)
      steering_adjust = Kp * heading_error + min_command;

    return steering_adjust;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
