package experiments
// Method #2: Define a custom incarnation and import stuff from it
object MyIncarnation
    extends it.unibo.scafi.incarnations.BasicAbstractIncarnation
import MyIncarnation._
import it.unibo.scafi.simulation.frontend.{Launcher, Settings}

// An "aggregate program" can be seen as a function from a Context to an Export
// The Context is the input for a local computation: includes state
//  from previous computations, sensor data, and exports from neighbours.
// The export is a tree-like data structure that contains all the information needed
//  for coordinating with neighbours. It also contains the output of the computation.
class MyAggregateProgram extends AggregateProgram with StandardSensorNames {
  // Main program expression driving the ensemble
  // This is run in a loop for each agent
  // According to this expression, coordination messages are automatically generated
  // The platform/middleware/simulator is responsible for coordination
  override def main() = gradient(isSource)

  // The gradient is the (self-adaptive) field of the minimum distances from source nodes
  // `rep` is the construct for state transformation (remember the round-by-round loop behaviour)
  // `mux` is a purely functional multiplexer (selects the first or second branch according to condition)
  // `foldhoodPlus` folds over the neighbourhood (think like Scala's fold)
  // (`Plus` means "without self"--with plain `foldhood`, the device itself is folded)
  // `nbr(e)` denotes the values to be locally computed and shared with neighbours
  // `nbrRange` is a sensor that, when folding, returns the distance wrt each neighbour
  def gradient(source: Boolean): Double =
    rep(Double.PositiveInfinity) { distance =>
      mux(source) { 0.0 } {
        foldhoodPlus(Double.PositiveInfinity)(Math.min)(nbr {
          distance
        } + nbrRange)
      }
    }

  // A custom local sensor
  def isSource = sense[Boolean]("sens1")
  // A custom "neighbouring sensor"
  def nbrRange = nbrvar[Double](NBR_RANGE)
}

import it.unibo.scafi.simulation.frontend.{Launcher, Settings}
object SimulationRunner extends Launcher {
  Settings.Sim_ProgramClass = "experiments.MyAggregateProgram"
  Settings.ShowConfigPanel = false
  launch()
}
