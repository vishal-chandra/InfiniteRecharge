/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Add your docs here.
 */
public class DataPoint {
    double xAngle;
    double yAngle;
    double area;
    double yDistance;

    public DataPoint(double xAngle, double yAngle, double area, double yDistance) {
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.area = area;
        this.yDistance = yDistance;
    }

    public void displayDataPoint() {
        SmartDashboard.putNumber("LimelightX", xAngle);
        SmartDashboard.putNumber("LimelightY", yAngle);
        SmartDashboard.putNumber("LimelightDy", yDistance);
        SmartDashboard.putNumber("LimelightArea", area);
    }
}
