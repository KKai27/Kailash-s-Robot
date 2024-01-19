// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.Autos;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Flywheel;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
// import frc.robot.subsystems.Wheel;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.OperatorConstants;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  // private final Wheel m_Wheel = new Wheel();
  // private final Flywheel m_Flywheel = new Flywheel();
  // private final Shooter m_Shooter = new Shooter();
  private final Intake m_Intake = new Intake();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */

  private void configureBindings() {
    // m_driverController.a().whileTrue(new InstantCommand(() -> m_Wheel.moveForward())).whileFalse(new InstantCommand(() -> m_Wheel.stop()));
    // m_driverController.b().whileTrue(new InstantCommand(() -> m_Wheel.moveBackward())).whileFalse(new InstantCommand(() -> m_Wheel.stop()));
    // m_driverController.a().onTrue(new InstantCommand(() -> m_Flywheel.setMotorSpeed(1000))).onFalse(new InstantCommand(() -> m_Flywheel.setMotorSpeed(0)));
    m_driverController.leftBumper().onTrue(new InstantCommand(() -> m_Intake.setPivot(0)));
    m_driverController.rightBumper().onTrue(new InstantCommand(() -> m_Intake.setPivot(0)));
    m_driverController.a().onTrue(new InstantCommand(() -> m_Intake.intake())).onFalse(new InstantCommand(() -> m_Intake.stop()));
    m_driverController.b().onTrue(new InstantCommand(() -> m_Intake.outtake())).onFalse(new InstantCommand(() -> m_Intake.stop()));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
