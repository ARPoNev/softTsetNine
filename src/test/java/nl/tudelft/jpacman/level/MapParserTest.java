package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.PacmanConfigurationException;
import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.npc.Ghost;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MapParserTest {
    private MapParser mapParser;
    private final LevelFactory levelFactory = mock(LevelFactory.class);
    private final BoardFactory boardFactory = mock(BoardFactory.class);

    @BeforeEach
    void setUp() {
        mapParser = new MapParser(levelFactory, boardFactory);
        when(boardFactory.createGround()).thenReturn(mock(Square.class));
        when(boardFactory.createWall()).thenReturn(mock((Square.class)));
        when(levelFactory.createGhost()).thenReturn(mock(Ghost.class));
        when(levelFactory.createPellet()).thenReturn(mock(Pellet.class));
    }

    @Test
    @DisplayName("文件名为空")
    void parseMap1() {
        assertThatThrownBy(()->{
            mapParser.parseMap((String)null);
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("文件不存在")
    void parseMap2(){
        String file="/test.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(file);
        }).isInstanceOf(PacmanConfigurationException.class).hasMessage(
            "Could not get resource for: " + file
        );
    }

    @Test
    @DisplayName("文件存在")
    void parseMap3() throws Exception{
        String file="/simplemap.txt";
        mapParser.parseMap(file);
        verify(boardFactory,times(2)).createWall();
        verify(boardFactory,times(4)).createGround();
        verify(levelFactory,times(1)).createGhost();
    }

    @Test
    @DisplayName("无法识别的地图")
    void parseMap4() throws Exception{
        String file="/unrecognizedcharmap.txt";
        assertThatThrownBy(()->{
            mapParser.parseMap(file);
        }).isInstanceOf(PacmanConfigurationException.class).hasMessage(
            "Invalid character at "
                + 0 + "," + 0 + ": " + "A"
        );
    }

}
