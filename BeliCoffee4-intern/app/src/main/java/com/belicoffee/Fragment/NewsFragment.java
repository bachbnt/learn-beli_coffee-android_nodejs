package com.belicoffee.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.belicoffee.Adapter.NewsAdapter;
import com.belicoffee.Manager.NewsManager;
import com.belicoffee.Model.News;
import com.belicoffee.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements NewsManager.RequestListener, NewsAdapter.OnItemClickListener, View.OnClickListener {
    RecyclerView recyclerView;
    NewsManager newsManager;
    Button btnCreate;
    EditText etTitle, etContent;
    ImageView ivImage;
    ImageButton ibMore;
    CardView cvEdit;
    NewsAdapter adapter;
    boolean type = true, more = true;
    News currentNews;
    Uri imgUri;

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.rv_news_news);
        btnCreate = view.findViewById(R.id.btn_create_news);
        etTitle = view.findViewById(R.id.et_title_news);
        etContent = view.findViewById(R.id.et_content_news);
        ivImage = view.findViewById(R.id.iv_image_news);
        ibMore = view.findViewById(R.id.ib_more_news);
        cvEdit = view.findViewById(R.id.cv_edit_news);
        cvEdit.setVisibility(View.GONE);
        newsManager = NewsManager.getInstance();
        newsManager.setListenner(this);
        btnCreate.setOnClickListener(this);
        ivImage.setOnClickListener(this);
        ibMore.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        newsManager.getNews();
    }

    @Override
    public void onComplete(ArrayList<News> newsArrayList) {
        adapter = new NewsAdapter(newsArrayList, getContext(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onUpdateClick(News news) {
        currentNews = news;
        if (currentNews.getImage()!=null)
        Glide.with(getContext()).load(Uri.parse(currentNews.getImage())).into(ivImage);
        else ivImage.setImageResource(R.drawable.ic_image_white_160dp);
        ibMore.callOnClick();
        more = true;
        btnCreate.setText("Chỉnh sửa");
        etTitle.setText(news.getTitle());
        etContent.setText(news.getContent());
        type = false;
    }

    @Override
    public void onDeleteClick(News news) {
        newsManager.deleteNews(news.getId());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();
            ivImage.setImageURI(imgUri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_news:
                if (type) {
                    btnCreate.setEnabled(false);
                    uploadImage();
                } else {
                    if (imgUri == null) {
                        newsManager.updateNews(currentNews.getId(), etTitle.getText().toString(), etContent.getText().toString(), currentNews.getImage());
                        btnCreate.setText("Thêm mới");
                        adapter.notifyDataSetChanged();
                        etTitle.setText("");
                        etContent.setText("");
                        ivImage.setImageResource(R.drawable.ic_image_white_160dp);
                        type=true;
                    } else
                        btnCreate.setEnabled(false);
                        uploadImage();
                }
                break;
            case R.id.ib_more_news:
                if (more) {
                    cvEdit.setVisibility(View.VISIBLE);
                    ibMore.setImageResource(R.drawable.ic_expand_less_dark_cyan_48dp);
                } else {
                    cvEdit.setVisibility(View.GONE);
                    ibMore.setImageResource(R.drawable.ic_expand_more_dark_cyan_48dp);
                }
                more = !more;
                break;
            case R.id.iv_image_news:
                chooseImage();
                break;
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void uploadImage() {
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Images/" + getCurrentTime());
        UploadTask uploadTask = storageReference.putFile(imgUri);
        Log.i("tag", "StorageReference 2");
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("tag", "onSuccess 1");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("tag", "onSuccess 2");
                        if (type)
                            newsManager.createNews(etTitle.getText().toString(), etContent.getText().toString(), uri.toString());
                        else {
                            newsManager.updateNews(currentNews.getId(), etTitle.getText().toString(), etContent.getText().toString(), uri.toString());
                            btnCreate.setText("Thêm mới");
                        }
                        Log.i("tag", "onSuccess 3" + uri);
                        adapter.notifyDataSetChanged();
                        etTitle.setText("");
                        etContent.setText("");
                        ivImage.setImageResource(R.drawable.ic_image_white_160dp);
                        imgUri = null;
                        type = true;
                        btnCreate.setEnabled(true);
                    }
                });
            }
        });
    }
    public String getCurrentTime(){
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        return date;
    }
}
