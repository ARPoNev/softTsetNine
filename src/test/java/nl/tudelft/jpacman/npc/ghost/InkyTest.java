package nl.tudelft.jpacman.npc.ghost;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.google.common.collect.Lists;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.MapParser;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;

/**
 * 测试Inky的测试情况：
 * 1.地图没有Player对象.
 * 2.地图中没有Blinky对象.
 * 3.地图中如果Blinky在Player身后,而Inky在他前面,Inky远离吃豆人.
 * 4.Inky和Blinky在Player后面,Blink对象和Player之间无可达路径
 * 5.Inky和Blinky在Player后面,Inky到达不了Player.
 * 6.Inky和Blinky在Player后面,Inky到达Player.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InkyTest {
    private static MapParser mapParser;

    /**
     * 实例化游戏对象.
     */
    @BeforeAll
    public static void setup() {
        //用于角色显示
        PacManSprites sprites = new PacManSprites();

        LevelFactory levelFactory = new LevelFactory(
            sprites,
            new GhostFactory(sprites),
            mock(PointCalculator.class));
        BoardFactory boardFactory = new BoardFactory(sprites);
        GhostFactory ghostFactory = new GhostFactory(sprites);


        mapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);
    }

    /**
     * 地图没有Player对象.
     */
    @Test
    @DisplayName("地图没有Player对象")
    @Order(1)
    void departWithoutPlayer() {
        List<String> text = Lists.newArrayList(
            "###########",
            "#I   B    #",
            "###       #",
            "###  ######");
        Level level = mapParser.parseMap(text);

        //创建Inky
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        //创建Blinky
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());

        assertThat(level.isAnyPlayerAlive()).isFalse();

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.isPresent()).isFalse();
    }

    /**
     * 地图中没有Blinky对象.
     */
    @Test
    @DisplayName("地图中没有Blinky对象")
    @Order(2)
    void departWithoutBlinky() {
        List<String> text = Lists.newArrayList(
            "###########",
            "# I       #",
            "###       #",
            "######## P#");
        Level level = mapParser.parseMap(text);

        //创建Inky
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        //创建Blinky
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());


        //创建Player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);

        assertThat(blinky).isNull();
        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.isPresent()).isFalse();
    }

    /**
     * 地图中如果Blinky在Player身后,而Inky在他前面,Inky远离吃豆人.
     */
    @Test
    @DisplayName("地图中如果Blinky在Player身后,而Inky在他前面,Inky远离吃豆人")
    @Order(3)
    void InkyAwayPlayer() {
        List<String> text = Lists.newArrayList(
            "###########",
            "# I  B    #",
            "#####     #",
            "#  P#######");
        Level level = mapParser.parseMap(text);

        //创建Inky
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        //创建Blinky
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());

        //创建Player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.get()).isEqualTo(Direction.valueOf("EAST"));
    }

    /**
     * Inky和Blinky在Player后面,Blink对象和Player之间无可达路径.
     */
    @Test
    @DisplayName("Inky和Blinky在Player后面,Blink对象和Player之间无可达路径")
    @Order(4)
    void pathWithoutBlink() {
        List<String> text = Lists.newArrayList(
            "###########",
            "# I  B  # #",
            "######### #",
            "#######  P#");
        Level level = mapParser.parseMap(text);

        //创建Inky
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        //创建Blinky
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());

        //创建Player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.isPresent()).isFalse();
    }

    /**
     * Inky和Blinky在Player后面,Inky到达不了Player.
     */
    @Test
    @DisplayName("Inky和Blinky在Player后面,Inky到达不了Player")
    @Order(5)
    void pathWithoutInky() {
        List<String> text = Lists.newArrayList(
            "###########",
            "# I # B   #",
            "######### #",
            "#######  P#");
        Level level = mapParser.parseMap(text);

        //创建Inky
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        //创建Blinky
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());

        //创建Player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.isPresent()).isFalse();
    }

    /**
     * Inky和Blinky在Player后面,Inky到达Player.
     */
    @Test
    @DisplayName("Inky和Blinky在Player后面,Inky到达Player")
    @Order(6)
    void pathWithInky() {
        List<String> text = Lists.newArrayList(
            "###########",
            "# I   B   #",
            "######### #",
            "#######  P#");
        Level level = mapParser.parseMap(text);

        //创建Inky
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        //创建Blinky
        Blinky blinky = Navigation.findUnitInBoard(Blinky.class, level.getBoard());

        //创建Player
        Player player = new PlayerFactory(new PacManSprites()).createPacMan();
        player.setDirection(Direction.valueOf("WEST"));
        level.registerPlayer(player);

        //act:
        Optional<Direction> opt = inky.nextAiMove();

        //assert:
        assertThat(opt.get()).isEqualTo(Direction.valueOf("EAST"));
    }
}
