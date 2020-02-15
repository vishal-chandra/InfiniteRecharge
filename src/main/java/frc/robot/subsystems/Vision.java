package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  
  NetworkTableEntry tx, ty, ta, tv;

  ArrayList <DataPoint> dataPoint = new ArrayList<DataPoint>();
  
  /**
   * Creates a new Vision.
   */
  public Vision() {
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    ta = table.getEntry("ta");
    tv = table.getEntry("tv");
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

    //averages data points, stores in arraylist, displays a value every 10 data points
    if (dataPoint.size() < 10)
      dataPoint.add(new DataPoint(xAngle, yAngle, area, yDistance));
    else {
      double xAngleSum = 0;
      double yAngleSum = 0;
      double areaSum = 0;
      double yDistanceSum = 0;

      for (int i = 0; i < 10; i++) {
        xAngleSum += dataPoint.get(i).xAngle;
        yAngleSum += dataPoint.get(i).yAngle;
        areaSum += dataPoint.get(i).area;
        yDistanceSum += dataPoint.get(i).yDistance;
      }

      DataPoint averageDataPoint = new DataPoint(xAngleSum / 10, yAngleSum / 10, areaSum / 10, yDistanceSum / 10);
      averageDataPoint.displayDataPoint();

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
    final double Kp = 0.005555; // proportional control constant
    final double minCommand = 0.05; // minimum amount of power needed to actually make a movement

    final double x = tx.getDouble(0.0); // angle of adjustment

    final double headingError = -x;
    double steeringAdjust = 0.0;

    final double validTarget = tv.getDouble(0.0); 

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
  
  public double getXAngle() {
    double xAngle = tx.getDouble(0.0);
    return xAngle;
  }

  public double getValidTarget() {
    double validTarget = tv.getDouble(0.0);
    return validTarget;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
