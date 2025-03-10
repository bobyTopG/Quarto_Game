package be.kdg.quarto.view.ChooseAIView;

import be.kdg.quarto.model.AI;
import be.kdg.quarto.model.enums.AILevel;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChooseAIView extends BorderPane {
    private Button backButton, selectButton;
    private Image notFoundImage;
    private List<Image> AIImages; //order is important
    private List<AI> AICharacters;
    private Label chooseAILabel, nameLabel,
            descriptionLabel,difficultyHolderLabel, difficultyLabel;

    private HBox difficultyHBox;
    private VBox mainVBox, characterVBox;

    private ImageView selectedCharacter;

    public ChooseAIView() {
        initialiseNodes();
        layoutNodes();

    }
    private void initialiseNodes() {
        backButton = new Button("Back");
        selectButton = new Button("Select");
        chooseAILabel = new Label("Choose opponent :");
        nameLabel = new Label("Bob");
        descriptionLabel = new Label("New to the game, understands \n the basic rules but has \n no other knowledge");
        difficultyHolderLabel = new Label("Difficulty:");
        difficultyLabel = new Label("Easy");
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

    private void setSelectedCharacter(int n){
        if(n == -1){
            selectedCharacter.setImage(notFoundImage);
            nameLabel.setText("Unknown");
            difficultyLabel.setText("Secret");
            changeDifficultyColor(AILevel.MEDIUM);

            descriptionLabel.setText("Coming Soon...");
            return;
        }
        //set the character that is the nth on the list
        selectedCharacter.setImage(AIImages.get(n));
        nameLabel.setText(AICharacters.get(n).getName());
        AILevel level = AICharacters.get(n).getDifficultyLevel();
        difficultyLabel.setText(getDifficultyName(level));
        changeDifficultyColor(level);
        descriptionLabel.setText(AICharacters.get(n).getDescription());


    }

    private String getDifficultyName(AILevel level){
        String firstLetter = level.toString().substring(0, 1).toUpperCase();

        String restOfString = level.toString().substring(1).toLowerCase();

        return firstLetter + restOfString;


    }
    private void changeDifficultyColor(AILevel level){
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
        backButton.getStyleClass().add("action-button");
        backButton.getStyleClass().add("back-button");
        selectButton.getStyleClass().add("action-button");
        selectButton.getStyleClass().add("select-button");
        chooseAILabel.getStyleClass().add("choose-label");
        mainVBox.getStyleClass().add("main-vbox");

    }


    public void initialise(List<Image> images, List<AI> characters, Image notFoundImage) {
        this.AIImages = images;
        this.AICharacters = characters;
        this.notFoundImage = notFoundImage;


        // create a Hbox of buttons with the "amount" amount
        HBox AICharactersHBox = generateButtonsForAI(4);
        mainVBox.getChildren().add(1, AICharactersHBox);
    }

    private HBox generateButtonsForAI(int amount){
        HBox AICharactersHBox = new HBox();
        AICharactersHBox.getStyleClass().add("ai-characters-hbox");
        int count = 0;
        for (AI character : AICharacters) {
            Button AICharacterButton = new Button();
            AICharacterButton.getStyleClass().add("ai-button");

            Image image = AIImages.get(count);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            AICharacterButton.setGraphic(imageView);
            final int id = count;
            AICharacterButton.setOnAction(event -> {
                setSelectedCharacter(id);
                // Add your code here to execute when the button is clicked
            });

            AICharactersHBox.getChildren().add(AICharacterButton);
            count++;
        }
        for (int i = 0; i < amount-count; i++){
            Button notFoundButton = new Button();
            ImageView imageView = new ImageView(notFoundImage);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            notFoundButton.setGraphic(imageView);
            notFoundButton.getStyleClass().add("ai-button");
            AICharactersHBox.getChildren().add(notFoundButton);
            notFoundButton.setOnAction(event -> {
                setSelectedCharacter(-1);
                // Add your code here to execute when the button is clicked
            });

        }

        return AICharactersHBox;
    }

}
