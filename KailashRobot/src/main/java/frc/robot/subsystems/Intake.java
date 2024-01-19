// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;
import com.revrobotics.AbsoluteEncoder;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  private CANSparkMax leftPivot; 
  private CANSparkMax rightPivot;
  private CANSparkMax roller;
  private AbsoluteEncoder pivotEncoder;
  private PIDController pivotController;

  /** Creates a new Intake. */
  public Intake() {
        leftPivot = new CANSparkMax(IntakeConstants.kPivotLeftCanId, CANSparkMaxLowLevel.MotorType.kBrushless);
        rightPivot = new CANSparkMax(IntakeConstants.kPivotRightCanId, CANSparkMaxLowLevel.MotorType.kBrushless);
        roller = new CANSparkMax(IntakeConstants.kRollerCanId, CANSparkMaxLowLevel.MotorType.kBrushless);

        pivotEncoder = leftPivot.getAbsoluteEncoder(Type.kDutyCycle);
        pivotController = new PIDController(0.0005, 0, 0); //tune

        leftPivot.setIdleMode(IdleMode.kCoast);
        rightPivot.setIdleMode(IdleMode.kCoast);
        roller.setIdleMode(IdleMode.kCoast);

        rightPivot.follow(leftPivot);

        leftPivot.burnFlash();
        rightPivot.burnFlash();

        pivotEncoder.setPositionConversionFactor(IntakeConstants.kPivotGearRatio * 360);
        pivotEncoder.setZeroOffset(0); //tune

  }

  public double getPivot() {
      return pivotEncoder.getPosition() / IntakeConstants.kPivotGearRatio;
  }

  public void setPivot(double targetDegrees) {
    pivotController.setSetpoint(targetDegrees);
  }

  public void intake() {
    roller.setVoltage(2);
  }

  public void outtake() {
    roller.setVoltage(-2);
  }

  public void stop() {
    roller.setVoltage(0);
  }

  @Override
  public void periodic() {
    leftPivot.setVoltage(pivotController.calculate(getPivot()));
    SmartDashboard.putNumber("Intake Pivot Angle", getPivot());
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
