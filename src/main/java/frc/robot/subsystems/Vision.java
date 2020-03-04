/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  
  CameraServer camServer;
  UsbCamera camera;

  public Vision() {
    camServer = CameraServer.getInstance();
    camera = camServer.startAutomaticCapture("front camera", 0);

    camera.setResolution(320, 240);
    camera.setFPS(30);

    camServer.getServer().setSource(camera);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
