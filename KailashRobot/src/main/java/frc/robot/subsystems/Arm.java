// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
    private CANSparkMax armMotor;
    private AbsoluteEncoder armEncoder;
    private PIDController armController;
    private ArmFeedforward feedforward;
    private final Encoder encoder = new Encoder(4, 5);
    private final double encoderConversion = 1 / 4096 * 0.1 * Math.PI;

    public Arm() {
        try (PIDController armController = new PIDController(0, 0, 0)) {
        }
        armEncoder.setPositionConversionFactor(2 * Math.PI * 1.0);
        feedforward = new ArmFeedforward(0, 0, encoderConversion);
    }

    /**
    * Example command factory method.
    *
    * @return a command
    */
    public CommandBase exampleMethodCommand() {
        // Inline construction of command goes here.
        // Subsystem::RunOnce implicitly requires `this` subsystem.
        return runOnce(
            () -> {
              /* one-time action goes here */
            });
      }

    public void setMotorSpeed(double speed) {
        armMotor.set(speed);
    }
    public void setTargetDistance(double targetDistance) {
        armController.setSetpoint(targetDistance);
    }
    public void setArmPosition(double distance) {
        armController.setSetpoint(distance);
    }
    public void setCaculatedVoltage() {
        double voltage = armController.calculate(armEncoder.getPosition(), armController.getSetpoint());
        armMotor.setVoltage(voltage);
    }

    public void stop() {
        armMotor.set(0);
    }

    public double getEncoderMeters() {
        return encoder.get() * encoderConversion;
    }

      /**
       * An example method querying a boolean state of the subsystem (for example, a digital sensor).
       *
       * @return value of some boolean subsystem state, such as a digital sensor.
       */
      public boolean exampleCondition() {
        // Query some boolean state, such as a digital sensor.
        return false;
      }
    
      @Override
      public void periodic() {
        armMotor.setVoltage(armController.calculate(armEncoder.getPosition()) + feedforward.calculate(encoderConversion, encoderConversion, encoderConversion));
    }
    
      @Override
      public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
      }
}
