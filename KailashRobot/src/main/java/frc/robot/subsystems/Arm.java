package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
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
        armEncoder.setPositionConversionFactor(2 * Math.PI);
        feedforward = new ArmFeedforward(0, 0, encoderConversion);
    }


    public void setMotorSpeed(double speed) {
        armMotor.set(speed);
    }

    public void setTargetDistance(double targetDistance) {
        armController.setSetpoint(targetDistance);
    }

    public void stop() {
        armMotor.set(0);
    }

    public double getEncoderMeters() {
        return encoder.get() * encoderConversion;
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
