package com.easyFit.gui.back.comment;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.easyFit.entities.Comment;
import com.easyFit.services.CommentService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Comment currentComment = null;
    Button addBtn;
    Label contentLabel, dateLabel, postLabel;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Comments", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    private void addGUIs() {
        addBtn = new Button("Ajouter");

        this.add(addBtn);

        ArrayList<Comment> listComments = CommentService.getInstance().getAll();
        if (listComments.size() > 0) {
            for (Comment listComment : listComments) {
                this.add(makeCommentModel(listComment));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentComment = null;
            new Manage(this).show();
        });
    }

    private Component makeCommentModel(Comment comment) {
        Container commentModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        commentModel.setUIID("containerRounded");

        contentLabel = new Label("Contenu : " + comment.getContent());
        contentLabel.setUIID("labelCenter");

        dateLabel = new Label(new SimpleDateFormat("dd-MM-yyyy").format(comment.getDate()));
        dateLabel.setUIID("labelDefault");

        postLabel = new Label("Post : " + comment.getPost().getContent());
        postLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentComment = comment;
            new Manage(this).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce comment ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = CommentService.getInstance().delete(comment.getId());

                if (responseCode == 200) {
                    currentComment = null;
                    dlg.dispose();
                    commentModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du comment. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        commentModel.addAll(
                contentLabel, dateLabel,
                postLabel,
                btnsContainer
        );

        return commentModel;
    }
}