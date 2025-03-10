    package be.kdg.quarto.view.ChooseAIView;
    import be.kdg.quarto.model.AI;
    import javafx.scene.image.Image;
    import be.kdg.quarto.AICharacters;

    import java.util.ArrayList;
    import java.util.List;

    public class ChooseAIPresenter {
        //we add Boxed because that is a prefix for the character's button
        private final String pathToCharacters = "/resources/AICharacters/Boxed"; // + AIName + extension
        private final String AIButtonsExtension = ".png";
        private final String pathToStyleSheet = "./";
        private final String pathToNotFound = "/resources/AICharacters/BoxedNotFound.png";
        ChooseAIView view;
        public ChooseAIPresenter(ChooseAIView view) {
            this.view = view;
            List<Image> images = findAICharacterImagesForButtons();
            Image notFoundImage = new Image(pathToNotFound);
            view.initialise(images, AICharacters.getInstance().getCharacters(), notFoundImage); // Pass the images & characters to the view
            updateView();

        }

        private List<Image> findAICharacterImagesForButtons() {
            List<AI> AIList = AICharacters.getInstance().getCharacters();
            List<Image> images = new ArrayList<>();
            for (AI ai : AIList) {
                String imagePath = pathToCharacters + ai.getName() + AIButtonsExtension;
                Image image = new Image(getClass().getResource(imagePath).toString()); // Use getClass().getResource()
                images.add(image);
            }
            return images;
        }
        private void updateView() {
            view.getScene().getRoot().setStyle("-fx-background-color: #fff4d5;");
        }
    }
