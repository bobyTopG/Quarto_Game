package be.kdg.quarto.view.ChooseAIView;

import be.kdg.quarto.helpers.CreateHelper;
import be.kdg.quarto.model.Ai;
import be.kdg.quarto.model.Player;
import be.kdg.quarto.model.enums.AiLevel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ChooseAIView extends BorderPane {
    private Button backButton, selectButton;
    private Image notFoundImage;
    private List<Image> AIImages; //order is important
    private List<Player> AiCharacters;
    private Label chooseAILabel, nameLabel,
            descriptionLabel,difficultyHolderLabel, difficultyLabel;

    private HBox difficultyHBox;
    private List<Button> characterButtons = new ArrayList<>();
    private List<Button> notFoundButtons = new ArrayList<>();
    private VBox mainVBox, characterVBox;

    private ImageView selectedCharacter;

    public ChooseAIView() {
        initialiseNodes();
        layoutNodes();

    }
    private void initialiseNodes() {
        backButton = CreateHelper.createButton("Back",new String[]{"back-button","action-button"});
        selectButton = CreateHelper.createButton("Select", new String[]{"select-button","action-button"});
        chooseAILabel = new Label("Choose opponent :");
        nameLabel = new Label("");
        descriptionLabel = new Label("");
        difficultyHolderLabel = new Label("");
        difficultyLabel = new Label("");
        difficultyHBox = new HBox();
        mainVBox = new VBox();
        characterVBox = new VBox();
        selectedCharacter = new ImageView();
    }
    private void layoutNodes() {


        styleMainVBox();

        HBox buttonsHBox = new HBox();
        buttonsHBox.getStyleClass().add("buttons-hbox");
        buttonsHBox.getChildren().addAll(backButton, selectButton);


        mainVBox.getChildren().addAll(chooseAILabel, buttonsHBox);

        this.setLeft(mainVBox);


        styleCharacterVBox();
        difficultyHBox.getChildren().addAll(difficultyHolderLabel, difficultyLabel);

        selectedCharacter.setFitWidth(150);
        selectedCharacter.setFitHeight(150);
        characterVBox.getChildren().addAll(selectedCharacter,nameLabel,difficultyHBox,descriptionLabel);
        this.setRight(characterVBox);

    }

    public void setSelectedCharacter(int n){
        difficultyHolderLabel.setText("Difficulty:");
        if(n == -1){
            selectedCharacter.setImage(notFoundImage);
            nameLabel.setText("Unknown");
            difficultyLabel.setText("Secret");
            changeDifficultyColor(AiLevel.MEDIUM);

            descriptionLabel.setText("Coming Soon...");
            return;
        }
        //set the character that is the nth on the list
        selectedCharacter.setImage(AIImages.get(n));
        nameLabel.setText(AiCharacters.get(n).getName());
        if(AiCharacters.get(n) instanceof  Ai) {
            Ai ai = (Ai) AiCharacters.get(n);
            AiLevel level = ai.getDifficultyLevel();
            difficultyHolderLabel.setText("Difficulty:");

            difficultyLabel.setText(getDifficultyName(level));
            changeDifficultyColor(level);
            descriptionLabel.setText(ai.getDescription());
            descriptionLabel.setStyle("-fx-font-size: 20px;");

        }
        else {
            difficultyHolderLabel.setText("");
            difficultyLabel.setText("");
            descriptionLabel.setText("Have fun with your friends!");
            descriptionLabel.setStyle("-fx-font-size: 28px;");
        }



    }

    private String getDifficultyName(AiLevel level){
        String difficulty = "";
        if(level != AiLevel.MEDIUM)
             difficulty = level.toString();
        else
            difficulty = "MID";
        //EASY -> Easy
        String firstLetter = difficulty.substring(0, 1).toUpperCase();

        String restOfString = difficulty.substring(1).toLowerCase();

        return firstLetter + restOfString;
    }
    private void changeDifficultyColor(AiLevel level){
        switch(level){
            case EASY:
                difficultyLabel.setStyle("-fx-text-fill: #42AF3F");
                break;
            case MEDIUM:
                difficultyLabel.setStyle("-fx-text-fill: orange");
                break;
            case HARD:
                difficultyLabel.setStyle("-fx-text-fill: red");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + level);
        }
    }
    public void styleCharacterVBox(){
        characterVBox.getStyleClass().add("character-vbox");
        nameLabel.getStyleClass().add("name-label");
        descriptionLabel.getStyleClass().add("description-label");
        difficultyHolderLabel.getStyleClass().add("difficulty-holder-label");
        difficultyLabel.getStyleClass().add("difficulty-label");
        selectedCharacter.getStyleClass().add("selected-character-image");
        difficultyHBox.getStyleClass().add("difficulty-hbox");

    }
    private void styleMainVBox() {
        chooseAILabel.getStyleClass().add("choose-label");
        mainVBox.getStyleClass().add("main-vbox");

    }


     void initialise(List<Image> images, List<Player> characters, Image notFoundImage) {
        this.AIImages = images;
        this.AiCharacters = characters;
        this.notFoundImage = notFoundImage;


        // create a Hbox of buttons with the "amount" amount
        HBox AICharactersHBox = generateButtonsForAI(4);
        mainVBox.getChildren().add(1, AICharactersHBox);
    }

    private HBox generateButtonsForAI(int amount){
        HBox AICharactersHBox = new HBox();
        AICharactersHBox.getStyleClass().add("ai-characters-hbox");
        int count = 0;
        for (Player character : AiCharacters) {
            Button AICharacterButton = CreateHelper.createButton("","ai-button");
            Image image = AIImages.get(count);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            AICharacterButton.setGraphic(imageView);
            characterButtons.add(AICharacterButton);

            AICharactersHBox.getChildren().add(AICharacterButton);
            count++;
        }
        for (int i = 0; i < amount-count; i++){
            Button notFoundButton = CreateHelper.createButton("","ai-button");
            ImageView imageView = new ImageView(notFoundImage);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            notFoundButton.setGraphic(imageView);
            AICharactersHBox.getChildren().add(notFoundButton);
            notFoundButtons.add(notFoundButton);
        }

        return AICharactersHBox;
    }



     Button getBackButton() {
        return backButton;
    }

     List<Button> getCharacterButtons(){
        return characterButtons;
    }

     List<Button> getNotFoundButtons(){
        return notFoundButtons;
    }

     Button getSelectButton(){
        return selectButton;
    }
}
