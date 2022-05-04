package com.easyFit.gui.back.post;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Post;
import com.easyFit.gui.back.comment.ChoosePost;
import com.easyFit.services.PostService;
import com.easyFit.utils.Statics;

import java.io.IOException;

public class Manage extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");

    boolean imageEdited = false;
    boolean isChoose;

    Post currentPost;
    String selectedImage;

    Label objectLabel, contentLabel, imageLabel;
    TextField objectTF;
    TextArea contentTF;
    ImageViewer imageIV;
    Button selectImageButton, manageButton;

    Form previous;

    public Manage(Form previous, boolean isChoose) {
        super(DisplayAll.currentPost == null ? "Ajouter une publication" : "Modifier la publication", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        this.isChoose = isChoose;
        currentPost = DisplayAll.currentPost;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        objectLabel = new Label("Objet : ");
        objectLabel.setUIID("labelDefault");
        objectTF = new TextField();
        objectTF.setHint("Tapez l'objet du post");

        contentLabel = new Label("Contenu : ");
        contentLabel.setUIID("labelDefault");
        contentTF = new TextArea();
        contentTF.setHint("Tapez la description du post");

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentPost == null) {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
            manageButton = new Button("Ajouter");
        } else {
            objectTF.setText(currentPost.getObject());
            contentTF.setText(currentPost.getContent());

            if (currentPost.getImage() != null) {
                String url = Statics.POST_IMAGE_URL + currentPost.getImage();
                Image image = URLImage.createToStorage(
                        EncodedImage.createFromImage(theme.getImage("default.jpg").fill(500, 500), false),
                        url,
                        url,
                        URLImage.RESIZE_SCALE
                );
                imageIV = new ImageViewer(image);
            } else {
                imageIV = new ImageViewer(theme.getImage("default.jpg").fill(500, 500));
            }
            imageIV.setFocusable(false);

            selectImageButton.setText("Choisir  l'image");
            selectedImage = currentPost.getImage();

            manageButton = new Button("Modifier");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                objectLabel, objectTF,
                contentLabel, contentTF,
                imageLabel, imageIV, selectImageButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageEdited = true;
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Choisir  l'image");
        });

        if (currentPost == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PostService.getInstance().add(
                            new Post(
                                    objectTF.getText(),
                                    contentTF.getText(),
                                    selectedImage,
                                    0
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Post ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de post. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = PostService.getInstance().edit(
                            new Post(
                                    currentPost.getId(),
                                    objectTF.getText(),
                                    contentTF.getText(),
                                    selectedImage
                            ),
                            imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Post modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de post. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        if (isChoose) {
            ((ChoosePost) previous).refresh();
        } else {
            ((DisplayAll) previous).refresh();
        }
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (objectTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir l'objet", new Command("Ok"));
            return false;
        }

        if (contentTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le contenu", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }
        return true;
    }
}