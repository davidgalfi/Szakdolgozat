package org.example;

import org.example.outputMethods.Visual;

import java.util.Map;

import static HAL.Util.*;

public class Visualizer {

    final Visual visual;

    final boolean plotCells;

    final boolean plotInfectionConcentration;

    final static Map<Integer, Integer> colors =
            Map.of(
                    -1, RGB256(255, 255, 255),   // Empty cell color:    white,
                    Cell.T, RGB256(119, 198, 110),  // Target cell color:   green
                    Cell.I, RGB256(124, 65, 120),   // Infected cell color: purple
                    Cell.D, RGB(0, 0, 0)            // Dead cell color:     black
            );

    public Visualizer(Visual visual, boolean plotCells, boolean plotInfectionConcentration) {

        this.visual = visual;
        this.plotCells = plotCells;
        this.plotInfectionConcentration = plotInfectionConcentration;
    }

    /**
     * Draws the current state of the model on the visualization grid window.
     *
     * This method iterates through each agent in the model, retrieves its information, and sets the corresponding
     * visualization pixel on the grid window (if enabled). The concentration of the virus is
     * visualized using a heatmap on the same grid window (if enabled).
     *
     * @param G The experiment (at its given state) to be visualized.
     */
    public Void drawExperimentState(Experiment G) {
        for (int i = 0; i < G.length; i++) {

            if (plotCells) {

                Cell cell = G.GetAgent(i);
                visual.draw(i, cell == null ? colors.get(-1) : colors.get(cell.type));
            }

            if (plotInfectionConcentration) {

                visual.draw(plotCells ? 2 : 1, G.ItoX(i), G.ItoY(i), HeatMapRBG(G.infection.virusCon.Get(i)));
            }
        }

        visual.frameReady(G.timer);

        return null;
    }

    public void close() {

        visual.close();
    }
}
