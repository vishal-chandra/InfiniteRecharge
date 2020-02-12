package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  
  NetworkTableEntry tx, ty, ta, tv;
  
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
    double xAngle = tx.getDouble(0.0);
    double yAngle = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);

    //distance to target
    double targetHeight = 8.0833;
    double cameraHeight = 1.9167;
    double yDistance = (targetHeight - cameraHeight) / Math.tan(yAngle * Math.PI / 180); //height of target (needs to be adjusted) - height of limelight / tan(angle)

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", xAngle);
    SmartDashboard.putNumber("LimelightY", yAngle);
    SmartDashboard.putNumber("LimelightDy", yDistance);
    SmartDashboard.putNumber("LimelightArea", area);
  }

  /**
   * Generates driving and steering commands based on tracking data from
   * limelight.
   * 
   * @return steering adjustment value (+ right command, - left command)
   */
  public double makeAdjustment() {
    // NEED TO TEST ROBOT TO FIGURE THIS VALUE OUT:
    final double Kp = -0.1; // proportional control constant
    final double minCommand = 0.05; // minimum amount of power needed to actually make a movement

    final double x = tx.getDouble(0.0); // angle of adjustment

    final double headingError = -x;
    double steeringAdjust = 0.0;

    double validTarget = tv.getDouble(0.0); 

    // if angle of adjustment is less than 1.0, the robot needs to move at least
    // min_command
    if (x > 1.0)
      steeringAdjust = Kp * headingError - minCommand;
    else
      steeringAdjust = Kp * headingError + minCommand;
    
    if (validTarget < 1.0)
      steeringAdjust = 0.0;

    return steeringAdjust;

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
