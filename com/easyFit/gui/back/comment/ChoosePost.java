package com.easyFit.gui.back.comment;

import com.codename1.components.ImageViewer;
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

public class ChoosePost extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Form previousForm;
    Button addBtn;
    Label objectLabel, dateLabel, contentLabel;
    SpanLabel contentSpanLabel;
    ImageViewer imageIV;
    Button chooseBtn;
    Container btnsContainer;

    public ChoosePost(Form previous) {
        super("Choisir une publication", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
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
        addBtn = new Button("Ajouter post");
        this.add(addBtn);

        ArrayList<Post> listPosts = PostService.getInstance().getAll();
        if (listPosts.size() > 0) {
            for (Post posts : listPosts) {
                this.add(makePostModel(posts));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> new com.easyFit.gui.back.post.Manage(this, true).show());
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

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedPost = post;
            ((Manage) previousForm).refreshPost();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        postModel.addAll(
                objectLabel,
                imageIV,
                dateLabel,
                contentLabel,
                btnsContainer
        );

        return postModel;
    }
}