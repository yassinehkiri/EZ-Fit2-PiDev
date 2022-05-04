package com.easyFit.gui.back.comment;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Comment;
import com.easyFit.entities.Post;
import com.easyFit.services.CommentService;

public class Manage extends Form {

    public static Post selectedPost;
    Comment currentComment;

    Label contentLabel, postLabel, selectedPostLabel;
    TextField contentTF;
    Button selectPostButton, manageButton;

    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentComment == null ? "Ajouter un commentaire" : "Modifier commentaire", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedPost = null;
        currentComment = DisplayAll.currentComment;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshPost() {
        selectedPostLabel.setText(selectedPost.getObject());
        selectPostButton.setText("Choisir  post");
        this.refreshTheme();
    }

    private void addGUIs() {

        contentLabel = new Label("Commentaire : ");
        contentLabel.setUIID("labelDefault");
        contentTF = new TextField();
        contentTF.setHint("Tapez le contentu du commentaire");

        if (currentComment == null) {
            manageButton = new Button("Ajouter");
        } else {

            contentTF.setText(currentComment.getContent());
            selectedPost = currentComment.getPost();

            manageButton = new Button("Modifier");
        }

        postLabel = new Label("Post : ");
        postLabel.setUIID("labelDefault");
        if (selectedPost != null) {
            selectedPostLabel = new Label(selectedPost.getObject());
            selectPostButton = new Button("Modifier la publication");
        } else {
            selectedPostLabel = new Label("Aucune publication selectionné");
            selectPostButton = new Button("Choisir une publication");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                contentLabel, contentTF,
                postLabel, selectedPostLabel, selectPostButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectPostButton.addActionListener(l -> new ChoosePost(this).show());

        if (currentComment == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CommentService.getInstance().add(
                            new Comment(
                                    contentTF.getText(),
                                    selectedPost,
                                    0
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Comment ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de comment. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                } else {
                    Dialog.show("Avertissement", "Description vide", new Command("Ok"));
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CommentService.getInstance().edit(
                            new Comment(
                                    currentComment.getId(),
                                    contentTF.getText(),
                                    selectedPost
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Comment modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de comment. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {
        if (contentTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le contenu", new Command("Ok"));
            return false;
        }

        if (selectedPost == null) {
            Dialog.show("Avertissement", "Veuillez choisir une publication", new Command("Ok"));
            return false;
        }

        return true;
    }
}