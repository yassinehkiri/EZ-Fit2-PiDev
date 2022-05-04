package com.easyFit.gui.front.post;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.MainApp;
import com.easyFit.entities.Comment;
import com.easyFit.entities.Post;
import com.easyFit.entities.User;
import com.easyFit.gui.front.AccueilFront;
import com.easyFit.services.CommentService;
import com.easyFit.services.PostService;
import com.easyFit.services.UserService;
import com.easyFit.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static DisplayAll instance;
    public static Comment currentComment = null;
    public static Post currentPost = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label objectLabel, contentLabel;
    Label userLabel;
    ImageViewer userImage;
    SpanLabel contentSpanLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;
    Label contentCommentLabel, dateCommentLabel, userCommentLabel;
    ImageViewer userCommentImage;
    Button deleteCommentBtn;
    Container btnsCommentContainer;

    public DisplayAll() {
        super("Posts", new BoxLayout(BoxLayout.Y_AXIS));
        instance = this;

        addGUIs();
        addActions();

        super.getToolbar().hideToolbar();
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
        ArrayList<Post> listPosts = PostService.getInstance().getAll();
        if (listPosts.size() > 0) {
            for (Post listPost : listPosts) {
                this.add(makePostModel(listPost));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> {
            currentPost = null;
            new Manage(AccueilFront.accueilFrontForm).show();
        });
    }

    private Component makePostModel(Post post) {
        Container postModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        postModel.setUIID("containerRounded");

        User user = UserService.getInstance().getUserById(post.getUserId());

        userLabel = new Label(user.getUsername());
        userImage = new ImageViewer(theme.getImage("person.jpg").fill(150, 150));

        objectLabel = new Label(post.getObject());
        objectLabel.setUIID("labelCenter");

        contentLabel = new Label("Description : ");
        contentLabel.setUIID("labelDefault");

        contentSpanLabel = new SpanLabel(post.getContent());
        contentSpanLabel.setUIID("labelDefault");

        if (post.getImage() != null) {
            String url = Statics.POST_IMAGE_URL + post.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(1100, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
        }
        imageIV.setFocusable(false);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentPost = post;
            new com.easyFit.gui.front.post.Manage(AccueilFront.accueilFrontForm).show();
        });

        deleteBtn = new Button("Supprimer");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce post ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = PostService.getInstance().delete(post.getId());

                if (responseCode == 200) {
                    currentPost = null;
                    dlg.dispose();
                    postModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du post. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.WEST, editBtn);
        btnsContainer.add(BorderLayout.EAST, deleteBtn);

        Container userContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        userContainer.setUIID("containerUser");
        userContainer.addAll(userImage, userLabel);

        postModel.addAll(
                userContainer,
                objectLabel,
                imageIV,
                contentLabel,
                contentSpanLabel
        );

        if (user.getId() == MainApp.getSession().getId()) {
            postModel.add(btnsContainer);
        }

        Label commentsLabel = new Label("Commentaires");
        commentsLabel.setUIID("labelCenter");
        postModel.add(commentsLabel);

        ArrayList<Comment> listComments = post.getComments();
        if (listComments.size() > 0) {
            for (Comment comment : listComments) {
                postModel.add(makeCommentModel(comment));
            }
        } else {
            postModel.add(new Label("Aucun commentaire"));
        }

        Button addCommentButton = new Button("Ajouter un commentaire");
        TextArea addCommentTF = new TextArea();
        addCommentTF.setHint("Contenu du commentaire");
        addCommentButton.addActionListener(l -> {
            if (!addCommentTF.getText().equals("")) {
                Comment comment = new Comment(
                        addCommentTF.getText(),
                        post,
                        MainApp.getSession().getId()
                );
                int responseCode = CommentService.getInstance().add(comment);
                if (responseCode == 200) {
                    postModel.removeComponent(addCommentTF);
                    postModel.removeComponent(addCommentButton);
                    postModel.add(makeCommentModel(comment));
                    postModel.addAll(addCommentTF, addCommentButton);
                    addCommentTF.setText("");

                    postModel.revalidate();

                    // API NOTIF
                    ToastBar.getInstance().setPosition(TOP);
                    ToastBar.Status status = ToastBar.getInstance().createStatus();
                    status.setShowProgressIndicator(false);
                    status.setMessage("Commentaire ajouté avec succes");
                    status.setExpires(5000);
                    status.show();

                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de comment. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            } else {
                Dialog.show("Avertissement", "Description vide", new Command("Ok"));
            }
        });
        postModel.addAll(addCommentTF, addCommentButton);

        return postModel;
    }

    private Component makeCommentModel(Comment comment) {
        Container commentModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        commentModel.setUIID("containerRounded");

        User user = UserService.getInstance().getUserById(comment.getUserId());

        userCommentImage = new ImageViewer(theme.getImage("person.jpg").fill(150, 150));
        userCommentLabel = new Label(user.getUsername());

        if (comment.getDate() == null) {
            dateCommentLabel = new Label("");
            dateCommentLabel.setUIID("labelDefault");
        } else {
            dateCommentLabel = new Label(new SimpleDateFormat("dd-MM-yyyy hh:mm").format(comment.getDate()));
            dateCommentLabel.setUIID("labelDefault");
        }

        contentCommentLabel = new Label(comment.getContent());
        contentCommentLabel.setUIID("labelDefault");

        btnsCommentContainer = new Container(new BorderLayout());
        btnsCommentContainer.setUIID("containerButtons");

        deleteCommentBtn = new Button(FontImage.MATERIAL_DELETE);
        deleteCommentBtn.addActionListener(action -> {
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
                    this.revalidate();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression du comment. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsCommentContainer.add(BorderLayout.EAST, deleteCommentBtn);

        Container userContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        userContainer.setUIID("containerUser");
        userContainer.addAll(userCommentImage, userCommentLabel);

        commentModel.addAll(
                userContainer,
                dateCommentLabel,
                contentCommentLabel
        );

        if (user.getId() == MainApp.getSession().getId()) {
            commentModel.add(btnsCommentContainer);
        }


        return commentModel;
    }
}