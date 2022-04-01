package trello;

import data.Data;
import models.board.Board;
import models.board.Cards;
import models.board.Lists;
import org.junit.Assert;
import retrofit2.Call;
import retrofit2.Response;
import utils.Caller;
import utils.Printer;
import utils.ServiceGenerator;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Trello extends Caller {

    TrelloServices services = ServiceGenerator.generateService(TrelloServices.class);
    Printer log = new Printer(Trello.class);
    Properties properties = new Properties();

    public Trello(){
        try {
            properties.load(new FileReader("src/test/resources/test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createBoard(String boardName) {
        log.new Info("Creating a board...");
        Call<Board> board = services.createBoard(properties.getProperty("key"),properties.getProperty("token"),boardName);
        Board responseBoard = perform(board,true,"boardCreator");
        log.new Info("Board id is: "+responseBoard.getId());
        log.new Info("Name is verified as: "+responseBoard.getName());
        Data.context.put(boardName,responseBoard);
    }

    public void createList(String listName,String boardName){
        log.new Info("Creating a list...");
        Call<Lists> list = services.createList(((Board) Data.context.get(boardName)).getId(), properties.getProperty("key"), properties.getProperty("token"),listName);
        Lists responseList = perform(list,true,"listCreator");
        log.new Info("List is: "+responseList.getId());
        Data.context.put(listName,responseList);
    }

    public void createCard(String cardName, String listName){
        log.new Info("Creating Card...");
        Call<Cards> createCard = services.createCard(properties.getProperty("key"), properties.getProperty("token"), ((Lists) Data.context.get(listName)).getId(), cardName);
        Cards responseCard = perform(createCard,true,"cardCreator");
        log.new Info("Card is: "+responseCard.getId());
        Data.context.put(cardName,responseCard);

    }

    public void deleteBoard(String boardName){
        log.new Info("Deleting a board...");
        Call<Object> deleteRequest = services.deleteBoard(((Board) Data.context.get(boardName)).getId(),properties.getProperty("key"),properties.getProperty("token"));
        Response<Object> response = getResponse(deleteRequest,true,"boardDelete");
        Assert.assertTrue(response.isSuccessful());
        log.new Info("Successfully deleted board");
    }

    public void deleteCard(String cardName){
        log.new Info("Deleting a card...");
        Call<Object> deleteCardRequest = services.deleteCard(
                ((Cards) Data.context.get(cardName)).getId(),
                properties.getProperty("key"),
                properties.getProperty("token"));
        Response<Object> response = getResponse(deleteCardRequest,true,"boardDelete");
        Assert.assertTrue(response.isSuccessful());
        log.new Info("Successfully deleted board");
    }







}