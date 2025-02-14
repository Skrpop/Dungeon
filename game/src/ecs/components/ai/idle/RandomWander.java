package ecs.components.ai.idle;

import com.badlogic.gdx.ai.pfa.GraphPath;
import ecs.components.ai.AITools;
import ecs.entities.Entity;
import level.elements.tile.Tile;
import tools.Constants;

public class RandomWander implements IIdleAI {
    private final float radius;
    private GraphPath<Tile> path;
    private final int breakTime;
    private int currentBreak = 0;

    /**
     * Finds a point in the radius and moves there. When the point has been reached, a new
     * point in the radius is searched for from the current position.
     *
     * @param radius Radius in which a target point is to be searched for
     * @param breakTimeInSeconds How long to wait (in seconds) before searching for a new goal
     */
    public RandomWander(float radius, int breakTimeInSeconds) {
        this.radius = radius;
        this.breakTime = breakTimeInSeconds * Constants.FRAME_RATE;
    }

    @Override
    public void idle(Entity entity) {
        if (path == null || AITools.pathFinishedOrLeft(entity, path)) {
            if (currentBreak >= breakTime) {
                currentBreak = 0;
                path = AITools.calculatePathToRandomTileInRange(entity, radius);
                idle(entity);
            }
            currentBreak++;
        } else {
            AITools.move(entity, path);
        }
    }
}
