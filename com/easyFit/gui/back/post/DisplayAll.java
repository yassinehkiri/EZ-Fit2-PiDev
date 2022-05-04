package com.easyFit.gui.back.post;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.easyFit.entities.Post;
import com.easyFit.services.PostService;
import com.easyFit.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayAll extends Form {

    public static Post currentPost = null;
    Resources theme = UIManager.initFirstTheme("/theme");
    Button addBtn;
    Label objectLabel, dateLabel, contentLabel;
    SpanLabel contentSpanLabel;
    ImageViewer imageIV;
    Button editBtn, deleteBtn;
    Container btnsContainer;

    public DisplayAll(Form previous) {
        super("Posts", new BoxLayout(BoxLayout.Y_AXIS));

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
            new Manage(this, false).show();
        });
    }

    private Component makePostModel(Post post) {
        Container postModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        postModel.setUIID("containerRounded");

        objectLabel = new Label(post.getObject());
        objectLabel.setUIID("labelCenter");

        dateLabel = new Label(new SimpleDateFormat("dd-MM-yyyy").format(post.getDate()));
        dateLabel.setUIID("labelDefault");

        contentLabel = new Label("Description : ");
        contentLabel.setUIID("labelDefault");

        contentSpanLabel = new SpanLabel(post.getContent());
        contentSpanLabel.setUIID("labelDefault");

        if (post.getImage() != null) {
            String url = Statics.POST_IMAGE_URL + post.getImage();
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

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        editBtn = new Button("Modifier");
        editBtn.addActionListener(action -> {
            currentPost = post;
            new Manage(this, false).show();
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

        postModel.addAll(
                objectLabel,
                imageIV,
                dateLabel,
                contentLabel, contentSpanLabel,
                btnsContainer
        );

        return postModel;
    }
}