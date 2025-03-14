    package be.kdg.quarto.view.ChooseAIView;
    import be.kdg.quarto.model.Ai;
    import be.kdg.quarto.model.Game;
    import be.kdg.quarto.model.Human;
    import be.kdg.quarto.view.GameScreen.GamePresenter;
    import be.kdg.quarto.view.GameScreen.GameView;
    import be.kdg.quarto.view.StartScreen.StartPresenter;
    import be.kdg.quarto.view.StartScreen.StartView;
    import be.kdg.quarto.view.UiSettings;
    import javafx.scene.control.Button;
    import javafx.scene.image.Image;
    import be.kdg.quarto.helpers.AICharacters;

    import java.util.ArrayList;
    import java.util.List;

    public class ChooseAIPresenter {
        ChooseAIView view;
        Ai aiSelected;
        public ChooseAIPresenter(ChooseAIView view) {
            this.view = view;
            List<Image> images = findAICharacterImagesForButtons();
            String pathToNotFound = "/images/aiCharacters/BoxedNotFound.png";
            Image notFoundImage = new Image(pathToNotFound);
            view.initialise(images, AICharacters.getCharacters(), notFoundImage); // Pass the images & characters to the view
            updateView();
            addEventHandlers();
        }

        private List<Image> findAICharacterImagesForButtons() {
            List<Ai> AiList = AICharacters.getCharacters();
            List<Image> images = new ArrayList<>();
            for (Ai ai : AiList) {
                //we add Boxed because that is a prefix for the character's button
                // + AIName + extension
                String pathToCharacters = "/images/aiCharacters/Boxed";
                String AIButtonsExtension = ".png";
                String imagePath = pathToCharacters + ai.getName() + AIButtonsExtension;
                Image image = new Image(getClass().getResource(imagePath).toString()); // Use getClass().getResource()
                images.add(image);
            }
            return images;
        }
        UiSettings uiSettings = new UiSettings();

        private void addEventHandlers() {

            addEventListenersForAICharacterButtons();

            view.getBackButton().setOnMouseClicked(event -> {
                StartView startView = new StartView(uiSettings);
                view.getScene().setRoot(startView);
                new StartPresenter(startView);
            });

            view.getSelectButton().setOnMouseClicked(event -> {
                GameView gameView = new GameView();
                view.getScene().setRoot(gameView);
                new GamePresenter(new Game(new Human("Example","98eu98u2e198"), aiSelected), gameView);
            });
        }
        private void addEventListenersForAICharacterButtons(){
            int count = 0;
            for(Button characterButton : view.getCharacterButtons()){
                final int id = count;
                characterButton.setOnMouseClicked(event -> {
                    view.setSelectedCharacter(id);
                    view.getSelectButton().setDisable(false);
                    aiSelected = AICharacters.getCharacters().get(id);
                });
                count++;
            }
            for(Button notFoundButton : view.getNotFoundButtons()){
                notFoundButton.setOnMouseClicked(event -> {
                    view.setSelectedCharacter(-1);
                    view.getSelectButton().setDisable(true);
                });
            }
        }

            private void updateView() {
            view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        }
    }
