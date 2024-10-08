package org.example;

import HAL.GridsAndAgents.AgentSQ2Dunstackable;

/**
 * The `Cells` class represents individual cells in a 2D square grid simulation.
 * Each cell is an agent capable of interacting with its environment and undergoing state transitions.
 * The state of each cell is determined by the cell type, indicating its health status (target, infected, or dead).
 *
 * This class extends the `AgentSQ2Dunstackable` class with a generic parameter of type `Experiment`.
 * The generic type allows cells to be part of a specific experiment.
 *
 * The different cell types are defined as constants:
 * - `T (0)`: Target cell
 * - `I (1)`: Infected cell
 * - `D (2)`: Dead cell
 */
public class Cell extends AgentSQ2Dunstackable<Experiment> {

    /**
     * The type of the cell:
     * 0 - Target
     * 1 - Infected
     * 2 - Dead
     */
    public int type;

    /**
     * Constants representing different cell types or states in the simulation.
     * Each constant corresponds to a unique integer value.
     *
     * T (0): Target cell
     */
    public static final int T = 0;

    /**
     * Constants representing different cell types or states in the simulation.
     * Each constant corresponds to a unique integer value.
     *
     * I (1): Infected cell
     */
    public static final int I = 1;

    /**
     * Constants representing different cell types or states in the simulation.
     * Each constant corresponds to a unique integer value.
     *
     * D (2): Dead cell
     */
    public static final int D = 2;

    public Cell(){}

    /**
     * Initializes the cell based on its health status and type.
     *
     * This method sets the type of the cell based on its health status. The cell can be classified into the following types:
     * - Healthy (Type 0): The cell is in a healthy state.
     * - Infected (Type 1): The cell is infected.
     * - Dead (Type 2): The cell is dead.
     *
     * The method accepts boolean parameters indicating the cell's health status. If multiple parameters are true, the method
     * prioritizes the health status in the following order: Healthy > Infected > Dead > Capillary.
     */
    public void init(int cellType) {
        this.type = cellType;
    }

    /**
     * Handles the infection process for the cell based on drug concentration and virus concentration.
     *
     * This method calculates the infection probability for the cell, taking into account the drug concentration,
     * virus concentration, and other parameters. If the cell is in a healthy state and the calculated effective
     * infection probability is greater than a random value, the cell transitions to an infected state.
     *
     * The infection process considers a sigmoid function for drug efficacy, infection probability, and effective
     * infection probability. If the cell is not in a healthy state, the infection process is not applied.
     *
     * The infection process involves calculating the effective infection probability based on drug efficacy,
     * infection rate, cell dimensions, and virus concentration. If the random value is less than the
     * effective infection probability, the cell transitions to an infected state (Type 1).
     */
    public void stochasticInfection() {

        double virusConAtCell = G.infection.virusCon.Get(Isq());

        // Sigmoid function for drug efficacy
        double effectiveInfectionRate = G.infection.infectionRate;

        for (Treatment treatment: G.treatments) {
            effectiveInfectionRate *= 1 - treatment.drug.efficacy.get("infectionReduction").compute(treatment.concentration.Get(Isq()));
        }

        double infectionProbability = G.technical.timeStep * G.xDim * G.yDim * effectiveInfectionRate * virusConAtCell;

        if (G.rn.Double() < infectionProbability) {
            this.type = I; // Transition to infected state
        }
    }

    /**
     * Handles the death process for the infected cell based on death probability.
     *
     * This method checks if the cell is in an infected state (Type 1) and, if so, determines whether the cell
     * undergoes cell death based on a random value and the given death probability. If the random value is less
     * than the death probability, the cell transitions to a dead state (Type 2).
     *
     * The death process is applied only to infected cells (Type 1), and it involves checking if a random
     * value is less than the death probability. If true, the cell transitions to a dead state.
     */
    public void stochasticDeath() {

        double effectiveCellDeathRate = G.infection.cellDeathRate;

        for (Treatment treatment: G.treatments) {
            effectiveCellDeathRate *= 1 + treatment.drug.efficacy.get("cytotoxicity").compute(treatment.concentration.Get(Isq()));
        }

        double deathProbability = 1 - Math.exp(- effectiveCellDeathRate * G.technical.timeStep);

        if (G.rn.Double() < deathProbability) {
            this.type = D; // Transition to dead state
        }
    }

    public void stochasticStateChange() {
        if (this.type == I) {
            stochasticDeath();
        } else if(this.type == T){
            stochasticInfection();
        }
    }
}
