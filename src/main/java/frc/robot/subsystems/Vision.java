package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  
  NetworkTableEntry tx, ty, ta;

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
    double TargetHeight = 8.0833;
    double camerHeight = 1.9167;
    double yDistance = (targetHeight - cameraHeight) / Math.tan(yAngle * Math.PI / 180); //height of target (needs to be adjusted) - height of limelight / tan(angle)

    //averages data points, stores in arraylist, displays a value every 10 data points
    if (dataPoint.size < 10)
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

      for (int i = 0; i < 10; i++)
        dataPoints.remove(0);
    }
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

    boolean validTarget;
    
    if (tv.getDouble(0.0) < 1)
      validTarget = false;
    else
      validTarget = true;

    //if angle of adjustment is less than 1.0, the robot needs to move at least min_command
    if (x > 1.0)
      steering_adjust = Kp * heading_error - min_command;
    else 
      steering_adjust = Kp * heading_error + min_command;

    if (!validTarget)
      steering_adjust = 0;

    return steering_adjust;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
