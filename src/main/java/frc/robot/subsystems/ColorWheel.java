/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

//WPI
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//CTRE
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

//3205
import static frc.robot.Constants.*;

public class ColorWheel extends SubsystemBase {
  
  TalonSRX colorWheelTalon;

  public ColorWheel() {

    colorWheelTalon = new TalonSRX(kColorWheelTalonID);
  }

  public void colorControl() {

  }

  public void rotationControl() {

  }

}
