package nl.tudelft.jpacman.level;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
/**
 * player,ghost和pellet之间的碰撞.
 * @author Yan
 *
 */
public class PlayerCollisionsTest {


    private Ghost ghost = mock(Ghost.class);
    private Player player = mock(Player.class);
    private Pellet pellet = mock(Pellet.class);

    private final PointCalculator pointCalculator=mock(PointCalculator.class);

    @BeforeAll
    public static void setup() {

    }

    @Test
    @DisplayName("player碰撞Pellet")
    @Order(1)
    void playerCollidePellet() {

        PlayerCollisions pc = new PlayerCollisions(pointCalculator);
        pc.collide(player,pellet);

        verify(pellet,times(1)).leaveSquare();

    }

    @Test
    @DisplayName("ghost碰撞pellet,什么都没发生")
    @Order(2)
    void ghostCollidepellet() {

        PlayerCollisions pc = new PlayerCollisions(pointCalculator);
        pc.collide(ghost,pellet);

        verify(pellet,times(0)).leaveSquare();

    }

    @Test
    @DisplayName("Pellet碰撞ghost,什么都没发生")
    @Order(3)
    void pelletCollideGhost() {

        PlayerCollisions pc = new PlayerCollisions(pointCalculator);
        pc.collide(pellet,ghost);

        verify(pellet,times(0)).leaveSquare();

    }

    @Test
    @DisplayName("Pellet碰撞player")
    @Order(4)
    void pelletCollidePlayer() {

        PlayerCollisions pc = new PlayerCollisions(pointCalculator);
        pc.collide(pellet,player);

        verify(pellet,times(1)).leaveSquare();

    }

    @Test
    @DisplayName("player碰撞ghost")
    @Order(5)
    void playerCollideGhost() {

        PlayerCollisions pc = new PlayerCollisions(pointCalculator);
        pc.collide(player,ghost);

        verify(player,times(1)).setAlive(false);
        verify(player,times(1)).setKiller(ghost);
    }

    @Test
    @DisplayName("ghost碰撞player")
    @Order(6)
    void ghostCollideplayer() {

        PlayerCollisions pc = new PlayerCollisions(pointCalculator);
        pc.collide(ghost,player);
        verify(player,times(1)).setAlive(false);
        verify(player,times(1)).setKiller(ghost);
    }


}
