import board.Board;
import board.BoardCell;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.ZeroFunction;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javafx.scene.input.MouseButton.*;

/**
 * Created by noctuam on 16.07.16.
 */
public class Main extends Application{
    //Board board;
    Game game;
    String stdBtnColor = "-fx-background-color: #f0ffff;";
    String freeCellColor = "-fx-background-color: #00fa9a";
    String bombCellColor = "-fx-background-color: #ff4500;";
    Function<Game,String> gameInfo = g->{
        String info = "\n\n" ;
        Supplier<Stream<BoardCell>> cellStream = ()->g.board.board2CellStream(g.board.boardCells);
        int hidedCount = cellStream.get().filter(BoardCell::hidedCell).collect(Collectors.toList()).size();
        int opened = cellStream.get().filter(BoardCell::isOpen).collect(Collectors.toList()).size();
        int defusedCount = cellStream.get().filter(BoardCell::isDefused).collect(Collectors.toList()).size();
        info += String.format("Opened:[%d]\n Hided:[%d]\n Defused:[%d]\n",opened,hidedCount,defusedCount);
        info += String.format("-------------------\n");
        info += String.format("DefuseKits:[%d]",g.defuseKitCount);
        return info;
    };
    Function<String,int[]> parseId=s -> {
        String fst = s.substring(0,s.indexOf('_'));
        String snd = s.substring(s.indexOf('_')+1,s.length());
        int[] cell = new int[]{Integer.parseInt(fst),Integer.parseInt(snd)};
        return cell;
    };

    EventHandler<Event> getId = event -> {
        Control btn = ((Control)event.getSource());
        String strID= btn.getId();

        int [] newClick = parseId.apply(strID);

        MouseButton clickedButton = ((MouseEvent)event).getButton();
        if(clickedButton.equals(PRIMARY)){
            System.out.println("Main button clicked");
            btn.setDisable(true);
            game.doOpen(newClick[0],newClick[1]);
            btn.setStyle(game.isCleanCell(newClick[0],newClick[1])?freeCellColor:bombCellColor);
            if(!game.isActive()) {

            }


        }
        if(clickedButton.equals(SECONDARY)){
            game.doDefuse(newClick[0],newClick[1]);
            btn.setStyle(game.isDefused(newClick[0],newClick[1])?"-fx-background-color: #00fa9a;":stdBtnColor);

            System.out.println("Secondary clicked");
        }
        if(clickedButton.equals(MIDDLE)){
            System.out.println("Middle clicked");
        }
        System.out.println(strID);
        System.out.println(gameInfo.apply(game));
        //System.out.println(clickedCell[0]+","+clickedCell[1]);
        //System.out.println(currentActionTry.name());
    };

    public Button getFieldBtn(int x, int y){
        Button btn = new Button();
        String id = String.format("%d_%d",x,y);
        btn.setId(id);
        btn.setTooltip(new Tooltip(id));
        //btn.
        //btn.setLayoutY(50);
        btn.setStyle("-fx-border-color: black;"+
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;" +
                "-fx-border-radius: 0 0 0 0;" +
                stdBtnColor);
        btn.addEventHandler(MouseEvent.MOUSE_CLICKED,getId);
        //btn.setOnMouseClicked(cellClick);

        return btn;
    }

    public BiFunction<Integer,Integer,GridPane> getGridButtons = (x, y)->{
        GridPane grid = new GridPane();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                grid.add(getFieldBtn(j,i),j,i);
            }
        }
        grid.autosize();

        return grid;
    };
    public BiFunction<Integer,Integer,Button[][]>getArrayButtons = (x,y)->{
        Button[][] buttons = new Button[x][y];
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                buttons[i][j] = getFieldBtn(j,i);
            }
        }
        return buttons;
    };



    public static void main(String[] args) {
    //    board.Board board = new board.Board(10,10);
    //    System.out.println(board2String.apply(board.boardCells));

        Application.launch(args);
    }

    static Function<BoardCell[][],String> board2String = b->{
        String out = "";
        for (BoardCell[] row:b){
            for(BoardCell cell:row) out += cell.isBomb() && !cell.isOpen()?" B ":" * ";
            out += "\n";
        }
        return out;
    };

    @Override
    public void start(Stage primaryStage) throws Exception {
        //board = new Board(10,10);
        game = new Game(10,10);
        primaryStage.setTitle("Minesweeper");
        Group root = new Group();
        Pane grid = new Pane();
        GridPane paneGrid = getGridButtons.apply(10,10);

        root.getChildren().addAll(paneGrid);
       // Button test = new Button("test Colors");

        Scene scene = new Scene(root,300,300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
